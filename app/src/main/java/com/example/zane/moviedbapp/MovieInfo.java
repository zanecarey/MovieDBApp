package com.example.zane.moviedbapp;

import com.example.zane.moviedbapp.model.Details;
import com.example.zane.moviedbapp.model.Feed;
import com.example.zane.moviedbapp.model.Results;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface MovieInfo {

    @Headers("Content-Type: application/json")
    @GET
    Call<Details> getData(@Url String url);
}
