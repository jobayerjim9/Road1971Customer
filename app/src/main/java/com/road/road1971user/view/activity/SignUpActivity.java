package com.road.road1971user.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.road.road1971user.R;
import com.road.road1971user.model.UserProfile;

public class SignUpActivity extends AppCompatActivity {
    private TextInputLayout nameText,emailText;
    private Button finishButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        nameText=findViewById(R.id.nameText);
        emailText=findViewById(R.id.emailText);
        finishButton=findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nameText.getEditText().getText().toString().isEmpty() && !emailText.getEditText().getText().toString().isEmpty()) {
                    uploadToDatabase();
                }
            }
        });


    }

    private void uploadToDatabase() {
        UserProfile userProfile=new UserProfile(nameText.getEditText().getText().toString(),getIntent().getStringExtra("mobile"),emailText.getEditText().getText().toString());
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
