package com.example.zane.moviedbapp;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.zane.moviedbapp.model.Feed;
import com.example.zane.moviedbapp.model.NameResults;
import com.example.zane.moviedbapp.model.Results;

import java.util.ArrayList;
import java.util.jar.Attributes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FilterResults  extends AppCompatActivity {

    private static final String TAG = "FilterResults";
    private final int SORT_BY_SPINNER = 1;
    private final int RATING_SPINNER = 2;
    private final int GENRE_SPINNER = 3;

    Spinner sortBy_spinner;
    Spinner rating_spinner;
    Spinner genre_spinner;
    Button searchBtn, findID_btn, empty_btn;
    EditText search_edit_text, year_edit_text;
    String sortBy_value, rating_value;
    int with_genre_value = 0;
    int searchNameID;

    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> posters = new ArrayList<>();
    private ArrayList<Integer> movieIDs = new ArrayList<>();

    RecyclerViewAdapter adapter;

    Boolean recyclerViewStarted = false;

    Toolbar myToolbar;

    RadioButton before_radio, after_radio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_results);

        myToolbar = (Toolbar) findViewById(R.id.filter_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Discover");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sortBy_spinner = (Spinner) findViewById(R.id.sortBy_spinner);
        rating_spinner = (Spinner) findViewById(R.id.rating_spinner);
        genre_spinner = (Spinner) findViewById(R.id.genre_spinner);
        searchBtn = (Button) findViewById(R.id.filterSearch_btn);
        findID_btn = (Button) findViewById(R.id.findID);
        search_edit_text = (EditText) findViewById(R.id.search_edit_text);
        year_edit_text = (EditText) findViewById(R.id.year_edit_text);
        empty_btn = (Button) findViewById(R.id.empty_btn);
        after_radio = (RadioButton) findViewById(R.id.after_radio);
        before_radio = (RadioButton) findViewById(R.id.before_radio);

        addItemsToSpinners();
        addListenerToSpinners();

        //empty recycler view listener
        empty_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerViewStarted){
                    titles.clear();
                    posters.clear();
                    movieIDs.clear();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(FilterResults.this, "Movie list already empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //getID button listener
        findID_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //make sure a name has been entered before searching for the id
                if(search_edit_text.getText().toString().equals("")){
                    Toast.makeText(FilterResults.this, "No name entered.", Toast.LENGTH_SHORT).show();
                } else {

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://api.themoviedb.org/3/search/person/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    String name = search_edit_text.getText().toString();
                    name = name.replaceAll("\\s+", "%20");
                    String url = MainActivity.SEARCH_NAME_BASE_URL + name;

                    NameSearchInterface nameSearchInterface = retrofit.create(NameSearchInterface.class);
                    Call<NameResults> call = nameSearchInterface.getData(url);

                    call.enqueue(new Callback<NameResults>() {
                        @Override
                        public void onResponse(Call<NameResults> call, Response<NameResults> response) {
                            searchNameID = response.body().getResults().get(0).getId();
                        }

                        @Override
                        public void onFailure(Call<NameResults> call, Throwable t) {

                        }
                    });
                }
            }

        });

        //Search button listener
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.themoviedb.org/3/discover/movie/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                String url = MainActivity.DISCOVER_BASE_URL + createUrl();
                Log.d(TAG, "filter results url2 : " + url);

                MovieDBInterface movieDBAPI = retrofit.create(MovieDBInterface.class);
                Call<Feed> call = movieDBAPI.getData(url);

                call.enqueue(new Callback<Feed>() {
                    @Override
                    public void onResponse(Call<Feed> call, Response<Feed> response) {

                        if(response.body().getResults().size() == 0) Toast.makeText(FilterResults.this, "No Results", Toast.LENGTH_SHORT).show();
                        ArrayList<Results> resultsArrayList = response.body().getResults();

                        for(int i = 0; i < resultsArrayList.size(); i++){
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
            }
        });
    }

    //populate our spinners with the appropriate values
    public void addItemsToSpinners(){
        //sortby spinner
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.sort_by_values, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortBy_spinner.setAdapter(adapter1);

        //rating spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.ratings, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        rating_spinner.setAdapter(adapter2);

        //genre_spinner
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.genres, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        genre_spinner.setAdapter(adapter3);
    }

    //set listeners to each of our spinners
    private void addListenerToSpinners() {
        //sortby spinner
        sortBy_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
        rating_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        genre_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    public void setValue(int spinnerNumber, String value){
        //sortBy spinner
        if(spinnerNumber == 1){
            sortBy_value = sortByTranslate(value);
        }
        //rating spinner
        else if(spinnerNumber == 2){
            rating_value = value;
        }
        //genre spinner
        else{
            with_genre_value = getGenreValue(value);
        }
    }

    public String sortByTranslate(String value){
        if(value.equals("Most Popular")) return "sort_by=popularity.desc";
        if(value.equals("Least Popular")) return "sort_by=popularity.asc";
        if(value.equals("Newest")) return "sort_by=release_date.desc";
        if(value.equals("Oldest")) return "sort_by=release_date.asc";
        if(value.equals("Revenue")) return "sort_by=revenue.desc";
        else return value;
    }

    public int getGenreValue(String value){
        int genreID = 18;
        switch (value) {
            case "Select Genre" : genreID = 0;
                break;
            case "Action" : genreID = 28;
                break;
            case "Adventure" : genreID = 12;
                break;
            case "Animation" : genreID = 16;
                break;
            case "Comedy" : genreID = 35;
                break;
            case "Horror" : genreID = 27;
                break;
        }
        return genreID;
    }

    public String createUrl(){

        String url = "";
        url += "certification_country=US&";
        if(!sortBy_value.equals("Select Filter Method")) url += sortBy_value;
        if(!rating_value.equals("Select Rating")) url+= "&certification=" + rating_value;
        if(with_genre_value != 0) url += "&with_genres=" + with_genre_value;
        if(searchNameID!=0) url += "&with_people=" + searchNameID;
        if(!year_edit_text.getText().toString().equals("")){
            //check radio buttons
            if(after_radio.isChecked()){
                url += "&release_date.gte=" + year_edit_text.getText().toString();
            } else {
                url += "&release_date.lte=" + year_edit_text.getText().toString();
            }
        }
        return url;
    }

    private void initRecyclerView(){
        recyclerViewStarted = true;

        //Create our recyclerView
        RecyclerView recyclerView = findViewById(R.id.filterRecyclerView);
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
                Intent intent1 = new Intent(FilterResults.this, DisplayResults.class);
                startActivity(intent1);
                return true;

            case R.id.action_discover:

                return true;

            case R.id.action_watchlist:
                Intent intent3 = new Intent(FilterResults.this, WatchListDisplay.class);
                startActivity(intent3);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
