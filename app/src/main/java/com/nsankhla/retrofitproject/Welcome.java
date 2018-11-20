package com.nsankhla.retrofitproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Welcome extends AppCompatActivity {
    Button btn_part1, btn_part2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btn_part1 = findViewById(R.id.btn_part1);
        btn_part2 = findViewById(R.id.btn_part2);

        btn_part1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent part1 = new Intent(Welcome.this, MainActivity.class);
                startActivity(part1);
            }
        });
        btn_part2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent part2 = new Intent(Welcome.this, Contact.class);
                startActivity(part2);
            }
        });

    }
}
