package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PhotoDisplayActivity extends AppCompatActivity {


    private ArrayList<Album> albums;
    private Album selAlbum;
    private int selectionIndex;
    private int selPIndex;
    private Photo curr;

    ListView tagListView;
    TextView caption;
    TextView date;
    ImageView imageView;
    Button moveButton;
    Button junkButton;
    Button deleteButton;
    Button addTagButton;
    Button slideshowButton;
    Button captionButton;
    Button backAlbumButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        Intent myIntent = getIntent();
        this.albums = (ArrayList<Album>) myIntent.getSerializableExtra("albums");
        this.selectionIndex = myIntent.getIntExtra("selectedAlbum", -1);
        this.selPIndex = myIntent.getIntExtra("selectedPhoto", -1);

        if(selPIndex != -1){
            this.selAlbum = albums.get(selectionIndex);
            curr = selAlbum.getPhoto(selPIndex);
        }else{
            return;
        }

        caption = findViewById(R.id.Caption);
        date = findViewById(R.id.DisplayDateView);
        imageView = findViewById(R.id.DisplayImageView);




        deleteButton = findViewById(R.id.DeletePhotoButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PhotoDisplayActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Delete");
                builder.setMessage("sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        selAlbum.removePhoto(selPIndex);
                        FileController.saveAlbum(PhotoDisplayActivity.this, albums);
                        backToActivity();
                    }
                });
                builder.setNeutralButton("No", null);
                builder.show();
            }

        });

        moveButton = findViewById(R.id.MovePhotoButton);
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListMCAlert(false);
            }
        });

        junkButton = findViewById(R.id.junkCpButton);
        junkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListMCAlert(true);
            }
        });


        captionButton = findViewById(R.id.EditCaptionButton);
        captionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showChangeLangDialog();

            }
        });

        slideshowButton = findViewById(R.id.SlideShowStartButton);
        slideshowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoDisplayActivity.this, SlideShowActivity.class);
                intent.putExtra("albums", albums);
                intent.putExtra("selectedAlbum", selectionIndex);
                intent.putExtra("selectedPhoto", selPIndex);
                startActivityForResult(intent, 11);
            }
        });

        backAlbumButton = findViewById(R.id.BackToPhotoListButton);
        backAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoDisplayActivity.this, AlbumViewerActivity.class);
                intent.putExtra("albums", albums);
                intent.putExtra("selectedAlbum", selectionIndex);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });

        tagListView = findViewById(R.id.TagDisplayScreenListView);
        tagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                view.setSelected(true);
                Intent intent = new Intent(PhotoDisplayActivity.this, EditTagActivity.class);
                intent.putExtra("albums", albums);
                intent.putExtra("selectedAlbum", selectionIndex);
                intent.putExtra("selectedPhoto", selPIndex);
                intent.putExtra("selectedTag", position);
                startActivityForResult(intent, 13);
            }
        });

        addTagButton = findViewById(R.id.TagManagerButton);
        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoDisplayActivity.this, TagManagerActivity.class);
                intent.putExtra("albums", albums);
                intent.putExtra("selectedAlbum", selectionIndex);
                intent.putExtra("selectedPhoto", selPIndex);
                startActivityForResult(intent, 12);
            }
        });




        updateView(false);
        updateView(true);
    }




    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_alert, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.editorAlertBox);

        dialogBuilder.setTitle("Custom dialog");
        dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                curr.setCaption(edt.getText().toString());
                caption.setText(edt.getText().toString());
                FileController.saveAlbum(PhotoDisplayActivity.this, albums);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void ListMCAlert(boolean moveOrCopy){
        AlertDialog.Builder builder = new AlertDialog.Builder(PhotoDisplayActivity.this);
        builder.setTitle("Choose an animal");

        String[] otherTitles = new String[albums.size()];
        boolean []otherBool = new boolean[albums.size()];
        for (int i = 0; i < albums.size(); i++) {
            otherTitles[i] = albums.get(i).getAlbumName();
            otherBool[i] = false;
        }
        int moveLocation = 0;

        if(moveOrCopy == true) {
            builder.setMultiChoiceItems(otherTitles, otherBool, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    otherBool[which] = isChecked;
                }
            });
        }else{
            otherBool[moveLocation] = true;
            builder.setSingleChoiceItems(otherTitles, moveLocation, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    otherBool[moveLocation] = false;
                    otherBool[which] = true;
                }
            });
        }

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean backHome = false;
                boolean toSave = false;
                for(int i = 0; i< otherBool.length; i++){
                    if(otherBool[i] == true){
                        Photo photo = selAlbum.getPhoto(selPIndex);
                        if(moveOrCopy == false){
                            Album chosenAlbum = albums.get(i);
                            if(i != selectionIndex) {
                                chosenAlbum.addPhoto(photo);
                                selAlbum.removePhoto(selPIndex);
                                backHome = true;
                                toSave = true;
                                break;
                            }
                        }
                        else {
                            Album chosenAlbum = albums.get(i);
                            toSave = true;
                            try {
                                Photo clonedPhoto = photo.clone();
                                ArrayList<Tag> clonedTag = (ArrayList<Tag>) photo.getTagArrayList().clone();
                                clonedPhoto.setTag(clonedTag);
                                chosenAlbum.addPhoto(clonedPhoto);

                            }catch (Exception ex){
                                System.out.println(ex.toString());
                            }

                        }
                    }
                }
                if(toSave == true){
                    FileController.saveAlbum(PhotoDisplayActivity.this, albums);
                }
                if(backHome == true){
                    backToActivity();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void backToActivity(){
        Intent intent = new Intent(PhotoDisplayActivity.this, AlbumViewerActivity.class);
        intent.putExtra("albums", albums);
        intent.putExtra("selectedAlbum", selectionIndex);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 11){
            this.albums = (ArrayList<Album>) data.getSerializableExtra("albums");
            this.selectionIndex = data.getIntExtra("selectedAlbum", -1);
            this.selPIndex = data.getIntExtra("selectedPhoto",-1);
            curr = selAlbum.getPhoto(selPIndex);
            updateView(false);
        }else if (requestCode == 12){
            this.albums = (ArrayList<Album>) data.getSerializableExtra("albums");
            this.selectionIndex = data.getIntExtra("selectedAlbum", -1);
            this.selPIndex = data.getIntExtra("selectedPhoto",-1);
            selAlbum = albums.get(selectionIndex);
            curr = selAlbum.getPhoto(selPIndex);
            updateView(true);
        }
        else if (requestCode == 13){
            this.albums = (ArrayList<Album>) data.getSerializableExtra("albums");
            this.selectionIndex = data.getIntExtra("selectedAlbum", -1);
            this.selPIndex = data.getIntExtra("selectedPhoto",-1);
            selAlbum = albums.get(selectionIndex);
            curr = selAlbum.getPhoto(selPIndex);
            updateView(true);
        }
    }

    private void updateView(boolean updateTag){
        if(updateTag == false) {
            caption.setText(curr.getCaption());
            date.setText(curr.getDate());
            imageView.setImageURI(Uri.parse(curr.getImageFile()));
        }else{
            ArrayList<String> list = new ArrayList<String>();
            ArrayList<Tag>tagArr = curr.getTagArrayList();
            for(int i = 0; i< tagArr.size(); i++){
                list.add(tagArr.get(i).toString());
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
            tagListView.setAdapter(arrayAdapter);
        }
    }
}