package lesson04.eventsourcing;

public class Message {

    private String action;
    private String body;

    public Message() {
    }

    public Message(String action, String body) {
        this.action = action;
        this.body = body;
    }

    public String getAction() {
        return action;
    }

    public String getBody() {
        return body;
    }
}
