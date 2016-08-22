package com.mpandg.dailyselfie.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.mpandg.dailyselfie.data.DataSource;

/**
 * Created by Ali Kabiri on 8/21/2016.
 * Find me here: ali@kabiri.org
 */
public class Category implements Parcelable {

    public static final String KEY = "category_key";
    public static final java.lang.String POSITION_KEY = "position";
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.id);
    }

    protected Category(Parcel in) {
        this.name = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
