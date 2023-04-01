package io.ylab.intensive.lesson05.eventsourcing;

public enum Actions {

    SAVE("save"),
    DELETE("delete"),
    PRINT("print");

    private final String action;

    Actions(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
