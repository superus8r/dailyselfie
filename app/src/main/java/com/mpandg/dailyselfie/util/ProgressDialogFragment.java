package com.mpandg.dailyselfie.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.mpandg.dailyselfie.model.Category;

/**
 * Created by Ali Kabiri on 8/17/2016.
 * Find me here: ali@kabiri.org
 */
public class ProgressDialogFragment extends DialogFragment {

    public static final String TAG = "progress";
    public static final String KEY_TITLE = "progress";

    private ProgressDialog dialog;

    public static ProgressDialogFragment newInstance(int title) {

        ProgressDialogFragment fragment = new ProgressDialogFragment();
        // create a new bundle object and put the photo in it.
        Bundle args = new Bundle();
        args.putInt(KEY_TITLE, title);
        // put the bundle into the fragment.
        fragment.setArguments(args);
        // return the configured fragment.
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle(getResources().getString(getArguments().getInt(KEY_TITLE)));
        dialog.setProgress(20);

        return dialog;
    }

    public void onProgressUpdate(int value) {

        dialog.setProgress(value);
    }

}
