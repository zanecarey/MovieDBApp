package com.example.zane.moviedbapp.interfaces;

import com.example.zane.moviedbapp.model.CastResults;
import com.example.zane.moviedbapp.model.Feed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface CastInterface {

    @Headers("Content-Type: application/json")
    @GET
    Call<CastResults> getData(@Url String url);
}
