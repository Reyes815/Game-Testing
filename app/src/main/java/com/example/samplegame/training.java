package com.example.samplegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class training extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private boolean enableClick = false;
    private String answer = "";
    TextView wizard_dialogue;
    FloatingActionButton next_button;
    private String lastDocumentKey = null;
    private String a = "",a2 = "",a3 = "",a4 = "",a5 = "";
    private String b;
    private Handler handler;
    private Runnable runnable;
    private int charIndex = 0;
    private String fullText = "";
    private AnimationDrawable attackedAnimation2;
    private ImageView wizard1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alphabet_training);
        Log.d("training", "Failed");

        wizard_dialogue = findViewById(R.id.txtWizarddialogue);
        wizard1 = findViewById(R.id.wizard1);
        next_button = findViewById(R.id.next_button);
        // Initialize Firebase
        this.firestore =FirebaseFirestore.getInstance();

        handler = new Handler();

        final View goal_node2 = findViewById(R.id.flower_node_goal2); //
        final View goal_node = findViewById(R.id.flower_node_goal);
        final View start_knight = findViewById(R.id.knight1);
        final TextView path2_A_txtView = findViewById(R.id.path2_A_txtView);//
        final TextView path_A_txtView = findViewById(R.id.path_A_txtView);

        final View flower_node_2 = findViewById(R.id.flower_node_2);
        final TextView path_B_txtView = findViewById(R.id.path_B_txtView);
        final View flower_node_ = findViewById(R.id.flower_node_);//
        final TextView path2_B_txtView = findViewById(R.id.path2_B_txtView);//

        wizard1.setBackgroundResource(R.drawable.idle_wizard2);
        AnimationDrawable idleW = (AnimationDrawable) wizard1.getBackground();
        idleW.start();

        flower_node_.setBackgroundResource(R.drawable.dummy_attacked);
        goal_node2.setBackgroundResource(R.drawable.skeleton_attack);

        flower_node_2.setBackgroundResource(R.drawable.dummy_attacked);
        goal_node.setBackgroundResource(R.drawable.skeleton_attack);

        AnimationDrawable attackAnimation= (AnimationDrawable) goal_node.getBackground();
        AnimationDrawable attackedAnimation = (AnimationDrawable) flower_node_2.getBackground();

        AnimationDrawable attackAnimation2= (AnimationDrawable) goal_node2.getBackground();
        attackedAnimation2 = (AnimationDrawable) flower_node_.getBackground();

        firestore.collection("Dialogue")
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                // Document exists, you can access its data
                                String a = doc.getString("answer_key");
                                String b = doc.getString("dialogue");

                                Log.d("training", "Answer Key: " + a);
                                Log.d("training", "Dialogue: " + b);

                                String dialogue = doc.getString("dialogue");
                                displayTextWithAnimation(dialogue);
                                lastDocumentKey = doc.getId();
                            }
                        } else {
                            Log.d("training", "Failed: " + task.getException());
                        }
                    }
                });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the lastDocumentKey is available
                // Use the lastDocumentKey to query the next document
                Log.d("training", "Answer: " + answer);
                if ((a == null || a.isEmpty()) ||
                        (answer != null && (
                                (a != null && a.equals(answer)) ||
                                        (a2 != null && a2.equals(answer)) ||
                                        (a3 != null && a3.equals(answer)) ||
                                        (a4 != null && a4.equals(answer)) ||
                                        (a5 != null && a5.equals(answer))
                        ))) {
                    if(answer.isEmpty() && a != ""){
                        return;
                    }
                    Log.d("training","Wrong Answer");
                    charIndex = 0;
                    answer = "";
                    path_A_txtView.setBackgroundResource(R.drawable.rounded_corner);
                    path_B_txtView.setBackgroundResource(R.drawable.rounded_corner);
                    path2_A_txtView.setBackgroundResource(R.drawable.rounded_corner);
                    path2_B_txtView.setBackgroundResource(R.drawable.rounded_corner);
                    attackedAnimation.stop();
                    attackAnimation.stop();
                    attackedAnimation2.stop();
                    attackAnimation2.stop();
                    firestore.collection("Dialogue")
                            .orderBy(FieldPath.documentId())
                            .startAfter(lastDocumentKey)
                            .limit(1)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if(!task.getResult().isEmpty()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                a5 = document.getString("answer_key5");
                                                a4 = document.getString("answer_key4");
                                                a3 = document.getString("answer_key3");
                                                a2 = document.getString("answer_key2");
                                                a = document.getString("answer_key");
                                                String dialogue = document.getString("dialogue");
                                                displayTextWithAnimation(dialogue);

                                                Log.d("training", "Next - Answer Key: " + a);
                                                Log.d("training", "Next - Dialogue: " + b);

//                                                wizard_dialogue.setText(b);

                                                // Update the lastDocumentKey for the next iteration
                                                lastDocumentKey = document.getId();
                                                if (!a.isEmpty()) {
                                                    enableClick = true;
                                                } else {
                                                    enableClick = false;
                                                }
                                            }
                                        }else{
                                            Intent intent = new Intent(training.this, training3.class);
                                            startActivity(intent);
                                        }
                                    } else {
                                        Log.d("training", "Failed: " + task.getException());
                                    }
                                }
                            });
                }else{
                    Log.d("training","Wrong Answer");
                    answer = "";
                    path_A_txtView.setBackgroundResource(R.drawable.rounded_corner);
                    path_B_txtView.setBackgroundResource(R.drawable.rounded_corner);
                    path2_A_txtView.setBackgroundResource(R.drawable.rounded_corner);
                    path2_B_txtView.setBackgroundResource(R.drawable.rounded_corner);
                    attackedAnimation.stop();
                    attackAnimation.stop();
                    attackedAnimation2.stop();
                    attackAnimation2.stop();
                }
            }
        });

        goal_node.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(enableClick) {
//                    translate(start_knight, goal_node);
                    answer = answer + "M";
                    attackAnimation.start();
                }
            }
        });

        goal_node2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(enableClick) {
//                    translate(start_knight, goal_node);
                    answer = answer + "M";
                    attackAnimation2.start();
                }
            }
        });

        path_A_txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                translate(start_knight, goal_node);
                if(enableClick) {
                    answer = answer + "A";
                    path_A_txtView.setBackgroundResource(R.drawable.clicked_rounded_corner);
                }
            }
        });
        path2_A_txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                translate(start_knight, goal_node);
                if(enableClick) {
                    answer = answer + "A";
                    path2_A_txtView.setBackgroundResource(R.drawable.clicked_rounded_corner);
                }
            }
        });
        flower_node_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                translate(start_knight, flower_node_2);
                if(enableClick){
                    answer = answer + "D";
                    attackedAnimation.start();
                }
            }
        });

        flower_node_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                translate(start_knight, flower_node_2);
                if(enableClick){
                    answer = answer + "D";
                    attackedAnimation2.start();
                }
            }
        });

        path_B_txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                translate(start_knight, flower_node_2);
                if(enableClick) {
                    answer = answer + "B";
                    path_B_txtView.setBackgroundResource(R.drawable.clicked_rounded_corner);
                }
            }
        });

        path2_B_txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                translate(start_knight, flower_node_2);
                if(enableClick) {
                    answer = answer + "B";
                    path2_B_txtView.setBackgroundResource(R.drawable.clicked_rounded_corner);
                }
            }
        });
    }
    private void displayTextWithAnimation(final String text) {
        if (text != null) {
            fullText = text;
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (charIndex < fullText.length()) {
                        String partialText = fullText.substring(0, charIndex + 1);
                        wizard_dialogue.setText(partialText);
                        charIndex++;
                        if (charIndex < fullText.length()) {
                            handler.postDelayed(this, 50); // Adjust the delay time between characters
                        }
                    }
                }
            };
            handler.postDelayed(runnable, 50); // Initial delay before starting animation
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable); // To prevent memory leaks, remove callbacks when the activity is destroyed
    }
}