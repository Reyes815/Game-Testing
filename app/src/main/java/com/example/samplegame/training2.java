package com.example.samplegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class training2 extends AppCompatActivity {

    private Handler handler;
    public FloatingActionButton button;

    private FirebaseFirestore firestore;

    private char choice;

    ImageView keyA;

    private String lastDocumentKey = null;
    ImageView keyB;

    List<String> dialoguesList = new ArrayList<>();;

    TextView wizard_dialogue;

    int dialogue_counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training2);

        this.firestore = FirebaseFirestore.getInstance();

        button = findViewById(R.id.next_button);

        wizard_dialogue = findViewById(R.id.txtWizarddialogue);


        firestore.collection("Dialogue_Training2")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String b = doc.getString("Dialogue");
                                dialoguesList.add(b);
                                lastDocumentKey = doc.getId();
                            }
                            wizard_dialogue.setText(dialoguesList.get(0));
                        } else {
                            Log.d("training", "Failed: " + task.getException());
                        }
                    }
                });



        // Get references to your background and character ImageViews
        ImageView background1 = findViewById(R.id.imageView4);
        ImageView background2 = findViewById(R.id.imageView4_2);
        ImageView path = findViewById(R.id.imageView6);
        ImageView path2 = findViewById(R.id.imageView10);
        ImageView stone_gate = findViewById(R.id.stone_gate);
        ImageView stone_gate2 = findViewById(R.id.stone_gate2);
        ImageView character = findViewById(R.id.knight1);
        ImageView character2 = findViewById(R.id.knight2);

        // Set the initial position of the duplicate background
        background1.setY(0); // Start the first background at the top
        background2.setY(-background1.getHeight()); // Start the second background above the first one
        path2.setY(-background1.getHeight());
        stone_gate2.setY(-background1.getHeight());

        float totalBackgroundHeight = background1.getHeight() + background2.getHeight();
        // Calculate the distance you want the background to move vertically
        float distanceToMove = 1218;

        // Create a ValueAnimator for the vertical movement
        ValueAnimator backgroundAnimator = ValueAnimator.ofFloat(0, distanceToMove);
        backgroundAnimator.setDuration(5000); // Adjust duration as needed
        //backgroundAnimator.setRepeatCount(ValueAnimator.INFINITE); // Repeat the animation

        // Update the vertical position of the backgrounds during animation
        backgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                background1.setTranslationY(value);
                background2.setTranslationY(value - background1.getHeight());
                path.setTranslationY(value);
                path2.setTranslationY(value - background1.getHeight());
                stone_gate.setTranslationY(value);
                stone_gate2.setTranslationY(value - background1.getHeight());
            }
        });

        // Start the initial background animation
        //backgroundAnimator.start();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.custom_dialoug_for_training2, null);
        builder.setView(customView);


        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //dialog.show();

        keyA = customView.findViewById(R.id.Key_imageview_A);
        keyB = customView.findViewById(R.id.Key_imageview_B);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogue_counter++;

                if(dialogue_counter != 5){
                    wizard_dialogue.setText(dialoguesList.get(dialogue_counter));

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Your delayed code here
                            dialog.show();
                        }
                    }, 500);
                }else {
                    wizard_dialogue.setText("Congrats on Finishing the Training");
                }

            }
        });






        keyA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice = 'A';

                if (dialogue_counter == 1) {
                    translate(character, stone_gate);
                    dialog.dismiss();
                    // Delayed action with a Handler
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Your delayed code here
                            translate(character, character2);
                        }
                    }, 1500);  // 1000 milliseconds (1 second) delay

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Your delayed code here
                            wizard_dialogue.setText("Nope try again");
                        }
                    }, 1500);


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Your delayed code here
                            dialog.show();
                        }
                    }, 3000);  // 1000 milliseconds (1 second) delay
                }

                if(dialogue_counter == 3){
                    translate(character, stone_gate2);
                    dialog.dismiss();
                    // Delayed action with a Handler
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Your delayed code here
                            translate(character, character2);
                        }
                    }, 1500);  // 1000 milliseconds (1 second) delay

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Your delayed code here
                            wizard_dialogue.setText("Nope try again");
                        }
                    }, 1500);


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Your delayed code here
                            dialog.show();
                        }
                    }, 3000);  // 1000 milliseconds (1 second) delay
                }

                if(dialogue_counter == 2 || dialogue_counter == 4){
                    backgroundAnimator.start();
                    dialog.dismiss();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(dialogue_counter < 4){
                                wizard_dialogue.setText("Congrats!");
                            }
                        }
                    }, 1000 );


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            button.performClick();
                        }
                    }, 5500 );
                }

            }
        });

        keyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice = 'B';
                if(dialogue_counter == 1 || dialogue_counter == 3){
                    backgroundAnimator.start();
                    dialog.dismiss();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(dialogue_counter < 4){
                                wizard_dialogue.setText("Congrats!");
                            }
                        }
                    }, 1000 );

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            button.performClick();
                        }
                    }, 5500 );

                }

                if(dialogue_counter == 2){
                    translate(character, stone_gate2);
                    dialog.dismiss();
                    // Delayed action with a Handler
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Your delayed code here
                            translate(character, character2);
                        }
                    }, 1500);  // 1000 milliseconds (1 second) delay


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Your delayed code here
                            wizard_dialogue.setText("Nope try again");
                        }
                    }, 1500);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Your delayed code here
                            dialog.show();
                        }
                    }, 3000);  // 1000 milliseconds (1 second) delay
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