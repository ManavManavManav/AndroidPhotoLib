package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AlbumViewerActivity extends AppCompatActivity {

    TextView displayAlbumButton;
    Button backButton;
    Button saveButton;
    ListView imageListView;
    Button editBUtton;
    Button searchButton;
    ArrayList<Album> albumArrayList;
    int selectionIndex = 0;
    Album selAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_viewer);

        Intent myIntent = getIntent();
        this.albumArrayList = (ArrayList<Album>) myIntent.getSerializableExtra("album");
        this.selectionIndex = myIntent.getIntExtra("selectedAlbum", -1);
        if(selectionIndex == -1){
            return;
        }
        selAlbum = this.albumArrayList.get(selectionIndex);

        displayAlbumButton = findViewById(R.id.showAlbumNameView);
        String albumText = selAlbum.getAlbumName();
        displayAlbumButton.setText(albumText);

        saveButton = findViewById(R.id.AddImageButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);


                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 5);
                }
            }});

        backButton = findViewById(R.id.AlbumViewBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("albums", albumArrayList);
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }
        });

        editBUtton = findViewById(R.id.EditAlbumButton);
        editBUtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlbumViewerActivity.this, EditAlbumActivity.class);
                intent.putExtra("albums", albumArrayList);
                intent.putExtra("selectedAlbum", selectionIndex);
                startActivityForResult(intent, 10);
            }});

        searchButton=findViewById(R.id.searchFilterButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("SEARCH");
                Intent intent=new Intent(AlbumViewerActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });



        imageListView = findViewById(R.id.PhotoAlbumPageListView);
        imageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                view.setSelected(true);
                Intent intent = new Intent(AlbumViewerActivity.this, PhotoDisplayActivity.class);
                intent.putExtra("albums", albumArrayList);
                intent.putExtra("selectedAlbum", selectionIndex);
                intent.putExtra("selectedPhoto", position);
                startActivityForResult(intent, 7);
            }
        });

        updateList();
    }


    private void updateList(){
        ImageAdapter imageAdapter = new ImageAdapter(this, selAlbum);
        imageListView.setAdapter(imageAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            ClipData dataClipData = data.getClipData();
            boolean whetherAdded = false;

            if(dataClipData != null) {
                for (int i = 0; i < dataClipData.getItemCount(); i++) {
                    ClipData.Item item = dataClipData.getItemAt(i);
                    Uri uri = item.getUri();
                    selAlbum.addPhoto(new Photo(uri.toString()));
                    whetherAdded = true;
                }
            }else{
                Uri uri = data.getData();
                if(uri != null){
                    selAlbum.addPhoto(new Photo(uri.toString()));
                    whetherAdded = true;
                }
            }
            if(whetherAdded == true) {
                FileController.saveAlbum(this, albumArrayList);
                updateList();
            }

        }
        else if(requestCode == 10){
            this.albumArrayList = (ArrayList<Album>) data.getSerializableExtra("albums");
            this.selectionIndex = data.getIntExtra("selectedAlbum", -1);
            if(selectionIndex == -1) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("albums", albumArrayList);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            else{
                selAlbum = albumArrayList.get(selectionIndex);
                displayAlbumButton.setText(selAlbum.getAlbumName());
                updateList();
            }
        }
        else if(requestCode == 7){
            this.albumArrayList = (ArrayList<Album>) data.getSerializableExtra("albums");
            this.selectionIndex = data.getIntExtra("selectedAlbum", -1);
            if(selectionIndex != -1){
                this.selAlbum = albumArrayList.get(selectionIndex);
                updateList();
            }
            else{
                Intent returnIntent = new Intent();
                returnIntent.putExtra("albums", albumArrayList);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }




}