package com.example.zane.moviedbapp;

import com.example.zane.moviedbapp.model.CastResults;
import com.example.zane.moviedbapp.model.TrailerResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface TrailerInterface {

    @Headers("Content-Type: application/json")
    @GET
    Call<TrailerResults> getData(@Url String url);
}
