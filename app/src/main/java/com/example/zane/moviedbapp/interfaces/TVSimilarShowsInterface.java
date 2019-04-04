package com.example.zane.moviedbapp.interfaces;

import com.example.zane.moviedbapp.model.TVFeed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface TVSimilarShowsInterface {

    @Headers("Content-Type: application/json")
    @GET
    Call<TVFeed> getData(@Url String url);
}
