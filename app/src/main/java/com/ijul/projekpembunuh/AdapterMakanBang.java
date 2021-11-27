package com.ijul.projekpembunuh;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterMakanBang extends RecyclerView.Adapter<AdapterMakanBang.MovieViewHolder> {
    ArrayList<makanbang> arrayListMakanBang;

    public AdapterMakanBang(ArrayList<makanbang> arrayListMakanBang){
this.arrayListMakanBang =arrayListMakanBang;

    }

    @NonNull
    @Override
    public AdapterMakanBang.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMakanBang.MovieViewHolder holder, int position) {
        final makanbang makan = arrayListMakanBang.get(position);


        holder.TextViewRating.setText(makan.getRating());
        holder.TextViewRelease.setText(makan.getRating());
        holder.ImageViewTitle.setImageResource(makan.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(holder.itemView.getContext(), detailmakan.class);
                inten.putExtra("MOVIES", makan);
                holder.itemView.getContext().startActivity(inten);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListMakanBang.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView ImageViewTitle;
        TextView TextViewRating;
        TextView TextViewRelease;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ImageViewTitle = itemView.findViewById(R.id.tvTitle);
            TextViewRating = itemView.findViewById(R.id.tvRating);
            TextViewRelease = itemView.findViewById(R.id.tvReleaseDate);
        }
    }
}
