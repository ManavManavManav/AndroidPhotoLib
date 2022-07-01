package com.example.photos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class EditTagActivity extends AppCompatActivity {


    ArrayList<Album> albumArrayList;
    Album selAlbum;
    int sleAlbumIndex;
    int selPIndex;
    int tagIdx;
    Photo curr;
    Button back;
    Button make;
    Button delete;
    ToggleButton toggler;
    EditText tagValInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tag);

        Intent myIntent = getIntent();
        this.albumArrayList = (ArrayList<Album>) myIntent.getSerializableExtra("albums");
        this.sleAlbumIndex = myIntent.getIntExtra("selectedAlbum", -1);
        this.selPIndex = myIntent.getIntExtra("selectedPhoto", -1);
        this.tagIdx = myIntent.getIntExtra("selectedTag", -1);
        selAlbum = albumArrayList.get(sleAlbumIndex);
        curr = selAlbum.getPhoto(selPIndex);



        Tag currTag = curr.getTagArrayList().get(tagIdx);

        tagValInput = findViewById(R.id.TagNewValueInputBox);
        tagValInput.setText(currTag.getValue());

        toggler = findViewById(R.id.TagTypeToggle);
        if(currTag.getKey().toLowerCase().equals("person")){
            toggler.setChecked(false);
        }else{
            toggler.setChecked(true);
        }

        back = findViewById(R.id.TagEditBackButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTagActivity.this, PhotoDisplayActivity.class);
                intent.putExtra("albums", albumArrayList);
                intent.putExtra("selectedAlbum", sleAlbumIndex);
                intent.putExtra("selectedPhoto", selPIndex);
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });

        make = findViewById(R.id.MakeTagButton);
        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean whetherCor = false;
                String tagName = toggler.getText().toString();
                String tagValue = tagValInput.getText().toString();
                if(tagValue != null){
                    if(tagValue.length() > 0){
                        whetherCor = true;
                    }
                }
                if(whetherCor == true) {
                    Tag newTag = new Tag(tagName, tagValue);
                    boolean duplicatePresent = false;
                    for(int i = 0; i< curr.getTagArrayList().size(); i++) {
                        Tag _tag = curr.getTagArrayList().get(i);
                        if (_tag.equals(newTag) && i != tagIdx) {
                            duplicatePresent = true;
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditTagActivity.this);
                            builder.setCancelable(true);
                            builder.setTitle("Duplicate");
                            builder.setMessage("Duplicate album exists");
                            builder.setPositiveButton("OK", null);

                            builder.show();
                        }
                    }
                    if (duplicatePresent == false) {
                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(tagValInput.getWindowToken(), 0);
                        curr.getTagArrayList().set(tagIdx, newTag);
                        FileController.saveAlbum(EditTagActivity.this, albumArrayList);
                    }
                }
            }
        });

        delete = findViewById(R.id.DeleteTagButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditTagActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Delete tag");
                builder.setMessage("Sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        curr.getTagArrayList().remove(tagIdx);
                        FileController.saveAlbum(EditTagActivity.this, albumArrayList);
                        backToActivity();
                    }
                });
                builder.setNeutralButton("No", null);
                builder.show();
            }
        });
    }

    private void backToActivity(){
        Intent intent = new Intent(EditTagActivity.this, PhotoDisplayActivity.class);
        intent.putExtra("albums", albumArrayList);
        intent.putExtra("selectedAlbum", sleAlbumIndex);
        intent.putExtra("selectedPhoto", selPIndex);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}