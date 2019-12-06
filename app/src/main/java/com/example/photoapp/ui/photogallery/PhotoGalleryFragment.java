package com.example.photoapp.ui.photogallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.photoapp.PhotoDisplayActivity;
import com.example.photoapp.R;
import com.example.photoapp.models.Photo;

import com.example.photoapp.ImageThumbnailAdapter;

import java.util.ArrayList;

public class PhotoGalleryFragment extends Fragment {

    private PhotoGalleryViewModel photoGalleryViewModel;

    private ArrayList<Photo> photosArrList;

    private ListView photoListView;
    private Button searchButton;
    private EditText tagsInput;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        photoGalleryViewModel =
                ViewModelProviders.of(this).get(PhotoGalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        photoListView = root.findViewById(R.id.search_photo_list);
        searchButton = root.findViewById(R.id.search_submit);
        tagsInput = root.findViewById(R.id.search_tags);

        photoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent viewPhotoIntent = new Intent(getContext(), PhotoDisplayActivity.class);
                viewPhotoIntent.putExtra("photoId", photosArrList.get(position).getID());
                viewPhotoIntent.putExtra("photoUri", Uri.parse(photosArrList.get(position).getPath()));
                startActivity(viewPhotoIntent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] tagValues = tagsInput.getText().toString().trim().isEmpty() ? new String[]{} : tagsInput.getText().toString().split(",");
                photoGalleryViewModel.filterPhotoList(tagValues);
            }
        });

        photoGalleryViewModel.getPhotos().observe(this, new Observer<ArrayList<Photo>>() {
            @Override
            public void onChanged(ArrayList<Photo> photos) {
                photosArrList = photos;
                ImageThumbnailAdapter adapter = new ImageThumbnailAdapter(getContext(), R.layout.photo_thumbnail, photos);
                photoListView.setAdapter(adapter);
            }
        });
        return root;
    }
}