package com.example.zane.moviedbapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.zane.moviedbapp.adapters.RecyclerViewAdapter;
import com.example.zane.moviedbapp.interfaces.MovieDBInterface;
import com.example.zane.moviedbapp.model.Feed;
import com.example.zane.moviedbapp.model.Results;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NowPlayingDisplay extends AppCompatActivity {

    public static final String NOW_PLAYING = "https://api.themoviedb.org/3/movie/";

    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> posters = new ArrayList<>();
    ArrayList<Integer> movieIDs = new ArrayList<>();

    @BindView(R.id.playing_recyclerview)
    RecyclerView playingRecyclerview;
    @BindView(R.id.playing_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.playing_navView)
    NavigationView playingNavView;
    @BindView(R.id.playing_drawer_layout)
    DrawerLayout playingDrawerLayout;

    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing_display);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        playingNavView.setNavigationItemSelectedListener(menuItem -> {
            // set item as selected to persist highlight
            menuItem.setChecked(true);
            // close drawer when item is tapped
            playingDrawerLayout.closeDrawers();

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    Intent intent = new Intent(NowPlayingDisplay.this, MainActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.nav_search:
                    Intent intent1 = new Intent(NowPlayingDisplay.this, DisplayResults.class);
                    startActivity(intent1);
                    return true;

                case R.id.nav_discover:
                    Intent intent2 = new Intent(NowPlayingDisplay.this, FilterResults.class);
                    startActivity(intent2);
                    return true;

                case R.id.nav_watchlist:
                    Intent intent3 = new Intent(NowPlayingDisplay.this, WatchListDisplay.class);
                    startActivity(intent3);
                    return true;

                case R.id.nav_ratings:
                    Intent intent4 = new Intent(NowPlayingDisplay.this, MovieRatingsDisplay.class);
                    startActivity(intent4);
                    return true;
                default:
                    return true;
            }
        });

        getNowPlaying();
    }

    //get movies from now playing url
    private void getNowPlaying(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NOW_PLAYING)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDBInterface movieDBAPI = retrofit.create(MovieDBInterface.class);
        Call<Feed> call = movieDBAPI.getData(NOW_PLAYING + "now_playing?api_key=06ebf26c054d40dfaecf1f1b0e0965f8&language=en-US&page=1");

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                ArrayList<Results> resultsArrayList = response.body().getResults();

                for(int i = 0; i < 12; i++){
                    titles.add(resultsArrayList.get(i).getTitle());
                    posters.add(MainActivity.IMAGE_URL + resultsArrayList.get(i).getPoster_path());
                    movieIDs.add(resultsArrayList.get(i).getId());
                }
                initRecyclerView();
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {

            }
        });
    }

    public void initRecyclerView() {
        adapter = new RecyclerViewAdapter(movieIDs, titles, posters, 1, this);

        playingRecyclerview.setAdapter(adapter);

        playingRecyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //Toast.makeText(this, "Menu created", Toast.LENGTH_SHORT).show();
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                playingDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_home:
                Intent intent = new Intent(NowPlayingDisplay.this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_search:
                Intent intent1 = new Intent(NowPlayingDisplay.this, DisplayResults.class);
                startActivity(intent1);
                return true;

            case R.id.action_discover:
                Intent intent2 = new Intent(NowPlayingDisplay.this, FilterResults.class);
                startActivity(intent2);
                return true;

            case R.id.action_watchlist:
                Intent intent3 = new Intent(NowPlayingDisplay.this, WatchListDisplay.class);
                startActivity(intent3);
                return true;

            case R.id.action_ratings:
                Intent intent4 = new Intent(NowPlayingDisplay.this, MovieRatingsDisplay.class);
                startActivity(intent4);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
