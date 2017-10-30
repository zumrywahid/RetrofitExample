//ApiClient.java
package com.appsgit.retrofitexample.jsonclient;

import com.appsgit.retrofitexample.jsonclient.model.UserProfile;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created on 10/15/17.
 */

public interface ApiClient {

    @Multipart
    @POST("/appsgit-service/fileupload.php")
    Call<UserProfile> uploadImage(@Part("filename") RequestBody fileName, @Part MultipartBody.Part file);
}
