package com.example.photoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.photoapp.db.ApplicationDB;

import static com.example.photoapp.common.ContextProvider.setContext;

public class CreateTagActivity extends AppCompatActivity {

    private ApplicationDB db;

    private int activePhotoId;

    private Button submitButton;

    private EditText newTagValue;
    private Spinner newTagTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContext(this);
        setContentView(R.layout.activity_create_tag);

        Bundle extras = this.getIntent().getExtras();
        activePhotoId = extras.getInt("photoId");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add New Tag");
        actionBar.setDisplayHomeAsUpEnabled(true);

        db = new ApplicationDB(this);

        submitButton = findViewById(R.id.create_tag_submit);
        newTagValue = findViewById(R.id.create_tag_value);
        newTagTypeSpinner = findViewById(R.id.create_tag_type);

        newTagTypeSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.tag, new String[] {"Person", "Location"}));

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
        final String value = newTagValue.getText().toString().trim();
        final String type = newTagTypeSpinner.getSelectedItem().toString();
        final boolean empty = value.isEmpty() || value.length() == 0;

        if(!empty){

            if(!isUnique()) {
                Toast.makeText(this, "Tag must be unique", Toast.LENGTH_SHORT).show();
            } else {
                db.createTag(activePhotoId, value, type);
                finish();
            }
        } else{
            Toast.makeText(this, "Enter a tag value", Toast.LENGTH_SHORT).show();
        }
    }

    // TODO
    private boolean isUnique() {
        return true;
    }

}
