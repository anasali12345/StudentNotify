package com.example.sheikhanas.ProjectModel;

public class ProjectModel {
    private String ProjectDiscription, ImageUrl;

    public ProjectModel(String projectDiscription, String imageUrl) {
        ProjectDiscription = projectDiscription;
        ImageUrl = imageUrl;
    }

    public String getProjectDiscription() {
        return ProjectDiscription;
    }

    public void setProjectDiscription(String projectDiscription) {
        ProjectDiscription = projectDiscription;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
