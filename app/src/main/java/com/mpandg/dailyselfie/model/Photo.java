package com.mpandg.dailyselfie.model;

import android.content.Context;

import com.mpandg.dailyselfie.data.DataSource;

/**
 * Created by Ali Kabiri on 8/16/2016.
 * Find me here: ali@kabiri.org
 */
public class Photo {

    private String name;
    private String src;

    public Photo() {
        // null constructor.
    }

    public Photo(String name, String src) {
        this.name = name;
        this.src = src;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void save(Context context) {
        DataSource dataSource = new DataSource(context);
        dataSource.open();
        dataSource.insertPhoto(this);
        dataSource.close();
    }
}
