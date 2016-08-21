package com.mpandg.dailyselfie.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mpandg.dailyselfie.model.Category;
import com.mpandg.dailyselfie.model.Photo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ali Kabiri on 8/16/2016.
 * Find me here: ali@kabiri.org
 */
public class DataSource {

    private final String TAG = "dataSource";

    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase database;
    private Context context;

    // an string array holding our table's column names
    // which is going to be used by database methods.
    public static final String[] usersTableColumns = {
            DatabaseHelper.PHOTOS_COLUMN_ID,
            DatabaseHelper.PHOTOS_COLUMN_NAME,
            DatabaseHelper.PHOTOS_COLUMN_CATEGORY,
            DatabaseHelper.PHOTOS_COLUMN_SRC
    };

    public static final String[] categoriesTableColumns = {
            DatabaseHelper.CATEGORIES_COLUMN_ID,
            DatabaseHelper.CATEGORIES_COLUMN_NAME
    };

    public DataSource(Context context) {

        // instantiate the dbHelper object using our DatabaseHelper class.
        dbHelper = new DatabaseHelper(context);
        this.context = context;
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
        values.put(DatabaseHelper.PHOTOS_COLUMN_CATEGORY, photo.getCategory());
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
                photo.setCategory(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PHOTOS_COLUMN_CATEGORY)));
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

    public boolean deletePhotoByName(String name) {

        // where statement.
        String whereClause = DatabaseHelper.PHOTOS_COLUMN_NAME + "=?";
        String[] whereArgs = {name};

        // execute the delete query.
        int deleteId = database.delete(DatabaseHelper.TABLE_PHOTOS, whereClause, whereArgs);
        // log the id of deleted row.
        Log.i(TAG, "deleted photo id:" + deleteId);

        // if the id is zero, so there's a problem in deletion.
        return deleteId != 0;
    }

    // insert a category record into database .
    public void insertCategory (Category category) {

        // ContentValues implements a map interface.
        ContentValues values = new ContentValues();

        // put the data you want to insert into database.
        values.put(DatabaseHelper.CATEGORIES_COLUMN_NAME, category.getName());

        // passing the string array which we created earlier
        // and the contentValues which includes the values
        // into the insert method, inserts the values corresponding
        // to column names and returns the id of the inserted row.
        long insertId = database.insert(DatabaseHelper.TABLE_CATEGORIES , null, values);

        // log the insert id for debugging purposes.
        Log.i(TAG, "added item id:" + insertId);
    }

    // returns a list of string
    // containing the photos saved in the database.
    public List<Category> getCategories (){

        List<Category> categories;

        // creating the cursor to retrieve data.
        // cursor will contain the data when the
        // query is executed.
        Cursor cursor = database.query(DatabaseHelper.TABLE_CATEGORIES, categoriesTableColumns,
                null, null, null, null, null);

        // log the number of returned rows for debug.
        Log.i(TAG, "returned: " + cursor.getCount() + " rows .");

        // check if the cursor is not null.
        if(cursor.getCount()>0){

            // instantiate the list.
            categories = new ArrayList<>();

            // cursor starts from -1 index, so we should call
            // moveToNext method to iterate over the data it contains.
            while (cursor.moveToNext()) {

                Category category = new Category();

                // read the data in the cursor row using the index which is the column name.
                category.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.CATEGORIES_COLUMN_ID)));
                category.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PHOTOS_COLUMN_NAME)));

                // log the retrieved photo.
                Log.i(TAG, "category retrieved:" + category.getName());

                // now add the retrieved photo into the list.
                categories.add(category);
            }

            // now we have the photos in our string list
            return categories;

        } else {

            // if the cursor was empty, it means that
            // there was no photos found in the table,
            // so return null.
            return null;
        }
    }

    public boolean deleteCategoryByName(String name) {

        // where statement.
        String whereClause = DatabaseHelper.PHOTOS_COLUMN_NAME + "=?";
        String[] whereArgs = {name};

        // delete all existing photos before deleting the database.
        List<Photo> photos = null;
        DataSource dataSource = new DataSource(context);
        dataSource.open();
        photos = dataSource.getPhotos();
        dataSource.close();
        if (photos != null) {
            // if there are saved photos.
            for (Photo photo : photos) {
                // delete each photo respectively.
                photo.delete(context);
            }
        }

        // execute the delete query.
        int deleteId = database.delete(DatabaseHelper.TABLE_CATEGORIES, whereClause, whereArgs);
        // log the id of deleted row.
        Log.i(TAG, "deleted category id:" + deleteId);

        // if the id is zero, so there's a problem in deletion.
        return deleteId != 0;
    }
}
