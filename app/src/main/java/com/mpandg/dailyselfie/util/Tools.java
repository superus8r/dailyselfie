package com.mpandg.dailyselfie.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.mpandg.dailyselfie.model.Photo;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Ali Kabiri on 8/21/2016.
 * Find me here: ali@kabiri.org
 */
public class Tools {

    private static final String TAG = "tools";
    private static Tools ourInstance;

    final String DCIM_PATH = android.os.Environment.DIRECTORY_DCIM;

    public static Tools getInstance() {

        if (ourInstance == null) {
            ourInstance = new Tools();
            return ourInstance;
        }
        return ourInstance;
    }

    private Tools() {
    }

    public ArrayList<Photo> getExternalImagesPath(Context context) {
        Uri uri;
        Cursor cursor;
        int column_index_data;
        StringTokenizer st1;
        ArrayList<Photo> listOfAllImages = new ArrayList<>();
        String absolutePathOfImage = null;
        String imageName = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA};

        cursor = context.getContentResolver().query(uri, projection, null,
                null, null);

        // column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            imageName = new File(absolutePathOfImage).getName();
            listOfAllImages.add(new Photo(imageName, absolutePathOfImage));

            // log the photo name for debugging purposes.
            //Log.d(TAG, "photo to import:" + imageName);
        }

        cursor.close();
        return listOfAllImages;
    }

}
