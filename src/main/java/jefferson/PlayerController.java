package jefferson;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.TouchPoint;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PlayerController implements Initializable {
    @FXML
    private Pane root;
    @FXML
    private Pane contentArea;
    @FXML
    private MediaView mediaView;
    @FXML
    private ImageView imageView;
    @FXML
    private Slider slider;

    private Stage stage;
    private ContextMenu contextMenu = new ContextMenu();
    private Title title;
    private CyclicIndex index;
    private boolean mouseMode;

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setFullScreenExitHint("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (System.getProperty("mouseMode") != null) {
            this.mouseMode = true;
        }

        this.mediaView.fitWidthProperty().bind(this.contentArea.widthProperty());
        this.mediaView.fitHeightProperty().bind(this.contentArea.heightProperty());
        this.imageView.fitWidthProperty().bind(this.contentArea.widthProperty());
        this.imageView.fitHeightProperty().bind(this.contentArea.heightProperty());
        this.mediaView.setPreserveRatio(true);

        this.root.setOnContextMenuRequested(e -> {
            this.contextMenu.hide();
            this.contextMenu.show(stage, e.getScreenX(), e.getScreenY());
        });

        MenuItem menuItem = new MenuItem("全画面");
        menuItem.setOnAction(e -> {
            boolean fullScreen = this.stage.isFullScreen();
            this.stage.setFullScreen(!fullScreen);
        });
        this.contextMenu.getItems().add(menuItem);

        // 短時間に何度もスクロールしたときに、インデックスと効率的に飛ばすための処置
        Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        })
                .scheduleWithFixedDelay(() -> {
                    Platform.runLater(() -> {
                        if (scrollDelta != 0) {
                            long time = System.currentTimeMillis() - lastScrollTime;
                            if (200 < time) {
                                this.movePage(scrollDelta);
                                scrollDelta = 0;
                            }
                        }
                    });
                }, 0, 300, TimeUnit.MILLISECONDS);

        this.slider.valueProperty().addListener((o, old, value) -> {
            if (!this.slider.isValueChanging()) {
                int page = this.title.size() - value.intValue();
                this.changePage(page);
            }
        });
        
        this.slider.valueChangingProperty().addListener((o, old, changing) -> {
            if (!changing) {
                int page = this.title.size() - (int) this.slider.getValue();
                this.changePage(page);
            }
        });
    }
    
    private void changePage(int page) {
        this.index.set(page);
        this.play();
    }
    
    private long lastScrollTime;
    private int scrollDelta;

    private void movePage(int delta) {
        this.index.add(delta);
        this.play();
    }
    
    @FXML
    public void onScroll(ScrollEvent e) {
        if (!this.mouseMode) {
            return;
        }

        if (e.getDeltaY() < 0) {
            scrollDelta--;
        } else {
            scrollDelta++;
        }
        lastScrollTime = System.currentTimeMillis();
    }

    private boolean touching;
    private TouchPoint start;
    private static final double SWIPE_MOVE_THRESHOLD = 150.0;

    @FXML
    public void onTouchPressed(TouchEvent e) {
        if (this.mouseMode) {
            return;
        }

        this.touching = true;
        this.start = e.getTouchPoint();
    }
    @FXML
    public void onTouchMoved(TouchEvent e) {
        if (this.mouseMode) {
            return;
        }

        if (this.touching) {
            TouchPoint end = e.getTouchPoint();
            double delta = end.getScreenX() - start.getScreenX();
            double deltaAbs = Math.abs(delta);
            if (deltaAbs < SWIPE_MOVE_THRESHOLD) {
                this.contentArea.setOpacity(1 - 0.5 * (deltaAbs/ SWIPE_MOVE_THRESHOLD));
                this.contentArea.setTranslateX(delta);
            }
        }
    }

    @FXML
    public void onTouchReleased(TouchEvent e) {
        if (this.mouseMode) {
            return;
        }

        double delta = e.getTouchPoint().getScreenX() - this.start.getScreenX();
        if (SWIPE_MOVE_THRESHOLD <= Math.abs(delta)) {
            if (delta < 0) {
                this.movePage(1);
            } else if (0 < delta) {
                this.movePage(-1);
            }
        }

        this.touching = false;
        this.contentArea.setOpacity(1.0);
        this.contentArea.setTranslateX(0.0);
    }

    
    public void show(String id) {
        this.title = new Title(id);
        this.index = new CyclicIndex(this.title.size());

        this.slider.setMin(1);
        this.slider.setMax(this.title.size());
        this.slider.setValue(this.slider.getMax());
        
        this.play();
    }
    
    private void play() {
        Content content = this.title.content(this.index.value());
        content.show(this.mediaView, this.imageView);
    }
}
