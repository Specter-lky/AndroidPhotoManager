package com.example.photoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import static com.example.photoapp.common.Helpers.*;

import com.example.photoapp.db.ApplicationDB;

import com.example.photoapp.models.Photo;

import java.util.ArrayList;

public class PhotoDisplayActivity extends AppCompatActivity {

    private ApplicationDB db;

    private ImageView photoDisplay;

    private Button slideNext;
    private Button slidePrev;
    private Button manageTags;

    private Uri activePhotoUri;
    private int activePhotoIndex;
    private int activePhotoId;
    private int activeAlbumId;

    private ArrayList<Photo> albumPhotoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_photo);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Your Photo");

        Bundle extras = this.getIntent().getExtras();
        activePhotoUri = (Uri)extras.get("photoUri");
        activePhotoId = extras.getInt("photoId");
        activePhotoIndex = extras.getInt("photoIndex");
        activeAlbumId = extras.getInt("albumId");

        db = new ApplicationDB(this);
        albumPhotoList = getPhotoList(db.readPhotosByAlbum(activeAlbumId));
        db.closeReadable();

        photoDisplay = findViewById(R.id.photo_display);
        Picasso.with(this).setLoggingEnabled(true);
        Picasso.with(this).load(activePhotoUri).into(photoDisplay);

        slideNext = findViewById(R.id.slide_next);
        slidePrev = findViewById(R.id.slide_prev);
        handleButtonLocks();

        /**
         * Slideshow controllers
         */

        slideNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activePhotoIndex += 1;
                activePhotoUri = Uri.parse(albumPhotoList.get(activePhotoIndex).getPath());
                activePhotoId = albumPhotoList.get(activePhotoIndex).getID();

                Picasso.with(PhotoDisplayActivity.this).load(activePhotoUri).into(photoDisplay);
                handleButtonLocks();
            }
        });

        slidePrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activePhotoIndex -= 1;
                activePhotoUri = Uri.parse(albumPhotoList.get(activePhotoIndex).getPath());
                activePhotoId = albumPhotoList.get(activePhotoIndex).getID();

                Picasso.with(PhotoDisplayActivity.this).load(activePhotoUri).into(photoDisplay);
                handleButtonLocks();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();

        return true;
    }

    /**
     * Utilities
     */

    private void handleButtonLocks() {
        final int lastPhotoIndex = albumPhotoList.size() - 1;

        if (activePhotoIndex >= lastPhotoIndex && slideNext.isEnabled()) {
            slideNext.setEnabled(false);
        } else if (activePhotoIndex < lastPhotoIndex && !slideNext.isEnabled()) {
            slideNext.setEnabled(true);
        }

        if (activePhotoIndex <= 0 && slidePrev.isEnabled()) {
           slidePrev.setEnabled(false);
        } else if (activePhotoIndex > 0 && !slidePrev.isEnabled()) {
            slidePrev.setEnabled(true);
        }
    }
}
