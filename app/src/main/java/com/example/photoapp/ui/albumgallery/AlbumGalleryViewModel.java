package com.example.photoapp.ui.albumgallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlbumGalleryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AlbumGalleryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is album gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}