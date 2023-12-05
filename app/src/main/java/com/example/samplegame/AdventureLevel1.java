package com.example.samplegame;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AdventureLevel1 extends AppCompatActivity {
    Dialog myDialog;
    final Handler handler = new Handler();
    int delay = 1000;
    public enum HeartStatus {
        ALIVE,
        DEAD
    }

    Map<String, HeartStatus> booleanMap = new HashMap<>();
    int newHour;
    int newMinute;
    int current_hour;
    int current_minute;
    private static final int heart = R.drawable.heart;
    private static final int dead_heart = R.drawable.dead_heart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adventure_level1);
        myDialog = new Dialog(this);

        booleanMap.put("Heart1Alive", HeartStatus.ALIVE);
        booleanMap.put("Heart2Alive", HeartStatus.ALIVE);
        booleanMap.put("Heart3Alive", HeartStatus.ALIVE);

        // Retrieve image resource ID from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        int savedImageResourceId = preferences.getInt("imageResourceId", R.drawable.heart);
        // Set the ImageView with the retrieved resource ID
        ImageView system_heart1 = findViewById(R.id.heart1);
        ImageView system_heart2 = findViewById(R.id.heart2);
        ImageView system_heart3 = findViewById(R.id.heart3);
        system_heart1.setImageResource(savedImageResourceId);
        system_heart2.setImageResource(savedImageResourceId);
        system_heart3.setImageResource(savedImageResourceId);
        Button go = findViewById(R.id.movebtn);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //your method
                current_hour = getHour();
                current_minute = getMinute();
                go.setEnabled(heart_status("Heart3Alive"));
            }
        }, 0, 1000);//put here time 1000 milliseconds=1 second

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //your method
                if(!heart_status("Heart3Alive")){
                    Log.d("Heart 3 Check", "HEART 3 JUST DIED");
                    if(current_hour > newHour){
                        Regain_life();
                    }
                    if(current_hour == newHour && current_minute >= newMinute){
                        Regain_life();
                    }
                }else if(!heart_status("Heart2Alive")){
                    Log.d("Heart 2 Check", "HEART 2 JUST DIED");
                    if(current_hour > newHour){
                        Regain_life();
                    }
                    if(current_hour == newHour && current_minute >= newMinute){
                        Regain_life();
                    }
                }else if(!heart_status("Heart1Alive")){
                    Log.d("Heart 1 Check", "HEART 1 JUST DIED");
                    if(current_hour > newHour){
                        Regain_life();
                    }
                    if(current_hour == newHour && current_minute >= newMinute){
                        Regain_life();
                    }
                }
            }
        }, 0, 5000);//put here time 1000 milliseconds=1 second
    }

    public void Regain(View v){
        ImageView system_heart1 = findViewById(R.id.heart1);
        ImageView system_heart2 = findViewById(R.id.heart2);
        ImageView system_heart3 = findViewById(R.id.heart3);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("imageResourceId", R.drawable.heart);
        editor.apply();
        system_heart1.setImageResource(heart);
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
                {1, 2}, // From state 0 on input 'a', go to state 1; on input 'b', go to state 2
                {1, 1}, // From state 1 on input 'a', stay on state 1; on input 'b', stay on state 1
                {2, 2}  // From state 2 on input 'a', stay on state 2; on input 'b', stay on state 2
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

            // Transition logic
            if (currentState == 0 && inp == 0) {
                handler.postDelayed(() -> translate(knight, goal_node), delay += 1000);
            }

            if (currentState == 0 && inp == 1) {
                handler.postDelayed(() -> translate(knight, node2), delay += 1000);
            }
            // Update the current state using the transition table
            currentState = transition[currentState][inp];
        }

        // Check if the final state is an accepting state
        if (currentState == 1) {
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
        delay = 1000;
    }

    private void translate(View viewToMove, View target) {

        viewToMove.animate()
                .x(target.getX())
                .y(target.getY())
                .setDuration(1000)
                .start();
    }

    public void failed_Attempt(){
        ImageView system_heart1 = findViewById(R.id.heart1);
        ImageView system_heart2 = findViewById(R.id.heart2);
        ImageView system_heart3 = findViewById(R.id.heart3);

        newHour = getHour();
        if(getMinute()+1 >= 60){
            newMinute = (getMinute()+1) - 60;
            newHour++;
        } else {
            newMinute = getMinute() + 1;
        }

        if(heart_status("Heart1Alive")){
            saveImageResourceId(dead_heart);
            system_heart1.setImageResource(dead_heart);
            booleanMap.put("Heart1Alive", HeartStatus.DEAD);
        } else if (heart_status("Heart2Alive")) {
            saveImageResourceId(dead_heart);
            system_heart2.setImageResource(dead_heart);
            booleanMap.put("Heart2Alive", HeartStatus.DEAD);
        } else if (heart_status("Heart3Alive")) {
            saveImageResourceId(dead_heart);
            system_heart3.setImageResource(dead_heart);
            booleanMap.put("Heart3Alive", HeartStatus.DEAD);
        }


        Log.d("FAILED", "failed_Attempt");
    }

    public void Regain_life(){
        ImageView system_heart1 = findViewById(R.id.heart1);
        ImageView system_heart2 = findViewById(R.id.heart2);
        ImageView system_heart3 = findViewById(R.id.heart3);

        if(!heart_status("Heart3Alive")){
            saveImageResourceId(heart);
            system_heart3.setImageResource(heart);
            booleanMap.put("Heart3Alive", HeartStatus.ALIVE);
        } else if (!heart_status("Heart2Alive")) {
            saveImageResourceId(heart);
            system_heart2.setImageResource(heart);
            booleanMap.put("Heart2Alive", HeartStatus.ALIVE);
        } else if (!heart_status("Heart1Alive")) {
            saveImageResourceId(heart);
            system_heart1.setImageResource(heart);
            booleanMap.put("Heart1Alive", HeartStatus.ALIVE);
        }

    }

    private void showGamePopupFail() {
        myDialog.setContentView(R.layout.game_popup_fail);
        EditText input = findViewById(R.id.inputEditText);
        // Find the close button after setting the content view
        Button close = myDialog.findViewById(R.id.close_btn);
        Button home = myDialog.findViewById(R.id.home_btn);
        Button retry = myDialog.findViewById(R.id.retry_btn);
        Button prev = myDialog.findViewById(R.id.prev_btn);

        prev.setVisibility(View.GONE);
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
                input.setText("");
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        failed_Attempt();
    }

    private void showGamePopupSuccess() {
        myDialog.setContentView(R.layout.game_popup);
        Button close = myDialog.findViewById(R.id.close_btn);
        Button prev = myDialog.findViewById(R.id.prev_btn);
        Button home = myDialog.findViewById(R.id.home_btn);
        Button next = myDialog.findViewById(R.id.retry_btn);

        prev.setVisibility(View.GONE);
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
                Intent next_popup = new Intent(getApplicationContext(), AdventureLevel2.class);
                startActivity(next_popup);
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public boolean heart_status(String key) {
        return HeartStatus.ALIVE.equals(booleanMap.get(key));
    }

    public int getHour(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String Time = sdf.format(new Date());
        return Integer.parseInt(Time);
    }

    public int getMinute(){
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        String Time = sdf.format(new Date());
        return Integer.parseInt(Time);
    }

    private void saveImageResourceId(int resourceId) {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("imageResourceId", resourceId);
        editor.apply();
    }
}