package com.example.photos;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ImageAdapter extends ArrayAdapter {


    private Activity context;
    private Album selAlbum;


    public ImageAdapter(Activity context, Album selectedAlbum) {
        super(context, R.layout.image_item, selectedAlbum.getAlbum());
        this.context = context;

        this.selAlbum = selectedAlbum;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rank=convertView;
        LayoutInflater inflater = context.getLayoutInflater();

        if(convertView==null)
            rank = inflater.inflate(R.layout.image_item, null, true);

        ImageView thumbnail = (ImageView) rank.findViewById(R.id.imageTMB);
        TextView caption = (TextView) rank.findViewById(R.id.textView69);

        Photo photo = selAlbum.getPhoto(position);
        caption.setText(photo.getCaption());

        try{
                thumbnail.setImageURI(Uri.parse(photo.getImageFile()));

        }catch(Exception e){
            e.printStackTrace();
        }

        return  rank;
    }
}
