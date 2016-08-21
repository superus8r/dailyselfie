package com.mpandg.dailyselfie.util;

/**
 * Created by Ali Kabiri on 8/21/2016.
 * Find me here: ali@kabiri.org
 */
public class Tools {

    private static Tools ourInstance;

    public static Tools getInstance() {

        if (ourInstance == null) {
            ourInstance = new Tools();
            return ourInstance;
        }
        return ourInstance;
    }

    private Tools() {
    }


}
