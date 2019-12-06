package com.example.photoapp.ui.photogallery;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import com.example.photoapp.db.ApplicationDB;

import com.example.photoapp.models.Album;
import com.example.photoapp.models.Photo;

import static com.example.photoapp.common.ContextProvider.*;
import static com.example.photoapp.common.Helpers.*;


public class PhotoGalleryViewModel extends ViewModel {

    private ApplicationDB db;

    private MutableLiveData<ArrayList<Photo>> mPhotoList;

    public PhotoGalleryViewModel() {
        db = new ApplicationDB(getContext());

        mPhotoList = new MutableLiveData<>();
        mPhotoList.setValue(queryPhotoList());
    }

    public LiveData<ArrayList<Photo>> getPhotos() { return mPhotoList; }

    private ArrayList<Photo> queryPhotoList() {
        ArrayList<Photo> photos = getPhotoList(db.readPhotos());
        db.closeReadable();
        return photos;
    }
}