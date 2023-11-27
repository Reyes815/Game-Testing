package com.example.samplegame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AdventureLevel2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adventure_level2);

        final Handler handler = new Handler();

        EditText input = findViewById(R.id.inputEditText);
        ImageView knight = findViewById(R.id.knight);
        final View start_knight = findViewById(R.id.start_image);
        final View node1 = findViewById(R.id.flower_node_1);
        final View node2 = findViewById(R.id.flower_node_2);
        final View goal_node = findViewById(R.id.flower_node_goal);
        Button move = findViewById(R.id.B_btn);

        Drawable climbing_knight = getResources().getDrawable(R.drawable.climb_knight);
        Drawable idle_knight = getResources().getDrawable(R.drawable.idle_knight);
        Drawable falling_knight = getResources().getDrawable(R.drawable.falling_knight);

        move.setOnClickListener(new View.OnClickListener() {

            int failed = 0;
            @Override
            public void onClick(View v) {
                String strInput = input.getText().toString().trim();

                //check if empty
                if(input.getText().toString().isEmpty()){
                    Toast.makeText(getBaseContext(), "Try Again", Toast.LENGTH_SHORT).show();
                }

                //move to start node
                knight.setImageDrawable(climbing_knight);
                translate(knight, node1);

                //check input
                if(strInput.charAt(0) == 'A' || strInput.charAt(0) == 'a') {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            translate(knight, goal_node);
                        }
                    }, 1000);
                    Toast.makeText(getBaseContext(), "Congratulations", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            knight.setImageDrawable(idle_knight);
                        }
                    }, 2000);
                }else {
                    //wrong input
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            translate(knight, node2);
                        }
                    }, 1000);
                    Toast.makeText(getBaseContext(), "Try Again", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            knight.setImageDrawable(idle_knight);
                        }
                    }, 2000);
                    failed = 1;
                }

//                if(done_moving == 1){
//                    knight.setImageDrawable(idle_knight);
//                }

                //go back to start after failed level
                if(failed == 1){

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            knight.setImageDrawable(falling_knight);
                            translate(knight, start_knight);
                        }
                    }, 5000);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            knight.setImageDrawable(idle_knight);
                        }
                    }, 6000);
                }


            }
        });

    }

    private void translate(View viewToMove, View target) {

        viewToMove.animate()
                .x(target.getX())
                .y(target.getY())
                .setDuration(1000)
                .start();
    }
}