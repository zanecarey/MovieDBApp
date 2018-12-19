package com.example.zane.moviedbapp;





import com.example.zane.moviedbapp.model.NameResults;
import com.example.zane.moviedbapp.model.SearchName;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface NameSearchInterface {

    @Headers("Content-Type: application/json")
    @GET
    Observable<NameResults> getData(@Url String url);
}
