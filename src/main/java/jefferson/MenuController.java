package jefferson;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    private VBox root;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ResourceUtil.readLines("/media/title.txt").forEach(line -> {
            String[] tokens = line.split(",");
            String title = tokens[0];
            String id = tokens[1];

            Button button = new Button();
            button.setText(title);
            button.setOnAction(e -> {
                URL fxml = MenuController.class.getResource("/fxml/player.fxml");
                FXMLLoader loader = new FXMLLoader(fxml);
                try {
                    Parent parent = loader.load();

                    Scene scene = new Scene(parent);
                    Stage stage = new Stage();
                    Rectangle2D bounds = Screen.getPrimary().getBounds();
                    stage.setWidth(bounds.getWidth() * 0.8);
                    stage.setHeight(bounds.getHeight() * 0.8);
                    stage.setScene(scene);
                    stage.setTitle(title);
                    stage.show();
                    stage.setOnCloseRequest(ee -> {
                        Main.removeStage(stage);
                    });
                    Main.addStage(stage);

                    PlayerController controller = loader.getController();
                    controller.setStage(stage);
                    controller.show(id);
                } catch (IOException e1) {
                    throw new UncheckedIOException(e1);
                }
            });
            root.getChildren().add(button);
        });
    }
}
