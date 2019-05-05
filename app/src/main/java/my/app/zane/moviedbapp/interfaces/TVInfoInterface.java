package my.app.zane.moviedbapp.interfaces;

import my.app.zane.moviedbapp.model.TVDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface TVInfoInterface {

    @Headers("Content-Type: application/json")
    @GET
    Call<TVDetails> getData(@Url String url);
}
