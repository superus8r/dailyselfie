package com.mpandg.dailyselfie.data;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mpandg.dailyselfie.adapter.ImageAdapter;

/**
 * Created by Ali Kabiri on 8/21/2016.
 * Find me here: ali@kabiri.org
 */
public class SharedPrefs {

    private static final String KEY_VIEW_STYLE = "view_style";
    private static final String TAG = "shared_prefs";
    private static SharedPrefs ourInstance;

    private SharedPreferences sharedPreferences;

    public static SharedPrefs getInstance(Context context) {

        if (ourInstance == null) {
            ourInstance = new SharedPrefs(context);
            return ourInstance;
        }
        return ourInstance;
    }

    private SharedPrefs() {
        // private null constructor.
    }

    private SharedPrefs(Context context) {

        sharedPreferences = ((Activity) context).getPreferences(Context.MODE_PRIVATE);
    }

    @SuppressLint("CommitPrefEdits")
    public void saveViewStyle(int viewStyle) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_VIEW_STYLE, viewStyle);
        editor.commit();
        Log.d(TAG, "view style saved:" + viewStyle);
    }

    public int getViewStyle() {

        int viewStyle = sharedPreferences.getInt(KEY_VIEW_STYLE, ImageAdapter.STYLE_DEFAULT);
        Log.d(TAG, "retrieved viewStyle:" + viewStyle);
        return viewStyle;
    }
}
