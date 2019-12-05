package com.example.photoapp.ui.adddialog;

import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.photoapp.CreateAlbumActivity;

import static com.example.photoapp.MainActivity.*;

/**
 * Add Photo/Album dialog fragment. Manages redirection to create album screen
 *
 * @author Ryan Brandt
 */
public class AddDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("What would you like to do?")
                .setPositiveButton("Add Album", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent createAlbumIntent = new Intent(getActivity(), CreateAlbumActivity.class);
                        startActivity(createAlbumIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    } 
                });

        return builder.create();
    }

}
