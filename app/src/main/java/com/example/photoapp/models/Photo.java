package com.example.photoapp.models;

public class Photo {
    private String path;
    private String caption;
    private int id;
    private int albumId;

    public Photo(int id, int albumId, String path, String caption){
        this.id = id;
        this.albumId = albumId;
        this.path = path;
        this.caption = caption;
    }

    public String getPath(){
        return path;
    }

    public String getCaption() {
        return caption;
    }

    public int getID() {
        return id;
    }

    public int getAlbumID() {
        return albumId;
    }

    public String toString() {
        return path;
    }
}
