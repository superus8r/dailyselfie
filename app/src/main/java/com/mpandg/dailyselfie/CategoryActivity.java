package com.mpandg.dailyselfie;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mpandg.dailyselfie.adapter.CategoryAdapter;
import com.mpandg.dailyselfie.data.DataSource;
import com.mpandg.dailyselfie.model.Category;
import com.mpandg.dailyselfie.util.AddCategoryDialogFragment;
import com.mpandg.dailyselfie.util.DeleteCategoryDialogFragment;
import com.mpandg.dailyselfie.util.DeletePhotoDialogFragment;

import java.util.List;

public class CategoryActivity extends AppCompatActivity implements CategoryAdapter.ClickListener, AddCategoryDialogFragment.OnCategoryAddListener, DeleteCategoryDialogFragment.DeleteListener {

    private List<Category> categories;
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // retrieve category items from dataBase.
        DataSource dataSource = new DataSource(this);
        dataSource.open();
        categories = dataSource.getCategories();
        dataSource.close();

        recyclerView = (RecyclerView) findViewById(R.id.list);

        if (categories != null) {
            adapter = new CategoryAdapter(this, categories);
            initList();
        } else {

            // show no category place holder.
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //noinspection deprecation
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // show add category dialog.
                AddCategoryDialogFragment fragment = AddCategoryDialogFragment.newInstance();
                fragment.show(getSupportFragmentManager(), AddCategoryDialogFragment.TAG);
            }
        });
    }

    private void initList() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickListener(Category category) {

    }

    @Override
    public void onLongClickListener(Category category, int position) {

        DeleteCategoryDialogFragment fragment = DeleteCategoryDialogFragment.newInstance(category, position);
        fragment.show(getSupportFragmentManager(), DeletePhotoDialogFragment.TAG);
    }

    @Override
    public void onCategoryAdded(Category category) {

        // add the newly added category to adapter.
        adapter.addItem(category);
    }

    @Override
    public void onDeleteCategory(Category category, int position) {

        // delete the category from database.
        category.delete(this);
        // remove from category list
        categories.remove(position);
        // remove from adapter.
        adapter.removeItem(position);
    }
}