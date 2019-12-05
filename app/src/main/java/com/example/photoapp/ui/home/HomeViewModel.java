package com.example.photoapp.ui.home;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import com.example.photoapp.db.ApplicationDB;

import com.example.photoapp.models.Album;

import static com.example.photoapp.common.ContextProvider.*;

/**
 * Home view fragment model. Handles loading in home screen data (previous session & albums)
 * 
 * @author Ryan Brandt
 */
public class HomeViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Album>> albumList;

    private ApplicationDB db;

    public HomeViewModel() {
        db = new ApplicationDB(getContext());
        albumList = new MutableLiveData<>();
        albumList.setValue(queryAlbumList());
    }

    public LiveData<ArrayList<Album>> getAlbumList() {
        return albumList;
    }

    private ArrayList<Album> queryAlbumList() {
        ArrayList<Album> albums = new ArrayList<Album>();
        Cursor data =  db.readAllAlbums();

        while(data.moveToNext()) {
            albums.add(new Album(data.getInt(0), data.getString(1)));
        }

        data.close();
        db.closeReadable();

        return albums;
    }

    public void forceRefresh() {
        albumList.postValue(queryAlbumList());
    }

}