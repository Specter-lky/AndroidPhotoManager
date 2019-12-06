package com.example.photoapp.ui.photogallery;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import com.example.photoapp.db.ApplicationDB;

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

    public void filterPhotoList(String[] tagValues) {
        ArrayList<Photo> filteredPhotos = getPhotoList(db.readPhotos());
        db.closeReadable();


        if(tagValues.length > 0) {
            ArrayList<Integer> filteredPhotoIds = new ArrayList<>();
            Cursor data = db.readFilteredPhotoIds(tagValues);

            while (data.moveToNext()) {
                final int curId = data.getInt(0);
                if(!filteredPhotoIds.contains(curId)) {
                    filteredPhotoIds.add(curId);
                }
            }
            data.close();
            db.closeReadable();

            ArrayList<Photo> tmp = new ArrayList<>();

            for(Integer id : filteredPhotoIds ) {
                for( Photo p : filteredPhotos) {
                    if(p.getID() == id) {
                        tmp.add(p);
                        break;
                    }
                }
            }
            filteredPhotos = tmp;
        }
        mPhotoList.postValue(filteredPhotos);
    }

    private ArrayList<Photo> queryPhotoList() {
        ArrayList<Photo> photos = getPhotoList(db.readPhotos());
        db.closeReadable();
        return photos;
    }
}