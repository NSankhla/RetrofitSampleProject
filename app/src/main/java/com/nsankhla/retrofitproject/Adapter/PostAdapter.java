package com.nsankhla.retrofitproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nsankhla.retrofitproject.FullScreenImage;
import com.nsankhla.retrofitproject.Model.Worldpopulation;
import com.nsankhla.retrofitproject.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    Context context;
    List<Worldpopulation> postList;

    public PostAdapter(Context context, List<Worldpopulation> postList) {
        this.context = context;
        this.postList = postList;
    }

    public static String getRoughNumber(long value) {
        if (value <= 999) {
            return String.valueOf(value);
        }

        final String[] units = new String[]{"", "K", "M", "B", "P"};
        int digitGroups = (int) (Math.log10(value) / Math.log10(1000));
        return new DecimalFormat("#,##0.#").format(value / Math.pow(1000, digitGroups)) + "" + units[digitGroups];

    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_details, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, final int position) {

        holder.txt_name.setText(String.valueOf(postList.get(position).country));
        holder.txt_rank.setText(String.valueOf(postList.get(position).rank));


        Long val = Long.valueOf(postList.get(position).population.replace(",", "").trim());
        String t = getRoughNumber(val);


        holder.txt_population.setText(t);
        Picasso.get().load(String.valueOf(postList.get(position).flag)).placeholder(R.drawable.placeholder).into(holder.img_flag);

        holder.img_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FullScreenImage.class);
                intent.putExtra("image_url", postList.get(position).flag.toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
