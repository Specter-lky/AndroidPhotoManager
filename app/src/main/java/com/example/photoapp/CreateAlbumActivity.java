package com.example.photoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import static com.example.photoapp.common.Helpers.*;

import com.example.photoapp.db.ApplicationDB;

import static com.example.photoapp.common.ContextProvider.setContext;

/**
 * Simple activity to handle creating new Albums
 */
public class CreateAlbumActivity extends AppCompatActivity {

    private ApplicationDB db;

    private Button submitButton;
    private EditText newAlbumName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContext(this);
        setContentView(R.layout.activity_create_album);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create New Album");
        actionBar.setDisplayHomeAsUpEnabled(true);

        db = new ApplicationDB(this);

        submitButton = findViewById(R.id.create_album_submit);
        newAlbumName = findViewById(R.id.create_album_name);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCreate();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();

        return true;
    }

    private void handleCreate(){
        final String name = newAlbumName.getText().toString().trim();
        final boolean empty = name.isEmpty() || name.length() == 0;

        if(!empty){
            final boolean isUnique = albumIsUnique(db, name);

            if(!isUnique) {
                Toast.makeText(this, "Album name must be unique", Toast.LENGTH_SHORT).show();
            } else {
                db.createAlbum(name);
                finish();
                startActivity(new Intent(this, MainActivity.class));
            }
        } else{
            Toast.makeText(this, "Enter an album name", Toast.LENGTH_SHORT).show();
        }
    }

}
