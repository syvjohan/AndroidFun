package johan.laboration2;

/**
 * Created by johan on 2014-12-09.
 */
public class Group {
    private String name;
    private String id;

    public Group(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String SetName(String name) {
        return this.name = name;
    }

    public String SetId(String id) {
        return this.id = id;
    }

    public String GetName() {
        return name;
    }

    public String GetId() {
        return id;
    }

}
