package com.example.kirito.simplenote.entity;

/**
 * Created by kirito on 2017.02.14.
 */

public class Item {
    private String title;
    private String time;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Item() {
    }

    public Item(String title, String time, int id) {
        this.title = title;
        this.time = time;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
