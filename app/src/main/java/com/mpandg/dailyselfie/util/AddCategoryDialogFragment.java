package com.mpandg.dailyselfie.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mpandg.dailyselfie.R;
import com.mpandg.dailyselfie.model.Category;
import com.mpandg.dailyselfie.model.Photo;

/**
 * Created by Ali Kabiri on 8/17/2016.
 * Find me here: ali@kabiri.org
 */
public class AddCategoryDialogFragment extends DialogFragment {

    public static final String TAG = "add_category";

    private View view;

    public static AddCategoryDialogFragment newInstance () {

        return new AddCategoryDialogFragment();
    }

    @SuppressLint("InflateParams")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_category, null);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_add_black_24dp)
                .setTitle(R.string.add_category)
                .setView(view)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                // get the contents of textView.
                                TextView catName = (TextView) view.findViewById(R.id.cat_name);
                                // create a new category.
                                Category category = new Category(catName.getText().toString());
                                // save the newly created category.
                                category.save(getContext());
                                // inform the user about the successfully created category.
                                Toast.makeText(getContext(), R.string.category_added, Toast.LENGTH_SHORT).show();
                                // give the saved category to calling activity.
                                OnCategoryAddListener listener = (OnCategoryAddListener) getContext();
                                listener.onCategoryAdded(category);
                            }
                        })
                .setNegativeButton(R.string.alert_dialog_cancell,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                // user does not want to delete the photo.
                                // pass.
                            }
                        })
                .create();
    }

    public interface OnCategoryAddListener{
        void onCategoryAdded(Category category);
    }
}
