package com.example.samplegame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AdventureLevel2Activity extends AppCompatActivity {
    Dialog myDialog;
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
        Button move = findViewById(R.id.movebtn);
        myDialog = new Dialog(this);

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
                if((strInput.charAt(0) == 'A' || strInput.charAt(0) == 'a') && (strInput.charAt(1) == 'B' || strInput.charAt(1) == 'b')) {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            translate(knight, node2);
                        }
                    }, 1000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            translate(knight, goal_node);
                        }
                    }, 2000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            knight.setImageDrawable(idle_knight);
                        }
                    }, 3000);
                    Toast.makeText(getBaseContext(), "Congratulations", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            myDialog.setContentView(R.layout.game_popup);

                            // Find the close button after setting the content view
                            Button close = myDialog.findViewById(R.id.close_btn);
                            Button prev = myDialog.findViewById(R.id.prev_btn);
                            Button home = myDialog.findViewById(R.id.home_btn);
                            Button next = myDialog.findViewById(R.id.retry_btn);

                            prev.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent level1_popup = new Intent(getApplicationContext(), AdventureLevel1.class);
                                    startActivity(level1_popup);
                                }
                            });
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialog.dismiss();
                                }
                            });

                            home.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent home_popup = new Intent(getApplicationContext(), Level_Menu.class);
                                    startActivity(home_popup);
                                }
                            });

                            next.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent next_popup = new Intent(getApplicationContext(), AdventureLevel2Activity.class);
                                    startActivity(next_popup);
                                }
                            });

                            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            myDialog.show();
                        }
                    }, 3000);
                }else if(strInput.charAt(0) == 'A' || strInput.charAt(0) == 'a' && strInput.charAt(1) == '\0'){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            translate(knight, node2);
                        }
                    }, 1000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            knight.setImageDrawable(idle_knight);
                        }
                    }, 2000);
                    failed = 1;
                    Toast.makeText(getBaseContext(), "Try Again", Toast.LENGTH_SHORT).show();
                }else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            knight.setImageDrawable(idle_knight);
                        }
                    }, 1000);
                    failed = 1;
                    Toast.makeText(getBaseContext(), "Try Again", Toast.LENGTH_SHORT).show();
                }
                //go back to start after failed level
                if(failed == 1){

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            myDialog.setContentView(R.layout.game_popup_fail);

                            // Find the close button after setting the content view
                            Button close = myDialog.findViewById(R.id.close_btn);
                            Button home = myDialog.findViewById(R.id.home_btn);
                            Button retry = myDialog.findViewById(R.id.retry_btn);
                            Button prev = myDialog.findViewById(R.id.prev_btn);

                            prev.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent level1_popup = new Intent(getApplicationContext(), AdventureLevel1.class);
                                    startActivity(level1_popup);
                                }
                            });
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialog.dismiss();
                                }
                            });

                            home.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent home_popup = new Intent(getApplicationContext(), Level_Menu.class);
                                    startActivity(home_popup);
                                }
                            });

                            retry.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialog.dismiss();
                                }
                            });

                            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            myDialog.show();
                        }
                    }, 3000);

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