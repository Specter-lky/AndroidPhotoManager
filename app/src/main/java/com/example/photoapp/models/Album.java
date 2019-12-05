package com.example.photoapp.models;

public final class Album {
    private int id;
    private String name;

    public Album(String name){
        this.name = name;

    }

    public Album(int id, String name){
        this.id = id;
        this.name = name;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString(){
        return "Album - ID: "+id+" Name: "+name;
    }

}
