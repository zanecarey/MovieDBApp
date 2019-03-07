package com.example.zane.moviedbapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    //The base url of our database we will be querying
    public static final String SEARCH_BASE_URL = "https://api.themoviedb.org/3/search/movie/?api_key=06ebf26c054d40dfaecf1f1b0e0965f8&language=en-US&page=1&include_adult=false&query=";
    public static final String DISCOVER_BASE_URL = "https://api.themoviedb.org/3/discover/movie?api_key=06ebf26c054d40dfaecf1f1b0e0965f8&language=en-US&page=1&include_adult=false&";
    public static final String SEARCH_NAME_BASE_URL = "https://api.themoviedb.org/3/search/person/?api_key=06ebf26c054d40dfaecf1f1b0e0965f8&language=en-US&page=1&include_adult=false&query=";

    //The base url of the poster images we will be using
    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500/";

    @BindView(R.id.search_by_title_Btn)
    Button searchByTitleBtn;
    @BindView(R.id.my_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.search_by_genre_Btn)
    Button searchByGenreBtn;
    @BindView(R.id.watch_list_btn)
    Button watchListBtn;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.ratings_btn)
    Button ratingsBtn;

    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("MovieDBApp");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        mDrawerLayout = findViewById(R.id.main_drawer_layout);

        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    // set item as selected to persist highlight
                    menuItem.setChecked(true);
                    // close drawer when item is tapped
                    mDrawerLayout.closeDrawers();

                    switch (menuItem.getItemId()) {
                        case R.id.nav_search:
                            Intent intent1 = new Intent(MainActivity.this, DisplayResults.class);
                            startActivity(intent1);
                            return true;

                        case R.id.nav_discover:
                            Intent intent2 = new Intent(MainActivity.this, FilterResults.class);
                            startActivity(intent2);
                            return true;

                        case R.id.nav_watchlist:
                            Intent intent3 = new Intent(MainActivity.this, WatchListDisplay.class);
                            startActivity(intent3);
                            return true;

                        case R.id.nav_ratings:
                            Intent intent4 = new Intent(MainActivity.this, MovieRatingsDisplay.class);
                            startActivity(intent4);
                            return true;
                        case R.id.nav_playing:
                            Intent intent5 = new Intent(MainActivity.this, NowPlayingDisplay.class);
                            startActivity(intent5);
                            return true;
                        default:
                            return true;
                    }
                });
    }

    @OnClick({R.id.search_by_title_Btn, R.id.search_by_genre_Btn, R.id.watch_list_btn, R.id.ratings_btn, R.id.nowPlaying_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_by_title_Btn:
                Intent intent1 = new Intent(MainActivity.this, DisplayResults.class);
                startActivity(intent1);

                break;
            case R.id.search_by_genre_Btn:
                Intent intent2 = new Intent(MainActivity.this, FilterResults.class);
                startActivity(intent2);

                break;
            case R.id.watch_list_btn:
                Intent intent3 = new Intent(MainActivity.this, WatchListDisplay.class);
                startActivity(intent3);

                break;
            case R.id.ratings_btn:
                Intent intent4 = new Intent(MainActivity.this, MovieRatingsDisplay.class);
                startActivity(intent4);

                break;
            case R.id.nowPlaying_btn:
                Intent intent5 = new Intent(MainActivity.this, NowPlayingDisplay.class);
                startActivity(intent5);

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
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

            case R.id.action_ratings:
                Intent intent4 = new Intent(MainActivity.this, MovieRatingsDisplay.class);
                startActivity(intent4);
                return true;

            case R.id.action_playing:
                Intent intent5 = new Intent(MainActivity.this, NowPlayingDisplay.class);
                startActivity(intent5);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
