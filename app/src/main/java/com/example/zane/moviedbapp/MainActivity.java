package com.example.zane.moviedbapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //The base url of our database we will be querying
    public static final String SEARCH_BASE_URL = "https://api.themoviedb.org/3/search/movie/?api_key=06ebf26c054d40dfaecf1f1b0e0965f8&language=en-US&page=1&include_adult=false&query=";
    public static final String DISCOVER_BASE_URL = "https://api.themoviedb.org/3/discover/movie?api_key=06ebf26c054d40dfaecf1f1b0e0965f8&language=en-US&page=1&include_adult=false&";
    public static final String SEARCH_NAME_BASE_URL = "https://api.themoviedb.org/3/search/person/?api_key=06ebf26c054d40dfaecf1f1b0e0965f8&language=en-US&page=1&include_adult=false&query=";

    //The base url of the poster images we will be using
    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500/";
    public static final String KEY = "06ebf26c054d40dfaecf1f1b0e0965f8";

    Button searchByTitleBtn, searchByFiltersBtn, watchlist_btn, ratings_btn;

    Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(myToolbar);


        searchByTitleBtn = (Button) findViewById(R.id.search_by_title_Btn);
        searchByFiltersBtn = (Button) findViewById(R.id.search_by_genre_Btn);
        watchlist_btn = (Button) findViewById(R.id.watch_list_btn);
        ratings_btn = (Button) findViewById(R.id.ratings_btn);


        searchByTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayResults.class);
                startActivity(intent);
            }
        });

        searchByFiltersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FilterResults.class);
                startActivity(intent);
            }
        });

        watchlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WatchListDisplay.class);
                startActivity(intent);
            }
        });

        ratings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MovieRatingsDisplay.class);
                startActivity(intent);
            }
        });
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
            case R.id.action_search:
                Intent intent1 = new Intent(MainActivity.this, DisplayResults.class);
                startActivity(intent1);
                return true;

            case R.id.action_discover:
                Intent intent2 = new Intent(MainActivity.this, FilterResults.class);
                startActivity(intent2);
                return true;

            case R.id.action_watchlist:
                Intent intent3 = new Intent(MainActivity.this, WatchListDisplay.class);
                startActivity(intent3);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
