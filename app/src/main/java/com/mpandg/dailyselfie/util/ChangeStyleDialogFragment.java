package com.mpandg.dailyselfie.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.mpandg.dailyselfie.R;
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

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_line_style_black_24dp)
                .setTitle(R.string.choose_style)
                .setMultiChoiceItems(R.array.list_view_types, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                        Toast.makeText(getContext(), "pos:" + i + " bool:" + b, Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
    }
}
