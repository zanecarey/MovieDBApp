package com.example.zane.moviedbapp;

import com.example.zane.moviedbapp.interfaces.MovieDBInterface;
import com.example.zane.moviedbapp.interfaces.MovieDBTVInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit = null;

    //The base url of our database we will be querying
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String SEARCH_BASE_URL = "https://api.themoviedb.org/3/search/movie/?api_key=06ebf26c054d40dfaecf1f1b0e0965f8&language=en-US&page=1&include_adult=false&query=";
    public static final String SEARCH_BASE_URL_TV = "https://api.themoviedb.org/3/search/tv/?api_key=06ebf26c054d40dfaecf1f1b0e0965f8&language=en-US&page=1&include_adult=false&query=";
    public static final String DISCOVER_BASE_URL = "https://api.themoviedb.org/3/discover/movie?api_key=06ebf26c054d40dfaecf1f1b0e0965f8&language=en-US&page=1&include_adult=false&";
    public static final String SEARCH_NAME_BASE_URL = "https://api.themoviedb.org/3/search/person/?api_key=06ebf26c054d40dfaecf1f1b0e0965f8&language=en-US&page=1&include_adult=false&query=";

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
}
