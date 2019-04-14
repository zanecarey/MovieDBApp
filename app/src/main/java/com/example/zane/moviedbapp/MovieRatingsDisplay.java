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

public class MovieRatingsDisplay extends AppCompatActivity {


    DataBaseAdapter dbHelper;
    //lists for movies
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> posters = new ArrayList<>();
    ArrayList<Integer> movieIDs = new ArrayList<>();
    ArrayList<Integer> movieRatings = new ArrayList<>();
    //lists for shows
    ArrayList<String> showTitles = new ArrayList<>();
    ArrayList<String> showPosters = new ArrayList<>();
    ArrayList<Integer> showIDs = new ArrayList<>();
    ArrayList<Integer> showRatings = new ArrayList<>();

    @BindView(R.id.ratings_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.rating_recyclerview)
    RecyclerView ratingRecyclerview;
    @BindView(R.id.show_rating_recyclerview)
    RecyclerView showRatingRecyclerview;

    RecyclerViewAdapter adapter, showAdapter;
    DrawerLayout mDrawerLayout;
    @BindView(R.id.movieRatingsTextView)
    TextView movieRatingsTextView;
    @BindView(R.id.showRatingsTextView)
    TextView showRatingsTextView;


    Boolean movieListExpanded = true, showListExpanded = true;

    private Animation animationUp, animationDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_ratings_display);
        ButterKnife.bind(this);

        myToolbar = (Toolbar) findViewById(R.id.ratings_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("MovieDBApp");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);
        mDrawerLayout = findViewById(R.id.ratings_drawer_layout);

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
                            Intent intent = new Intent(MovieRatingsDisplay.this, MainActivity.class);
                            startActivity(intent);
                            return true;

                        case R.id.nav_search:
                            Intent intent1 = new Intent(MovieRatingsDisplay.this, DisplayResults.class);
                            startActivity(intent1);
                            return true;

                        case R.id.nav_discover:
                            Intent intent2 = new Intent(MovieRatingsDisplay.this, FilterResults.class);
                            startActivity(intent2);
                            return true;

                        case R.id.nav_watchlist:
                            Intent intent3 = new Intent(MovieRatingsDisplay.this, WatchListDisplay.class);
                            startActivity(intent3);
                            return true;

                        case R.id.nav_ratings:

                            return true;

                        case R.id.nav_playing:
                            Intent intent4 = new Intent(MovieRatingsDisplay.this, NowPlayingDisplay.class);
                            startActivity(intent4);
                            return true;

                        case R.id.nav_trending:
                            Intent intent5 = new Intent(MovieRatingsDisplay.this, TrendingDisplay.class);
                            startActivity(intent5);
                            return true;

                        default:
                            return true;
                    }
                });

        dbHelper = new DataBaseAdapter(this);

        //get the ratings data from database
        Cursor results = dbHelper.getAllData("movies_rated");
        Cursor showResults = dbHelper.getAllData("shows_rated");

        if (results.getCount() == 0 && showResults.getCount() == 0) {
            Toast.makeText(this, "Ratings List Empty!", Toast.LENGTH_SHORT).show();
        } else {
            while (results.moveToNext()) {
                movieIDs.add(results.getInt(1));
                titles.add(results.getString(2));

                if (results.getString(3).contains(MainActivity.IMAGE_URL)) {
                    posters.add(results.getString(3));
                } else {
                    posters.add(MainActivity.IMAGE_URL + results.getString(3));
                }

                movieRatings.add(results.getInt(4));
            }
            while (showResults.moveToNext()) {
                showIDs.add(showResults.getInt(1));
                showTitles.add(showResults.getString(2));
                if (showResults.getString(3).contains(MainActivity.IMAGE_URL)) {
                    showPosters.add(showResults.getString(3));
                } else {
                    showPosters.add(MainActivity.IMAGE_URL + showResults.getString(3));
                }
                showRatings.add(showResults.getInt(4));
            }
        }
        initRecyclerView();
        initShowRecyclerView();
    }

    public void initRecyclerView() {

        //Create an adapter for our recyclerview
        adapter = new RecyclerViewAdapter(movieIDs, titles, posters, 1, this, movieRatings);

        //set adapter to recyclerview
        ratingRecyclerview.setAdapter(adapter);
        //set the layout mode to LinearLayout
        ratingRecyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initShowRecyclerView() {
        //Create an adapter for our show recyclerview
        showAdapter = new RecyclerViewAdapter(showIDs, showTitles, showPosters, 3, this, showRatings);

        showRatingRecyclerview.setAdapter(showAdapter);

        showRatingRecyclerview.setLayoutManager(new LinearLayoutManager(this));
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
                Intent intent = new Intent(MovieRatingsDisplay.this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_search:
                Intent intent1 = new Intent(MovieRatingsDisplay.this, DisplayResults.class);
                startActivity(intent1);
                return true;

            case R.id.action_discover:
                Intent intent2 = new Intent(MovieRatingsDisplay.this, FilterResults.class);
                startActivity(intent2);
                return true;

            case R.id.action_watchlist:
                Intent intent3 = new Intent(MovieRatingsDisplay.this, WatchListDisplay.class);
                startActivity(intent3);
                return true;

            case R.id.action_ratings:
                return true;

            case R.id.action_playing:
                Intent intent4 = new Intent(MovieRatingsDisplay.this, NowPlayingDisplay.class);
                startActivity(intent4);
                return true;

            case R.id.action_trending:
                Intent intent5 = new Intent(MovieRatingsDisplay.this, TrendingDisplay.class);
                startActivity(intent5);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @OnClick({R.id.movieRatingsTextView, R.id.showRatingsTextView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.movieRatingsTextView:
                if(movieListExpanded){
                    ratingRecyclerview.startAnimation(animationUp);
                    CountDownTimer timer = new CountDownTimer(200, 16) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            ratingRecyclerview.setVisibility(View.GONE);
                        }
                    };
                    timer.start();
                    movieRatingsTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_black_24dp,0,0,0);
                    movieListExpanded = false;
                } else {
                    ratingRecyclerview.startAnimation(animationDown);
                    CountDownTimer timer = new CountDownTimer(200, 16) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            ratingRecyclerview.setVisibility(View.VISIBLE);
                        }
                    };
                    timer.start();
                    movieRatingsTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down_black_24dp,0,0,0);
                    movieListExpanded = true;
                }
                break;
            case R.id.showRatingsTextView:
                if(showListExpanded){
                    showRatingRecyclerview.startAnimation(animationUp);
                    CountDownTimer timer = new CountDownTimer(200, 16) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            showRatingRecyclerview.setVisibility(View.GONE);
                        }
                    };
                    timer.start();
                    showRatingsTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_black_24dp ,0,0,0);
                    showListExpanded = false;
                } else {
                    showRatingRecyclerview.startAnimation(animationDown);
                    CountDownTimer timer = new CountDownTimer(200, 16) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            showRatingRecyclerview.setVisibility(View.VISIBLE);
                        }
                    };
                    timer.start();
                    showRatingsTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down_black_24dp ,0,0,0);
                    showListExpanded = true;
                }
                break;
        }
    }
}