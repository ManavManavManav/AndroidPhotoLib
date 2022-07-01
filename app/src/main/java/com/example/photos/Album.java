package com.example.photos;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    private static final long serialVersionUID = 1L;
    String albumName;
    ArrayList<Photo> photoArrayList;


    public Album(String newName) {
        albumName = newName;
        photoArrayList = new ArrayList<Photo>();
    }

    public int getPhotoCount() {
        if(photoArrayList != null) {
            return photoArrayList.size();
        }
        else{
            return 0;
        }
    }

    public Photo getPhoto(int index) {
        return photoArrayList.get(index);
    }

    public ArrayList<Photo> getAlbum() {
        return photoArrayList;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void removePhoto(int index) {
        photoArrayList.remove(index);
    }

    public void setAlbumName(String myNewName) {
        albumName = myNewName;
    }

    public boolean equals(Album toComp) {
        return getAlbumName().equalsIgnoreCase(toComp.getAlbumName());
    }

    public void addPhoto(Photo a) {
        photoArrayList.add(a);
    }

    public String toString() {
        return albumName + ", " + getPhotoCount();
    }

    public static class albumChecker {

        public static boolean alreadyAlbum(String myTitle, ArrayList<Album> albums) {
            for (int i = 0; i < albums.size(); i++) {
                if (albums.get(i).equals(new Album(myTitle))) {
                    return true;
                }
            }
            return false;
        }

        public static ArrayList<String> AlbumToString (ArrayList<Album> albums){
            ArrayList<String> listStr = new ArrayList<String>();
            for(int i = 0; i< albums.size(); i++){
                listStr.add(albums.get(i).toString());
            }
            return listStr;
        }

    }
}
