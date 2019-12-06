package com.example.photoapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.photoapp.db.ApplicationDB;
import com.example.photoapp.models.Photo;

import java.util.ArrayList;

import static com.example.photoapp.common.ContextProvider.setContext;
import static com.example.photoapp.common.Helpers.*;

public class OpenAlbumActivity extends AppCompatActivity {

    private static final int PHOTO_PICKER_REQUEST = 1;

    private ApplicationDB db;

    private ListView albumPhotoListView;

    private Button addPhoto;
    private Button deletePhoto;
    private Button viewPhoto;

    private int activePhotoId;
    private String activePhotoPath;
    private int activePhotoIndex;

    private String activeAlbumName;
    private int activeAlbumId;

    private ArrayList<Photo> photoArrList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContext(this);
        setContentView(R.layout.activity_open_album);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle extras = this.getIntent().getExtras();
        activeAlbumName = extras.getString("albumName");
        activeAlbumId = extras.getInt("albumId");

        actionBar.setTitle(activeAlbumName);

        db = new ApplicationDB(this);

        albumPhotoListView = findViewById(R.id.album_photo_list);
        photoArrList = new ArrayList<>();
        loadAlbumPhotos();

        albumPhotoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                activePhotoId = photoArrList.get(position).getID();
                activePhotoPath = photoArrList.get(position).getPath();
                activePhotoIndex = position;
                view.setSelected(true);
            }
        });

        addPhoto = findViewById(R.id.add_photo);
        deletePhoto = findViewById(R.id.delete_photo);
        viewPhoto = findViewById(R.id.view_photo);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent();
                photoPickerIntent.setType("image/*");
                photoPickerIntent.setAction(Intent.ACTION_PICK);
                startActivityForResult(photoPickerIntent, PHOTO_PICKER_REQUEST);
            }
        });

        deletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemSelected()) {
                    db.deletePhoto(activePhotoId);
                    clearSelection();
                }
            }
        });

        viewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemSelected()) {
                    Intent photoDisplayIntent = new Intent(OpenAlbumActivity.this, PhotoDisplayActivity.class);
                    photoDisplayIntent.putExtra("photoUri", Uri.parse(activePhotoPath));
                    photoDisplayIntent.putExtra("photoId", activePhotoId);
                    photoDisplayIntent.putExtra("photoIndex", activePhotoIndex);
                    photoDisplayIntent.putExtra("albumId", activeAlbumId);
                    startActivity(photoDisplayIntent);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == 1) {
                String selectedImageUri = data.getData().toString();
                db.createPhoto(activeAlbumId, selectedImageUri);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        clearSelection();
    }

    /**
     * Utilities
     */

    private void loadAlbumPhotos() {
        Cursor data = db.readPhotosByAlbum(activeAlbumId);
        photoArrList = getPhotoList(data);

        db.closeReadable();
        populateListView(this, R.layout.photo, albumPhotoListView, photoArrList);
    }

    private boolean itemSelected() {
        if(activePhotoId == -1) {
            Toast.makeText(this, "You need to select an photo first.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void clearSelection() {
        loadAlbumPhotos();
        activePhotoId = -1;
        activePhotoIndex = -1;
        activePhotoPath = null;
    }

}
