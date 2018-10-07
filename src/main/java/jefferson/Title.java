package jefferson;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Title {
    private final String id;
    private final List<ContentControl> controlList;

    public Title(String id) {
        this.id = id;
        this.controlList = this.createControlList(this.id);
    }
    
    private List<ContentControl> createControlList(String id) {
        List<String> lines = ResourceUtil.readLines("/media/" + id + "/control.txt");
        return lines.stream().map(ContentControl::new).collect(Collectors.toList());
    }
    
    public int size() {
        return this.controlList.size();
    }
    
    public Content content(int index) {
        ContentControl control = this.controlList.get(index);
        URL url = Title.class.getResource("/media/" + this.id + "/" + control.fileName);
        return new Content(control, url);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Title title = (Title) o;
        return Objects.equals(id, title.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
