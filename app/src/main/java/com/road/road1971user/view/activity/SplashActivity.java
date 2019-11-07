package com.road.road1971user.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.road.road1971user.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startActivity(new Intent(this,HomeActivity.class));
        finish();
        RotateAnimation anim = new RotateAnimation(0f, 350f, 15f, 15f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(2000);


// Start animating the image
        final ImageView splash = findViewById(R.id.logoSplash);
        splash.startAnimation(anim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splash.setAnimation(null);
                if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                {
                    checkSignUp();
                }
                else {
                    startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                    finish();
                }

            }
        },1000);

    }

    private void checkSignUp() {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("user").child("UserProfile").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    startActivity(new Intent(SplashActivity.this,HomeActivity.class));
                    finish();
                }
                else
                {
                    Intent intent=new Intent(SplashActivity.this,SignInActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
