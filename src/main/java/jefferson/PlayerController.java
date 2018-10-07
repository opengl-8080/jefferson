package jefferson;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PlayerController implements Initializable {
    @FXML
    private Region contentArea;
    @FXML
    private MediaView mediaView;
    @FXML
    private ImageView imageView;
    
    private Title title;
    private CyclicIndex index;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.mediaView.fitWidthProperty().bind(this.contentArea.widthProperty());
        this.mediaView.fitHeightProperty().bind(this.contentArea.heightProperty());
        this.imageView.fitWidthProperty().bind(this.contentArea.widthProperty());
        this.imageView.fitHeightProperty().bind(this.contentArea.heightProperty());
        this.mediaView.setPreserveRatio(true);

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
                                index.add(scrollDelta);
                                play();
                                scrollDelta = 0;
                            }
                        }
                    });
                }, 0, 300, TimeUnit.MILLISECONDS);
    }
    
    private long lastScrollTime;
    private int scrollDelta;
    
    @FXML
    public void onScroll(ScrollEvent e) {
        if (e.getDeltaY() < 0) {
            scrollDelta--;
        } else {
            scrollDelta++;
        }
        lastScrollTime = System.currentTimeMillis();
    }
    
    
    public void show(String id) {
        this.title = new Title(id);
        this.index = new CyclicIndex(this.title.size());
        
        this.play();
    }
    
    private void play() {
        Content content = this.title.content(this.index.value());
        content.show(this.mediaView, this.imageView);
    }
}
