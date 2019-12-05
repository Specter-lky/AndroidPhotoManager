package com.example.photoapp.common;

import android.widget.ListView;
import android.widget.ArrayAdapter;

import android.content.Context;

import java.util.ArrayList;

/**
 * Purely static non-instantiable helper methods class. Contains all common utility methods.
 * 
 * @author Ryan Brandt
 */
public final class Helpers {

    private Helpers() {}

    /**
     * ListView population helper
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

}