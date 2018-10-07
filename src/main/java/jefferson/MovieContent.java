package jefferson;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URL;

public class MovieContent {
    private final ContentControl control;
    private final URL mediaUrl;
    private MediaPlayer mediaPlayer;
    
    public MovieContent(ContentControl control, URL mediaUrl) {
        this.control = control;
        this.mediaUrl = mediaUrl;
        this.mediaPlayer = this.createMediaPlayer();
    }
    
    private MediaPlayer createMediaPlayer() {
        Media media = new Media(mediaUrl.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        if (control.isRepeat()) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        mediaPlayer.setStartTime(control.startTime);
        return mediaPlayer;
    }

    public void setOnError(Runnable callback) {
        this.mediaPlayer.setOnError(callback);
    }

    public void playAt(MediaView mediaView) {
        this.mediaPlayer.setOnError(() -> {
            this.mediaPlayer = this.createMediaPlayer();
            this.playAt(mediaView);
        });
        mediaView.setMediaPlayer(this.mediaPlayer);
        this.mediaPlayer.play();
    }
}
