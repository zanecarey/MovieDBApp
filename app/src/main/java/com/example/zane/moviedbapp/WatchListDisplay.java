package com.example.zane.moviedbapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.zane.moviedbapp.adapters.DataBaseAdapter;
import com.example.zane.moviedbapp.adapters.RecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WatchListDisplay extends AppCompatActivity {

    private static final String TAG = "WatchListDisplay";

    DataBaseAdapter dbHelper;

    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> posters = new ArrayList<>();
    ArrayList<Integer> movieIDs = new ArrayList<>();

    @BindView(R.id.watch_list_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.watch_list_recyclerView)
    RecyclerView watchListRecyclerView;

    RecyclerViewAdapter adapter;
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list_display);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("MovieDBApp");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        mDrawerLayout = findViewById(R.id.watch_list_drawer_layout);

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
        if (results.getCount() == 0) {
            Toast.makeText(this, "Watchlist Empty!", Toast.LENGTH_SHORT).show();
        } else {
            while (results.moveToNext()) {
                Log.d(TAG, "DATABASE ENTRY IDS: " + results.getInt(0));
                movieIDs.add(results.getInt(1));
                titles.add(results.getString(2));
                if(results.getString(3).contains(MainActivity.IMAGE_URL)){
                    posters.add(results.getString(3));
                } else {
                    posters.add(MainActivity.IMAGE_URL + results.getString(3));
                }
            }
        }
        initRecyclerView();
    }

    public void initRecyclerView() {
        //Create an adapter for our recyclerview
        adapter = new RecyclerViewAdapter(movieIDs, titles, posters, 1,this);

        //set adapter to recyclerview
        watchListRecyclerView.setAdapter(adapter);

        //set the layout mode to LinearLayout
        watchListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
}