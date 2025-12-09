package com.example.tutorial.models;

public class RoadmapNode {
    private String title;
    private String description;
    private boolean unlocked;

    public RoadmapNode(String title, String description, boolean unlocked) {
        this.title = title;
        this.description = description;
        this.unlocked = unlocked;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
}

