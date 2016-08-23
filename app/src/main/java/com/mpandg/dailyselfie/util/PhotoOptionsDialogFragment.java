package com.mpandg.dailyselfie.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.mpandg.dailyselfie.R;
import com.mpandg.dailyselfie.model.Photo;

/**
 * Created by Ali Kabiri on 8/17/2016.
 * Find me here: ali@kabiri.org
 */
public class PhotoOptionsDialogFragment extends DialogFragment {

    public static final String TAG = "photo_options_dialog";
    public static final int OPTION_SET_CATEGORY = 1;
    public static final int OPTION_SHARE = 2;
    public static final int OPTION_DELETE = 3;

    public static PhotoOptionsDialogFragment newInstance (Photo photo, int position) {

        PhotoOptionsDialogFragment fragment = new PhotoOptionsDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(Photo.KEY, photo);
        args.putInt(Photo.POSITION_KEY, position);

        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.choose_option)
                .setItems(R.array.photo_option_items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // send the style change to the parent activity.
                        OnPhotoOptionsItemClickListener listener = (OnPhotoOptionsItemClickListener) getContext();
                        Photo photo = getArguments().getParcelable(Photo.KEY);
                        listener.onPhotoOptionsItemClick(photo, getArguments().getInt(Photo.POSITION_KEY), i+1);
                    }
                })
                .create();
    }

    public interface OnPhotoOptionsItemClickListener {
        void onPhotoOptionsItemClick(Photo photo, int position, int itemIndex);
    }
}
