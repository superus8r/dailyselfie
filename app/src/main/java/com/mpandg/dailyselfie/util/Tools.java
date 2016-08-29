package com.mpandg.dailyselfie.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

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

    public ArrayList<String> getExternalImagesPath(Context context) {
        Uri uri;
        Cursor cursor;
        int column_index;
        StringTokenizer st1;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA };

        cursor = context.getContentResolver().query(uri, projection, null,
                null, null);

        // column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index);
            listOfAllImages.add(absolutePathOfImage);

            // log the photo name for debugging purposes.
            Log.d(TAG, "photo address to import:" + absolutePathOfImage);
        }

        cursor.close();
        return listOfAllImages;
    }

}
