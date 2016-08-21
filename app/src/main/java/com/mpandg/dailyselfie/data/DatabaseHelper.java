package com.mpandg.dailyselfie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ali Kabiri on 8/16/2016.
 * Find me here: ali@kabiri.org
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // this tag is used for logging purposes.
    public static final String TAG = "database";

    // database version.
    public static final int DATABASE_VERSION = 2;
    // database file name.
    public static final String DATABASE_NAME = "database.db";

    // photo table details:
    public static final String TABLE_PHOTOS = "photos";
    public static final String PHOTOS_COLUMN_ID = "id";
    public static final String PHOTOS_COLUMN_NAME = "name";
    public static final String PHOTOS_COLUMN_CATEGORY = "category";
    public static final String PHOTOS_COLUMN_SRC = "src";

    // category table details:
    public static final String TABLE_CATEGORIES = "categories";
    public static final String CATEGORIES_COLUMN_ID = "id";
    public static final String CATEGORIES_COLUMN_NAME = "name";

    // query of creating photos table.
    public static final String CREATE_PHOTOS_TABLE =
            "CREATE TABLE " + TABLE_PHOTOS + " (" +
                    PHOTOS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PHOTOS_COLUMN_NAME + " TEXT, " +
                    PHOTOS_COLUMN_CATEGORY + " TEXT, " +
                    PHOTOS_COLUMN_SRC + " TEXT" + ")";

    // query of creating categories table.
    public static final String CREATE_CATEGORIES_TABLE =
            "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                    CATEGORIES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CATEGORIES_COLUMN_NAME + " TEXT" + ")";

    // query string of deleting users table.
    public static final String DELETE_TABLES =
            "DROP TABLE IF EXISTS " + TABLE_PHOTOS + ";" +
            "DROP TABLE IF EXISTS " + TABLE_CATEGORIES + ";";

    public DatabaseHelper(Context context) {

        // create the database using given information.
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // this line executes the query we made earlier
        // to create the photos table.
        sqLiteDatabase.execSQL(CREATE_PHOTOS_TABLE);
        sqLiteDatabase.execSQL(CREATE_CATEGORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        // execute the delete query we made earlier.
        sqLiteDatabase.execSQL(DELETE_TABLES);

        // explicit call to onCreate.
        onCreate(sqLiteDatabase);
    }
}
