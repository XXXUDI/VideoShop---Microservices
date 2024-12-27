package com.socompany.core.event;

public class VideoEvent {

    private long id;
    private String title;
    private String description;
    private float price;
    private String action;

    public VideoEvent(long id, String title, String description, float price, String action) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.action = action;
    }

    public VideoEvent() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
