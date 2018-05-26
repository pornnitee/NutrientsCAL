package com.example.starius.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            overridePendingTransition(0, 0);
        }

        ImageView img = (ImageView)findViewById(R.id.logo);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.welcome);
        img.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                overridePendingTransition(0, 0);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
