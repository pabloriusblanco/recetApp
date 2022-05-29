package com.example.recetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.card.MaterialCardView;

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        MaterialCardView textContainer = findViewById(R.id.splash_text_container);
        LottieAnimationView animation_splash = findViewById(R.id.animation_splash);
        ImageView logo_spoonacular = findViewById(R.id.logo_spoonacular);

        // ANIMACIONES
        logo_spoonacular.startAnimation(getFadeAnimation(1,0,1000));
        textContainer.animate().translationX(1000).setDuration(1000).setStartDelay(2500).setInterpolator(new AccelerateDecelerateInterpolator());
        animation_splash.animate().translationX(-1000).setDuration(1000).setStartDelay(2500).setInterpolator(new AccelerateDecelerateInterpolator());

        // Sleep de la pantalla
        Thread thread = new Thread(){
            public void run() {
                try {
                    Thread.sleep(4000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(ActivitySplash.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        thread.start();
    }

    private final AlphaAnimation getFadeAnimation(Integer fadeStart, Integer fadeEnd, Integer duration) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(fadeStart, fadeEnd);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setStartOffset(2500);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.setFillAfter(true);

       return alphaAnimation;
    }
}