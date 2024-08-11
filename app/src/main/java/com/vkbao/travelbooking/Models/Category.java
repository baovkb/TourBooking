package com.vkbao.travelbooking.Models;

import androidx.annotation.NonNull;

public class Category {
    private String category_id;
    private String Name;
    private String ImagePath;

    public Category() {
    }

    public Category(String category_id, String name, String imagePath) {
        this.category_id = category_id;
        Name = name;
        ImagePath = imagePath;
    }

    @NonNull
    @Override
    public String toString() {
        return Name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}
