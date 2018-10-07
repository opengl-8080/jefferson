package jefferson;

import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;

import java.net.URL;

public class Content {
    private MovieContent movie;
    private ImageContent image;
    
    public Content(ContentControl control, URL url) {
        if (control.isMovie()) {
            this.movie = new MovieContent(control, url);
        } else if (control.isImage()) {
            this.image = new ImageContent(control, url);
        } else {
            throw new RuntimeException("unknown content. url=" + url);
        }
    }
    
    public void show(MediaView mediaView, ImageView imageView) {
        if (this.movie != null) {
            imageView.setImage(null);
            imageView.setVisible(false);
            imageView.setManaged(false);
            mediaView.setVisible(true);
            mediaView.setManaged(true);
            this.movie.playAt(mediaView);
        } else {
            if (mediaView.getMediaPlayer() != null) {
                mediaView.getMediaPlayer().stop();
            }
            mediaView.setMediaPlayer(null);
            mediaView.setVisible(false);
            mediaView.setManaged(false);
            imageView.setVisible(true);
            imageView.setManaged(true);
            this.image.showTo(imageView);
        }
    }
}
