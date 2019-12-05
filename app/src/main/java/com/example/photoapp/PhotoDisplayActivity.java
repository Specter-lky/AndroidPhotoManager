package com.example.photoapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class PhotoDisplayActivity extends AppCompatActivity {

    private ImageView photoDisplay;

    private Button slideshowLauncher;
    private Button manageTags;

    private Uri activePhotoUri;
    private int activePhotoId;

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

        photoDisplay = findViewById(R.id.photo_display);
        Picasso.with(this).load(activePhotoUri).into(photoDisplay);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();

        return true;
    }
}
