package com.example.sheikhanas.ResearchModel;

public class ResearchModel {
    private String researchDiscription, ImageUrl;

    public ResearchModel(String researchDiscription, String imageUrl) {
        this.researchDiscription = researchDiscription;
        ImageUrl = imageUrl;
    }

    public String getResearchDiscription() {
        return researchDiscription;
    }

    public void setResearchDiscription(String researchDiscription) {
        this.researchDiscription = researchDiscription;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
