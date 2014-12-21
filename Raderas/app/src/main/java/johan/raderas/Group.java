package johan.raderas;

/**
 * Created by johan on 2014-12-20.
 */
public class Group {
    private String ID;
    private String name;

    public Group() {}

    public Group(String name, String ID) {
        this.name = name;
        this.ID = ID;
    }
    public String getID() {
        return ID;
    }
    public String getName() {
        return name;
    }
}
