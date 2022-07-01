package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class EditAlbumActivity extends AppCompatActivity {

    Button renameButton;
    Button deleteButton;
    Button backButton;
    TextView albumNameDisplay;
    private ArrayList<Album> albumArrayList;
    private int selectionIndex;
    private Album curr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_album);

        Intent myIntent = getIntent();
        this.albumArrayList = (ArrayList<Album>) myIntent.getSerializableExtra("albums");
        this.selectionIndex = myIntent.getIntExtra("selectedAlbum", -1);

        if(selectionIndex == -1){
            return;
        }
        curr = albumArrayList.get(selectionIndex);

        albumNameDisplay = findViewById(R.id.AlbumEditAlbumNameDisplay);
        albumNameDisplay.setText(curr.getAlbumName());


        renameButton = findViewById(R.id.RenameAlbumButton);
        renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renamDialogBox();
            }
        });

        deleteButton = findViewById(R.id.AlbumDeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditAlbumActivity.this);
                builder.setCancelable(true);
                builder.setTitle("delete");
                builder.setMessage("sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        albumArrayList.remove(selectionIndex);
                        FileController.saveAlbum(EditAlbumActivity.this, albumArrayList);
                        Intent intent = new Intent(EditAlbumActivity.this, AlbumViewerActivity.class);
                        intent.putExtra("albums", albumArrayList);
                        intent.putExtra("selectedAlbum", -1);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
                builder.setNeutralButton("No", null);
                builder.show();
            }
        });

        backButton = findViewById(R.id.BackToAlbumDisplayButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditAlbumActivity.this, AlbumViewerActivity.class);
                intent.putExtra("albums", albumArrayList);
                intent.putExtra("selectedAlbum", selectionIndex);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }



    public void renamDialogBox() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_alert, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.editorAlertBox);
        edt.setText("");
        dialogBuilder.setTitle("Rename ");
        dialogBuilder.setMessage("New Album Name");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                curr.setAlbumName(edt.getText().toString());
                albumNameDisplay.setText(curr.getAlbumName());
                FileController.saveAlbum(EditAlbumActivity.this, albumArrayList);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        AlertDialog alert = dialogBuilder.create();
        alert.show();
    }
}