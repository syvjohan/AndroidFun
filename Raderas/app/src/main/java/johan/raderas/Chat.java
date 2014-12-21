package johan.raderas;

/**
 * Created by johan on 2014-12-21.
 */
public class Chat {
    private String from;
    private String message;
    private String time;

    public Chat() {}

    public Chat(String from, String message, String time) {
        this.from = from;
        this.message = message;
        this.time = time;
    }
    public String getFrom() {
        return from;
    }
    public String getMessage() {
        return message;
    }

    public String getTime() {
        time = timeStamp();
        return time;
    }

    public String timeStamp() {
        java.util.Date date= new java.util.Date();
        return date.toString();
    }
}
