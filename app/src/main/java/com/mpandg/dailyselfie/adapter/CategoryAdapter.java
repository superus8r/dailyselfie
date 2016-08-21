package com.mpandg.dailyselfie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mpandg.dailyselfie.R;
import com.mpandg.dailyselfie.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ali Kabiri on 8/16/2016.
 * Find me here: ali@kabiri.org
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private static List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {

        this.context = context;
        if (categories == null){
            //noinspection AccessStaticViaInstance
            this.categories = new ArrayList<>();
        } else {
            //noinspection AccessStaticViaInstance
            this.categories = categories;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_category, parent, false);

        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position) {

        holder.bind(context);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View root;
        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            name = (TextView) itemView.findViewById(R.id.name);
        }

        public void bind(Context context) {

            // prepare the listener.
            final ClickListener listener = (ClickListener) context;

            // bind views to data.
            final Category category = categories.get(getAdapterPosition());
            name.setText(category.getName());

            // register the listeners.
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClickListener(category);
                }
            });
            root.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onLongClickListener(category, getAdapterPosition());
                    return true;
                }
            });
        }
    }

    public void addItem (Category category) {

        // add the new photo to the list.
        categories.add(category);
        // refresh the adapter.
        notifyDataSetChanged();
    }

    public void removeItem(int position) {

        // remove the photo from the list.
        categories.remove(position);
        // refresh the adapter.
        notifyDataSetChanged();
    }

    public interface ClickListener{

        void onClickListener(Category category);
        void onLongClickListener(Category category, int position);
    }
}
