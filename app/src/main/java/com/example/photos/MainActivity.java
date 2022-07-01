package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    ListView albumView;
    ArrayList<Album> albums;

    private Button addAlbum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        albums = FileController.loadAlbums(MainActivity.this);


        addAlbum = findViewById(R.id.mainAddAlbumButton);
        addAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AddAlbumActivity.class);
                intent.putExtra("album", albums);
                changeActivity(0, intent);
            }
        });

        albumView = (ListView)findViewById(R.id.mainAlbumListView);
        albumView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                view.setSelected(true);
                Intent intent = new Intent(MainActivity.this, AlbumViewerActivity.class);
                intent.putExtra("album", albums);
                intent.putExtra("selectedAlbum", position);
                changeActivity(1, intent);

            }
        });

        updateList();
    }

    private void changeActivity(int requestCode, Intent intent){
        startActivityForResult(intent, requestCode);
    }

    private void updateList(){
        ArrayList<String> list = Album.albumChecker.AlbumToString(albums);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        albumView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if(resultCode == Activity.RESULT_OK) {
                this.albums = (ArrayList<Album>) data.getSerializableExtra("albums");
                updateList();
            }
        }else if(requestCode == 1){
            this.albums = (ArrayList<Album>) data.getSerializableExtra("albums");
            updateList();
        }
    }

}