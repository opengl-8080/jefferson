package jefferson;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }

    public static void removeStage(Stage stage) {
        stages.remove(stage);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = Main.class.getResource("/fxml/menu.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Jefferson");
        primaryStage.setOnCloseRequest(e -> closeAllStages());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private static Set<Stage> stages = new HashSet<>();
    
    public static void addStage(Stage stage) {
        stages.add(stage);
    }
    
    private void closeAllStages() {
        stages.forEach(Stage::close);
    }
}
