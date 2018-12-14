package com.example.zane.moviedbapp;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.nfc.Tag;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list_display);

        watchlist_textView = (TextView) findViewById(R.id.watchlist_textView);

        dbHelper = new DataBaseAdapter(this);
        //get the watchlist data
        Cursor results = dbHelper.getAllData();
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
}
