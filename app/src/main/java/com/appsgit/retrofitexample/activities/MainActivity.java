package com.appsgit.retrofitexample.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.appsgit.retrofitexample.R;
import com.appsgit.retrofitexample.jsonclient.model.UserProfile;
import com.appsgit.retrofitexample.jsonclient.services.UserService;
import com.appsgit.retrofitexample.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();
    ImageView imageView;
    static final int REQUEST_IMAGE_CAPTURE = 1000;

    UserService service;

    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = new UserService();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        imageView = (ImageView) findViewById(R.id.imageView);
    }

    public void selectImage(View view) {
        try {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Create the File where the photo should go
            File photoFile = null;

            try {

                photoFile = FileUtils.createImageFile(this);
                mCurrentPhotoPath = photoFile.getAbsolutePath();

            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (photoFile != null) {

                //getUriForFile, which returns a content:// URI. For more recent apps targeting Android 7.0 (API level 24) and higher,
                //don't forget to change your package name
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.appsgit.retrofitexample.fileprovider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void uploadImage(View view) {
        try {
            Log.d(TAG, "uploadImage: mCurrentPhotoPath " + mCurrentPhotoPath);

            if (mCurrentPhotoPath != null && !mCurrentPhotoPath.equals("")) {

                String photnName = "testimage.jpg";

                service.saveUserImage(this, photnName, new File(mCurrentPhotoPath), new Callback<UserProfile>() {
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                        showMessage("Image successfully uploaded to server.!");
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {
                        showMessage("Upload Failed.!");
                    }
                });
            } else {
                showMessage("No Image found to be uploaded.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode " + requestCode);
        Log.d(TAG, "onActivityResult: resultCode == RESULT_OK ? " + (resultCode == RESULT_OK));
        if (REQUEST_IMAGE_CAPTURE == requestCode && resultCode == RESULT_OK) {

           try {

               Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
               imageView.setImageBitmap(imageBitmap);

           } catch (Exception ex) {
               ex.printStackTrace();
           }
        }
    }

    public void showMessage(String message) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Message");
            alertDialog.setMessage(message);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
