package my.app.zane.moviedbapp.interfaces;

import my.app.zane.moviedbapp.model.TVFeed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface MovieDBTVInterface {

    @Headers("Content-Type: application/json")
    @GET
    Call<TVFeed> getData(@Url String url);
}
