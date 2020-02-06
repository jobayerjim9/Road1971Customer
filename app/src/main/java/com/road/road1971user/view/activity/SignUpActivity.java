package com.road.road1971user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.road.road1971user.R;
import com.road.road1971user.model.UserProfile;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private TextInputLayout nameText;
    private Button finishButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        nameText=findViewById(R.id.nameText);

        finishButton=findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                name= Objects.requireNonNull(nameText.getEditText()).getText().toString().trim();
                if(name.isEmpty()) {
                   nameText.setErrorEnabled(true);
                   nameText.setError("Please Enter Your Name");
                }
                else
                {
                    uploadToDatabase();
                }

            }
        });
    }

    private void uploadToDatabase() {
        UserProfile userProfile=new UserProfile(nameText.getEditText().getText().toString(),getIntent().getStringExtra("mobile"),getIntent().getStringExtra("email"));
        DatabaseReference profile= FirebaseDatabase.getInstance().getReference("user").child("UserProfile");
        if(FirebaseAuth.getInstance().getUid()!=null) {
            profile.child(FirebaseAuth.getInstance().getUid()).setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(SignUpActivity.this, "Sign Up Completed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this,HomeActivity.class));
                        finish();
                    }
                }

            });
        }

    }
}
