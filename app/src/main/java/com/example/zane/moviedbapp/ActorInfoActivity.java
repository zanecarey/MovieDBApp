package com.example.zane.moviedbapp;

import android.content.Intent;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zane.moviedbapp.adapters.RecyclerViewAdapter;
import com.example.zane.moviedbapp.interfaces.ActorInfoInterface;
import com.example.zane.moviedbapp.interfaces.CreditsInterface;
import com.example.zane.moviedbapp.model.ActorInfo;
import com.example.zane.moviedbapp.model.CreditResults;
import com.example.zane.moviedbapp.model.Credits;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActorInfoActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://api.themoviedb.org/3/person/";
    private static final String KEY = "?api_key=06ebf26c054d40dfaecf1f1b0e0965f8";

    int actorID;
    String birth, death, name, birthPlace, profile_path, biography;
    @BindView(R.id.deathLinearLayout)
    LinearLayout deathLinearLayout;
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> posters = new ArrayList<>();
    private ArrayList<Integer> movieIDs = new ArrayList<>();

    @BindView(R.id.actorInfo_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.actor_image)
    ImageView actorImage;
    @BindView(R.id.actorName_textView)
    TextView actorNameTextView;
    @BindView(R.id.birth_textView)
    TextView birthTextView;
    @BindView(R.id.death_textView)
    TextView deathTextView;
    @BindView(R.id.birthPlace_textView)
    TextView birthPlaceTextView;
    @BindView(R.id.biography_textView)
    TextView biographyTextView;
    @BindView(R.id.actorInfoLayout)
    LinearLayout actorInfoLayout;
    @BindView(R.id.actor_info_layout)
    DrawerLayout actorInfoDrawerLayout;
    @BindView(R.id.actorInfoNavView)
    NavigationView navView;
    @BindView(R.id.nameBase_textView)
    TextView nameBaseTextView;
    @BindView(R.id.birthBase_textView)
    TextView birthBaseTextView;
    @BindView(R.id.deathBase_textView)
    TextView deathBaseTextView;
    @BindView(R.id.birthPlaceBase_textView)
    TextView birthPlaceBaseTextView;
    @BindView(R.id.bioBase_textView)
    TextView bioBaseTextView;
    @BindView(R.id.knownFor_recyclerView)
    RecyclerView knownForRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_info);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);

        enableNavView();

        actorID = getID();
        getActorInfo(actorID);
        getCredits(actorID);
    }

    //get the actors information using retrofit
    private void getActorInfo(int id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        String url = BASE_URL + id + KEY;
        ActorInfoInterface actorInfoInterface = retrofit.create(ActorInfoInterface.class);
        Call<ActorInfo> call = actorInfoInterface.getData(url);

        call.enqueue(new Callback<ActorInfo>() {
            @Override
            public void onResponse(Call<ActorInfo> call, Response<ActorInfo> response) {
                birth = response.body().getBirthday();
                death = response.body().getDeathday();
                name = response.body().getName();
                birthPlace = response.body().getPlace_of_birth();
                biography = response.body().getBiography();
                profile_path = response.body().getProfile_path();
                setActorInfo();
            }

            @Override
            public void onFailure(Call<ActorInfo> call, Throwable t) {

            }
        });
    }

    //get the actors movie credits
    private void getCredits(int id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        String url = BASE_URL + id + "/combined_credits" + KEY;
        CreditsInterface creditsInterface = retrofit.create(CreditsInterface.class);
        Call<Credits> call = creditsInterface.getData(url);

        call.enqueue(new Callback<Credits>() {
            @Override
            public void onResponse(Call<Credits> call, Response<Credits> response) {
                ArrayList<CreditResults> results = response.body().getCreditResults();

                for (int i = 0; i < results.size(); i++) {
                    titles.add(results.get(i).getTitle());
                    posters.add(MainActivity.IMAGE_URL + results.get(i).getPoster_path());
                    movieIDs.add(results.get(i).getId());
                }
                initCreditRecyclerView();
            }

            @Override
            public void onFailure(Call<Credits> call, Throwable t) {

            }
        });
    }

    //Display all of our info
    private void setActorInfo() {

        actorNameTextView.setText(name);
        birthTextView.setText(birth);
        if (death == null) {
            deathLinearLayout.setVisibility(View.GONE);
        } else {
            deathTextView.setText(death);
        }
        birthPlaceTextView.setText(birthPlace);
        biographyTextView.setText(biography);


        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .fitCenter();

        Glide.with(this)
                .asBitmap()
                .load(MainActivity.IMAGE_URL + profile_path)
                .apply(options)
                .into(actorImage);
    }

    //check if our intent data exists before trying to retrieve it
    private int getID() {

        if (getIntent().hasExtra("actor_id")) {
            actorID = getIntent().getIntExtra("actor_id", 0);
            return actorID;
        } else return 0;
    }

    private void initCreditRecyclerView() {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(movieIDs, titles, posters, 2, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        knownForRecyclerView.setLayoutManager(layoutManager);
        knownForRecyclerView.setAdapter(adapter);
    }

    private void enableNavView() {
        navView.setNavigationItemSelectedListener(
                menuItem -> {
                    // set item as selected to persist highlight
                    menuItem.setChecked(true);
                    // close drawer when item is tapped
                    actorInfoDrawerLayout.closeDrawers();

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            Intent intent = new Intent(ActorInfoActivity.this, MainActivity.class);
                            startActivity(intent);
                            return true;

                        case R.id.nav_search:
                            Intent intent1 = new Intent(ActorInfoActivity.this, DisplayResults.class);
                            startActivity(intent1);
                            return true;

                        case R.id.nav_discover:
                            Intent intent2 = new Intent(ActorInfoActivity.this, FilterResults.class);
                            startActivity(intent2);
                            return true;

                        case R.id.nav_watchlist:
                            Intent intent3 = new Intent(ActorInfoActivity.this, WatchListDisplay.class);
                            startActivity(intent3);
                            return true;

                        case R.id.nav_ratings:
                            Intent intent4 = new Intent(ActorInfoActivity.this, MovieRatingsDisplay.class);
                            startActivity(intent4);
                            return true;

                        case R.id.nav_playing:
                            Intent intent5 = new Intent(ActorInfoActivity.this, NowPlayingDisplay.class);
                            startActivity(intent5);
                            return true;

                        case R.id.nav_trending:
                            Intent intent6 = new Intent(ActorInfoActivity.this, TrendingDisplay.class);
                            startActivity(intent6);
                            return true;
                        default:
                            return true;
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
            case android.R.id.home:
                actorInfoDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_home:
                Intent intent = new Intent(ActorInfoActivity.this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_search:
                Intent intent1 = new Intent(ActorInfoActivity.this, DisplayResults.class);
                startActivity(intent1);
                return true;

            case R.id.action_discover:
                Intent intent2 = new Intent(ActorInfoActivity.this, FilterResults.class);
                startActivity(intent2);
                return true;

            case R.id.action_watchlist:
                Intent intent3 = new Intent(ActorInfoActivity.this, WatchListDisplay.class);
                startActivity(intent3);
                return true;

            case R.id.action_ratings:
                Intent intent4 = new Intent(ActorInfoActivity.this, MovieRatingsDisplay.class);
                startActivity(intent4);
                return true;

            case R.id.action_playing:
                Intent intent5 = new Intent(ActorInfoActivity.this, NowPlayingDisplay.class);
                startActivity(intent5);
                return true;

            case R.id.action_trending:
                Intent intent6 = new Intent(ActorInfoActivity.this, TrendingDisplay.class);
                startActivity(intent6);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
