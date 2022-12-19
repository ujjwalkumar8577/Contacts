package com.ujjwalkumar.contacts.models;

public class Contact {
    String id, name, group;
    long number, timestamp;

    public Contact() {
        this.id = "";
        this.name = "";
        this.number = 0L;
        this.group = "NA";
        this.timestamp = 0L;
    }

    public Contact(String id, String name, long number, String group, long timestamp) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.group = group;
        this.timestamp = timestamp;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
