package com.example.sheikhanas.EventModel;

public class EventModel {
    private String EventDescription, ImageUrl;

    public EventModel(String eventDescription, String imageUrl) {
        EventDescription = eventDescription;
        ImageUrl = imageUrl;
    }

    public String getEventDescription() {
        return EventDescription;
    }

    public void setEventDescription(String eventDescription) {
        EventDescription = eventDescription;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
