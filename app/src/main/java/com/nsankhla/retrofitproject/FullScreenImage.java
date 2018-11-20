package com.nsankhla.retrofitproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class FullScreenImage extends AppCompatActivity {
    ImageView image;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        getSupportActionBar().hide();
        url = getIntent().getStringExtra("image_url");

        image = findViewById(R.id.full_Screen);
        Picasso.get().load(url).into(image);
    }
}
