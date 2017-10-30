package com.appsgit.retrofitexample.jsonclient.services;

import android.content.Context;

import com.appsgit.retrofitexample.jsonclient.ApiClient;
import com.appsgit.retrofitexample.jsonclient.ApiClientBuilder;
import com.appsgit.retrofitexample.jsonclient.model.UserProfile;
import com.appsgit.retrofitexample.utils.FileUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserService {

    public void saveUserImage(Context context, String photoName, File imageFile, Callback<UserProfile> callback) {

        // create upload service client
        ApiClient service = ApiClientBuilder.getMGClient();

        RequestBody requestFile =
                RequestBody.create(MediaType.parse(FileUtils.getFileExtension(imageFile.getAbsolutePath())), imageFile);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("fileToUpload", imageFile.getName(), requestFile);

        // add another part within the multipart request
        String imageName = photoName;
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, imageName);

        Call<UserProfile> result =  service.uploadImage(description, body);

        result.enqueue(callback);

    }

}
