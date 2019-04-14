package com.example.zane.moviedbapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zane.moviedbapp.adapters.CastRecyclerView;
import com.example.zane.moviedbapp.adapters.DataBaseAdapter;
import com.example.zane.moviedbapp.adapters.RecyclerViewAdapter;
import com.example.zane.moviedbapp.interfaces.CastInterface;
import com.example.zane.moviedbapp.interfaces.RecommendationsInterface;
import com.example.zane.moviedbapp.interfaces.TrailerInterface;
import com.example.zane.moviedbapp.model.Cast;
import com.example.zane.moviedbapp.model.CastResults;
import com.example.zane.moviedbapp.model.TVDetails;
import com.example.zane.moviedbapp.model.TVFeed;
import com.example.zane.moviedbapp.model.TVResults;
import com.example.zane.moviedbapp.model.TrailerResults;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TVDetailsActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener{

    @BindView(R.id.tvDetails_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.tv_poster)
    ImageView tvPoster;
    @BindView(R.id.tv_trailer_button)
    Button tvTrailerButton;
    @BindView(R.id.tv_rate_btn)
    Button tvRateBtn;
    @BindView(R.id.tv_ratingBar)
    RatingBar tvRatingBar;
    @BindView(R.id.tv_details_recRecyclerView)
    RecyclerView tvDetailsRecRecyclerView;
    @BindView(R.id.createdBy_textView)
    TextView createdByTextView;
    @BindView(R.id.firstDate_textView)
    TextView firstDateTextView;
    @BindView(R.id.lastDate_textView)
    TextView lastDateTextView;
    @BindView(R.id.genre_textView)
    TextView genreTextView;
    @BindView(R.id.numEpisodes_textView)
    TextView numEpisodesTextView;
    @BindView(R.id.numSeasons_textView)
    TextView numSeasonsTextView;
    @BindView(R.id.tv_overview_textView)
    TextView tvOverviewTextView;
    @BindView(R.id.tv_details_recyclerView)
    RecyclerView tvDetailsRecyclerView;
    @BindView(R.id.tvDetailsLayout)
    LinearLayout tvDetailsLayout;
    @BindView(R.id.tvDetailsScrollview)
    ScrollView tvDetailsScrollview;
    @BindView(R.id.tv_FAButton)
    FloatingActionButton tvFAButton;
    @BindView(R.id.tv_navView)
    NavigationView tvNavView;
    @BindView(R.id.tv_drawerLayout)
    DrawerLayout tvDrawerLayout;

    DataBaseAdapter dbHelper;

    YouTubePlayerSupportFragment youtubeFragment;
    int tvID, numEpisodes, numSeasons, rating;

    String created_by, name, genre, firstAired, lastAired, overview, poster_path, youtubeTrailer;

    //values for cast of movie
    ArrayList<String> actors = new ArrayList<>();
    ArrayList<String> characters = new ArrayList<>();
    ArrayList<String> profile_pics = new ArrayList<>();
    ArrayList<Integer> actorIDs = new ArrayList<>();

    //values for similar shows
    ArrayList<String> recTitles = new ArrayList<>();
    ArrayList<String> recPosters = new ArrayList<>();
    ArrayList<Integer> recIDs = new ArrayList<>();

    CastRecyclerView castAdapter;
    RecyclerViewAdapter similarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvdetails);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);

        youtubeFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.tv_youtube_fragment);
        tvID = getID();
        getShowInfo(tvID);
        getCastInfo(tvID);
        getVideo(tvID);
        getRecommendations(tvID);

        tvNavView.setNavigationItemSelectedListener(menuItem -> {
            // set item as selected to persist highlight
            menuItem.setChecked(true);
            // close drawer when item is tapped
            tvDrawerLayout.closeDrawers();

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    Intent intent = new Intent(TVDetailsActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.nav_search:
                    Intent intent1 = new Intent(TVDetailsActivity.this, DisplayResults.class);
                    startActivity(intent1);
                    return true;

                case R.id.nav_discover:
                    Intent intent2 = new Intent(TVDetailsActivity.this, FilterResults.class);
                    startActivity(intent2);
                    return true;

                case R.id.nav_watchlist:
                    Intent intent3 = new Intent(TVDetailsActivity.this, WatchListDisplay.class);
                    startActivity(intent3);
                    return true;

                case R.id.nav_ratings:
                    Intent intent4 = new Intent(TVDetailsActivity.this, MovieRatingsDisplay.class);
                    startActivity(intent4);
                    return true;

                case R.id.nav_trending:
                    Intent intent5 = new Intent(TVDetailsActivity.this, TrendingDisplay.class);
                    startActivity(intent5);
                    return true;
                default:
                    return true;
            }
        });
    }

    //check if our intent data exists before trying to retrieve it
    private int getID() {

        if (getIntent().hasExtra("tv_id")) {
            tvID = getIntent().getIntExtra("tv_id", 0);
            return tvID;
        } else return 0;
    }

    //retreive info on the show
    private void getShowInfo(int tvID) {
        String url = RetrofitInstance.TV_DETAILS_BASE_URL + tvID+ RetrofitInstance.KEY;

        Call<TVDetails> call = RetrofitInstance.getTVDetails().getData(url);

        call.enqueue(new Callback<TVDetails>() {
            @Override
            public void onResponse(Call<TVDetails> call, Response<TVDetails> response) {
                if(response.body().getCreated_by().size() == 0){
                    created_by = "";
                } else {
                    created_by = response.body().getCreated_by().get(0).getName();
                }
                name = response.body().getName();
                firstAired = response.body().getFirst_air_date();
                lastAired = response.body().getLast_air_date();
                if(response.body().getGenre().size() == 0){
                    genre = "";
                } else {
                    genre = response.body().getGenre().get(0).getName();
                }
                numEpisodes = response.body().getNumber_of_episodes();
                numSeasons = response.body().getNumber_of_seasons();
                overview = response.body().getOverview();
                poster_path = response.body().getPoster_path();
                getSupportActionBar().setTitle(name);
                setInfo();
            }

            @Override
            public void onFailure(Call<TVDetails> call, Throwable t) {

            }
        });
    }

    //get cast info
    private void getCastInfo(int tvID){
        String url = RetrofitInstance.TV_DETAILS_BASE_URL + tvID + "/credits" + RetrofitInstance.KEY;

        Call<CastResults> call = RetrofitInstance.getCast().getData(url);

        call.enqueue(new Callback<CastResults>() {
            @Override
            public void onResponse(Call<CastResults> call, Response<CastResults> response) {
                ArrayList<Cast> results = response.body().getCast();
                if (results.size() == 0) {
                    Toast.makeText(TVDetailsActivity.this, "No results", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < results.size(); i++) {
                        if (i > 12) break;
                        actors.add(results.get(i).getName());
                        characters.add(results.get(i).getCharacter());
                        profile_pics.add(MainActivity.IMAGE_URL + results.get(i).getProfile_path());
                        actorIDs.add(results.get(i).getId());
                    }
                    initCastRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<CastResults> call, Throwable t) {

            }
        });
    }

    private void getVideo(int tvID) {

        String url = RetrofitInstance.TV_DETAILS_BASE_URL + tvID + "/videos" + RetrofitInstance.KEY;

        Call<TrailerResults> call = RetrofitInstance.getVideo().getData(url);

        call.enqueue(new Callback<TrailerResults>() {
            @Override
            public void onResponse(Call<TrailerResults> call, Response<TrailerResults> response) {
                if (response.body().getResults().size() > 0) {

                    youtubeTrailer = response.body().getResults().get(0).getKey();
                } else {
                    Toast.makeText(TVDetailsActivity.this, "No trailer found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TrailerResults> call, Throwable t) {

            }
        });
    }

    private void getRecommendations(int tvID) {
        String url = RetrofitInstance.TV_DETAILS_BASE_URL + tvID + "/recommendations" + RetrofitInstance.KEY;

        Call<TVFeed> call = RetrofitInstance.getSimilar().getData(url);

        call.enqueue(new Callback<TVFeed>() {
            @Override
            public void onResponse(Call<TVFeed> call, Response<TVFeed> response) {
                ArrayList<TVResults> results = response.body().getResults();

                for (int i = 0; i < results.size(); i++) {
                    recTitles.add(results.get(i).getTitle());
                    recPosters.add(MainActivity.IMAGE_URL + results.get(i).getPoster_path());
                    recIDs.add(results.get(i).getId());
                }
                initRecRecyclerView();
            }

            @Override
            public void onFailure(Call<TVFeed> call, Throwable t) {

            }
        });
    }

    private void setInfo(){
        createdByTextView.setText(created_by);
        firstDateTextView.setText(firstAired);
        lastDateTextView.setText(lastAired);
        genreTextView.setText(genre);
        numEpisodesTextView.setText(String.valueOf(numEpisodes));
        numSeasonsTextView.setText(String.valueOf(numSeasons));
        tvOverviewTextView.setText(overview);

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .fitCenter();

        Glide.with(this)
                .asBitmap()
                .load(MainActivity.IMAGE_URL + poster_path)
                .apply(options)
                .into(tvPoster);
    }

    private void initCastRecyclerView() {

        castAdapter = new CastRecyclerView(actors, characters, profile_pics, actorIDs, this);

        tvDetailsRecyclerView.setAdapter(castAdapter);

        tvDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initRecRecyclerView(){
        similarAdapter = new RecyclerViewAdapter(recIDs, recTitles, recPosters, 2, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        tvDetailsRecRecyclerView.setLayoutManager(layoutManager);

        tvDetailsRecRecyclerView.setAdapter(similarAdapter);
    }

    @OnClick({R.id.tv_FAButton, R.id.tv_rate_btn, R.id.tv_trailer_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_FAButton:
                saveToWatchList();
                break;
            case R.id.tv_rate_btn:
                rating = (int)tvRatingBar.getRating();
                saveToRatings();
                break;

            case R.id.tv_trailer_button:
                youtubeFragment.initialize(RetrofitInstance.YOUTUBE_KEY, TVDetailsActivity.this);
        }
    }

    //check if show is already in watchlist, if not, add it, then display snackbar message
    public void saveToWatchList() {

        dbHelper = new DataBaseAdapter(this);

        if (dbHelper.alreadyInDatabase(tvID, "shows")) {
            Snackbar.make(tvDetailsLayout, "ALREADY IN WATCHLIST", Snackbar.LENGTH_LONG)
                    .setAction("GO TO", snackbarListenerWatchList)
                    .setActionTextColor(getResources().getColor(R.color.colorMovieDBgreen))
                    .show();
        } else {
            dbHelper.addShow(tvID, name, poster_path);
            Snackbar.make(tvDetailsLayout, "ADDED MOVIE", Snackbar.LENGTH_LONG)
                    .setAction("GO TO", snackbarListenerWatchList)
                    .setActionTextColor(getResources().getColor(R.color.colorMovieDBgreen))
                    .show();
        }
    }

    //save a show rating to our database
    public void saveToRatings() {
        dbHelper = new DataBaseAdapter(this);

        if (dbHelper.alreadyInDatabase(tvID, "shows_rated")) {
            Snackbar.make(tvDetailsLayout, "ALREADY RATED", Snackbar.LENGTH_LONG)
                    .setAction("GO TO", snackbarListenerRating)
                    .setActionTextColor(getResources().getColor(R.color.colorMovieDBgreen))
                    .show();
        } else {
            dbHelper.addShowRating(tvID, name, poster_path, rating);
            Snackbar.make(tvDetailsLayout, "RATED MOVIE", Snackbar.LENGTH_LONG)
                    .setAction("GO TO", snackbarListenerRating)
                    .setActionTextColor(getResources().getColor(R.color.colorMovieDBgreen))
                    .show();
        }
    }




    // Listener for our watchlist snackbar
    View.OnClickListener snackbarListenerWatchList = v -> {
        Intent intent = new Intent(TVDetailsActivity.this, WatchListDisplay.class);
        startActivity(intent);
    };

    // Listener for our movie rating snackbar
    View.OnClickListener snackbarListenerRating = v -> {
        Intent intent = new Intent(TVDetailsActivity.this, MovieRatingsDisplay.class);
        startActivity(intent);
    };



    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            //youTubePlayer.setFullscreen(true);
            youTubePlayer.loadVideo(youtubeTrailer);
            youTubePlayer.play();
            //youTubePlayer.cueVideo(youtubeVideo);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                tvDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_home:
                Intent intent = new Intent(TVDetailsActivity.this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_search:
                Intent intent1 = new Intent(TVDetailsActivity.this, DisplayResults.class);
                startActivity(intent1);
                return true;

            case R.id.action_discover:
                Intent intent2 = new Intent(TVDetailsActivity.this, FilterResults.class);
                startActivity(intent2);
                return true;

            case R.id.action_watchlist:
                Intent intent3 = new Intent(TVDetailsActivity.this, WatchListDisplay.class);
                startActivity(intent3);
                return true;

            case R.id.action_ratings:
                Intent intent4 = new Intent(TVDetailsActivity.this, MovieRatingsDisplay.class);
                startActivity(intent4);
                return true;

            case R.id.action_playing:
                Intent intent5 = new Intent(TVDetailsActivity.this, NowPlayingDisplay.class);
                startActivity(intent5);
                return true;

            case R.id.action_trending:
                Intent intent6 = new Intent(TVDetailsActivity.this, TrendingDisplay.class);
                startActivity(intent6);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //Toast.makeText(this, "Menu created", Toast.LENGTH_SHORT).show();
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }
}
