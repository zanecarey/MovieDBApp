package my.app.zane.moviedbapp.interfaces;





import my.app.zane.moviedbapp.model.NameResults;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface NameSearchInterface {

    @Headers("Content-Type: application/json")
    @GET
    Observable<NameResults> getData(@Url String url);
}
