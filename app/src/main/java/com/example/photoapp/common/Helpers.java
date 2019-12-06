package com.example.photoapp.common;

import android.database.Cursor;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import android.content.Context;

import com.example.photoapp.models.Album;

import com.example.photoapp.db.ApplicationDB;
import com.example.photoapp.models.Photo;
import com.example.photoapp.models.Tag;

import java.util.ArrayList;

/**
 * Purely static non-instantiable helper methods class. Contains all common utility methods.
 * 
 * @author Ryan Brandt
 */
public final class Helpers {

    private Helpers() {}

    /**
     * Generic ListView population helper
     *
     * @param context Current activity context
     * @param lv ListView instance to be populated
     * @param resource The ListView type
     * @param content The ArrayList containing the contents to be inserted
     */
    public static <T extends Object> void populateListView (Context context, int resource, ListView lv, ArrayList<T> content) {
        ArrayAdapter<T> adapter = new ArrayAdapter<T>(context, resource, content);
        lv.setAdapter(adapter);
    }

    /**
     * Utility to validate album name uniqueness
     *
     * @param db ApplicationDB instance
     * @param name Album name in question
     * @return boolean representing if is unique
     */
    public static boolean albumIsUnique (ApplicationDB db, String name) {
        Cursor data = db.readAllAlbums();
        ArrayList<Album> albumList = new ArrayList<>();

        while(data.moveToNext()){
            albumList.add(new Album(data.getInt(0),data.getString(1)));
        }

        data.close();
        db.closeReadable();

        for(int i = 0; i< albumList.size();i++) {
            if (albumList.get(i).getName().equalsIgnoreCase(name)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Helper to read query data in a list of Photo instances
     *
     * @param data A cursor pointing at Photo rows
     * @return An ArrayList containing the corresponding data in Photo objects
     */
    public static ArrayList<Photo> getPhotoList(Cursor data) {
        ArrayList<Photo> photos = new ArrayList<>();

        while(data.moveToNext()) {
            photos.add(new Photo(data.getInt(0), data.getInt(1), data.getString(2), data.getString(3)));
        }

        return photos;
    }

    /**
     * Helper to read query data into a list of Tag instances
     *
     * @param data A cursor pointing at Tag rows
     * @return An ArrayList containg the corresponding data in Tag objects
     */
    public static ArrayList<Tag> getTagList(Cursor data) {
        ArrayList<Tag> tags = new ArrayList<>();

        while(data.moveToNext()) {
            tags.add(new Tag(data.getInt(0), data.getString(1), data.getString(2)));
        }

        return tags;
    }

}