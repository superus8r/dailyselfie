package com.mpandg.dailyselfie;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mpandg.dailyselfie.util.NotificationReceiver;
import com.mpandg.dailyselfie.adapter.ImageAdapter;
import com.mpandg.dailyselfie.data.DataSource;
import com.mpandg.dailyselfie.model.Photo;
import com.mpandg.dailyselfie.util.PhotoDeleteListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PhotoDeleteListener {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final long TWO_MINUTES = 2 * 60 * 1000;

    private static final String TAG = "MainActivity";
    private String mCurrentPhotoPath;
    private String mPhotoName;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get a new instance of dataSource to access dataBase.
        DataSource dataSource = new DataSource(this);

        // init recyclerView.
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //fetch the photos from dataBase.
        dataSource.open();
        List<Photo> photos = dataSource.getPhotos();
        dataSource.close();

        // init the adapter.
        adapter = new ImageAdapter(this, photos);

        // set the adapter
        recyclerView.setAdapter(adapter);

        // Create a pending intent for the reminder notifications.
        Intent notificationIntent = new Intent(MainActivity.this, NotificationReceiver.class);
        PendingIntent notificationPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, notificationIntent, 0);

        // get reference to alarm manager.
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // send the notification using the alarm.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + TWO_MINUTES,
                TWO_MINUTES,
                notificationPendingIntent);
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent.
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // create the file where the photo should go.
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.i(TAG, "created image file:" + photoFile.getAbsolutePath());
            } catch (IOException e) {
                // error occured while creating the File.
                Log.e(TAG, "dispatchTakePictureIntent: " + e.getMessage());
            }
            // continue only if the File was successfully created.
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {

        // create an image file name.
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, // prefix
                ".jpg", // suffix
                storageDir // directory
        );

        // save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        mPhotoName = imageFileName;
        Log.i(TAG, "current photo path:" + mCurrentPhotoPath);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // create a new photo object using current taken photo details.
            Photo photo = new Photo(mPhotoName, mCurrentPhotoPath);
            // save the photo.
            photo.save(this);
            // update the adapter.
            adapter.addItem(photo);
            //adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.take_photo:
                dispatchTakePictureIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDeletePhoto(Photo photo) {

        // delete the photo.
        photo.delete();

        // inform the user about deletion.
        Toast.makeText(this, R.string.deleted, Toast.LENGTH_SHORT).show();
    }
}
