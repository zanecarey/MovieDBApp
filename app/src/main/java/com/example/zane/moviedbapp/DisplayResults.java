package com.example.zane.moviedbapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class DisplayResults extends AppCompatActivity {
    private static final String TAG = "DisplayResults";


    @BindView(R.id.my_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.movie_search_editText)
    EditText movieSearchEditText;
    @BindView(R.id.search_Btn)
    Button searchBtn;
    @BindView(R.id.empty_Btn)
    Button emptyBtn;
    @BindView(R.id.recyclerView)
    RecyclerView displayRecyclerView;

    //ArrayLists to store the titles and posters which we insert into the recycler view
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> posters = new ArrayList<>();
    private ArrayList<Integer> movieIDs = new ArrayList<>();

    RecyclerViewAdapter adapter;

    String movieTitle;

    //boolean to make sure we don't empty the recycler view before it has been initialized
    boolean recyclerViewStarted = false;

    DrawerLayout mDrawerLayout;

    LayoutAnimationController animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);
        mDrawerLayout = findViewById(R.id.display_drawer_layout);

        int resID = R.anim.layout_animation_fall_down;
        animation = AnimationUtils.loadLayoutAnimation(this, resID);

        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    // set item as selected to persist highlight
                    menuItem.setChecked(true);
                    // close drawer when item is tapped
                    mDrawerLayout.closeDrawers();

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            Intent intent = new Intent(DisplayResults.this, MainActivity.class);
                            startActivity(intent);
                            return true;

                        case R.id.nav_search:

                            return true;

                        case R.id.nav_discover:
                            Intent intent2 = new Intent(DisplayResults.this, FilterResults.class);
                            startActivity(intent2);
                            return true;

                        case R.id.nav_watchlist:
                            Intent intent3 = new Intent(DisplayResults.this, WatchListDisplay.class);
                            startActivity(intent3);
                            return true;

                        case R.id.nav_ratings:
                            Intent intent4 = new Intent(DisplayResults.this, MovieRatingsDisplay.class);
                            startActivity(intent4);
                            return true;

                        case R.id.nav_playing:
                            Intent intent5 = new Intent(DisplayResults.this, NowPlayingDisplay.class);
                            startActivity(intent5);
                            return true;

                        case R.id.nav_trending:
                            Intent intent6 = new Intent(DisplayResults.this, TrendingDisplay.class);
                            startActivity(intent6);
                            return true;
                        default:
                            return true;
                    }
                });

    }

    //empty the recyclerview
    @OnClick(R.id.empty_Btn)
    public void onEmptyBtnClicked() {
        if (recyclerViewStarted) {
            titles.clear();
            posters.clear();
            movieIDs.clear();
            adapter.notifyDataSetChanged();
        } else {
            Animation shake = AnimationUtils.loadAnimation(DisplayResults.this, R.anim.shake);
            emptyBtn.startAnimation(shake);
            Toast.makeText(DisplayResults.this, "Movie list already empty!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.search_Btn)
    public void onSearchBtnClicked() {

        hideSoftKeyboard(this);


        //Make sure the search field is empty or not
        if (movieSearchEditText.getText().toString().equals("")) {

            //load our animation
            Animation shake = AnimationUtils.loadAnimation(DisplayResults.this, R.anim.shake);
            searchBtn.startAnimation(shake);

            Toast.makeText(DisplayResults.this, "No title entered!", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(DisplayResults.this, "Searching!", Toast.LENGTH_SHORT).show();
            //Get the title from edit text
            movieTitle = movieSearchEditText.getText().toString().toLowerCase();
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

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerView");

        //set flag to true so our empty button doesn't cause crash
        recyclerViewStarted = true;
        //Create our recyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        //Create an adapter for our recyclerview
        adapter = new RecyclerViewAdapter(movieIDs, titles, posters, 1,this);

        //set adapter to recyclerview
        recyclerView.setAdapter(adapter);

        //set animation for recyclerview
        recyclerView.setLayoutAnimation(animation);

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
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_home:
                Intent intent = new Intent(DisplayResults.this, MainActivity.class);
                startActivity(intent);
                return true;

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

            case R.id.action_ratings:
                Intent intent4 = new Intent(DisplayResults.this, MovieRatingsDisplay.class);
                startActivity(intent4);
                return true;

            case R.id.action_playing:
                Intent intent5 = new Intent(DisplayResults.this, NowPlayingDisplay.class);
                startActivity(intent5);
                return true;

            case R.id.action_trending:
                Intent intent6 = new Intent(DisplayResults.this, TrendingDisplay.class);
                startActivity(intent6);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    //hide keyboard after search button his clicked
    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
