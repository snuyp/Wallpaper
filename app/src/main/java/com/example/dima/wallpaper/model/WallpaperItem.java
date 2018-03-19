package com.example.dima.wallpaper.model;

/**
 * Created by Dima on 16.03.2018.
 */

public class WallpaperItem {
    private String categoryId;
    private String imageUrl;

    public WallpaperItem() {
    }

    public WallpaperItem(String categoryId, String imageUrl) {
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
