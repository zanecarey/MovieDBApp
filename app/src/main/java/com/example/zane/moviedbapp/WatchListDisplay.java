package com.example.zane.moviedbapp;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.zane.moviedbapp.adapters.DataBaseAdapter;
import com.example.zane.moviedbapp.adapters.RecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WatchListDisplay extends AppCompatActivity {

    private static final String TAG = "WatchListDisplay";

    DataBaseAdapter dbHelper;

    //lists for movies
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> posters = new ArrayList<>();
    ArrayList<Integer> movieIDs = new ArrayList<>();

    //lists for shows
    ArrayList<String> showTitles = new ArrayList<>();
    ArrayList<String> showPosters = new ArrayList<>();
    ArrayList<Integer> showIDs = new ArrayList<>();

    @BindView(R.id.watch_list_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.watch_list_recyclerView)
    RecyclerView watchListRecyclerView;
    @BindView(R.id.watch_list_shows_recyclerView)
    RecyclerView showWatchListRecyclerView;
    @BindView(R.id.movieWatchlistTextView)
    TextView movieWatchListTextView;

    RecyclerViewAdapter adapter, showAdapter;
    DrawerLayout mDrawerLayout;

    Boolean movieListExpanded = true, showListExpanded = true;
    @BindView(R.id.showWatchListTextView)
    TextView showWatchListTextView;

    private Animation animationUp, animationDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list_display);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("MovieDBApp");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);
        mDrawerLayout = findViewById(R.id.watch_list_drawer_layout);


        //animation for moving the lists
        animationUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        animationUp.setDuration(200);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        animationDown.setDuration(200);


        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    // set item as selected to persist highlight
                    menuItem.setChecked(true);
                    // close drawer when item is tapped
                    mDrawerLayout.closeDrawers();

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            Intent intent = new Intent(WatchListDisplay.this, MainActivity.class);
                            startActivity(intent);
                            return true;

                        case R.id.nav_search:
                            Intent intent1 = new Intent(WatchListDisplay.this, DisplayResults.class);
                            startActivity(intent1);
                            return true;

                        case R.id.nav_discover:
                            Intent intent2 = new Intent(WatchListDisplay.this, FilterResults.class);
                            startActivity(intent2);
                            return true;

                        case R.id.nav_watchlist:

                            return true;

                        case R.id.nav_ratings:
                            Intent intent4 = new Intent(WatchListDisplay.this, MovieRatingsDisplay.class);
                            startActivity(intent4);
                            return true;

                        case R.id.nav_playing:
                            Intent intent5 = new Intent(WatchListDisplay.this, NowPlayingDisplay.class);
                            startActivity(intent5);
                            return true;

                        case R.id.nav_trending:
                            Intent intent6 = new Intent(WatchListDisplay.this, TrendingDisplay.class);
                            startActivity(intent6);
                            return true;
                        default:
                            return true;
                    }
                });

        dbHelper = new DataBaseAdapter(this);

        //get the watchlist data
        Cursor results = dbHelper.getAllData("movies");
        Cursor showResults = dbHelper.getAllData("shows");

        if (results.getCount() == 0 && showResults.getCount() == 0) {
            Toast.makeText(this, "Watchlist Empty!", Toast.LENGTH_SHORT).show();
        } else {
            while (results.moveToNext()) {

                movieIDs.add(results.getInt(1));
                titles.add(results.getString(2));
                if (results.getString(3).contains(MainActivity.IMAGE_URL)) {
                    posters.add(results.getString(3));
                } else {
                    posters.add(MainActivity.IMAGE_URL + results.getString(3));
                }
            }
            while (showResults.moveToNext()) {
                showIDs.add(showResults.getInt(1));
                showTitles.add(showResults.getString(2));
                if (showResults.getString(3).contains(MainActivity.IMAGE_URL)) {
                    showPosters.add(showResults.getString(3));
                } else {
                    showPosters.add(MainActivity.IMAGE_URL + showResults.getString(3));
                }
            }
        }
        initRecyclerView();
        initShowRecyclerView();
    }

    public void initRecyclerView() {
        //Create an adapter for our recyclerview
        adapter = new RecyclerViewAdapter(movieIDs, titles, posters, 1, this);

        //set adapter to recyclerview
        watchListRecyclerView.setAdapter(adapter);

        //set the layout mode to LinearLayout
        watchListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initShowRecyclerView() {
        //Create an adapter for our show recyclerview
        showAdapter = new RecyclerViewAdapter(showIDs, showTitles, showPosters, 3, this);

        showWatchListRecyclerView.setAdapter(showAdapter);

        showWatchListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_home:
                Intent intent = new Intent(WatchListDisplay.this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_search:
                Intent intent1 = new Intent(WatchListDisplay.this, DisplayResults.class);
                startActivity(intent1);
                return true;

            case R.id.action_discover:
                Intent intent2 = new Intent(WatchListDisplay.this, FilterResults.class);
                startActivity(intent2);
                return true;

            case R.id.action_watchlist:

                return true;

            case R.id.action_ratings:
                Intent intent3 = new Intent(WatchListDisplay.this, MovieRatingsDisplay.class);
                startActivity(intent3);
                return true;

            case R.id.action_playing:
                Intent intent4 = new Intent(WatchListDisplay.this, NowPlayingDisplay.class);
                startActivity(intent4);
                return true;

            case R.id.action_trending:
                Intent intent5 = new Intent(WatchListDisplay.this, TrendingDisplay.class);
                startActivity(intent5);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @OnClick({R.id.movieWatchlistTextView})
    public void onViewClicked() {
        if (movieListExpanded) {
            watchListRecyclerView.startAnimation(animationUp);

            CountDownTimer timer = new CountDownTimer(200, 16) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    watchListRecyclerView.setVisibility(View.GONE);
                }
            };

            timer.start();
            movieWatchListTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_black_24dp,0,0,0);
            movieListExpanded = false;
        } else {
            watchListRecyclerView.startAnimation(animationDown);
            CountDownTimer timer = new CountDownTimer(200, 16) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    watchListRecyclerView.setVisibility(View.VISIBLE);
                }
            };
            timer.start();
            movieWatchListTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down_black_24dp,0,0,0);
            movieListExpanded = true;
        }
    }

    @OnClick(R.id.showWatchListTextView)
    public void onViewClicked2() {
        if(showListExpanded) {
            showWatchListRecyclerView.startAnimation(animationUp);
            CountDownTimer timer = new CountDownTimer(200, 16) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    showWatchListRecyclerView.setVisibility(View.GONE);
                }
            };
            timer.start();
            showWatchListTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_black_24dp,0,0,0);
            showListExpanded = false;
        } else {
            showWatchListRecyclerView.startAnimation(animationDown);
            CountDownTimer timer = new CountDownTimer(200, 16) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    showWatchListRecyclerView.setVisibility(View.VISIBLE);
                }
            };
            timer.start();
            showWatchListTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down_black_24dp,0,0,0);
            showListExpanded = true;
        }
    }
}