package com.example.sheikhanas.PublicationModel;

public class PublicationModel {
    private String PublicationDisc, ImageUrl;

    public PublicationModel(String publicationDisc, String imageUrl) {
        PublicationDisc = publicationDisc;
        ImageUrl = imageUrl;
    }

    public String getPublicationDisc() {
        return PublicationDisc;
    }

    public void setPublicationDisc(String publicationDisc) {
        PublicationDisc = publicationDisc;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
