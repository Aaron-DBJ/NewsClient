package com.example.jenny.newsclient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface NewsInterface {
    @Headers("Authorization:APPCODE 7296ebfbae0b4421b9ca7bce70bd17eb")
    @GET("index?")
    Call<ResponseBody> getNews(@Query("type") String type);
}
