module jefferson {
    exports jefferson;
    opens jefferson to javafx.fxml;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.media;
}