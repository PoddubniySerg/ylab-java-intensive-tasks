package lesson04.eventsourcing;

public enum Actions {

    SAVE("save"),
    DELETE("delete");

    private final String action;

    Actions(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
