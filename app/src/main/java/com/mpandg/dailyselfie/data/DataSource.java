package com.mpandg.dailyselfie.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mpandg.dailyselfie.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ali Kabiri on 8/16/2016.
 * Find me here: ali@kabiri.org
 */
public class DataSource {

    private final String TAG = "dataSource";

    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase database;

    // an string array holding our table's column names
    // which is going to be used by database methods.
    public static final String[] usersTableColumns = {
            DatabaseHelper.PHOTOS_COLUMN_ID,
            DatabaseHelper.PHOTOS_COLUMN_NAME,
            DatabaseHelper.PHOTOS_COLUMN_SRC
    };

    public DataSource(Context context) {

        // instantiate the dbHelper object using our DatabaseHelper class.
        dbHelper = new DatabaseHelper(context);
    }

    public void open(){

        // opening the database or
        // creating the table structures for the first time.
        // the helper class knows when to open or create the database.
        database = dbHelper.getWritableDatabase();

        // log the event for debugging purposes.
        Log.i(TAG, "database opened");
    }

    public boolean isOpen () {

        // check if the database is already open.
        return database.isOpen();
    }

    public void close(){

        // close the database.
        database.close();

        // log the event for debugging purposes.
        Log.i(TAG, "database closed");
    }

    // insert a photo record into database .
    public void insertPhoto (Photo photo) {

        // ContentValues implements a map interface.
        ContentValues values = new ContentValues();

        // put the data you want to insert into database.
        values.put(DatabaseHelper.PHOTOS_COLUMN_NAME, photo.getName());
        values.put(DatabaseHelper.PHOTOS_COLUMN_SRC, photo.getSrc());

        // passing the string array which we created earlier
        // and the contentValues which includes the values
        // into the insert method, inserts the values corresponding
        // to column names and returns the id of the inserted row.
        long insertId = database.insert(DatabaseHelper.TABLE_PHOTOS , null, values);

        // log the insert id for debugging purposes.
        Log.i(TAG, "added name id:" + insertId);
    }

    // returns a list of string
    // containing the photos saved in the database.
    public List<Photo> getPhotos (){

        List<Photo> photos;

        // creating the cursor to retrieve data.
        // cursor will contain the data when the
        // query is executed.
        Cursor cursor = database.query(DatabaseHelper.TABLE_PHOTOS, usersTableColumns,
                null, null, null, null, null);

        // log the number of returned rows for debug.
        Log.i(TAG, "returned: " + cursor.getCount() + " rows .");

        // check if the cursor is not null.
        if(cursor.getCount()>0){

            // instantiate the list.
            photos = new ArrayList<>();

            // cursor starts from -1 index, so we should call
            // moveToNext method to iterate over the data it contains.
            while (cursor.moveToNext()) {

                Photo photo = new Photo();

                // read the data in the cursor row using the index which is the column name.
                photo.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PHOTOS_COLUMN_NAME)));
                photo.setSrc(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PHOTOS_COLUMN_SRC)));

                // log the retrieved photo.
                Log.i(TAG, "photo retrieved:" + photo.getName());

                // now add the retrieved photo into the list.
                photos.add(photo);
            }

            // now we have the photos in our string list
            return photos;

        } else {

            // if the cursor was empty, it means that
            // there was no photos found in the table,
            // so return null.
            return null;
        }
    }
}
