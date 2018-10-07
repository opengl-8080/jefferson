package jefferson;

import javafx.util.Duration;

public class ContentControl {
    final String fileName;
    private RepeatControl repeatControl;
    Duration startTime;

    public ContentControl(String line) {
        String[] tokens = line.split(",");
        this.fileName = tokens[0];
        
        if (this.isMovie()) {
            this.repeatControl = RepeatControl.valueOf(tokens[1]);
            if (this.isRepeat()) {
                this.startTime = Duration.millis(Double.valueOf(tokens[2]));
            } else {
                this.startTime = Duration.millis(0);
            }
        }
    }
    
    public boolean isMovie() {
        return this.fileName.endsWith(".mp4");
    }
    
    public boolean isImage() {
        return this.fileName.endsWith(".png");
    }

    public boolean isRepeat() {
        return this.repeatControl == RepeatControl.REPEAT;
    }
}
