package com.example.samplegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class training extends AppCompatActivity {

    private FirebaseFirestore db;

    private DocumentReference dialogueRef;

    private List<String> dataset = new ArrayList<>();

    TextView wizard_dialogue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alphabet_training);
        Log.d("training", "Failed");

        wizard_dialogue = findViewById(R.id.txtWizarddialogue);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        dialogueRef = db.document("Dialogue/mNTpH0tXVP48DLm5P9ef");

        dialogueRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        // Document exists, you can access its data
                        String a = doc.getString("answer_key");
                        String b = doc.getString("dialogue");

                        Log.d("training", "Answer Key: " + a);
                        Log.d("training", "Dialogue: " + b);

                        wizard_dialogue.setText(b);
                    } else {
                        Log.d("training", "Document not found");
                    }
                } else {
                    Log.d("training", "Failed: " + task.getException());
                }
            }
        });







//
//        final Handler handler = new Handler();
//
//        EditText input = findViewById(R.id.inputEditText);
//        ImageView knight = findViewById(R.id.knight);
//        final View start_knight = findViewById(R.id.start_image);
//        final View node1 = findViewById(R.id.flower_node_1);
//        final View node2 = findViewById(R.id.dummy1_target2);
//        final View goal_node = findViewById(R.id.skeleton1_target1);
//        Button move = findViewById(R.id.B_btn);
//
//        Drawable climbing_knight = getResources().getDrawable(R.drawable.climb_knight);
//        Drawable idle_knight = getResources().getDrawable(R.drawable.idle_knight);
//        Drawable falling_knight = getResources().getDrawable(R.drawable.falling_knight);
//
//        move.setOnClickListener(new View.OnClickListener() {
//
//            int failed = 0;
//            @Override
//            public void onClick(View v) {
//                String strInput = input.getText().toString().trim();
//
//                //check if empty
//                if(input.getText().toString().isEmpty()){
//                    Toast.makeText(getBaseContext(), "Try Again", Toast.LENGTH_SHORT).show();
//                }
//
//                //move to start node
//                knight.setImageDrawable(climbing_knight);
//                translate(knight, node1);
//
//                //check input
//                if(strInput.charAt(0) == 'A' || strInput.charAt(0) == 'a') {
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            translate(knight, goal_node);
//                        }
//                    }, 1000);
//                    Toast.makeText(getBaseContext(), "Congratulations", Toast.LENGTH_SHORT).show();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            knight.setImageDrawable(idle_knight);
//                        }
//                    }, 2000);
//                }else {
//                    //wrong input
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            translate(knight, node2);
//                        }
//                    }, 1000);
//                    Toast.makeText(getBaseContext(), "Try Again", Toast.LENGTH_SHORT).show();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            knight.setImageDrawable(idle_knight);
//                        }
//                    }, 2000);
//                    failed = 1;
//                }
//
////                if(done_moving == 1){
////                    knight.setImageDrawable(idle_knight);
////                }
//
//                //go back to start after failed level
//                if(failed == 1){
//
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            knight.setImageDrawable(falling_knight);
//                            translate(knight, start_knight);
//                        }
//                    }, 5000);
//
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            knight.setImageDrawable(idle_knight);
//                        }
//                    }, 6000);
//                }
//
//
//            }
//        });

    }

    private void translate(View viewToMove, View target) {

        viewToMove.animate()
                .x(target.getX())
                .y(target.getY())
                .setDuration(1000)
                .start();
    }
}
