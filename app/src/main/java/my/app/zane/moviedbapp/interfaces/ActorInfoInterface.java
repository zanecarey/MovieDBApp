package my.app.zane.moviedbapp.interfaces;

import my.app.zane.moviedbapp.model.ActorInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface ActorInfoInterface {

    @Headers("Content-Type: application/json")
    @GET
    Call<ActorInfo> getData(@Url String url);
}
