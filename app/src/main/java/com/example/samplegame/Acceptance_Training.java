package com.example.samplegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Acceptance_Training extends AppCompatActivity {

    public FloatingActionButton button;

    private FirebaseFirestore firestore;

    private char choice;

    TextView A_path;

    private String lastDocumentKey = null;
    TextView B_path;

    List<String> dialoguesList = new ArrayList<>();;

    TextView wizard_dialogue;

    int dialogue_counter = 0;

    int state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training4);

        this.firestore = FirebaseFirestore.getInstance();

        button = findViewById(R.id.next_button);

        wizard_dialogue = findViewById(R.id.txtWizarddialogue);

        state = 0;


        firestore.collection("Dialogue_for_Acceptance_Training")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String b = doc.getString("Dialogue");
                                dialoguesList.add(b);
                                lastDocumentKey = doc.getId();
                                dialogue_counter++;
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

        //A path image
        ImageView path_forA = findViewById(R.id.path_forA);
        ImageView path_forA2 = findViewById(R.id.path_forA2);

        //B path image
        ImageView path_forB = findViewById(R.id.path_forB);
        ImageView path_forB2 = findViewById(R.id.path_forB2);

        //Stone Gate A
        ImageView stone_gate_forA = findViewById(R.id.stone_gate_forA);
        ImageView stone_gate_forA2 = findViewById(R.id.stone_gate_forA2);

        //Stone Gate B
        ImageView stone_gate_forB = findViewById(R.id.stone_gate_forB);
        ImageView stone_gate_forB2 = findViewById(R.id.stone_gate_forB2);

        //Letter A
        A_path = findViewById(R.id.path2_A_txtView);
        TextView A_path2 = findViewById(R.id.path2_A_txtView2);

        //Letter B
        B_path = findViewById(R.id.path2_B_txtView);
        TextView B_path2 = findViewById(R.id.path2_B_txtView2);

        //Knight
        ImageView character = findViewById(R.id.knight1);
        ImageView character2 = findViewById(R.id.knight2);


        // Set the initial position of the duplicate background
        background1.setY(0); // Start the first background at the top
        background2.setY(-background1.getHeight()); // Start the second background above the first one
        path_forA2.setY(-background1.getHeight());
        character2.setY(-background1.getHeight());

        float distanceToMove = 1218;

        // Create a ValueAnimator for the vertical movement
        ValueAnimator backgroundAnimator = ValueAnimator.ofFloat(0, distanceToMove);
        backgroundAnimator.setDuration(5000); // Adjust duration as needed



        backgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                background1.setTranslationY(value);
                background2.setTranslationY(value - background1.getHeight());

                //path A
                path_forA.setTranslationY(value);
                path_forA2.setTranslationY(value - background1.getHeight());

                //path B
                path_forB.setTranslationY(value);
                path_forB2.setTranslationY(value - background1.getHeight());

                //stone gate for A
                stone_gate_forA.setTranslationY(value);
                stone_gate_forA2.setTranslationY(value - background1.getHeight());

                //stone gate for B
                stone_gate_forB.setTranslationY(value);
                stone_gate_forB2.setTranslationY(value - background1.getHeight());

                //Letter A
                A_path.setTranslationY(value);
                A_path2.setTranslationY(value - background1.getHeight());

                //Letter B
                B_path.setTranslationY(value);
                B_path2.setTranslationY(value - background1.getHeight());

                //Knight
                character2.setTranslationY(value - background1.getHeight());
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wizard_dialogue.setText(dialogue_counter);
                dialogue_counter++;
            }
        });

        //wizard_dialogue.setText(state);

        A_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate(character, stone_gate_forA);

                switch (state){
                    case 0:
                        state = 3;
                        wizard_dialogue.setText("State 3");
                        break;
                    case 1:
                        state = 2;
                        wizard_dialogue.setText("State 2");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                path_forA2.setVisibility(View.GONE);
                                path_forB2.setVisibility(View.GONE);
                                stone_gate_forA2.setVisibility(View.GONE);
                                stone_gate_forB2.setVisibility(View.GONE);
                                A_path2.setVisibility(View.GONE);
                                B_path2.setVisibility(View.GONE);
                            }
                        }, 1000);

                        break;
                    case 2:
                        state = 2;
                        wizard_dialogue.setText("State 2");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                path_forA2.setVisibility(View.GONE);
                                path_forB2.setVisibility(View.GONE);
                                stone_gate_forA2.setVisibility(View.GONE);
                                stone_gate_forB2.setVisibility(View.GONE);
                                A_path2.setVisibility(View.GONE);
                                B_path2.setVisibility(View.GONE);
                            }
                        }, 1000);
                        break;
                    case 3:
                        state = 3;
                        wizard_dialogue.setText("State 3");
                        break;
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backgroundAnimator.start();
                    }
                }, 1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forA);
                    }
                }, 1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forA);
                    }
                }, 2500);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forA2);
                    }
                }, 3000);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forA);
                    }
                }, 3500);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forA);
                    }
                }, 4000);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forA);
                    }
                }, 4500);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forA);
                    }
                }, 5000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forA);
                    }
                }, 5500);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forA);
                    }
                }, 6000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, character2);
                    }
                }, 6500);
            }
        });

        A_path2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //translate(character, stone_gate_forA);

                switch (state){
                    case 0:
                        state = 3;
                        wizard_dialogue.setText("State 3");
                        break;
                    case 1:
                        state = 2;
                        wizard_dialogue.setText("State 2");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                path_forA2.setVisibility(View.GONE);
                                path_forB2.setVisibility(View.GONE);
                                stone_gate_forA2.setVisibility(View.GONE);
                                stone_gate_forB2.setVisibility(View.GONE);
                                A_path2.setVisibility(View.GONE);
                                B_path2.setVisibility(View.GONE);
                            }
                        }, 1000);

                        break;
                    case 2:
                        state = 2;
                        wizard_dialogue.setText("State 2");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                path_forA2.setVisibility(View.GONE);
                                path_forB2.setVisibility(View.GONE);
                                stone_gate_forA2.setVisibility(View.GONE);
                                stone_gate_forB2.setVisibility(View.GONE);
                                A_path2.setVisibility(View.GONE);
                                B_path2.setVisibility(View.GONE);
                            }
                        }, 1000);

                        break;
                    case 3:
                        state = 3;
                        wizard_dialogue.setText("State 3");
                        break;
                }


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backgroundAnimator.start();
                    }
                }, 1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forA);
                    }
                }, 1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forA);
                    }
                }, 2500);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forA);
                    }
                }, 3000);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forA);
                    }
                }, 3500);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forA);
                    }
                }, 4000);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forA);
                    }
                }, 4500);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forA);
                    }
                }, 5000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forA);
                    }
                }, 5500);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forA);
                    }
                }, 6000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, character2);
                    }
                }, 6500);
            }
        });


        B_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate(character, stone_gate_forB);

                switch (state){
                    case 0:
                        state = 1;
                        wizard_dialogue.setText("State 1");
                        break;
                    case 1:
                        state = 3;
                        wizard_dialogue.setText("State 3");
                        break;
                    case 2:
                        state = 2;
                        wizard_dialogue.setText("State 2");


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                path_forA2.setVisibility(View.GONE);
                                path_forB2.setVisibility(View.GONE);
                                stone_gate_forA2.setVisibility(View.GONE);
                                stone_gate_forB2.setVisibility(View.GONE);
                                A_path2.setVisibility(View.GONE);
                                B_path2.setVisibility(View.GONE);
                            }
                        }, 1000);

                        break;
                    case 3:
                        state = 3;
                        wizard_dialogue.setText("State 3");
                        break;
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backgroundAnimator.start();
                    }
                }, 1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forB);
                    }
                }, 1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forB);
                    }
                }, 2500);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forB);
                    }
                }, 3000);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forB);
                    }
                }, 3500);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forB);
                    }
                }, 4000);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forB);
                    }
                }, 4500);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forB);
                    }
                }, 5000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forB);
                    }
                }, 5500);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forB);
                    }
                }, 6000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, character2);
                    }
                }, 6500);
            }
        });


        B_path2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //translate(character, stone_gate_forB);

                switch (state){
                    case 0:
                        state = 1;
                        wizard_dialogue.setText("State 1");
                        break;
                    case 1:
                        state = 3;
                        wizard_dialogue.setText("State 3");
                        break;
                    case 2:
                        state = 2;
                        wizard_dialogue.setText("State 2");


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                path_forA2.setVisibility(View.GONE);
                                path_forB2.setVisibility(View.GONE);
                                stone_gate_forA2.setVisibility(View.GONE);
                                stone_gate_forB2.setVisibility(View.GONE);
                                A_path2.setVisibility(View.GONE);
                                B_path2.setVisibility(View.GONE);
                            }
                        }, 1000);

                        break;
                    case 3:
                        state = 3;
                        wizard_dialogue.setText("State 3");
                        break;
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backgroundAnimator.start();
                    }
                }, 1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forB);
                    }
                }, 1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forB);
                    }
                }, 2500);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forB);
                    }
                }, 3000);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forB);
                    }
                }, 3500);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forB);
                    }
                }, 4000);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forB);
                    }
                }, 4500);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forB);
                    }
                }, 5000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forB);
                    }
                }, 5500);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, stone_gate_forB);
                    }
                }, 6000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate(character, character2);
                    }
                }, 6500);
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