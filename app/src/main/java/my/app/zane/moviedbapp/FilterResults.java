package my.app.zane.moviedbapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import my.app.zane.moviedbapp.adapters.RecyclerViewAdapter;
import my.app.zane.moviedbapp.interfaces.MovieDBInterface;
import my.app.zane.moviedbapp.interfaces.MovieDBTVInterface;
import my.app.zane.moviedbapp.interfaces.NameSearchInterface;
import my.app.zane.moviedbapp.model.Feed;
import my.app.zane.moviedbapp.model.NameResults;
import my.app.zane.moviedbapp.model.Results;
import my.app.zane.moviedbapp.model.TVFeed;
import my.app.zane.moviedbapp.model.TVResults;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class FilterResults extends AppCompatActivity {


    private boolean hideButtonFlag = true;

    private final int SORT_BY_SPINNER = 1;
    private final int RATING_SPINNER = 2;
    private final int GENRE_SPINNER = 3;
    private final int TYPE_SPINNER = 4;

    String sortBy_value, rating_value;
    int with_genre_value = 0;
    int searchNameID;

    String returnType = "Movie";

    RecyclerViewAdapter adapter;

    Boolean recyclerViewStarted = false;

    @BindView(R.id.filter_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.type_spinner)
    Spinner typeSpinner;
    @BindView(R.id.sortBy_spinner)
    Spinner sortBySpinner;
    @BindView(R.id.rating_spinner)
    Spinner ratingSpinner;
    @BindView(R.id.genre_spinner)
    Spinner genreSpinner;
    @BindView(R.id.filterSearch_btn)
    Button filterSearchBtn;
    @BindView(R.id.filterRecyclerView)
    RecyclerView filterRecyclerView;
    @BindView(R.id.empty_btn)
    Button emptyBtn;
    @BindView(R.id.search_edit_text)
    EditText searchEditText;
    @BindView(R.id.year_edit_text)
    EditText yearEditText;
    @BindView(R.id.before_radio)
    RadioButton beforeRadio;
    @BindView(R.id.after_radio)
    RadioButton afterRadio;
    @BindView(R.id.filterLayout)
    RelativeLayout filterLayout;
    @BindView(R.id.hide_button)
    CardView hideButton;
    @BindView(R.id.arrow)
    ImageView arrow;

    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> posters = new ArrayList<>();
    private ArrayList<Integer> movieIDs = new ArrayList<>();

    DrawerLayout mDrawerLayout;

    LayoutAnimationController animation;

    private Animation animationUp, animationDown;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_results);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Discover");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);
        mDrawerLayout = findViewById(R.id.filter_drawer_layout);

        //prevent keyboard popup
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //animation for moving the filters
        animationUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        animationUp.setDuration(200);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        animationDown.setDuration(200);

        //animation for recyclerview
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
                            Intent intent = new Intent(FilterResults.this, MainActivity.class);
                            startActivity(intent);
                            return true;

                        case R.id.nav_search:
                            Intent intent1 = new Intent(FilterResults.this, DisplayResults.class);
                            startActivity(intent1);
                            return true;

                        case R.id.nav_discover:

                            return true;

                        case R.id.nav_watchlist:
                            Intent intent3 = new Intent(FilterResults.this, WatchListDisplay.class);
                            startActivity(intent3);
                            return true;

                        case R.id.nav_ratings:
                            Intent intent4 = new Intent(FilterResults.this, MovieRatingsDisplay.class);
                            startActivity(intent4);
                            return true;

                        case R.id.nav_playing:
                            Intent intent5 = new Intent(FilterResults.this, NowPlayingDisplay.class);
                            startActivity(intent5);
                            return true;

                        case R.id.nav_trending:
                            Intent intent6 = new Intent(FilterResults.this, TrendingDisplay.class);
                            startActivity(intent6);
                            return true;
                        default:
                            return true;
                    }
                });

        addItemsToSpinners();
        addListenerToSpinners();

    }

    @OnClick(R.id.filterSearch_btn)
    public void onFindIDClicked() {
        //hide keyboard, filters
        hideSoftKeyboard(this);
        hideFilters();

        if (!searchEditText.getText().toString().equals("")) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/search/person/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            String name = searchEditText.getText().toString();
            name = name.replaceAll("\\s+", "%20");
            String url_name = MainActivity.SEARCH_NAME_BASE_URL + name;

            NameSearchInterface nameSearchInterface = retrofit.create(NameSearchInterface.class);

            //Call<NameResults> call = nameSearchInterface.getData(url);
            Observable<NameResults> call = nameSearchInterface.getData(url_name);

            call.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<NameResults>() {

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(NameResults nameResults) {
                            searchNameID = nameResults.getResults().get(0).getId();
                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {
                            getMovies();
                        }
                    });
        } else {
            getMovies();
        }
    }

    public void getMovies() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/discover/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        //check if set to movie or show
        if (returnType.equals("Movie")) {
            String url = MainActivity.DISCOVER_BASE_URL + createUrl();

            MovieDBInterface movieDBAPI = retrofit.create(MovieDBInterface.class);
            Call<Feed> call = movieDBAPI.getData(url);

            call.enqueue(new Callback<Feed>() {
                @Override
                public void onResponse(Call<Feed> call, Response<Feed> response) {
                    if (response.body().getResults().size() == 0) {
                        Toast.makeText(FilterResults.this, "No Results", Toast.LENGTH_SHORT).show();
                    }
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
                    Toast.makeText(FilterResults.this, "FAILURE", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            String url = RetrofitInstance.TV_DISCOVER_BASE + createUrl();

            MovieDBTVInterface tvAPI = retrofit.create(MovieDBTVInterface.class);
            Call<TVFeed> call = tvAPI.getData(url);

            call.enqueue(new Callback<TVFeed>() {
                @Override
                public void onResponse(Call<TVFeed> call, Response<TVFeed> response) {
                    ArrayList<TVResults> results = response.body().getResults();

                    for (int i = 0; i < results.size(); i++) {
                        //Add the title and image for RecyclerView
                        titles.add(results.get(i).getTitle());
                        posters.add(MainActivity.IMAGE_URL + results.get(i).getPoster_path());
                        movieIDs.add(results.get(i).getId());
                    }
                    initRecyclerView();
                }

                @Override
                public void onFailure(Call<TVFeed> call, Throwable t) {

                }
            });
        }

    }


    @OnClick(R.id.empty_btn)
    public void onEmptyBtnClicked() {
        if (recyclerViewStarted) {
            titles.clear();
            posters.clear();
            movieIDs.clear();
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(FilterResults.this, "Movie list already empty!", Toast.LENGTH_SHORT).show();
        }
    }

    //populate our spinners with the appropriate values
    public void addItemsToSpinners() {
        //type spinner
        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(this,
                R.array.type, android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeSpinner.setAdapter(adapterType);


        //sortby spinner
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.sort_by_values, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortBySpinner.setAdapter(adapter1);

        //rating spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.ratings, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ratingSpinner.setAdapter(adapter2);

        //genre_spinner
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.genres, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        genreSpinner.setAdapter(adapter3);

    }

    //set listeners to each of our spinners
    private void addListenerToSpinners() {

        //type spinner
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemSelectedInSpinner = parent.getItemAtPosition(position).toString();

                setValue(TYPE_SPINNER, itemSelectedInSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //sortby spinner
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                String itemSelectedInSpinner = parent.getItemAtPosition(pos).toString();

                setValue(SORT_BY_SPINNER, itemSelectedInSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //rating spinner
        ratingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                String itemSelectedInSpinner = parent.getItemAtPosition(pos).toString();

                setValue(RATING_SPINNER, itemSelectedInSpinner);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //genre spinner
        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                String itemSelectedInSpinner = parent.getItemAtPosition(pos).toString();

                setValue(GENRE_SPINNER, itemSelectedInSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    //Set our filter values based upon the spinner items chosen
    public void setValue(int spinnerNumber, String value) {
        //sortBy spinner
        if (spinnerNumber == SORT_BY_SPINNER) {
            sortBy_value = sortByTranslate(value);
        }
        //rating spinner
        else if (spinnerNumber == RATING_SPINNER) {
            rating_value = value;
        }
        //genre spinner
        else if (spinnerNumber == GENRE_SPINNER) {
            with_genre_value = getGenreValue(value);
        } else {
            if (value.equals("Movie")) {

                //change filter options to movie options
                ArrayAdapter<CharSequence> movieTypeAdapter = ArrayAdapter.createFromResource(this,
                        R.array.sort_by_values, android.R.layout.simple_spinner_item);
                movieTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                sortBySpinner.setAdapter(movieTypeAdapter);

                //change items to movie ratings
                ArrayAdapter<CharSequence> movieAdapter = ArrayAdapter.createFromResource(this,
                        R.array.ratings, android.R.layout.simple_spinner_item);
                movieAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                ratingSpinner.setAdapter(movieAdapter);

                returnType = "Movie";

            } else {

                //change filter options to show options
                ArrayAdapter<CharSequence> tvTypeAdapter = ArrayAdapter.createFromResource(this,
                        R.array.tv_sort_by_values, android.R.layout.simple_spinner_item);
                tvTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                sortBySpinner.setAdapter(tvTypeAdapter);

                //change items to show ratings
                ArrayAdapter<CharSequence> ratingAdapter = ArrayAdapter.createFromResource(this,
                        R.array.show_ratings, android.R.layout.simple_spinner_item);
                ratingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                ratingSpinner.setAdapter(ratingAdapter);

                returnType = "TV Show";
            }
        }
    }

    //set our filter method
    public String sortByTranslate(String value) {
        if (value.equals("Most Popular")) return "sort_by=popularity.desc";
        if (value.equals("Least Popular")) return "sort_by=popularity.asc";
        if (value.equals("Newest")) return "sort_by=release_date.desc";
        if (value.equals("Oldest")) return "sort_by=release_date.asc";
        if (value.equals("Revenue")) return "sort_by=revenue.desc";
        else return value;
    }

    //set genre value based off option selected
    public int getGenreValue(String value) {
        int genreID = 0;
        switch (value) {
            case "Select Genre":
                break;
            case "Action":
                genreID = 28;
                break;
            case "Adventure":
                genreID = 12;
                break;
            case "Animation":
                genreID = 16;
                break;
            case "Comedy":
                genreID = 35;
                break;
            case "Drama":
                genreID = 18;
                break;
            case "Documentary":
                genreID = 99;
                break;
            case "Horror":
                genreID = 27;
                break;
            case "Science Fiction":
                genreID = 878;
                break;
        }
        return genreID;
    }

    //Create our URL based all the filter inputs
    public String createUrl() {

        String url = "";
        url += "certification_country=US&";
        if (!sortBy_value.equals("Select Filter Method")) url += sortBy_value;
        if (!rating_value.equals("Select Rating")) url += "&certification=" + rating_value;
        if (with_genre_value != 0) url += "&with_genres=" + with_genre_value;
        if (searchNameID != 0) url += "&with_people=" + searchNameID;
        if (!yearEditText.getText().toString().equals("")) {
            //check radio buttons
            if (afterRadio.isChecked()) {
                url += "&release_date.gte=" + yearEditText.getText().toString();
            } else {
                url += "&release_date.lte=" + yearEditText.getText().toString();
            }
        }
        return url;
    }

    private void initRecyclerView() {
        recyclerViewStarted = true;

        //Create an adapter for our recyclerview
        if(returnType.equals("Movie")){
            adapter = new RecyclerViewAdapter(movieIDs, titles, posters, 1, this);

        } else {
            adapter = new RecyclerViewAdapter(movieIDs, titles, posters, 3, this);

        }

        //set adapter to recyclerview
        filterRecyclerView.setAdapter(adapter);

        //add animation
        filterRecyclerView.setLayoutAnimation(animation);

        //set the layout mode to LinearLayout
        filterRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                Intent intent = new Intent(FilterResults.this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_search:
                Intent intent1 = new Intent(FilterResults.this, DisplayResults.class);
                startActivity(intent1);
                return true;

            case R.id.action_discover:

                return true;

            case R.id.action_watchlist:
                Intent intent3 = new Intent(FilterResults.this, WatchListDisplay.class);
                startActivity(intent3);
                return true;

            case R.id.action_ratings:
                Intent intent4 = new Intent(FilterResults.this, MovieRatingsDisplay.class);
                startActivity(intent4);
                return true;

            case R.id.action_playing:
                Intent intent5 = new Intent(FilterResults.this, NowPlayingDisplay.class);
                startActivity(intent5);
                return true;

            case R.id.action_trending:
                Intent intent6 = new Intent(FilterResults.this, TrendingDisplay.class);
                startActivity(intent6);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    //hide button will move the filters out of the way to allow better visiblity of recycler view
    @OnClick(R.id.hide_button)
    public void hideFilters() {
        if (hideButtonFlag) {//set filters layout to invisible
            filterLayout.startAnimation(animationUp);
            CountDownTimer timer = new CountDownTimer(200, 16) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    filterLayout.setVisibility(View.GONE);
                }
            };
            timer.start();

            arrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
            hideButtonFlag = false;

        } else { //set back to visible

            filterLayout.startAnimation(animationDown);

            CountDownTimer timer = new CountDownTimer(200, 16) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    filterLayout.setVisibility(View.VISIBLE);
                }
            };
            timer.start();
            arrow.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
            hideButtonFlag = true;
        }
    }

    //hide keyboard after search button his clicked
    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View focusedView = activity.getCurrentFocus();
        if(focusedView != null){
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}