package com.example.proyecto_iot.delegadoActividad;

import android.net.Uri;

public class CarouselModel {
    private Uri imageUri;
    private String title;

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
