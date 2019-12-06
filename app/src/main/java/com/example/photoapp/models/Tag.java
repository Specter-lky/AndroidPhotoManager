package com.example.photoapp.models;

public class Tag {
    private int id;
    private String type;
    private String value;

    public Tag(int id, String type, String value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return type + ": " + value;
    }

}
