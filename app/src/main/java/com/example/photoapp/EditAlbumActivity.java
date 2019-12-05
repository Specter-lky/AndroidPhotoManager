package com.example.photoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.photoapp.db.ApplicationDB;

import static com.example.photoapp.common.ContextProvider.setContext;
import static com.example.photoapp.common.Helpers.albumIsUnique;

/**
 * Simple activity to manage editing albums
 */
public class EditAlbumActivity extends AppCompatActivity {
    private ApplicationDB db;

    private Button submitButton;
    private EditText editedAlbumName;

    private String activeAlbumName;
    private int activeAlbumId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContext(this);
        setContentView(R.layout.activity_edit_album);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Album");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle extras = this.getIntent().getExtras();
        db = new ApplicationDB(this);

        activeAlbumId = extras.getInt("albumId");
        activeAlbumName = extras.getString("albumName");

        submitButton = (Button)findViewById(R.id.edit_album_submit);
        editedAlbumName = (EditText)findViewById(R.id.edit_album_name);

        editedAlbumName.setText(activeAlbumName);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleEdit();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();

        return true;
    }

    private void handleEdit(){
        final String name = editedAlbumName.getText().toString().trim();
        final boolean empty = name.isEmpty() || name.length() == 0;

        if(!empty){
            final boolean isUnique = albumIsUnique(db, name);

            if(!isUnique) {
                Toast.makeText(this, "Album name must be unique", Toast.LENGTH_SHORT).show();
            } else {
                db.editAlbum(activeAlbumId, name);
                finish();
                startActivity(new Intent(this, MainActivity.class));
            }
        } else{
            Toast.makeText(this, "Enter an album name", Toast.LENGTH_SHORT).show();
        }
    }

}
