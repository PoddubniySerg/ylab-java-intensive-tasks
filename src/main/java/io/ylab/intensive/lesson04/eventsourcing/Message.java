package io.ylab.intensive.lesson04.eventsourcing;

public class Message {

    private String action;
    private String content;

    public Message() {
    }

    public Message(String action, String content) {
        this.action = action;
        this.content = content;
    }

    public String getAction() {
        return action;
    }

    public String getContent() {
        return content;
    }
}
