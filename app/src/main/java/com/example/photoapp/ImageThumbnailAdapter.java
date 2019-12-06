package com.example.photoapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.photoapp.models.Photo;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Custom ListView adapter to present image thumbnails
 *
 * @author Ryan Brandt
 */
public class ImageThumbnailAdapter extends ArrayAdapter {
    private int resourceLayout;
    private Context mContext;

    public ImageThumbnailAdapter(Context context, int resource, ArrayList<Photo> photos) {
        super(context, resource, photos);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Photo p = (Photo)getItem(position);

        if(p != null) {
            ImageView imageView = v.findViewById(R.id.thumbnail);
            Picasso.with(getContext()).load(Uri.parse(p.getPath())).into(imageView);
        }

        return v;
    }

}
