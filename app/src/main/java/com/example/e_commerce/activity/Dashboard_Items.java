package com.example.e_commerce.activity;

public class Dashboard_Items  {

    String title;

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public Dashboard_Items(String actions) {
        this.actions = actions;
    }

    String actions;
    int image;

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }



    public Dashboard_Items(String title,  int image,String actions) {
        this.title = title;
        this.actions = actions;
        this.image = image;
    }
}
