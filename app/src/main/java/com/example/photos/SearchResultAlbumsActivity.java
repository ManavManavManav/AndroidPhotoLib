package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchResultAlbumsActivity extends AppCompatActivity {

    ArrayList<Album> albumArrayList;
    int selectedalbumindex = 0;
    Album selectedAlbum;

    TextView displayAlbumButton;
    Button backButton;
    Button saveButton;
    ListView imageListView;
    Button editBUtton;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_albums);

        imageListView=findViewById(R.id.searchListView);

        Intent myIntent = getIntent();
        this.albumArrayList = (ArrayList<Album>) myIntent.getSerializableExtra("album");
        selectedalbumindex=albumArrayList.size()-1;
        selectedAlbum = this.albumArrayList.get(selectedalbumindex);


        updateList();
    }


    private void updateList(){
        ImageAdapter imageAdapter = new ImageAdapter(this, selectedAlbum);
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
                    selectedAlbum.addPhoto(new Photo(uri.toString()));
                    whetherAdded = true;
                }
            }else{
                Uri uri = data.getData();
                if(uri != null){
                    selectedAlbum.addPhoto(new Photo(uri.toString()));
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
            this.selectedalbumindex = data.getIntExtra("selectedAlbum", -1);
            if(selectedalbumindex == -1) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("albums", albumArrayList);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            else{
                selectedAlbum = albumArrayList.get(selectedalbumindex);
                displayAlbumButton.setText(selectedAlbum.getAlbumName());
                updateList();
            }
        }
        else if(requestCode == 7){
            this.albumArrayList = (ArrayList<Album>) data.getSerializableExtra("albums");
            this.selectedalbumindex = data.getIntExtra("selectedAlbum", -1);
            if(selectedalbumindex != -1){
                this.selectedAlbum = albumArrayList.get(selectedalbumindex);
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