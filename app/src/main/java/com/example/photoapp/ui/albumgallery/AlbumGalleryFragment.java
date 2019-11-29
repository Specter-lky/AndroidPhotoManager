package com.example.photoapp.ui.albumgallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.photoapp.R;

public class AlbumGalleryFragment extends Fragment {

    private AlbumGalleryViewModel albumGalleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        albumGalleryViewModel =
                ViewModelProviders.of(this).get(AlbumGalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_album_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        albumGalleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}