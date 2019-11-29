package com.example.photoapp.ui.adddialog;

import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Add Photo/Album dialog fragment. Manages loading in new photos, and redirecting to create album screen.
 *
 * @author Ryan Brandt
 */
public class AddDialogFragment extends DialogFragment {
    public static final int PHOTO_GALLERY_REQUEST = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("What would you like to add?")
                .setPositiveButton("Add Photo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, PHOTO_GALLERY_REQUEST);
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setNegativeButton("Add Album", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // redirect to add album screen
                    }
                });

        return builder.create();
    }

}
