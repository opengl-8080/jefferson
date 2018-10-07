package jefferson;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

public class ImageContent {
    private final Image image;
    
    public ImageContent(ContentControl control, URL url) {
        this.image = new Image(url.toString());
    }
    
    public void showTo(ImageView imageView) {
        imageView.setImage(this.image);
    }
}
