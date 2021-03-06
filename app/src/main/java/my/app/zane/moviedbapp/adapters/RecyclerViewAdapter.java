package my.app.zane.moviedbapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import my.app.zane.moviedbapp.MovieDetails;
import my.app.zane.moviedbapp.R;
import my.app.zane.moviedbapp.TVDetailsActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.constraint.Constraints.TAG;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> titles;
    private ArrayList<String> posters;
    private ArrayList<Integer> movieIDs;
    private ArrayList<Integer> ratings = new ArrayList<>();
    private int type;
    private Context context;

    private Animation animationUp, animationDown;


    private DataBaseAdapter dbHelper;

    //normal constructor
    public RecyclerViewAdapter(ArrayList<Integer> movieIDs, ArrayList<String> titles, ArrayList<String> posters, int type, Context context) {
        this.movieIDs = movieIDs;
        this.titles = titles;
        this.posters = posters;
        this.context = context;
        this.type = type;
        animationUp = AnimationUtils.loadAnimation(this.context, R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(this.context, R.anim.slide_down);
    }

    //rating constructor
    public RecyclerViewAdapter(ArrayList<Integer> movieIDs, ArrayList<String> titles, ArrayList<String> posters, int type, Context context, ArrayList<Integer> ratings) {
        this.movieIDs = movieIDs;
        this.titles = titles;
        this.posters = posters;
        this.context = context;
        this.type = type;
        this.ratings = ratings;
        animationUp = AnimationUtils.loadAnimation(this.context, R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(this.context, R.anim.slide_down);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (type == 1 || type == 3) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem,
                    viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recommendation_listitem,
                    viewGroup, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called");
        final int value = i;
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo);

        if (type == 1 || type == 3) {
            Glide.with(context)
                    .asBitmap()
                    .load(posters.get(i))
                    .apply(options)
                    .into(viewHolder.image);

            viewHolder.imageName.setText(titles.get(i));

            //determine if the adapter is called from ratings activity or not, based off ratings arraylist
            if (!ratings.isEmpty()) {

                RatingBar ratingBar = new RatingBar(context);
                ratingBar.setIsIndicator(true);
                ratingBar.setScaleX(0.5f);
                ratingBar.setScaleY(0.5f);
                ratingBar.setPivotX(0);
                switch (ratings.get(i)) {
                    case 1:
                        ratingBar.setRating(1);
                        break;
                    case 2:
                        ratingBar.setRating(2);
                        break;
                    case 3:
                        ratingBar.setRating(3);
                        break;
                    case 4:
                        ratingBar.setRating(4);
                        break;
                    case 5:
                        ratingBar.setRating(5);
                        break;
                    default:
                        ratingBar.setRating(0);
                        break;
                }
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.BELOW, R.id.imageName);
                params.addRule(RelativeLayout.END_OF, R.id.circularImageView);
                //params.addRule(RelativeLayout.ABOVE, R.id.item_layout);
                params.setMargins(100,0,0,0);
                ratingBar.setLayoutParams(params);
                //ratingBar.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                // ratingBar.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.BELOW, R.id.imageName));
                viewHolder.listRelativeLayout.addView(ratingBar);
            }

            //on click for a list item, expand to allow user to do various things like add, view, rate
            viewHolder.parentLayout.setOnClickListener(view -> {

                if (viewHolder.isExpanded) {
                    viewHolder.itemLayout.startAnimation(animationUp);
                    CountDownTimer timer = new CountDownTimer(100, 16) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            viewHolder.itemLayout.setVisibility(View.GONE);
                            viewHolder.listRelativeLayout.setBackgroundColor(Color.WHITE);
                        }
                    };
                    timer.start();

                    viewHolder.isExpanded = false;


                } else {

                    viewHolder.itemLayout.startAnimation(animationDown);
                    CountDownTimer timer = new CountDownTimer(100, 16) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            viewHolder.itemLayout.setVisibility(View.VISIBLE);
                            viewHolder.listRelativeLayout.setBackgroundColor(Color.parseColor("#beefd0"));
                        }
                    };
                    timer.start();

                    viewHolder.isExpanded = true;

                    //view button
                    viewHolder.viewTextView.setOnClickListener(v -> {
                        if(type == 1){
                            Intent intent = new Intent(context, MovieDetails.class);
                            intent.putExtra("movie_id", movieIDs.get(i));
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, TVDetailsActivity.class);
                            intent.putExtra("tv_id", movieIDs.get(i));
                            context.startActivity(intent);
                        }

                    });


                    //add button
                    viewHolder.addTextView.setOnClickListener(v -> {

                        //add movie to watchlist
                        dbHelper = new DataBaseAdapter(context);
                        if(type==1){
                            if (dbHelper.alreadyInDatabase(movieIDs.get(i), "movies")) {
                                Toast.makeText(context, "Already in watchlist", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
                                dbHelper.addMovie(movieIDs.get(i), titles.get(i), posters.get(i));
                            }
                        } else {
                            if (dbHelper.alreadyInDatabase(movieIDs.get(i), "shows")) {
                                Toast.makeText(context, "Already in watchlist", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
                                dbHelper.addShow(movieIDs.get(i), titles.get(i), posters.get(i));
                            }
                        }
                    });


                    //rate button
                    viewHolder.rateTextView.setOnClickListener(v -> {
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(context, R.style.AlertDialog);
                        builder.setTitle("Rate movie");

                        //ratingbar for dialog
                        RatingBar ratingBar = new RatingBar(context);

                        builder.setView(ratingBar);
                        builder.setPositiveButton("Rate", ((dialog, which) -> {
                            dbHelper = new DataBaseAdapter(context);
                            int rating = (int) ratingBar.getRating();
                            if(type ==1){
                                if (dbHelper.alreadyInDatabase(movieIDs.get(i), "movies_rated")) {
                                    Toast.makeText(context, "Already Rated", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Rated", Toast.LENGTH_SHORT).show();
                                    dbHelper.addRating(movieIDs.get(i), titles.get(i), posters.get(i), rating);
                                }
                            } else {
                                if (dbHelper.alreadyInDatabase(movieIDs.get(i), "shows_rated")) {
                                    Toast.makeText(context, "Already Rated", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Rated", Toast.LENGTH_SHORT).show();
                                    dbHelper.addShowRating(movieIDs.get(i), titles.get(i), posters.get(i), rating);
                                }
                            }
                        }));

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    });
                }
            });
        } else {
            Glide.with(context)
                    .asBitmap()
                    .load(posters.get(i))
                    .apply(options)
                    .into(viewHolder.image2);

            viewHolder.parentLayout.setOnClickListener(v -> {
                if(type == 2){
                    Intent intent = new Intent(context, MovieDetails.class);
                    intent.putExtra("movie_id", movieIDs.get(i));
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, TVDetailsActivity.class);
                    intent.putExtra("tv_id", movieIDs.get(i));
                    context.startActivity(intent);
                }

            });
        }

        //long click listener for list items, if long click, display dialog to remove from list
        viewHolder.parentLayout.setOnLongClickListener(view -> {

            //launch alert dialog, ask if want to remove from watchlist
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(context, R.style.AlertDialog);
            builder.setTitle("Remove movie");
            builder.setMessage("Do you want to remove this movie from your watchlist?");

            //if yes, movie removed from database, deleted from recycler view, then updated
            builder.setPositiveButton("YES", (dialogInterface, i12) -> {
                dbHelper = new DataBaseAdapter(context);
                if (ratings.isEmpty()) {
                    dbHelper.deleteMovie(movieIDs.get(value), "movies");
                } else {
                    dbHelper.deleteMovie(movieIDs.get(value), "movies_rated");
                    ratings.remove(value);
                }
                movieIDs.remove(value);
                titles.remove(value);
                posters.remove(value);
                notifyItemRemoved(value);
                notifyItemRangeChanged(value, titles.size());
            });

            builder.setNegativeButton("NO", (dialogInterface, i1) -> dialogInterface.cancel());

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.colorMovieDBgreen));
            dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.colorMovieDBgreen));
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        ImageView image2;
        TextView imageName;
        RelativeLayout parentLayout;
        boolean isExpanded = false;
        LinearLayout itemLayout;
        TextView viewTextView, addTextView, rateTextView;
        RelativeLayout listRelativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            if (type == 1 || type == 3) {
                image = itemView.findViewById(R.id.circularImageView);
                parentLayout = itemView.findViewById(R.id.parentLayout);
                imageName = itemView.findViewById(R.id.imageName);
                itemLayout = itemView.findViewById(R.id.item_layout);
                viewTextView = itemView.findViewById(R.id.view_textview);
                addTextView = itemView.findViewById(R.id.add_textview);
                rateTextView = itemView.findViewById(R.id.rate_textview);
                listRelativeLayout = itemView.findViewById(R.id.list_relativeLayout);

            } else {
                image2 = itemView.findViewById(R.id.poster_imageView);
                parentLayout = itemView.findViewById(R.id.rec_layout);
                //imageName = itemView.findViewById(R.id.rec_textView);
            }
        }
    }
}
