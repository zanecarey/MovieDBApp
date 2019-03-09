package com.example.zane.moviedbapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zane.moviedbapp.MovieDetails;
import com.example.zane.moviedbapp.R;

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

    DataBaseAdapter dbHelper;

    public RecyclerViewAdapter(ArrayList<Integer> movieIDs, ArrayList<String> titles, ArrayList<String> posters, int type, Context context) {
        this.movieIDs = movieIDs;
        this.titles = titles;
        this.posters = posters;
        this.context = context;
        this.type = type;
    }

    public RecyclerViewAdapter(ArrayList<Integer> movieIDs, ArrayList<String> titles, ArrayList<String> posters, int type, Context context, ArrayList<Integer> ratings) {
        this.movieIDs = movieIDs;
        this.titles = titles;
        this.posters = posters;
        this.context = context;
        this.type = type;
        this.ratings = ratings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if(type == 1){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem,
                    viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recommendation_listitem,
                    viewGroup, false);
        }
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called");
        final int value = i;
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo);

        Glide.with(context)
                .asBitmap()
                .load(posters.get(i))
                .apply(options)
                .into(viewHolder.image);

        //determine if the adapter is called from ratings activity or not, based off ratings arraylist
        if(ratings.isEmpty()){
            viewHolder.imageName.setText(titles.get(i));
        } else {
            viewHolder.imageName.setText(titles.get(i) + " " + ratings.get(i) + "/5");
        }

        viewHolder.parentLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, MovieDetails.class);
            intent.putExtra("movie_id", movieIDs.get(i));
            context.startActivity(intent);
        });

        viewHolder.parentLayout.setOnLongClickListener(view -> {

            //launch alert dialog, ask if want to remove from watchlist
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(context, R.style.AlertDialog);
            builder.setTitle("Remove movie");
            builder.setMessage("Do you want to remove this movie from your watchlist?");

            //if yes, movie removed from database, deleted from recycler view, then updated
            builder.setPositiveButton("YES", (dialogInterface, i12) -> {
                dbHelper = new DataBaseAdapter(context);
                if(ratings.isEmpty()){
                    dbHelper.deleteMovie(movieIDs.get(value), "movies");
                } else {
                    dbHelper.deleteMovie(movieIDs.get(value), "movies_rated");
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView imageName;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            if(type == 1){
                image = itemView.findViewById(R.id.circularImageView);
                parentLayout = itemView.findViewById(R.id.parentLayout);
                imageName = itemView.findViewById(R.id.imageName);
            } else {
                image = itemView.findViewById(R.id.poster_imageView);
                parentLayout = itemView.findViewById(R.id.rec_layout);
                imageName = itemView.findViewById(R.id.rec_textView);
            }


        }
    }
}
