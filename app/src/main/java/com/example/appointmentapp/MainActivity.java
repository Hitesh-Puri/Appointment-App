package com.example.appointmentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static int SPLASH_SCREEN=3000;

    Animation topAnim,bottomAnim;
    ImageView image;
    TextView name, tagging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FOR REMOVING STATUS BAR FROM TOP
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //animations
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //hooks
        image= findViewById(R.id.logo);
        name= findViewById(R.id.display);
        tagging = findViewById(R.id.tag);

        image.setAnimation(topAnim);
        name.setAnimation(bottomAnim);
        tagging.setAnimation(bottomAnim);

        //FOR LINKING THIS MAIN ACTIVITY TO THE NEXT LOGIN ACTIVITY
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,Login.class);

                // Attach all the elements those you want to animate in design
                Pair[] pairs=new Pair[2];
                pairs[0]=new Pair<View,String>(image,"logo2");
                pairs[1]=new Pair<View,String>(name,"hello");

                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                startActivity(intent, options.toBundle());

            }
        },SPLASH_SCREEN);
    }
}