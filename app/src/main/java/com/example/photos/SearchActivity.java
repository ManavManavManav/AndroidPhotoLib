package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ArrayList<String> locs=new ArrayList<String>();
        ArrayList<String> persons=new ArrayList<String>();

        ArrayList<Album> albums = FileController.loadAlbums(SearchActivity.this);

        for(int i=0;i<albums.size();i++){
            for(int j=0;j<albums.get(i).getPhotoCount();j++){
                for(int z=0;z<albums.get(i).photoArrayList.get(j).getTagArrayList().size();z++){
                    Tag curr=albums.get(i).photoArrayList.get(j).getTagArrayList().get(z);
                    if(curr.getKey().equals("Location")){
                        locs.add(curr.getValue());
                    }
                    else if(curr.getKey().equals("Person")){
                        persons.add(curr.getValue());
                    }
                }
            }
        }

        AutoCompleteTextView locationATV=findViewById(R.id.locationSearch);
        ArrayAdapter<String> adapterLoc=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,locs);
        locationATV.setAdapter(adapterLoc);

        AutoCompleteTextView personATV=findViewById(R.id.personSearch);
        ArrayAdapter<String> adapterPerson=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,persons);
        personATV.setAdapter(adapterPerson);


        Button searchButtonAND=findViewById(R.id.searchFilterButtonActualAND);
        searchButtonAND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String location=locationATV.getText().toString();
                String person=personATV.getText().toString();

                if(location.isEmpty()&&person.isEmpty()){
                    return;
                }
                Album temp=new Album("temp");

                for(int i=0;i<albums.size();i++){
                    for(int j=0;j<albums.get(i).getPhotoCount();j++){
                        for(int z=0;z<albums.get(i).photoArrayList.get(j).getTagArrayList().size();z++){
                            Tag curr=albums.get(i).photoArrayList.get(j).getTagArrayList().get(z);
                            if(curr.getKey().equals("Location")&&curr.getKey().equals("Person")){
                                if(curr.getValue().equalsIgnoreCase(location)){
                                    temp.addPhoto(albums.get(i).photoArrayList.get(j));
                                }
                            }
                        }
                    }
                }
                albums.add(temp);
                System.out.println("SEARCH CLICKED");
                Intent intent = new Intent(SearchActivity.this, SearchResultAlbumsActivity.class);
                intent.putExtra("album", albums);
                intent.putExtra("selectedAlbum", albums.size()-1);
                startActivityForResult(intent, 1);
            }
        });

        Button searchButtonOR=findViewById(R.id.searchFilterButtonActualOR);
        searchButtonOR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String location=locationATV.getText().toString();
                String person=personATV.getText().toString();

                if(location.isEmpty()&&person.isEmpty()){
                    return;
                }
                Album temp=new Album("temp");

                for(int i=0;i<albums.size();i++){
                    for(int j=0;j<albums.get(i).getPhotoCount();j++){
                        for(int z=0;z<albums.get(i).photoArrayList.get(j).getTagArrayList().size();z++){
                            Tag curr=albums.get(i).photoArrayList.get(j).getTagArrayList().get(z);
                            if(curr.getKey().equals("Location")){
                                if(curr.getValue().equalsIgnoreCase(location)){
                                    temp.addPhoto(albums.get(i).photoArrayList.get(j));
                                }
                            }
                            else if(curr.getKey().equals("Person")){
                                if(curr.getValue().equalsIgnoreCase(person)){
                                    temp.addPhoto(albums.get(i).photoArrayList.get(j));
                                }
                            }
                        }
                    }
                }
                albums.add(temp);
                System.out.println("SEARCH CLICKED");
                Intent intent = new Intent(SearchActivity.this, SearchResultAlbumsActivity.class);
                intent.putExtra("album", albums);
                intent.putExtra("selectedAlbum", albums.size()-1);
                startActivityForResult(intent, 1);
            }
        });
    }
}