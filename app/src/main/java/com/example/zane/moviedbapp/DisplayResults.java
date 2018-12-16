package com.example.zane.moviedbapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zane.moviedbapp.model.Feed;
import com.example.zane.moviedbapp.model.Results;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DisplayResults extends AppCompatActivity {
    private static final String TAG = "DisplayResults";

    EditText searchEditText;
    Button search_Btn, empty_Btn;
    String movieTitle;

    //ArrayLists to store the titles and posters which we insert into the recycler view
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> posters = new ArrayList<>();
    private ArrayList<Integer> movieIDs = new ArrayList<>();

    RecyclerViewAdapter adapter;

    Toolbar myToolbar;

    //boolean to make sure we don't empty the recycler view before it has been initialized
    boolean recyclerViewStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchEditText = (EditText) findViewById(R.id.movie_search_editText);
        search_Btn = (Button) findViewById(R.id.search_Btn);
        empty_Btn = (Button) findViewById(R.id.empty_Btn);

        //empty the receycler view button
        empty_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerViewStarted){

                    titles.clear();
                    posters.clear();
                    movieIDs.clear();
                    adapter.notifyDataSetChanged();
                } else {
                    Animation shake = AnimationUtils.loadAnimation(DisplayResults.this, R.anim.shake);
                    empty_Btn.startAnimation(shake);
                    Toast.makeText(DisplayResults.this, "Movie list already empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //OnClick, retrieves the JSON from the chose movie's query url
        search_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Make sure the search field is empty or not
                if(searchEditText.getText().toString().equals("")){

                    //load our animation
                    Animation shake = AnimationUtils.loadAnimation(DisplayResults.this, R.anim.shake);
                    search_Btn.startAnimation(shake);

                    Toast.makeText(DisplayResults.this, "No title entered!", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(DisplayResults.this, "Searching!", Toast.LENGTH_SHORT).show();
                    //Get the title from edit text
                    movieTitle = searchEditText.getText().toString().toLowerCase();
                    //modify the string to make it HTTP standard
                    movieTitle = movieTitle.replaceAll("\\s+", "%20");

                    String url = MainActivity.SEARCH_BASE_URL + movieTitle;

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(MainActivity.SEARCH_BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    MovieDBInterface movieDBAPI = retrofit.create(MovieDBInterface.class);
                    Call<Feed> call = movieDBAPI.getData(url);

                    call.enqueue(new Callback<Feed>() {
                        @Override
                        public void onResponse(Call<Feed> call, Response<Feed> response) {
                            Log.d(TAG, "onResponse: Server Response" + response.toString());
                            if (response.body().getResults().size() == 0)
                                Toast.makeText(DisplayResults.this, "No Results", Toast.LENGTH_SHORT).show();
                            ArrayList<Results> resultsArrayList = response.body().getResults();

                            for (int i = 0; i < resultsArrayList.size(); i++) {
                                //Add the title and image for RecyclerView
                                titles.add(resultsArrayList.get(i).getTitle());
                                posters.add(MainActivity.IMAGE_URL + resultsArrayList.get(i).getPoster_path());
                                movieIDs.add(resultsArrayList.get(i).getId());
                            }
                            initRecyclerView();
                        }

                        @Override
                        public void onFailure(Call<Feed> call, Throwable t) {
                            Log.e(TAG, "onFailure: FAIL" + t.getMessage());
                            Toast.makeText(DisplayResults.this, "", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerView");

        //set flag to true so our empty button doesn't cause crash
        recyclerViewStarted = true;
        //Create our recyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
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

                return true;

            case R.id.action_discover:
                Intent intent2 = new Intent(DisplayResults.this, FilterResults.class);
                startActivity(intent2);
                return true;

            case R.id.action_watchlist:
                Intent intent3 = new Intent(DisplayResults.this, WatchListDisplay.class);
                startActivity(intent3);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
