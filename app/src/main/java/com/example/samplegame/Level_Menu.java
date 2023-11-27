package com.example.samplegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Level_Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_menu);

        final View training_btn = findViewById(R.id.basicsbtn);
        final View adventure_btn = findViewById(R.id.adventurebtn);

        training_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent training = new Intent(getApplicationContext(), training.class);
                startActivity(training);
            }
        });

        adventure_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adventure = new Intent(getApplicationContext(), AdventureLevel1.class);
                startActivity(adventure);
            }
        });

    }
}
