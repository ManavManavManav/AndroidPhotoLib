package com.example.photos;

import android.graphics.Bitmap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Photo implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private String caption="";
    private Calendar time;
    private ArrayList<Tag> tagArrayList;
    private String location;

    public Photo(String pictureLocation) {
        caption = "";
        time = Calendar.getInstance();
        time.set(Calendar.MILLISECOND, 0);
        tagArrayList = new ArrayList<Tag>();

        location = pictureLocation;
    }

    public String getImageFile() {
        return this.location;
    }

    public String getDate() {
        return time.getTime().toString();
    }

    public void addTag(Tag a) {
        tagArrayList.add(a);
    }

    public void setTag(ArrayList<Tag> tagList) {
        this.tagArrayList = tagList;
    }

    public ArrayList<Tag> getTagArrayList() {
        return tagArrayList;
    }

    public void setCaption(String myCaption) {
        caption = myCaption;
    }

    public Photo clone() throws CloneNotSupportedException {
        return (Photo) super.clone();
    }

    public String getCaption() {
        return caption;
    }



}
