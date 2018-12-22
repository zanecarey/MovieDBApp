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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieRatingsDisplay extends AppCompatActivity {


    DataBaseAdapter dbHelper;

    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> posters = new ArrayList<>();
    ArrayList<Integer> movieIDs = new ArrayList<>();
    ArrayList<Integer> movieRatings = new ArrayList<>();

    @BindView(R.id.ratings_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.rating_recyclerview)
    RecyclerView ratingRecyclerview;

    RecyclerViewAdapter adapter;
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_ratings_display);
        ButterKnife.bind(this);

        myToolbar = (Toolbar) findViewById(R.id.ratings_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Ratings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        mDrawerLayout = findViewById(R.id.ratings_drawer_layout);

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
                        default:
                            return true;
                    }
                });

        dbHelper = new DataBaseAdapter(this);

        //get the ratings data from database
        Cursor results = dbHelper.getAllData("movies_rated");
        if (results.getCount() == 0) {
            Toast.makeText(this, "Ratings List Empty!", Toast.LENGTH_SHORT).show();
        } else {
            while (results.moveToNext()) {
                movieIDs.add(results.getInt(1));
                titles.add(results.getString(2) + " " + results.getInt(4) + "/10");
                posters.add(MainActivity.IMAGE_URL + results.getString(3));
                //movieRatings.add(results.getInt(4));
            }
        }
        initRecyclerView();
    }

    public void initRecyclerView() {

        //Create an adapter for our recyclerview
        adapter = new RecyclerViewAdapter(movieIDs, titles, posters, this);

        //set adapter to recyclerview
        ratingRecyclerview.setAdapter(adapter);
        //set the layout mode to LinearLayout
        ratingRecyclerview.setLayoutManager(new LinearLayoutManager(this));
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
                Intent intent3 = new Intent(MovieRatingsDisplay.this, FilterResults.class);
                startActivity(intent3);
                return true;

            case R.id.action_ratings:
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}