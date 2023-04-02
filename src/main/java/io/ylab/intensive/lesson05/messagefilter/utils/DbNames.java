package io.ylab.intensive.lesson05.messagefilter.utils;

public enum DbNames {

    TABLE("filters"),
    COLUMN("forbidden");

    private final String name;

    DbNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
