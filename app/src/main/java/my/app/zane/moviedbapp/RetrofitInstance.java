package my.app.zane.moviedbapp;

import my.app.zane.moviedbapp.interfaces.CastInterface;
import my.app.zane.moviedbapp.interfaces.MovieDBInterface;
import my.app.zane.moviedbapp.interfaces.MovieDBTVInterface;
import my.app.zane.moviedbapp.interfaces.TVInfoInterface;
import my.app.zane.moviedbapp.interfaces.TVSimilarShowsInterface;
import my.app.zane.moviedbapp.interfaces.TrailerInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit = null;

    //The base url of our database we will be querying
    public static final String KEY = "?api_key=06ebf26c054d40dfaecf1f1b0e0965f8";
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String SEARCH_BASE_URL = "https://api.themoviedb.org/3/search/movie/?api_key=06ebf26c054d40dfaecf1f1b0e0965f8&language=en-US&page=1&include_adult=false&query=";
    public static final String SEARCH_BASE_URL_TV = "https://api.themoviedb.org/3/search/tv/?api_key=06ebf26c054d40dfaecf1f1b0e0965f8&language=en-US&page=1&include_adult=false&query=";
    public static final String DISCOVER_BASE_URL = "https://api.themoviedb.org/3/discover/movie?api_key=06ebf26c054d40dfaecf1f1b0e0965f8&language=en-US&page=1&include_adult=false&";
    public static final String SEARCH_NAME_BASE_URL = "https://api.themoviedb.org/3/search/person/?api_key=06ebf26c054d40dfaecf1f1b0e0965f8&language=en-US&page=1&include_adult=false&query=";
    public static final String TV_DETAILS_BASE_URL = "https://api.themoviedb.org/3/tv/";
    public static final String YOUTUBE_KEY = "AIzaSyBpl-8D7BLWaBV6wqKoa6Y08lKdXBKeKUE";
    public static final String CREDITS_BASE_URL = "https://api.themoviedb.org/3/person/";
    public static final String TV_DISCOVER_BASE = "https://api.themoviedb.org/3/discover/tv?api_key=06ebf26c054d40dfaecf1f1b0e0965f8&language=en-US&page=1&include_adult=false&";

    public static MovieDBInterface getMovieSearchService() {

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                           .baseUrl(SEARCH_BASE_URL)
                           .addConverterFactory(GsonConverterFactory.create())
                           .build();
        }
        return retrofit.create(MovieDBInterface.class);
    }

    public static MovieDBTVInterface getTVSearchService() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(SEARCH_BASE_URL_TV)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(MovieDBTVInterface.class);
    }

    public static TVInfoInterface getTVDetails() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(TV_DETAILS_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(TVInfoInterface.class);
    }

    public static CastInterface getCast() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(CastInterface.class);
    }

    public static TrailerInterface getVideo() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(TrailerInterface.class);
    }

    public static TVSimilarShowsInterface getSimilar(){
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(TVSimilarShowsInterface.class);
    }
}
