package com.mpandg.dailyselfie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpandg.dailyselfie.R;
import com.mpandg.dailyselfie.model.Photo;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ali Kabiri on 8/16/2016.
 * Find me here: ali@kabiri.org
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    public static final int STYLE_DEFAULT = 1;
    public static final int STYLE_LINEAR = 1;
    public static final int STYLE_GRID = 2;

    private Context context;
    private static List<Photo> photos;
    private int listViewType;

    public ImageAdapter(Context context, List<Photo> photos, int listViewType) {

        this.context = context;
        if (photos == null){
            //noinspection AccessStaticViaInstance
            this.photos = new ArrayList<>();
        } else {
            //noinspection AccessStaticViaInstance
            this.photos = photos;
        }
        this.listViewType = listViewType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        // create a new view
        if (listViewType == STYLE_GRID){

            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_photo_grid, parent, false);
        } else {

            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_photo, parent, false);
        }
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {

        holder.bind(context);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View root;
        public ImageView image;
        public TextView name;
        public TextView category;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            category = (TextView) itemView.findViewById(R.id.category);
        }

        public void bind(Context context) {

            // prepare the listener.
            final ClickListener listener = (ClickListener) context;

            // bind views to data.
            final Photo photo = photos.get(getAdapterPosition());
            Picasso.with(context)
                    .load(new File(photo.getSrc()))
                    .resize(128, 128)
                    .onlyScaleDown()
                    .centerCrop()
                    .into(image);
            name.setText(photo.getName());
            category.setText(photo.getCategory());

            // register the listeners.
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClickListener(photo);
                }
            });
            root.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onLongClickListener(photo, getAdapterPosition());
                    return true;
                }
            });
        }
    }

    public void addItem (Photo photo) {

        // add the new photo to the list.
        photos.add(photo);
        // refresh the adapter.
        notifyDataSetChanged();
    }

    public void updateItem (Photo photo, int position) {

        Photo adapterPhoto = photos.get(position);
        // update the photo.
        adapterPhoto.setName(photo.getName());
        adapterPhoto.setCategory(photo.getCategory());
        adapterPhoto.setSrc(photo.getSrc());
        // refresh the adapter item.
        notifyItemChanged(position);
    }

    public void removeItem(int position) {

        // remove the photo from the list.
        photos.remove(position);
        // refresh the adapter.
        notifyDataSetChanged();
    }

    public interface ClickListener{

        void onClickListener(Photo photo);
        void onLongClickListener(Photo photo, int position);
    }
}
