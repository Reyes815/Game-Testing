package com.example.samplegame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AdventureLevel3 extends AppCompatActivity {
    Dialog myDialog;
    final Handler handler = new Handler();
    int delay = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adventure_level3);
        myDialog = new Dialog(this);
    }

    public void Move(View v){
        EditText input = findViewById(R.id.inputEditText);
        input.onEditorAction(EditorInfo.IME_ACTION_DONE);

        ImageView knight = findViewById(R.id.knight);
        knight.setBackgroundResource(R.drawable.climbing_knight);
        AnimationDrawable climb = (AnimationDrawable) knight.getBackground();
        Drawable idle_knight = getResources().getDrawable(R.drawable.idle_knight);
        Drawable falling_knight = getResources().getDrawable(R.drawable.falling_knight);

        final View start_knight = findViewById(R.id.start_image);
        final View node1 = findViewById(R.id.flower_node_1);
        final View node2 = findViewById(R.id.flower_node_2);
        final View node3 = findViewById(R.id.flower_node_3);
        final View goal_node = findViewById(R.id.flower_node_goal);

        String strInput = input.getText().toString().trim();
        // Convert input to lowercase for case-insensitive comparison
        String strInputLower = strInput.toLowerCase();

        //check if empty
        if (strInput.isEmpty()) {
            Toast.makeText(getBaseContext(), "Input is empty", Toast.LENGTH_SHORT).show();
            return; // Exit the method if input is empty
        }

        //move to start node
        knight.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
        climb.start();
        translate(knight, node1);

        // DFA transition table
        int[][] transition = {
                {1, 0}, // From state 0 on input 'a', go to state 1; on input 'b', stay on state 0
                {1, 2}, // From state 1 on input 'a', stay on state 1; on input 'b', go to state 2
                {3, 0}, // From state 2 on input 'a', go to state 3; on input 'b', go to state 0
                {3, 3}  // From state 3 on input 'a', stay on state 3; on input 'b', stay on state 3
        };

        int currentState = 0; // Initial state is 0

        // Process each character in the input string
        for (int i = 0; i < strInputLower.length(); i++) {
            char inputSymbol = strInputLower.charAt(i);

            // Convert the character to an integer (assuming 'a' corresponds to 0 and 'b' corresponds to 1)
            int inp;
            if (inputSymbol == 'a') {
                inp = 0;
            } else if (inputSymbol == 'b') {
                inp = 1;
            } else {
                return;
            }
            //start node
            if (currentState == 0 && inp == 0) {
                handler.postDelayed(() -> translate(knight, node2), delay);
            }
            if (currentState == 0 && inp == 1) {
                break;
            }
            if (currentState == 1 && inp == 0) {
                break;
            }
            if (currentState == 1 && inp == 1) {
                handler.postDelayed(() -> translate(knight, node3), delay += 1000);
            }
            if (currentState == 2 && inp == 0) {
                handler.postDelayed(() -> translate(knight, goal_node), delay += 1000);
            }
            if (currentState == 2 && inp == 1) {
                handler.postDelayed(() -> translate(knight, node1), delay += 1000);
            }

            // Update the current state using the transition table
            currentState = transition[currentState][inp];
        }

        // Check if the final state is an accepting state
        if (currentState == 3) {
            handler.postDelayed(() -> climb.stop(), delay += 100);
            handler.postDelayed(() -> showGamePopupSuccess(), delay += 1000);
        } else {
            handler.postDelayed(() -> climb.stop(), delay += 100);
            handler.postDelayed(() -> showGamePopupFail(), delay += 1000);
        }
        //reset
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                knight.setImageDrawable(falling_knight);
                translate(knight, start_knight);
            }
        }, delay+=1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                climb.stop();
                knight.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                knight.setImageDrawable(idle_knight);
            }
        }, 6000);
    }

    private void translate(View viewToMove, View target) {

        viewToMove.animate()
                .x(target.getX())
                .y(target.getY())
                .setDuration(1000)
                .start();
    }

    private void showGamePopupFail() {
        myDialog.setContentView(R.layout.game_popup_fail);

        // Find the close button after setting the content view
        Button close = myDialog.findViewById(R.id.close_btn);
        Button home = myDialog.findViewById(R.id.home_btn);
        Button retry = myDialog.findViewById(R.id.retry_btn);
        Button prev = myDialog.findViewById(R.id.prev_btn);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent level2_popup = new Intent(getApplicationContext(), AdventureLevel2.class);
                startActivity(level2_popup);
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

    private void showGamePopupSuccess() {
        myDialog.setContentView(R.layout.game_popup);
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
                Intent next_popup = new Intent(getApplicationContext(), AdventureLevel4.class);
                startActivity(next_popup);
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}