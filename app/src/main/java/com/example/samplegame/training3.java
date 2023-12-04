package com.example.samplegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.List;

public class training3 extends AppCompatActivity {



    private FirebaseFirestore firestore;
    private boolean enableClick = false;
    private String answer = "";
    TextView wizard_dialogue;
    FloatingActionButton next_button;
    private String lastDocumentKey = null;
    private String a = "",a2;
    private String b;
    private boolean monsterA = false;
    private boolean monsterB = false;
    private boolean clicked = false;
    private boolean deadState = false;
    private Handler handler = null;
    private ImageView START;
    private View goal_node2;
    private View goal_node;
    private View start_knight;
    private TextView path2_A_txtView;
    private TextView path_A_txtView;
    private View flower_node_2;
    private TextView path_B_txtView;
    private TextView path2_B_txtView;
    private AnimationDrawable attackedAnimation;
    private AnimationDrawable attackAnimation2;
    private AnimationDrawable attackAnimation;
    private AnimationDrawable knightAttack;
    private boolean AisDead = false;
    private Runnable runnable;
    private int charIndex = 0;
    private String fullText = "";
    private ImageView wizard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training3);
        Log.d("training", "Failed");

        wizard_dialogue = findViewById(R.id.txtWizarddialogue);
        next_button = findViewById(R.id.next_button);
        this.firestore =FirebaseFirestore.getInstance();
        handler = new Handler();
        START = findViewById(R.id.START);
        goal_node2 = findViewById(R.id.flower_node_goal2);
        goal_node = findViewById(R.id.flower_node_goal);
        start_knight = findViewById(R.id.knight1);
        path2_A_txtView = findViewById(R.id.path2_A_txtView);
        path_A_txtView = findViewById(R.id.path_A_txtView);
        flower_node_2 = findViewById(R.id.flower_node_2);
        path_B_txtView = findViewById(R.id.path_B_txtView);
        path2_B_txtView = findViewById(R.id.path2_B_txtView);
        goal_node2.setBackgroundResource(R.drawable.skeleton_attack);
        flower_node_2.setBackgroundResource(R.drawable.dummy_attacked);
        attackedAnimation = (AnimationDrawable) flower_node_2.getBackground();
        attackAnimation2= (AnimationDrawable) goal_node2.getBackground();

        goal_node.setBackgroundResource(R.drawable.skeleton_attack);
        attackAnimation= (AnimationDrawable) goal_node.getBackground();

        wizard.setBackgroundResource(R.drawable.idle_wizard2);
        AnimationDrawable idleW = (AnimationDrawable) wizard.getBackground();
        idleW.start();

        start_knight.setBackgroundResource(R.drawable.knight_attack);
        knightAttack = (AnimationDrawable) start_knight.getBackground();

        firestore.collection("Dialogue3")
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
                                if(!a.isEmpty()){
                                    enableClick = true;
                                }else{
                                    enableClick = false;
                                }
                            }
                        } else {
                            Log.d("training", "Failed: " + task.getException());
                        }
                    }
                });
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                charIndex = 0;
                if(monsterB){
                    goal_node2.setBackgroundResource(R.drawable.skeleton_death);
                    AnimationDrawable skeletonDeath2 = (AnimationDrawable) goal_node2.getBackground();

                    skeletonDeath2.start();
                }
                if(!clicked){
                    knightAttack.stop();
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(start_knight, START);
                    }
                }, 1000);
                Log.d("training", "Answer: " + answer);
                boolean flag = false;
                if(answer.isEmpty() && a != ""){
                    flag = true;
                }else if(deadState|| answer.equals("B")|| answer.matches("[A]+")){
                    flag = true;
                }
                if(flag){
                    rollBack();
                }
                else if(a == null || a.isEmpty() || answer.matches(a) || answer.matches(a2)) {
                    firestore.collection("Dialogue3")
                            .orderBy(FieldPath.documentId())
                            .startAfter(lastDocumentKey)
                            .limit(1)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            a2 = document.getString("answer_key2");
                                            a = document.getString("answer_key");
                                            b = document.getString("dialogue");

                                            Log.d("training", "Next - Answer Key: " + a);
                                            Log.d("training", "Next - Dialogue: " + b);

                                            String dialogue = document.getString("dialogue");
                                            displayTextWithAnimation(dialogue);

                                            lastDocumentKey = document.getId();
                                            if(!a.isEmpty()){
                                                enableClick = true;
                                            }else{
                                                enableClick = false;
                                            }
                                        }
                                    } else {
                                        Log.d("training", "Failed: " + task.getException());
                                    }
                                }
                            });
                }else{
                    rollBack();
                }
            }
        });

        path_A_txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(enableClick) {

                    monsterA = true;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            translate(start_knight, goal_node);
                        }
                    }, 1000);
                    if(!AisDead){
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                goal_node.setBackgroundResource(R.drawable.skeleton_attack);
                                AnimationDrawable attackAnimation= (AnimationDrawable) goal_node.getBackground();
                                attackAnimation.start();
                                knightAttack.start();
                                AisDead = true;
                            }
                        }, 1000);
                    }
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

                    if(AisDead){
                        goal_node.setBackgroundResource(R.drawable.skeleton_death);
                        AnimationDrawable skeletonDeath = (AnimationDrawable) goal_node.getBackground();
                        skeletonDeath.start();


                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                monsterA = false;
                                translate(start_knight, START);
                                attackAnimation.stop();
                                knightAttack.stop();
                            }
                        }, 1000);
                        answer = answer + "A";
                        path2_A_txtView.setBackgroundResource(R.drawable.clicked_rounded_corner);
                    }
                }
            }
        });

        path_B_txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(enableClick && !clicked) {
                    deadState = true;
                    clicked = true;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            translate(start_knight, flower_node_2);
                        }
                    }, 1000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            attackedAnimation.start();
                            knightAttack.start();
                            monsterA = false;
                        }
                    }, 1000);

                    answer = answer + "C";
                    path_B_txtView.setBackgroundResource(R.drawable.clicked_rounded_corner);
                }
            }
        });

        path2_B_txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(enableClick && monsterA) {
                        goal_node.setBackgroundResource(R.drawable.skeleton_death);
                        AnimationDrawable skeletonDeath = (AnimationDrawable) goal_node.getBackground();
                        skeletonDeath.start();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            translate(start_knight, goal_node2);
                        }
                    }, 1000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            attackAnimation2.start();
                            knightAttack.start();
                            monsterB = true;
                        }
                    }, 1000);
                    answer = answer + "B";
                    path2_B_txtView.setBackgroundResource(R.drawable.clicked_rounded_corner);
                }
            }
        });
    }

    private void translate(View viewToMove, View target) {

        float targetCenterX = target.getX() + (target.getWidth() - viewToMove.getWidth()) / 2;
        float targetCenterY = target.getY() + (target.getHeight() - viewToMove.getHeight()) / 2;

        viewToMove.animate()
                .x(targetCenterX)
                .y(targetCenterY)
                .setDuration(1000)
                .start();
    }

    private void rollBack(){
            Log.d("training","Wrong Answer");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    translate(start_knight, START);
                }
            }, 1000);
            answer = "";
            path_A_txtView.setBackgroundResource(R.drawable.rounded_corner);
            path_B_txtView.setBackgroundResource(R.drawable.rounded_corner);
            path2_A_txtView.setBackgroundResource(R.drawable.rounded_corner);
            path2_B_txtView.setBackgroundResource(R.drawable.rounded_corner);
            attackedAnimation.stop();
            attackAnimation.stop();
            attackAnimation2.stop();
            knightAttack.stop();

            goal_node2.setBackgroundResource(R.drawable.skeleton_attack);
            flower_node_2.setBackgroundResource(R.drawable.dummy_attacked);
            goal_node.setBackgroundResource(R.drawable.skeleton_attack);
            deadState = false;
            monsterB = false;
            monsterA = false;
            clicked = false;

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