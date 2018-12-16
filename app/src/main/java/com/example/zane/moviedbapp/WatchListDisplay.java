package com.example.zane.moviedbapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.nfc.Tag;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WatchListDisplay extends AppCompatActivity {

    private static final String TAG = "WatchListDisplay";



    TextView watchlist_textView;
    RecyclerViewAdapter adapter;

    DataBaseAdapter dbHelper;

    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> posters = new ArrayList<>();
    ArrayList<Integer> movieIDs = new ArrayList<>();

    Toolbar myToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list_display);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Watchlist");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        watchlist_textView = (TextView) findViewById(R.id.watchlist_textView);

        dbHelper = new DataBaseAdapter(this);
        //get the watchlist data
        Cursor results = dbHelper.getAllData("movies");
        if(results.getCount() == 0){
            Toast.makeText(this, "Watchlist Empty!", Toast.LENGTH_SHORT).show();
        } else{
            while(results.moveToNext()){
                Log.d(TAG, "DATABASE ENTRY IDS: " + results.getInt(0));
                movieIDs.add(results.getInt(1));
                titles.add(results.getString(2));
                posters.add(MainActivity.IMAGE_URL + results.getString(3));
            }
        }
        initRecyclerView();
    }

    public void initRecyclerView(){
        //Create our recyclerView
        RecyclerView recyclerView = findViewById(R.id.watch_list_recyclerView);
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
                Intent intent1 = new Intent(WatchListDisplay.this, DisplayResults.class);
                startActivity(intent1);
                return true;

            case R.id.action_discover:
                Intent intent2 = new Intent(WatchListDisplay.this, FilterResults.class);
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
