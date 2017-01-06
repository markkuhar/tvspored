package com.kuhar.kos.tvspored;

/**
 * Created by Mark on 5. 01. 2017.
 */

public class MainItem {
    private String title;
    private String description;

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    private String startTime;
    private String endTime;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MainItem(String title, String description, String startTime, String endTime) {
        super();
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
