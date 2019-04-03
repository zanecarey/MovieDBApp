package com.example.zane.moviedbapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TVDetailsActivity extends AppCompatActivity {

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
    @BindView(R.id.navView)
    NavigationView navView;


    YouTubePlayerSupportFragment youtubeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvdetails);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_white_24dp);

        youtubeFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.tv_youtube_fragment);
    }
}
