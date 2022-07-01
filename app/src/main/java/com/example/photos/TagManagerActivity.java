package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class TagManagerActivity extends AppCompatActivity {


    ArrayList<Album> albums;
    int selectionIndex;
    int selPIndex;
    Photo curr;
    Button back;
    Button add;
    ToggleButton toggle;
    EditText tVal;
    ListView tagList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_manager);

        Intent myIntent = getIntent();
        this.albums = (ArrayList<Album>) myIntent.getSerializableExtra("albums");
        this.selectionIndex = myIntent.getIntExtra("selectedAlbum", -1);
        this.selPIndex = myIntent.getIntExtra("selectedPhoto", -1);
        curr = albums.get(selectionIndex).getPhoto(selPIndex);

        tagList = findViewById(R.id.TagManagerListView);
        toggle = findViewById(R.id.TagToggle);

        back = findViewById(R.id.TagManagerBackButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TagManagerActivity.this, PhotoDisplayActivity.class);
                intent.putExtra("albums", albums);
                intent.putExtra("selectedAlbum", selectionIndex);
                intent.putExtra("selectedPhoto", selPIndex);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });

        add = findViewById(R.id.AddTagManagerButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasAdded = false;
                boolean isCorr = false;
                String tagName = toggle.getText().toString();
                String tagValue = tVal.getText().toString();
                if(tagValue != null){
                    if(tagValue.length() > 0){
                        isCorr = true;
                    }
                }
                if(isCorr == true){
                    Tag newTag = new Tag(tagName, tagValue);
                    boolean dupeExists = false;
                    for (Tag _tag : curr.getTagArrayList()) {
                        if (_tag.equals(newTag)) {
                            dupeExists = true;
                            AlertDialog.Builder builder = new AlertDialog.Builder(TagManagerActivity.this);
                            builder.setCancelable(true);
                            builder.setTitle("Duplicate");
                            builder.setMessage("Duplicate album exists");
                            builder.setPositiveButton("OK", null);

                            builder.show();
                            return;
                        }
                    }
                    if(dupeExists == false){

                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(tVal.getWindowToken(), 0);
                        curr.addTag(newTag);
                        FileController.saveAlbum(TagManagerActivity.this, albums);
                        updateView();
                    }
                }
            }
        });

        tVal = findViewById(R.id.Tag);
        updateView();
    }

    private void updateView(){
        tVal.setText("");
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<Tag>tagArr = curr.getTagArrayList();
        for(int i = 0; i< tagArr.size(); i++){
            list.add(tagArr.get(i).toString());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        tagList.setAdapter(arrayAdapter);
    }
}