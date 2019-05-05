package my.app.zane.moviedbapp.interfaces;

import my.app.zane.moviedbapp.model.CastResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface CastInterface {

    @Headers("Content-Type: application/json")
    @GET
    Call<CastResults> getData(@Url String url);
}
