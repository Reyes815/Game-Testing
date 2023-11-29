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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class training extends AppCompatActivity {

    private FirebaseFirestore firestore;

    private List<String> dataset = new ArrayList<>();
    private String answer = "";
    TextView wizard_dialogue;
    FloatingActionButton next_button;
    private String lastDocumentKey = null;
    private String a;
    private String b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alphabet_training);
        Log.d("training", "Failed");

        wizard_dialogue = findViewById(R.id.txtWizarddialogue);
        next_button = findViewById(R.id.next_button);
        // Initialize Firebase
        this.firestore =FirebaseFirestore.getInstance();


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
                // Check if the lastDocumentKey is available
                    // Use the lastDocumentKey to query the next document
                Log.d("training", "Answer: " + answer);
                if(a == null || a.isEmpty() || answer.matches(a)) {
                    firestore.collection("Dialogue")
                            .orderBy(FieldPath.documentId())
                            .startAfter(lastDocumentKey)
                            .limit(1)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            a = document.getString("answer_key");
                                            b = document.getString("dialogue");

                                            Log.d("training", "Next - Answer Key: " + a);
                                            Log.d("training", "Next - Dialogue: " + b);

                                            wizard_dialogue.setText(b);

                                            // Update the lastDocumentKey for the next iteration
                                            lastDocumentKey = document.getId();
                                        }
                                    } else {
                                        Log.d("training", "Failed: " + task.getException());
                                    }
                                }
                            });
                }else{
                    Log.d("training","Wrong Answer");
                    answer = "";
                }
            }
        });

        final Handler handler = new Handler();
        final View goal_node = findViewById(R.id.flower_node_goal);
        final View start_knight = findViewById(R.id.knight1);
        final TextView path_A_txtView = findViewById(R.id.path_A_txtView);
        final View flower_node_2 = findViewById(R.id.flower_node_2);
        final TextView path_B_txtView = findViewById(R.id.path_B_txtView);

        goal_node.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate(start_knight, goal_node);
            }
        });

        path_A_txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                translate(start_knight, goal_node);
                answer = answer+"A";
                path_A_txtView.setBackgroundResource(R.drawable.clicked_rounded_corner);
            }
        });

        flower_node_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate(start_knight, flower_node_2);
            }
        });


        path_B_txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                translate(start_knight, flower_node_2);
                answer = answer+"B";
                path_B_txtView.setBackgroundResource(R.drawable.clicked_rounded_corner);
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
