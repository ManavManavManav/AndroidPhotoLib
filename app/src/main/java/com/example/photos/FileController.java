package com.example.photos;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileController {
    public static ArrayList<Album> loadAlbums(Context context) {
        String fileName = "albums.dat";
        ArrayList<Album> albumArrayList = new ArrayList<Album>();

        if(existCheck(context)) {
            try {

                File outerFile = new File( context.getFilesDir(),"storage/"+fileName);

                FileInputStream fis = new FileInputStream(outerFile.getPath());
                ObjectInputStream ois = new ObjectInputStream(fis);
                albumArrayList = (ArrayList<Album>) ois.readObject();

                ois.close();
                fis.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        return albumArrayList;
    }

    public static void saveAlbum(Context context, ArrayList<Album> bigList) {
        String fileName = "albums.dat";
        existCheck(context);
        try {

            File outerFile = new File( context.getFilesDir(),"storage/"+fileName);

            FileOutputStream file = new FileOutputStream(outerFile.getPath());
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(bigList);
            out.close();
            file.close();

        } catch (Exception ex) {
             System.out.println(ex.getMessage());
        }
    }

    private static boolean existCheck(Context context){
        boolean fileExists = false;
        String fileName = "albums.dat";
        File file = new File( context.getFilesDir(),"storage");
        if(!file.exists()){
            file.mkdir();
        }else{
            fileExists = true;
        }

        try{
            File gpxfile = new File(file, fileName);
            if(gpxfile.exists() == true){
                fileExists = true;
            }
            else{
                FileWriter writer = new FileWriter(gpxfile);
                writer.write("");
                writer.flush();
                writer.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return fileExists;
    }

}
