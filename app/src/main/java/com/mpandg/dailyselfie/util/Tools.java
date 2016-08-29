package com.mpandg.dailyselfie.util;

import android.util.Log;

import java.io.File;

/**
 * Created by Ali Kabiri on 8/21/2016.
 * Find me here: ali@kabiri.org
 */
public class Tools {

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

    public void importPhotos() {

        File dcimDir = new File(DCIM_PATH);
        File[] listOfFiles = dcimDir.listFiles();

        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    Log.d("File ", listOfFiles[i].getName());
                } else if (listOfFiles[i].isDirectory()) {
                    Log.d("Directory ", listOfFiles[i].getName());
                }
            }
        }
    }

}
