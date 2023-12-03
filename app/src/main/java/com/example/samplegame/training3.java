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
    private List<String> dataset = new ArrayList<>();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training3);
        Log.d("training", "Failed");

        wizard_dialogue = findViewById(R.id.txtWizarddialogue);
        next_button = findViewById(R.id.next_button);
        // Initialize Firebase
        this.firestore =FirebaseFirestore.getInstance();
        final Handler handler = new Handler();
        final ImageView START = findViewById(R.id.START);
        final View goal_node2 = findViewById(R.id.flower_node_goal2); //
        final View goal_node = findViewById(R.id.flower_node_goal);
        final View start_knight = findViewById(R.id.knight1);
        final TextView path2_A_txtView = findViewById(R.id.path2_A_txtView);//
        final TextView path_A_txtView = findViewById(R.id.path_A_txtView);
        final ImageView pathA = findViewById(R.id.pathA);
        final View flower_node_2 = findViewById(R.id.flower_node_2);
        final TextView path_B_txtView = findViewById(R.id.path_B_txtView);
//        final View flower_node_ = findViewById(R.id.flower_node_);//
        final TextView path2_B_txtView = findViewById(R.id.path2_B_txtView);//

//        start_knight.setBackgroundResource(R.drawable.idle_knight);
//        flower_node_.setBackgroundResource(R.drawable.dummy_attacked);
        goal_node2.setBackgroundResource(R.drawable.skeleton_attack);
        flower_node_2.setBackgroundResource(R.drawable.dummy_attacked);
        AnimationDrawable attackedAnimation = (AnimationDrawable) flower_node_2.getBackground();
        AnimationDrawable attackAnimation2= (AnimationDrawable) goal_node2.getBackground();

        goal_node.setBackgroundResource(R.drawable.skeleton_attack);
        AnimationDrawable attackAnimation= (AnimationDrawable) goal_node.getBackground();


        start_knight.setBackgroundResource(R.drawable.knight_attack);
        AnimationDrawable knightAttack = (AnimationDrawable) start_knight.getBackground();
//        AnimationDrawable attackedAnimation2 = (AnimationDrawable) flower_node_.getBackground();

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

                                wizard_dialogue.setText(b);
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
//                        start_knight.setBackgroundResource(R.drawable.idle_knight);
                    }
                }, 1000);
                // Check if the lastDocumentKey is available
                // Use the lastDocumentKey to query the next document
                Log.d("training", "Answer: " + answer);
                boolean flag = false;
                if(answer.isEmpty() && a != ""){
                    flag = true;
                }else if(deadState|| answer.equals("B")|| answer.matches("[A]+")){
                    flag = true;
                }
                if(flag){
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
//                    attackedAnimation2.stop();
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
                else if(a == null || a.isEmpty() || answer.matches(a) || answer.matches(a2)) {
//
//                    answer = "";
//                    path_A_txtView.setBackgroundResource(R.drawable.rounded_corner);
//                    path_B_txtView.setBackgroundResource(R.drawable.rounded_corner);
//                    path2_A_txtView.setBackgroundResource(R.drawable.rounded_corner);
//                    path2_B_txtView.setBackgroundResource(R.drawable.rounded_corner);

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

                                            wizard_dialogue.setText(b);

                                            // Update the lastDocumentKey for the next iteration
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
//                    attackedAnimation2.stop();
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
            }
        });

//        goal_node.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(enableClick) {
////                    translate(start_knight, goal_node);
//                    answer = answer + "M";
//                    attackAnimation.start();
//                }
//            }
//        });

//        goal_node2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(enableClick) {
////                    translate(start_knight, goal_node);
//                    answer = answer + "M";
//                    attackAnimation2.start();
//                }
//            }
//        });

        path_A_txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                translate(start_knight, goal_node);


                if(enableClick) {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            translate(start_knight, goal_node);
                        }
                    }, 1000);
                    if(!monsterA){
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                goal_node.setBackgroundResource(R.drawable.skeleton_attack);
                                AnimationDrawable attackAnimation= (AnimationDrawable) goal_node.getBackground();
                                attackAnimation.start();
                                knightAttack.start();
                                monsterA = true;
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

                    if(monsterA){
                        goal_node.setBackgroundResource(R.drawable.skeleton_death);
                        AnimationDrawable skeletonDeath = (AnimationDrawable) goal_node.getBackground();
                        skeletonDeath.start();


                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

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
//        flower_node_2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                translate(start_knight, flower_node_2);
//                if(enableClick){
//                    answer = answer + "D";
//                    attackedAnimation.start();
//                }
//            }
//        });

//        flower_node_.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                translate(start_knight, flower_node_2);
//                if(enableClick){
//                    answer = answer + "D";
//                    attackedAnimation2.start();
//                }
//            }
//        });

        path_B_txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                translate(start_knight, flower_node_2);

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
                            monsterA = true;
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
}