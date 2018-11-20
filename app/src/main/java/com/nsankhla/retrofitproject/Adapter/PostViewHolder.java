package com.nsankhla.retrofitproject.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nsankhla.retrofitproject.R;

public class PostViewHolder extends RecyclerView.ViewHolder {
    TextView txt_name, txt_rank, txt_population;
    ImageView img_flag;

    public PostViewHolder(View itemView) {
        super(itemView);
        txt_name = itemView.findViewById(R.id.txt_countryName);
        txt_rank = itemView.findViewById(R.id.txt_countryRank);
        txt_population = itemView.findViewById(R.id.txt_countryPopulation);
        img_flag = itemView.findViewById(R.id.img_flag);


    }
}
