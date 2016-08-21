package com.mpandg.dailyselfie.model;

import android.content.Context;

import com.mpandg.dailyselfie.data.DataSource;

/**
 * Created by Ali Kabiri on 8/21/2016.
 * Find me here: ali@kabiri.org
 */
public class Category {

    private int id;
    private String name;

    public Category(){

        // null constructor.
    }

    public Category(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void save(Context context) {

        DataSource dataSource = new DataSource(context);
        dataSource.open();
        dataSource.insertCategory(this);
        dataSource.close();
    }

    public void delete(Context context) {

        DataSource dataSource = new DataSource(context);
        dataSource.open();
        dataSource.deleteCategoryByName(this.name);
        dataSource.close();
    }
}
