package com.example.e_commerce.model;

public class Category {

    String super_category;
    String super_category_id;
    String category_id;

    String image;
    String image_format;
    String name;


    public Category(String super_category, String id, String image, String image_format, String name) {
        this.super_category = super_category;
         this.image = image;
        this.image_format = image_format;
        this.name = name;
    }

    public Category() {
    }

    public String getSuper_category_id() {
        return super_category_id;
    }

    public void setSuper_category_id(String super_category_id) {
        this.super_category_id = super_category_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public Category(String category_id) {
        this.category_id = category_id;
    }





    public String getSuper_category() {
        return super_category;
    }

    public void setSuper_category(String super_category) {
        this.super_category = super_category;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_format() {
        return image_format;
    }

    public void setImage_format(String image_format) {
        this.image_format = image_format;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "category_id='" + category_id + '\'' +
                '}';
    }

}
