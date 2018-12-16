package com.example.zane.moviedbapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MovieRatingsDisplay extends AppCompatActivity {

    TextView watchlist_textView;
    RecyclerViewAdapter adapter;

    DataBaseAdapter dbHelper;

    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> posters = new ArrayList<>();
    ArrayList<Integer> movieIDs = new ArrayList<>();
    ArrayList<Integer> movieRatings = new ArrayList<>();

    Toolbar myToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_ratings_display);

        myToolbar = (Toolbar) findViewById(R.id.ratings_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Ratings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DataBaseAdapter(this);

        //get the ratings data from database
        Cursor results = dbHelper.getAllData("movies_rated");
        if(results.getCount() == 0){
            Toast.makeText(this, "Ratings List Empty!", Toast.LENGTH_SHORT).show();
        } else{
            while(results.moveToNext()){
                movieIDs.add(results.getInt(1));
                titles.add(results.getString(2) + " " + results.getInt(4) + "/10");
                posters.add(MainActivity.IMAGE_URL + results.getString(3));
                //movieRatings.add(results.getInt(4));
            }
        }
        initRecyclerView();
    }

    public void initRecyclerView(){
        //Create our recyclerView
        RecyclerView recyclerView = findViewById(R.id.rating_recyclerview);
        //Create an adapter for our recyclerview
        adapter = new RecyclerViewAdapter(movieIDs, titles, posters, this);

        //set adapter to recyclerview
        recyclerView.setAdapter(adapter);
        //set the layout mode to LinearLayout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                Intent intent1 = new Intent(MovieRatingsDisplay.this, DisplayResults.class);
                startActivity(intent1);
                return true;

            case R.id.action_discover:
                Intent intent2 = new Intent(MovieRatingsDisplay.this, FilterResults.class);
                startActivity(intent2);
                return true;

            case R.id.action_watchlist:

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
