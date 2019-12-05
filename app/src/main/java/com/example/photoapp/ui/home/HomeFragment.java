package com.example.photoapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import static com.example.photoapp.common.Helpers.*;

import com.example.photoapp.db.ApplicationDB;

import com.example.photoapp.models.Album;

import com.example.photoapp.R;

/**
 * Main Home fragment. Displays Album List and handles all list related events.
 *
 * @author Ryan Brandt
 */
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private ApplicationDB db;

    private ArrayList<Album> albumList;
    private int activeAlbumId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        db = new ApplicationDB(getContext());

        this.homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        activeAlbumId = -1;
        albumList = new ArrayList<Album>();
        
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final ListView albumListView = root.findViewById(R.id.album_list);
        final Button editButton = root.findViewById(R.id.edit_album);
        final Button openButton = root.findViewById(R.id.open_album);
        final Button deleteButton = root.findViewById(R.id.delete_album);

        homeViewModel.getAlbumList().observe(this, new Observer<ArrayList<Album>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Album> albums) {
                albumList.clear();
                if(albums.size() > 0) {
                    albumList = albums;
                } else {
                    Toast.makeText(getContext(), "You don't have any albums. Add one to get started.", Toast.LENGTH_LONG).show();
                }
                populateListView(getActivity(), R.layout.album, albumListView, albums);
            }
        });

        /**
         * Event Listeners
         */
        albumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activeAlbumId = albumList.get(position).getId();
                view.setSelected(true);
            }
        });

        deleteButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemSelected()) {
                    db.deleteAlbum(activeAlbumId);
                    homeViewModel.forceRefresh();
                }
            }
        });

        editButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemSelected()) {
                    // do stuff
                }
            }
        });

        openButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemSelected()) {
                    // do stuff
                }
            }
        });

        return root;
    }

    /**
     * Utilities
     */
    private boolean itemSelected() {
        if(activeAlbumId == -1) {
            Toast.makeText(getContext(), "You need to select an album first.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}