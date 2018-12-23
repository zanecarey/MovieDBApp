package com.example.zane.moviedbapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CastRecyclerView extends RecyclerView.Adapter<CastRecyclerView.ViewHolder> {

    private ArrayList<String> actors;
    private ArrayList<String> characters;
    private ArrayList<String> profile_pics;
    private ArrayList<Integer> ids;
    private Context context;

    public CastRecyclerView(ArrayList<String> actors, ArrayList<String> characters, ArrayList<String> profile_pics, ArrayList<Integer> ids, Context context) {
        this.actors = actors;
        this.characters = characters;
        this.profile_pics = profile_pics;
        this.ids = ids;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem,
                viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo);

        Glide.with(context)
                .asBitmap()
                .load(profile_pics.get(i))
                .apply(options)
                .into(viewHolder.image);

        viewHolder.imageName.setText(actors.get(i) + "\n");
        viewHolder.imageName.append(characters.get(i));

        viewHolder.parentLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, ActorInfoActivity.class);
            intent.putExtra("actor_id", ids.get(i));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return actors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView imageName;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.circularImageView);
            imageName = itemView.findViewById(R.id.imageName);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}
