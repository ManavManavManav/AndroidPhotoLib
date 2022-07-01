package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SlideShowActivity extends AppCompatActivity {

    Album selAlbum;
    int selectionIndex;
    int selPhoto;
    Photo curr;
    Button back;
    Button next;
    Button prev;
    ImageView imageView;
    TextView caption;
    TextView date;
    ArrayList<Album>albums;

    private void update(){
        caption.setText(curr.getCaption());
        date.setText(curr.getDate());
        imageView.setImageURI(Uri.parse(curr.getImageFile()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);

        Intent intent1 = getIntent();
        this.albums = (ArrayList<Album>) intent1.getSerializableExtra("albums");
        this.selectionIndex = intent1.getIntExtra("selectedAlbum", -1);
        this.selPhoto = intent1.getIntExtra("selectedPhoto", -1);
        selAlbum = albums.get(selectionIndex);
        curr = selAlbum.getPhoto(selPhoto);


        caption = findViewById(R.id.SlideShowCaptionView);
        date = findViewById(R.id.SlideShowDateBox);
        imageView = findViewById(R.id.slideShowImageView);


        next = findViewById(R.id.nextSlideShowButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selPhoto < selAlbum.getPhotoCount()-1){
                    selPhoto++;
                    curr = selAlbum.getPhoto(selPhoto);
                    update();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SlideShowActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Album border");
                    builder.setMessage("Cant go further");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
            }
        });

        prev = findViewById(R.id.prevSlideShowButton);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selPhoto > 0){
                    selPhoto--;
                    curr = selAlbum.getPhoto(selPhoto);
                    update();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SlideShowActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Album border");
                    builder.setMessage("Cant go further");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
            }
        });

        back = findViewById(R.id.BackToDisplayImageButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SlideShowActivity.this, PhotoDisplayActivity.class);
                intent.putExtra("albums", albums);
                intent.putExtra("selectedAlbum", selectionIndex);
                intent.putExtra("selectedPhoto", selPhoto);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });

        update();
    }


}