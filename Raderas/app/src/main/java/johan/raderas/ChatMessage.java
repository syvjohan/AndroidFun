package johan.raderas;

/**
 * Created by johan on 2014-12-21.
 */
public class ChatMessage {
    String from;
    String message;
    String time;
    String id;

    public ChatMessage() {}

    public ChatMessage(String from, String message, String time, String id) {
        this.from = from;
        this.message = message;
        this.time = time;
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFrom() {
        return this.from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        time = timeStamp();
        return this.time;
    }

    public String timeStamp() {
        java.util.Date date= new java.util.Date();
        return date.toString();
    }
}
