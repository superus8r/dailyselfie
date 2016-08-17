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
import java.util.List;

/**
 * Created by Ali Kabiri on 8/16/2016.
 * Find me here: ali@kabiri.org
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context context;
    private static List<Photo> photos;

    public ImageAdapter(Context context, List<Photo> photos) {

        this.context = context;
        //noinspection AccessStaticViaInstance
        this.photos = photos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_photo, parent, false);
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

        public ImageView image;
        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
        }

        public void bind(Context context) {

            // bind views to data.
            Photo photo = photos.get(getAdapterPosition());
            Picasso.with(context)
                    .load(new File(photo.getSrc()))
                    .into(image);
            name.setText(photo.getName());
        }
    }

    public void addItem (Photo photo) {
        photos.add(photo);
        notifyDataSetChanged();
    }

    public interface ClickListener{

        void onClickListener(Photo photo);
        void onLongClickListener(Photo photo);
    }
}
