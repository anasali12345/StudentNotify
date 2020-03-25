package com.example.sheikhanas.ScheduleModel;

public class ScheduleModel {
    private String scheduleDiscription, ImageUrl;

    public ScheduleModel(String scheduleDiscription, String imageUrl) {
        this.scheduleDiscription = scheduleDiscription;
        ImageUrl = imageUrl;
    }

    public String getScheduleDiscription() {
        return scheduleDiscription;
    }

    public void setScheduleDiscription(String scheduleDiscription) {
        this.scheduleDiscription = scheduleDiscription;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
