package com.mpandg.dailyselfie.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.mpandg.dailyselfie.R;
import com.mpandg.dailyselfie.data.SharedPrefs;
import com.mpandg.dailyselfie.model.Photo;

/**
 * Created by Ali Kabiri on 8/17/2016.
 * Find me here: ali@kabiri.org
 */
public class ChangeStyleDialogFragment extends DialogFragment {

    public static final String TAG = "change_style_dialog";

    public static ChangeStyleDialogFragment newInstance () {

        return new ChangeStyleDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // get the sharePrefs instance.
        SharedPrefs prefs = SharedPrefs.getInstance(getActivity());

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_line_style_black_24dp)
                .setTitle(R.string.choose_style)
                .setSingleChoiceItems(R.array.list_view_types, prefs.getViewStyle()-1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // send the style change to the parent activity.
                        OnViewStyleChangeListener listener = (OnViewStyleChangeListener) getContext();
                        listener.onViewStyleChange(i+1);
                    }
                })
                .create();
    }

    public interface OnViewStyleChangeListener {
        void onViewStyleChange(int viewStyle);
    }
}
