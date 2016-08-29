package com.mpandg.dailyselfie;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mpandg.dailyselfie.adapter.ImageAdapter;
import com.mpandg.dailyselfie.data.DataSource;
import com.mpandg.dailyselfie.data.SharedPrefs;
import com.mpandg.dailyselfie.model.Category;
import com.mpandg.dailyselfie.model.Photo;
import com.mpandg.dailyselfie.util.ChangeStyleDialogFragment;
import com.mpandg.dailyselfie.util.DeletePhotoDialogFragment;
import com.mpandg.dailyselfie.util.NotificationReceiver;
import com.mpandg.dailyselfie.util.PhotoOptionsDialogFragment;
import com.mpandg.dailyselfie.util.ProgressDialogFragment;
import com.mpandg.dailyselfie.util.Tools;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DeletePhotoDialogFragment.DeleteListener,
        ImageAdapter.ClickListener,
        ChangeStyleDialogFragment.OnViewStyleChangeListener,
        PhotoOptionsDialogFragment.OnPhotoOptionsItemClickListener {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final long TWO_MINUTES = 2 * 60 * 1000;

    private static final String TAG = "MainActivity";
    private String mCurrentPhotoPath;
    private String mPhotoName;
    private ImageAdapter adapter;
    private DataSource dataSource;
    private RecyclerView recyclerView;
    private ProgressDialogFragment progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // show the recyclerView.
        SharedPrefs prefs = SharedPrefs.getInstance(this);  // read the defined style from shared prefs.
        initGallery(prefs.getViewStyle());

        // Create a pending intent for the reminder notifications.
        Intent notificationIntent = new Intent(MainActivity.this, NotificationReceiver.class);
        PendingIntent notificationPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, notificationIntent, 0);

        // get reference to alarm manager.
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // send the notification using the alarm.
        alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + TWO_MINUTES,
                TWO_MINUTES,
                notificationPendingIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // update the style whenever user comes back to this activity.
        SharedPrefs prefs = SharedPrefs.getInstance(this);
        initGallery(prefs.getViewStyle());
    }

    private void initGallery(int type) {

        // get a new instance of dataSource to access dataBase.
        dataSource = new DataSource(this);

        // init recyclerView.
        recyclerView = (RecyclerView) findViewById(R.id.list);

        // decide what layoutManager to load.
        RecyclerView.LayoutManager layoutManager;
        if (type == ImageAdapter.STYLE_GRID) {
            layoutManager = new GridLayoutManager(this, 3);
        } else {
            layoutManager = new LinearLayoutManager(this);
        }
        // set the layout manager for the adapter.
        recyclerView.setLayoutManager(layoutManager);

        // check if a category filter is applied.
        Category category = getIntent().getParcelableExtra(Category.KEY);

        //fetch the photos from dataBase.
        dataSource.open();
        List<Photo> photos;
        if (category != null) {
            // filter photos by category.
            photos = dataSource.getPhotos(category);
            // set the title corresponding to category.
            setTitle(category.getName());
            // show the back button.
            //noinspection ConstantConditions
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        } else {
            // show all photos.
            photos = dataSource.getPhotos();
        }
        dataSource.close();

        // init the adapter.
        adapter = new ImageAdapter(this, photos, type);

        // set the adapter
        recyclerView.setAdapter(adapter);
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
        } else if (requestCode == CategoryActivity.REQUEST_GET_CATEGORY && resultCode == RESULT_OK) {

            // get data which exists in the intent object.
            Photo photo = data.getParcelableExtra(Photo.KEY);
            Category category = data.getParcelableExtra(Category.KEY);
            int adapterPosition = data.getExtras().getInt(Photo.POSITION_KEY);
            Log.i(TAG, "category:" + category.getName() + " is going to be set for photo:" + photo.getName());

            // set the received category for the photo.
            photo.setCategory(category.getName());
            photo.save(this);

            // update the adapter.
            adapter.updateItem(photo, adapterPosition);
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
            case R.id.action_take_photo:
                dispatchTakePictureIntent();
                return true;

            case R.id.action_change_style:
                ChangeStyleDialogFragment fragment = ChangeStyleDialogFragment.newInstance();
                fragment.show(getSupportFragmentManager(), ChangeStyleDialogFragment.TAG);
                return true;
            case R.id.action_categories:
                startActivity(new Intent(this, CategoryActivity.class));
                return true;
            case R.id.action_import:
                // show the progress dialog.
                progressDialog = ProgressDialogFragment.newInstance(R.string.import_msg);
                progressDialog.show(getSupportFragmentManager(), ProgressDialogFragment.TAG);
                // start the task.
                ImportPhotosTask task = new ImportPhotosTask();
                task.execute();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDeletePhoto(Photo photo, int position) {

        // delete the photo.
        boolean deleted = photo.delete(this);

        // refresh the adapter.
        adapter.removeItem(position);

        // inform the user about deletion.
        Toast.makeText(this, deleted ? R.string.deleted : R.string.not_deleted, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickListener(Photo photo) {

        // open the photoView activity.
        Intent intent = new Intent(this, FullscreenPhotoViewer.class);
        intent.putExtra(Photo.KEY, photo);
        startActivity(intent);
    }

    @Override
    public void onLongClickListener(Photo photo, int position) {

        // open the options dialog.
        PhotoOptionsDialogFragment fragment = PhotoOptionsDialogFragment.newInstance(photo, position);
        fragment.show(getSupportFragmentManager(), PhotoOptionsDialogFragment.TAG);
    }

    @Override
    public void onViewStyleChange(int viewStyle) {

        // update the viewStyle to prefs.
        SharedPrefs prefs = SharedPrefs.getInstance(this);
        prefs.saveViewStyle(viewStyle);

        // update the recyclerView.
        initGallery(viewStyle);
    }

    @Override
    public void onPhotoOptionsItemClick(Photo photo, int position, int itemIndex) {

        switch (itemIndex) {

            case PhotoOptionsDialogFragment.OPTION_SET_CATEGORY:
                // set the category.
                Intent photoOptionsIntent = new Intent(this, CategoryActivity.class);
                // put the photo in the intent object.
                photoOptionsIntent.putExtra(Photo.KEY, photo);
                photoOptionsIntent.putExtra(Photo.POSITION_KEY, position);
                startActivityForResult(photoOptionsIntent, CategoryActivity.REQUEST_GET_CATEGORY);
                break;
            case PhotoOptionsDialogFragment.OPTION_SHARE:
                // share the photo.
                share(photo);
                break;
            case PhotoOptionsDialogFragment.OPTION_DELETE:
                // open the delete dialog.
                DeletePhotoDialogFragment fragment = DeletePhotoDialogFragment.newInstance(photo, position);
                fragment.show(getSupportFragmentManager(), DeletePhotoDialogFragment.TAG);
                break;
        }
    }

    private void share(Photo photo) {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(photo.getSrc())));
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
    }

    private class ImportPhotosTask extends AsyncTask<Void, Integer, Void> {
        protected Void doInBackground(Void... nothing) {

            Tools tools = Tools.getInstance();
            List<Photo> photos = tools.getExternalImagesPath(MainActivity.this);
            int count = photos.size();
            for (int i = 0; i < count; i++) {
                // publish the progress.
                publishProgress((int) ((i / (float) count) * 100));

                // save the photo.
                Photo photo = photos.get(i);
                photo.save(MainActivity.this);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            // update the value on the dialog.
            progressDialog.onProgressUpdate(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
