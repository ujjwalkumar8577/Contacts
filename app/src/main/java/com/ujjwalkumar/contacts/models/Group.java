package com.ujjwalkumar.contacts.models;

public class Group {
    String id, name;

    public Group() {
        this.id = "";
        this.name = "NA";
    }

    public Group(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
