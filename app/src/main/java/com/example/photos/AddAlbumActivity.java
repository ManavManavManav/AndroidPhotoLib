package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class AddAlbumActivity extends AppCompatActivity {

    private ArrayList<Album> albums;

    Button makeAlbumConfirmButton;
    Button dontMakeAlbumButton;
    EditText albumNameInputBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_album);

        Intent myIntent = getIntent();
        this.albums = (ArrayList<Album>) myIntent.getSerializableExtra("album");

        albumNameInputBox = findViewById(R.id.newAlbumCreateNameInputBox);

        makeAlbumConfirmButton = findViewById(R.id.makeAlbumConfirmButton);
        makeAlbumConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String albumInput = albumNameInputBox.getText().toString();
                boolean whetherSave = false;
                if(albumInput != null){
                    if(albumInput.length() > 0){
                        whetherSave = true;
                    }
                }
                if(whetherSave == true) {
                    if (Album.albumChecker.alreadyAlbum(albumInput, albums) == true) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(AddAlbumActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("Already Present");
                        builder.setMessage("album exists");
                        builder.setPositiveButton("OK", null);
                        builder.show();
                    } else {
                        Album album2 = new Album(albumInput);
                        albums.add(album2);
                        FileController.saveAlbum(AddAlbumActivity.this, albums);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("albums", albums);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                }
            }
        });

        dontMakeAlbumButton = findViewById(R.id.dontMakeAlbumButton);
        dontMakeAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }
        });

    }
}