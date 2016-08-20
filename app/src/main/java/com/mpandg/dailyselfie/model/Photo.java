package com.mpandg.dailyselfie.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.mpandg.dailyselfie.data.DataSource;

import java.io.File;

/**
 * Created by Ali Kabiri on 8/16/2016.
 * Find me here: ali@kabiri.org
 */
public class Photo implements Parcelable {

    public static final String KEY = "photo_object";
    public static final String POSITION_KEY = "position";
    private String name;
    private String category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.src);
    }

    protected Photo(Parcel in) {
        this.name = in.readString();
        this.src = in.readString();
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public boolean delete(Context context) {

        // delete the physical photo which exists on the internal storage.
        File photoFile = new File(this.src);
        boolean fileDeleted = photoFile.delete();

        // delete the photo record from database.
        DataSource dataSource = new DataSource(context);
        // since the photo names are unique, we can delete it by its name.
        dataSource.open();
        boolean recordDeleted = dataSource.deletePhotoByName(this.name);
        dataSource.close();

        // if both phases of deletion was true, return true.
        return fileDeleted && recordDeleted;
    }
}
