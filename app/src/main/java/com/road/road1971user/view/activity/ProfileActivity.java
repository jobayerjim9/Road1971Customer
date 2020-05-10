package com.road.road1971user.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.road.road1971user.R;
import com.road.road1971user.model.UserProfile;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    TextView nameText, mobileNumberText, logoutProfile;
    ImageView backButtonProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nameText = findViewById(R.id.nameText);
        mobileNumberText = findViewById(R.id.mobileNumberText);
        logoutProfile = findViewById(R.id.logoutProfile);
        backButtonProfile = findViewById(R.id.backButtonProfile);
        backButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        logoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, SignInActivity.class));
                finish();
            }
        });
        getUserProfile();

    }

    private void getUserProfile() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user").child("UserProfile").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                if (userProfile != null) {
                    nameText.setText(userProfile.getName());
                    mobileNumberText.setText(userProfile.getMobile());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
