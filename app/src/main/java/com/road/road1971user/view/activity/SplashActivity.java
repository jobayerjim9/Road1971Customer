package com.road.road1971user.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.road.road1971user.R;

public class SplashActivity extends AppCompatActivity {
    RotateAnimation anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        startActivity(new Intent(this,HomeActivity.class));
//        finish();
        anim = new RotateAnimation(0f, 350f, 15f, 15f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else
            {
                final ImageView splash = findViewById(R.id.logoSplash);
                splash.startAnimation(anim);
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    checkSignUp();
                } else {
                    startActivity(new Intent(SplashActivity.this, SignInActivityEmail.class));
                    finish();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        splash.setAnimation(null);
                    }
                },1000);
            }
        }
// Start animating the image


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
                    FirebaseAuth.getInstance().signOut();
                    Intent intent=new Intent(SplashActivity.this,SignInActivityEmail.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                            startActivity(new Intent(SplashActivity.this, SignInActivityEmail.class));
                            finish();
                        }

                    }
                },1000);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                Toast.makeText(this, "Please Allow Location Permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
