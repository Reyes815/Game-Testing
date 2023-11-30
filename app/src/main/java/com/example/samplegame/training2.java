package com.example.samplegame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.checkerframework.checker.units.qual.A;

public class training2 extends AppCompatActivity {

    private Handler handler;
    public FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training2);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.custom_dialoug_for_training2, null);
        builder.setView(customView);

        AlertDialog dialog = builder.create();
        button = findViewById(R.id.next_button);
        //dialog.show();


        // Get references to your background and character ImageViews
        ImageView background1 = findViewById(R.id.imageView4);
        ImageView background2 = findViewById(R.id.imageView4_2);
        ImageView character = findViewById(R.id.knight1);

        // Set the initial position of the duplicate background
        background1.setY(0); // Start the first background at the top
        background2.setY(-background1.getHeight()); // Start the second background above the first one

        float totalBackgroundHeight = background1.getHeight() + background2.getHeight();
        // Calculate the distance you want the background to move vertically
        float distanceToMove = 1339;

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
            }
        });

        // Start the initial background animation
        backgroundAnimator.start();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundAnimator.start();
            }
        });
    }
}