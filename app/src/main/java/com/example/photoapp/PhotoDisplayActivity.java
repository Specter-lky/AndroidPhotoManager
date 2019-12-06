package com.example.photoapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.photoapp.models.Tag;
import com.squareup.picasso.Picasso;

import static com.example.photoapp.common.Helpers.*;
import static com.example.photoapp.common.ContextProvider.*;

import com.example.photoapp.db.ApplicationDB;

import com.example.photoapp.models.Photo;

import java.util.ArrayList;

/**
 * Photo display activity. Manages displaying photo, tags, and corresponding logic.
 *
 * @author Ryan Brandt
 */
public class PhotoDisplayActivity extends AppCompatActivity {

    private ApplicationDB db;

    private ImageView photoDisplay;
    private ListView tagListView;

    private Button slideNext;
    private Button slidePrev;
    private Button addTag;
    private Button deleteTag;

    private Uri activePhotoUri;
    private int activePhotoIndex;
    private int activePhotoId;
    private int activeAlbumId;

    private int activeTagId;
    private ArrayList<Tag> tagArrList;

    private ArrayList<Photo> albumPhotoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContext(this);
        setContentView(R.layout.activity_display_photo);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Your Photo");

        activeTagId = -1;

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

        tagListView = findViewById(R.id.tag_list);
        loadTags();

        tagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                activeTagId = tagArrList.get(position).getId();
                view.setSelected(true);
            }
        });

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
                loadTags();
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
                loadTags();
            }
        });

        addTag = findViewById(R.id.tag_add);
        deleteTag = findViewById(R.id.tag_delete);

        /**
         * Click Listeners
         */

        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createTagIntent = new Intent(PhotoDisplayActivity.this, CreateTagActivity.class);
                createTagIntent.putExtra("photoId", activePhotoId);
                startActivity(createTagIntent);
            }
        });

        deleteTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tagSelected()) {
                    db.deleteTag(activeTagId);
                    loadTags();
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
    public void onResume() {
        super.onResume();
        loadTags();
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

    private void loadTags() {
        Cursor data = db.readTagsByPhoto(activePhotoId);
        tagArrList = new ArrayList<>();

        while(data.moveToNext()) {
            tagArrList.add(new Tag(data.getInt(0), data.getString(1), data.getString(2)));
        }

        populateListView(this, R.layout.tag, tagListView, tagArrList);
        db.closeReadable();
    }

    private boolean tagSelected() {
        if(activeTagId == -1) {
            Toast.makeText(this, "You need to select a tag first.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
