package com.example.photoapp;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import java.util.ArrayList;

import android.database.Cursor;

import com.example.photoapp.models.Album;

import com.example.photoapp.db.ApplicationDB;

import static com.example.photoapp.common.ContextProvider.*;

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

        db = new ApplicationDB(this);

        submitButton = (Button)findViewById(R.id.create_album_submit);
        newAlbumName = (EditText)findViewById(R.id.create_album_name);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCreate();
            }
        });
    }

    public void handleCreate(){
        final String name = newAlbumName.getText().toString().trim();
        final boolean empty = name.isEmpty() || name.length() == 0;

        if(!empty){
            final boolean isUnique = isUnique(name);

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

    public boolean isUnique(String name){
        Cursor data = db.readAllAlbums();
        ArrayList<Album> albumList = new ArrayList<>();

        while(data.moveToNext()){
            albumList.add(new Album(data.getInt(0),data.getString(1)));
        }

        data.close();
        db.closeReadable();

        for(int i = 0; i< albumList.size();i++) {
            if (albumList.get(i).getName().equalsIgnoreCase(name)) {
                return false;
            }
        }

        return true;
    }

}
