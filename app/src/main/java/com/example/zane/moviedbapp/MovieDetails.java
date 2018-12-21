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
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zane.moviedbapp.model.Cast;
import com.example.zane.moviedbapp.model.CastResults;
import com.example.zane.moviedbapp.model.Details;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetails extends AppCompatActivity {

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String KEY = "?api_key=06ebf26c054d40dfaecf1f1b0e0965f8";

    String title, tagline, overview, genre, poster_path, release_date;
    int runtime, budget, movieID, revenue, rating;
    DataBaseAdapter dbHelper;
    ArrayList<String> actors = new ArrayList<>();
    ArrayList<String> characters = new ArrayList<>();
    ArrayList<String> profile_pics = new ArrayList<>();

    CastRecyclerView adapter;


    @BindView(R.id.details_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.details_scrollview)
    ScrollView detailsScrollview;
    @BindView(R.id.poster)
    ImageView poster;
    @BindView(R.id.FAButton)
    FloatingActionButton FAButton;
    @BindView(R.id.rate_btn)
    Button rateBtn;
    @BindView(R.id.number_picker)
    NumberPicker numberPicker;
    @BindView(R.id.detailsLayout)
    LinearLayout detailsLayout;
    @BindView(R.id.tagline_textView)
    TextView taglineTextView;
    @BindView(R.id.runtime_textView)
    TextView runtimeTextView;
    @BindView(R.id.budget_textView)
    TextView budgetTextView;
    @BindView(R.id.revenue_textView)
    TextView revenueTextView;
    @BindView(R.id.date_textView)
    TextView dateTextView;
    @BindView(R.id.genre_textView)
    TextView genreTextView;
    @BindView(R.id.overview_textView)
    TextView overviewTextView;
    @BindView(R.id.details_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navView)
    NavigationView navView;
    @BindView(R.id.cast_textView)
    TextView castTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        enableNavView();

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        int movieID = getID();
        getCastInfo(movieID);
        getMovieInfo(movieID);

    }

    private void getCastInfo(int movieID){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        String url = BASE_URL + movieID + "/credits" + KEY;
        CastInterface castInterface = retrofit.create(CastInterface.class);
        Call<CastResults> call = castInterface.getData(url);

        call.enqueue(new Callback<CastResults>() {
            @Override
            public void onResponse(Call<CastResults> call, Response<CastResults> response) {
                ArrayList<Cast> results = response.body().getCast();
                if(results.size() == 0){
                    Toast.makeText(MovieDetails.this, "No results", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(MovieDetails.this, results.get(0).getName() + results.get(0).getCharacter() + results.get(0).getProfile_path(), Toast.LENGTH_SHORT).show();
                    for(int i = 0; i < 10; i++){
                        actors.add(results.get(i).getName());
                        characters.add(results.get(i).getCharacter());
                        profile_pics.add(MainActivity.IMAGE_URL + results.get(i).getProfile_path());
                    }
                    initRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<CastResults> call, Throwable t) {

            }
        });


    }

    private void getMovieInfo(int movieID){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String url = BASE_URL + movieID + KEY;
        MovieInfo movieInfo = retrofit.create(MovieInfo.class);
        Call<Details> call = movieInfo.getData(url);

        call.enqueue(new Callback<Details>() {
            @Override
            public void onResponse(Call<Details> call, Response<Details> response) {

                title = response.body().getTitle();
                tagline = response.body().getTagline();
                overview = response.body().getOverview();
                runtime = response.body().getRuntime();
                budget = response.body().getBudget();
                revenue = response.body().getRevenue();
                release_date = response.body().getRelease_date();
                if (response.body().getGenre().size() == 0) {
                    genre = "";
                } else {
                    genre = response.body().getGenre().get(0).getName();
                }
                poster_path = response.body().getPoster_path();
                getSupportActionBar().setTitle(title);
                setInfo(title, tagline, overview, runtime, budget, revenue, release_date, genre, poster_path);
            }

            @Override
            public void onFailure(Call<Details> call, Throwable t) {
                Toast.makeText(MovieDetails.this, "", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Input all the info into the textviews
    private void setInfo(String title, String tagline, String overview, int runtime, int budget, int revenue, String release_date, String genre, String poster_path) {

        //Use all the movie info and put into textview
        SpannableString mTagline = new SpannableString("Tagline:");
        mTagline.setSpan(new UnderlineSpan(), 0, mTagline.length(), 0);

        SpannableString mRuntime = underlineString("Runtime:");
        SpannableString mBudget = underlineString("Budget:");
        SpannableString mRevenue = underlineString("Revenue:");
        SpannableString mReleaseDate = underlineString("Release Date:");
        SpannableString mGenre = underlineString("Genre:");
        SpannableString mOverview = underlineString("Overview:");

        taglineTextView.append(mTagline);
        taglineTextView.append(" " + tagline + "\n");

        runtimeTextView.append(mRuntime);
        runtimeTextView.append(" " + runtime + " min" + "\n");

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        String budgetAsString = numberFormat.format(budget);
        budgetTextView.append(mBudget);
        budgetTextView.append(" $" + budgetAsString + "\n");

        String revenueAsString = numberFormat.format(revenue);
        revenueTextView.append(mRevenue);
        revenueTextView.append(" $" + revenueAsString + "\n");

        dateTextView.append(mReleaseDate);
        dateTextView.append(" " + release_date + "\n");

        genreTextView.append(mGenre);
        genreTextView.append(" " + genre + "\n");

        overviewTextView.append(mOverview);
        overviewTextView.append(" " + overview + "\n");

        ImageView poster = findViewById(R.id.poster);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .fitCenter();

        Glide.with(this)
                .asBitmap()
                .load(MainActivity.IMAGE_URL + poster_path)
                .apply(options)
                .into(poster);
    }

    //make our strings underlined
    private SpannableString underlineString(String input) {
        SpannableString content = new SpannableString(input);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        return content;
    }

    //check if our intent data exists before trying to retrieve it
    private int getID() {

        if (getIntent().hasExtra("movie_id")) {
            movieID = getIntent().getIntExtra("movie_id", 0);
            return movieID;
        } else return 0;

    }

    //check if movie is already in watchlist, if not, add it, then display snackbar message
    public void saveToWatchList() {

        dbHelper = new DataBaseAdapter(this);

        if (dbHelper.alreadyInDatabase(movieID, "movies")) {
            Snackbar.make(detailsLayout, "ALREADY IN WATCHLIST", Snackbar.LENGTH_LONG)
                    .setAction("GO TO", snackbarListenerWatchList)
                    .setActionTextColor(getResources().getColor(R.color.colorMovieDBgreen))
                    .show();
        } else {
            dbHelper.addMovie(movieID, title, poster_path);
            Snackbar.make(detailsLayout, "ADDED MOVIE", Snackbar.LENGTH_LONG)
                    .setAction("GO TO", snackbarListenerWatchList)
                    .setActionTextColor(getResources().getColor(R.color.colorMovieDBgreen))
                    .show();
        }
    }

    //save a movie rating to our database
    public void saveToRatings() {
        dbHelper = new DataBaseAdapter(this);

        if (dbHelper.alreadyInDatabase(movieID, "movies_rated")) {
            Snackbar.make(detailsLayout, "ALREADY RATED", Snackbar.LENGTH_LONG)
                    .setAction("GO TO", snackbarListenerRating)
                    .setActionTextColor(getResources().getColor(R.color.colorMovieDBgreen))
                    .show();
        } else {
            dbHelper.addRating(movieID, title, poster_path, rating);
            Snackbar.make(detailsLayout, "ADDED MOVIE", Snackbar.LENGTH_LONG)
                    .setAction("GO TO", snackbarListenerRating)
                    .setActionTextColor(getResources().getColor(R.color.colorMovieDBgreen))
                    .show();
        }
    }

    // Listener for our watchlist snackbar
    View.OnClickListener snackbarListenerWatchList = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MovieDetails.this, WatchListDisplay.class);
            startActivity(intent);
        }
    };

    // Listener for our movie rating snackbar
    View.OnClickListener snackbarListenerRating = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MovieDetails.this, MovieRatingsDisplay.class);
            startActivity(intent);
        }
    };

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
                Intent intent = new Intent(MovieDetails.this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_search:
                Intent intent1 = new Intent(MovieDetails.this, DisplayResults.class);
                startActivity(intent1);
                return true;

            case R.id.action_discover:
                Intent intent2 = new Intent(MovieDetails.this, FilterResults.class);
                startActivity(intent2);
                return true;

            case R.id.action_watchlist:
                Intent intent3 = new Intent(MovieDetails.this, WatchListDisplay.class);
                startActivity(intent3);
                return true;

            case R.id.action_ratings:
                Intent intent4 = new Intent(MovieDetails.this, MovieRatingsDisplay.class);
                startActivity(intent4);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @OnClick({R.id.FAButton, R.id.rate_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.FAButton:
                saveToWatchList();
                break;
            case R.id.rate_btn:
                rating = numberPicker.getValue();
                saveToRatings();
                break;
        }
    }

    //enable navview
    private void enableNavView(){
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                Intent intent = new Intent(MovieDetails.this, MainActivity.class);
                                startActivity(intent);
                                return true;

                            case R.id.nav_search:
                                Intent intent1 = new Intent(MovieDetails.this, DisplayResults.class);
                                startActivity(intent1);
                                return true;

                            case R.id.nav_discover:
                                Intent intent2 = new Intent(MovieDetails.this, FilterResults.class);
                                startActivity(intent2);
                                return true;

                            case R.id.nav_watchlist:
                                Intent intent3 = new Intent(MovieDetails.this, WatchListDisplay.class);
                                startActivity(intent3);
                                return true;

                            case R.id.nav_ratings:
                                Intent intent4 = new Intent(MovieDetails.this, MovieRatingsDisplay.class);
                                startActivity(intent4);
                                return true;
                            default:
                                return true;
                        }
                    }
                });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.details_recyclerView);

        adapter = new CastRecyclerView(actors, characters, profile_pics, this);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}