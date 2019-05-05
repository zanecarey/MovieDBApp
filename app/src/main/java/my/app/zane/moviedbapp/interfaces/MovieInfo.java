package my.app.zane.moviedbapp.interfaces;

import my.app.zane.moviedbapp.model.Details;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface MovieInfo {

    @Headers("Content-Type: application/json")
    @GET
    Call<Details> getData(@Url String url);
}
