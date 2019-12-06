package com.example.photoapp.ui.photogallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.photoapp.R;
import com.example.photoapp.models.Photo;

import static com.example.photoapp.common.Helpers.*;

import java.util.ArrayList;

public class PhotoGalleryFragment extends Fragment {

    private PhotoGalleryViewModel photoGalleryViewModel;

    private ListView photoListView;
    private Button searchButton;
    private EditText personTagsInput;
    private EditText locationTagsInput;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        photoGalleryViewModel =
                ViewModelProviders.of(this).get(PhotoGalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        photoListView = root.findViewById(R.id.search_photo_list);
        searchButton = root.findViewById(R.id.search_submit);
        personTagsInput = root.findViewById(R.id.person_tags);
        locationTagsInput = root.findViewById(R.id.location_tags);

        photoGalleryViewModel.getPhotos().observe(this, new Observer<ArrayList<Photo>>() {
            @Override
            public void onChanged(ArrayList<Photo> photos) {
                populateListView(getContext(), R.layout.photo, photoListView, photos);
            }
        });
        return root;
    }
}