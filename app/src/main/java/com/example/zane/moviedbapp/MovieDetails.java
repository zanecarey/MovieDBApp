package com.example.zane.moviedbapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zane.moviedbapp.model.Details;
import com.example.zane.moviedbapp.model.Feed;
import com.example.zane.moviedbapp.model.Results;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetails extends AppCompatActivity {

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String KEY = "?api_key=06ebf26c054d40dfaecf1f1b0e0965f8";

    String title, tagline, overview, genre, poster_path, release_date;
    int runtime, budget, movieID, revenue;
    DataBaseAdapter dbHelper;
    Button watchlist_btn;
    TextView movieTitle, movieInfo;
    LinearLayout detailsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        watchlist_btn = (Button) findViewById(R.id.watchlist_btn);
        movieTitle = findViewById(R.id.movieTitle);
        movieInfo = findViewById(R.id.movieInfo);
        //make our info text view scrollable
        movieInfo.setMovementMethod(new ScrollingMovementMethod());

        //listener for button to add to watchlist
        watchlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToWatchList();
            }
        });

        detailsLayout = (LinearLayout) findViewById(R.id.detailsLayout);

        int movieID = getID();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String url = BASE_URL + movieID + KEY;
        MovieInfo movieInfo = retrofit.create(MovieInfo.class);
        Call<Details> call = movieInfo.getData(url);

        call.enqueue(new Callback<Details>() {
            @Override
            public void onResponse(Call<Details> call, Response<Details> response) {

                title = response.body().getTitle();
                tagline = response.body().getTagline();
                overview = response.body().getOverview();
                runtime = response.body().getRuntime();
                budget = response.body().getBudget();
                revenue = response.body().getRevenue();
                release_date = response.body().getRelease_date();
                if(response.body().getGenre().size() == 0){
                    genre = "";
                }else{
                    genre = response.body().getGenre().get(0).getName();
                }
                poster_path = response.body().getPoster_path();

                setInfo(title, tagline, overview, runtime, budget, revenue, release_date, genre, poster_path);
            }

            @Override
            public void onFailure(Call<Details> call, Throwable t) {
                Toast.makeText(MovieDetails.this, "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setInfo(String title, String tagline, String overview, int runtime, int budget, int revenue, String release_date, String genre, String poster_path){


        //Make the movie title underlined
        SpannableString content = new SpannableString(title);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        movieTitle.setText(content);

        //Use all the movie info and put into textview
        movieInfo.append("Tagline: " + tagline + "\n" +
                "Runtime: " + runtime + " min" + "\n" +
                "Budget: $" + budget +   "\n" +
                "Revenue: $" + revenue + "\n" +
                "Release Date: " + release_date + "\n" +
                "Genre: " + genre + "\n" +
                "Overview: " + overview);

        ImageView poster = findViewById(R.id.poster);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .fitCenter();

        Glide.with(this)
                .asBitmap()
                .load(MainActivity.IMAGE_URL + poster_path)
                .apply(options)
                .into(poster);
    }

    //check if our intent data exists before trying to retrieve it
    private int getID(){

        if(getIntent().hasExtra("movie_id")){
            movieID = getIntent().getIntExtra("movie_id", 0);
            return movieID;
        } else return 0;

    }

    //check if movie is already in watchlist, if not, add it, then display snackbar message
    public void saveToWatchList(){

        dbHelper = new DataBaseAdapter(this);

        if(dbHelper.alreadyInDatabase(movieID)){
            Snackbar.make(detailsLayout, "ALREADY IN WATCHLIST", Snackbar.LENGTH_LONG)
                    .setAction("GO TO", snackbarListener)
                    .setActionTextColor(getResources().getColor(R.color.colorMovieDBgreen))
                    .show();
        } else {
            dbHelper.addMovie(movieID, title, poster_path);
            Snackbar.make(detailsLayout, "ADDED MOVIE", Snackbar.LENGTH_LONG)
                    .setAction("GO TO", snackbarListener)
                    .setActionTextColor(getResources().getColor(R.color.colorMovieDBgreen))
                    .show();
        }
    }

    // Listener for our snackbar
    View.OnClickListener snackbarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MovieDetails.this, WatchListDisplay.class);
            startActivity(intent);
        }
    };

}
