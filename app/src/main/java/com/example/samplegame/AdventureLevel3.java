package com.example.samplegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AdventureLevel3 extends AppCompatActivity {
    Dialog myDialog;
    final Handler handler = new Handler();
    int delay = 1000;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    int newHour = 0;
    int newMinute = 0;
    int current_hour = 0;
    int current_minute = 0;
    private static final int heart = R.drawable.heart;
    private static final int dead_heart = R.drawable.dead_heart;
    // Get the singleton instance
    Map<String, Object> Dead_Update = new HashMap<>();
    Map<String, Object> Alive_Update = new HashMap<>();
    Map<String, Object> Hour = new HashMap<>();
    Map<String, Object> Minute = new HashMap<>();
    DocumentReference heart_1_ref = firestore.collection("Life_System").document("Heart_1");
    DocumentReference heart_2_ref = firestore.collection("Life_System").document("Heart_2");
    DocumentReference heart_3_ref = firestore.collection("Life_System").document("Heart_3");
    DocumentReference Timer = firestore.collection("Life_System").document("Timer");
    String heart_1_status;
    String heart_2_status;
    String heart_3_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adventure_level3);
        myDialog = new Dialog(this);

        Dead_Update.put("Status", "DEAD");
        Alive_Update.put("Status", "ALIVE");

        // Set the ImageView with the retrieved resource ID
        ImageView system_heart1 = findViewById(R.id.heart1);
        ImageView system_heart2 = findViewById(R.id.heart2);
        ImageView system_heart3 = findViewById(R.id.heart3);
        Button go = findViewById(R.id.movebtn);

        heart_1_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Assuming 'status' is the field in your document
                        heart_1_status = document.getString("Status");
                        if ("ALIVE".equals(heart_1_status)) {
                            system_heart1.setImageResource(heart);
                        } else {
                            system_heart1.setImageResource(dead_heart);
                        }
                    } else {
                        Log.d("Document does not exist", "No such document");
                    }
                } else {
                    Log.d("Failed to get doc", "get failed with ", task.getException());
                }
            }
        });

        heart_2_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Assuming 'status' is the field in your document
                        heart_2_status = document.getString("Status");
                        if ("ALIVE".equals(heart_2_status)) {
                            system_heart2.setImageResource(heart);
                        } else {
                            system_heart2.setImageResource(dead_heart);
                        }
                    } else {
                        Log.d("Document does not exist", "No such document");
                    }
                } else {
                    Log.d("Failed to get doc", "get failed with ", task.getException());
                }
            }
        });

        heart_3_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Assuming 'status' is the field in your document
                        heart_3_status = document.getString("Status");
                        if ("ALIVE".equals(heart_3_status)) {
                            system_heart3.setImageResource(heart);
                        } else {
                            system_heart3.setImageResource(dead_heart);
                        }
                    } else {
                        Log.d("Document does not exist", "No such document");
                    }
                } else {
                    Log.d("Failed to get doc", "get failed with ", task.getException());
                }
            }
        });

        // Create a handler for periodic checks
        final Handler statusCheckHandler = new Handler();
        // Create a Timer for periodic time checks
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                current_hour = getHour();
                current_minute = getMinute();
                checkAndRegainLife();
            }
        }, 0, 1000);  // 1000 milliseconds = 1 second
        // Define a runnable to check the status periodically
        final Runnable statusCheckRunnable = new Runnable() {
            @Override
            public void run() {

                // Check and update the status for heart_1
                heart_1_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String newStatus = document.getString("Status");
                                if (!newStatus.equals(heart_1_status)) {
                                    heart_1_status = newStatus;
                                    updateHeartStatus(heart_1_status, system_heart1);
                                }
                            } else {
                                Log.d("Document does not exist", "No such document");
                            }
                        } else {
                            Log.d("Failed to get doc", "get failed with ", task.getException());
                        }
                    }
                });

                // Check and update the status for heart_2
                heart_2_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String newStatus = document.getString("Status");
                                if (!newStatus.equals(heart_2_status)) {
                                    heart_2_status = newStatus;
                                    updateHeartStatus(heart_2_status, system_heart2);
                                }
                            } else {
                                Log.d("Document does not exist", "No such document");
                            }
                        } else {
                            Log.d("Failed to get doc", "get failed with ", task.getException());
                        }
                    }
                });

                // Check and update the status for heart_3
                heart_3_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String newStatus = document.getString("Status");
                                if (!newStatus.equals(heart_3_status)) {
                                    heart_3_status = newStatus;
                                    updateHeartStatus(heart_3_status, system_heart3);
                                }
                            } else {
                                Log.d("Document does not exist", "No such document");
                            }
                        } else {
                            Log.d("Failed to get doc", "get failed with ", task.getException());
                        }
                    }
                });

                // Schedule the next status check after a delay (e.g., every 5 seconds)
                statusCheckHandler.postDelayed(this, 5000);  // 5000 milliseconds = 5 seconds
            }
        };
        // Start the initial status check
        statusCheckHandler.post(statusCheckRunnable);
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

        if(heart_3_status.equals("DEAD")){
            myDialog.setContentView(R.layout.no_lives_popup);
            Button home = myDialog.findViewById(R.id.home_btn);
            Button ads = myDialog.findViewById(R.id.next_btn);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent home_popup = new Intent(getApplicationContext(), Level_Menu.class);
                    startActivity(home_popup);
                }
            });
            ads.setEnabled(false);
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
            return;
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
        handler.postDelayed(() -> climb.stop(), delay += 100);
        if (currentState == 3) {
            handler.postDelayed(() -> showGamePopupSuccess(), delay += 1000);
        } else {
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
        }, delay+=1000);
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
        newHour = getHour();
        if(getMinute()+5 >= 60){
            newMinute = (getMinute()+1) - 60;
            newHour++;
        } else {
            newMinute = getMinute() + 5;
        }
        Hour.put("Hour", newHour);
        Minute.put("Minute", newMinute);
        Timer.update(Hour);
        Timer.update(Minute);

        if(heart_1_status.equals("ALIVE")){
            heart_1_ref.update(Dead_Update);
        } else if (heart_2_status.equals("ALIVE")) {
            heart_2_ref.update(Dead_Update);
        } else if (heart_3_status.equals("ALIVE")) {
            heart_3_ref.update(Dead_Update);
        }
    }

    public void Regain_life(){
        if(heart_3_status.equals("DEAD")){
            heart_3_ref.update(Alive_Update);
        } else if (heart_2_status.equals("DEAD")) {
            heart_2_ref.update(Alive_Update);
        } else if (heart_1_status.equals("DEAD")) {
            heart_1_ref.update(Alive_Update);
        }
    }

    private void showGamePopupFail() {
        myDialog.setContentView(R.layout.game_popup_fail);
        EditText input = findViewById(R.id.inputEditText);
        // Find the close button after setting the content view
        Button close = myDialog.findViewById(R.id.close_btn);
        Button home = myDialog.findViewById(R.id.home_btn);
        Button retry = myDialog.findViewById(R.id.next_btn);
        Button prev = myDialog.findViewById(R.id.prev_btn);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prev_popup = new Intent(getApplicationContext(), AdventureLevel2.class);
                startActivity(prev_popup);
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
        Button next = myDialog.findViewById(R.id.next_btn);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prev_popup = new Intent(getApplicationContext(), AdventureLevel2.class);
                startActivity(prev_popup);
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

    private void checkAndRegainLife() {
        Timer.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Assuming 'status' is the field in your document
                        Double hour = document.getDouble("Hour");
                        Double minute = document.getDouble("Minute");

                        if (heart_1_status.equals("DEAD") && hour != null && minute != null) {
                            if (current_hour > hour || (current_hour == hour && current_minute > minute)) {
                                Regain_life();
                            }
                        }
                        // Add similar checks for heart_2 and heart_3 if needed
                    } else {
                        Log.d("Document does not exist", "No such document");
                    }
                } else {
                    Log.d("Failed to get doc", "get failed with ", task.getException());
                }
            }
        });
    }

    // Add this method to update the UI based on the new heart status
    private void updateHeartStatus(String newStatus, ImageView imageView) {
        if ("ALIVE".equals(newStatus)) {
            imageView.setImageResource(heart);
        } else {
            imageView.setImageResource(dead_heart);
        }
    }
}