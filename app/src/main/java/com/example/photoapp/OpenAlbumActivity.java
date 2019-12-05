package com.example.photoapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.photoapp.db.ApplicationDB;

import static com.example.photoapp.common.ContextProvider.setContext;

public class OpenAlbumActivity extends AppCompatActivity {

    private ApplicationDB db;

    private String activeAlbumName;
    private int activeAlbumId;

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();

        return true;
    }

}
