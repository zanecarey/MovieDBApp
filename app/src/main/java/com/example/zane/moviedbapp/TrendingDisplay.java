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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ScrollView;

import com.example.zane.moviedbapp.adapters.RecyclerViewAdapter;
import com.example.zane.moviedbapp.interfaces.MovieDBInterface;
import com.example.zane.moviedbapp.model.Feed;
import com.example.zane.moviedbapp.model.Results;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrendingDisplay extends AppCompatActivity {

    public static final String TRENDING = "https://api.themoviedb.org/3/trending/movie/";

    ArrayList<String> titlesTrending = new ArrayList<>();
    ArrayList<String> postersTrending = new ArrayList<>();
    ArrayList<Integer> movieIDsTrending = new ArrayList<>();

    RecyclerViewAdapter adapterTrending;

    @BindView(R.id.trending_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.trending_recyclerview)
    RecyclerView trendingRecyclerview;
    @BindView(R.id.trending_scrollview)
    ScrollView trendingScrollview;
    @BindView(R.id.trending_navView)
    NavigationView trendingNavView;
    @BindView(R.id.trending_drawer_layout)
    DrawerLayout trendingDrawerLayout;

    LayoutAnimationController animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending_display);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("MovieDBApp");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);

        int resID = R.anim.layout_animation_fall_down;
        animation = AnimationUtils.loadLayoutAnimation(this, resID);
        trendingNavView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);

            trendingDrawerLayout.closeDrawers();

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    Intent intent = new Intent(TrendingDisplay.this, MainActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.nav_search:
                    Intent intent1 = new Intent(TrendingDisplay.this, DisplayResults.class);
                    startActivity(intent1);
                    return true;

                case R.id.nav_discover:
                    Intent intent2 = new Intent(TrendingDisplay.this, FilterResults.class);
                    startActivity(intent2);
                    return true;

                case R.id.nav_watchlist:
                    Intent intent3 = new Intent(TrendingDisplay.this, WatchListDisplay.class);
                    startActivity(intent3);
                    return true;

                case R.id.nav_ratings:
                    Intent intent4 = new Intent(TrendingDisplay.this, MovieRatingsDisplay.class);
                    startActivity(intent4);
                    return true;

                case R.id.nav_playing:
                    Intent intent5 = new Intent(TrendingDisplay.this, TrendingDisplay.class);
                    startActivity(intent5);
                    return true;
                default:
                    return true;
            }
        });
        getTrendingMovies();
    }

    private void getTrendingMovies(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TRENDING)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDBInterface movieDBAPI = retrofit.create(MovieDBInterface.class);
        Call<Feed> call = movieDBAPI.getData(TRENDING + "day?api_key=06ebf26c054d40dfaecf1f1b0e0965f8&language=en-US&page=1");

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                ArrayList<Results> resultsArrayList = response.body().getResults();

                for (int i = 0; i < 12; i++) {
                    titlesTrending.add(resultsArrayList.get(i).getTitle());
                    postersTrending.add(MainActivity.IMAGE_URL + resultsArrayList.get(i).getPoster_path());
                    movieIDsTrending.add(resultsArrayList.get(i).getId());
                }
                initRecyclerView();
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {

            }
        });
    }

    private void initRecyclerView(){
        adapterTrending = new RecyclerViewAdapter(movieIDsTrending, titlesTrending, postersTrending, 1, this);

        trendingRecyclerview.setAdapter(adapterTrending);
        trendingRecyclerview.setLayoutAnimation(animation);
        trendingRecyclerview.setLayoutManager(new LinearLayoutManager(this));
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
                trendingDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_home:
                Intent intent = new Intent(TrendingDisplay.this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_search:
                Intent intent1 = new Intent(TrendingDisplay.this, DisplayResults.class);
                startActivity(intent1);
                return true;

            case R.id.action_discover:
                Intent intent2 = new Intent(TrendingDisplay.this, FilterResults.class);
                startActivity(intent2);
                return true;

            case R.id.action_watchlist:
                Intent intent3 = new Intent(TrendingDisplay.this, WatchListDisplay.class);
                startActivity(intent3);
                return true;

            case R.id.action_ratings:
                Intent intent4 = new Intent(TrendingDisplay.this, MovieRatingsDisplay.class);
                startActivity(intent4);
                return true;

            case R.id.action_playing:
                Intent intent5 = new Intent(TrendingDisplay.this, NowPlayingDisplay.class);
                startActivity(intent5);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
