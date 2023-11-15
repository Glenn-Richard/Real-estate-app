package com.example.realestatemanager.models;

import java.io.Serializable;

public class Photo implements Serializable {

    private String url;
    private String description;

    public Photo(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public Photo() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
