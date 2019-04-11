package com.example.zane.moviedbapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.example.zane.moviedbapp.adapters.RecyclerViewAdapter;
import com.example.zane.moviedbapp.interfaces.MovieDBInterface;
import com.example.zane.moviedbapp.model.Feed;
import com.example.zane.moviedbapp.model.Results;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NowPlayingDisplay extends AppCompatActivity {

    public static final String NOW_PLAYING = "https://api.themoviedb.org/3/movie/";

    ArrayList<String> titlesPlaying = new ArrayList<>();
    ArrayList<String> postersPlaying = new ArrayList<>();
    ArrayList<Integer> movieIDsPlaying = new ArrayList<>();

    ArrayList<String> titlesUpcoming = new ArrayList<>();
    ArrayList<String> postersUpcoming = new ArrayList<>();
    ArrayList<Integer> movieIDsUpcoming = new ArrayList<>();

    @BindView(R.id.playing_recyclerview)
    RecyclerView playingRecyclerview;
    @BindView(R.id.playing_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.playing_navView)
    NavigationView playingNavView;
    @BindView(R.id.playing_drawer_layout)
    DrawerLayout playingDrawerLayout;

    RecyclerViewAdapter adapterPlaying;
    RecyclerViewAdapter adapterUpcoming;
    @BindView(R.id.upcoming_recyclerview)
    RecyclerView upcomingRecyclerview;

    LayoutAnimationController animation;
    @BindView(R.id.nowPlayingTextView)
    TextView nowPlayingTextView;
    @BindView(R.id.upcomingTextView)
    TextView upcomingTextView;

    Boolean playingExpanded = true, upcomingExpanded = true;

    private Animation animationUp, animationDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing_display);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("MovieDBApp");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);

        int resID = R.anim.layout_animation_fall_down;
        animation = AnimationUtils.loadLayoutAnimation(this, resID);

        //animation for moving the lists
        animationUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        animationUp.setDuration(200);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        animationDown.setDuration(200);


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

                case R.id.nav_trending:
                    Intent intent5 = new Intent(NowPlayingDisplay.this, TrendingDisplay.class);
                    startActivity(intent5);
                    return true;
                default:
                    return true;
            }
        });

        getNowPlaying();
        getUpcoming();
    }

    //get movies from now playing url
    private void getNowPlaying() {
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

                for (int i = 0; i < 12; i++) {
                    titlesPlaying.add(resultsArrayList.get(i).getTitle());
                    postersPlaying.add(MainActivity.IMAGE_URL + resultsArrayList.get(i).getPoster_path());
                    movieIDsPlaying.add(resultsArrayList.get(i).getId());
                }
                initRecyclerViewPlaying();
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {

            }
        });
    }

    private void getUpcoming() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NOW_PLAYING)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDBInterface movieDBAPI = retrofit.create(MovieDBInterface.class);
        Call<Feed> call = movieDBAPI.getData(NOW_PLAYING + "upcoming?api_key=06ebf26c054d40dfaecf1f1b0e0965f8&language=en-US&page=1");

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                ArrayList<Results> resultsArrayList = response.body().getResults();

                for (int i = 0; i < 12; i++) {
                    titlesUpcoming.add(resultsArrayList.get(i).getTitle());
                    postersUpcoming.add(MainActivity.IMAGE_URL + resultsArrayList.get(i).getPoster_path());
                    movieIDsUpcoming.add(resultsArrayList.get(i).getId());
                }
                initRecyclerViewUpcoming();
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {

            }
        });

    }

    public void initRecyclerViewPlaying() {
        adapterPlaying = new RecyclerViewAdapter(movieIDsPlaying, titlesPlaying, postersPlaying, 1, this);

        playingRecyclerview.setAdapter(adapterPlaying);
        playingRecyclerview.setLayoutAnimation(animation);
        playingRecyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    public void initRecyclerViewUpcoming() {
        adapterUpcoming = new RecyclerViewAdapter(movieIDsUpcoming, titlesUpcoming, postersUpcoming, 1, this);

        upcomingRecyclerview.setAdapter(adapterUpcoming);
        upcomingRecyclerview.setLayoutAnimation(animation);
        upcomingRecyclerview.setLayoutManager(new LinearLayoutManager(this));
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

            case R.id.action_trending:
                Intent intent5 = new Intent(NowPlayingDisplay.this, TrendingDisplay.class);
                startActivity(intent5);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @OnClick({R.id.nowPlayingTextView, R.id.upcomingTextView})
    public void onViewClicked(View view) {
        switch(view.getId()) {
            case R.id.nowPlayingTextView:
                if(playingExpanded) {
                    playingRecyclerview.startAnimation(animationUp);
                    CountDownTimer timer = new CountDownTimer(200, 16) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            playingRecyclerview.setVisibility(View.GONE);
                        }
                    };
                    timer.start();
                    playingExpanded = false;
                } else {
                    playingRecyclerview.startAnimation(animationDown);
                    CountDownTimer timer = new CountDownTimer(200, 16) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            playingRecyclerview.setVisibility(View.VISIBLE);
                        }
                    };
                    timer.start();
                    playingExpanded = true;
                }
                break;
            case R.id.upcomingTextView:
                if(upcomingExpanded) {
                    upcomingRecyclerview.startAnimation(animationUp);
                    CountDownTimer timer = new CountDownTimer(200, 16) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            upcomingRecyclerview.setVisibility(View.GONE);
                        }
                    };
                    timer.start();
                    upcomingExpanded = false;
                } else {
                    upcomingRecyclerview.startAnimation(animationDown);
                    CountDownTimer timer = new CountDownTimer(200, 16) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            upcomingRecyclerview.setVisibility(View.VISIBLE);
                        }
                    };
                    timer.start();
                    upcomingExpanded = true;
                }
        }
    }
}
