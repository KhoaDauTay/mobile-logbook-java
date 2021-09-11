package com.example.log_book_java;

import com.google.gson.annotations.JsonAdapter;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HouseService {
    @GET("api/{path}/")
    Call<List<FormModel>> listHouse(@Path("path") String path);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("api/houses/")
    Call<ResponseBody> createHouse(@Body RequestBody body);

    @DELETE("api/houses/{id}/")
    Call<Void> deleteHouse(@Path("id") int id);
}
