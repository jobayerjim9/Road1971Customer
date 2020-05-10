package com.road.road1971user.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.road.road1971user.R;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    private TextInputLayout phoneNumberInput;
    private TextInputLayout emailInput;
    private TextInputLayout passwordInput1;
    private TextInputLayout passwordInput2;
    private TextView signInState;
    private Button signInButton;
    private TextInputLayout otp_view;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    private String mobile;
    ProgressDialog progressDialog;
    // [END declare_auth]
    private CardView emailPassLayout;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        phoneNumberInput=findViewById(R.id.phoneNumberInput);
        signInState=findViewById(R.id.signInState);
        cardView = findViewById(R.id.cardView);
        signInButton=findViewById(R.id.signInButton);
        emailPassLayout=findViewById(R.id.emailPassLayout);
        emailInput=findViewById(R.id.emailInput);
        passwordInput1=findViewById(R.id.passwordInput1);
        passwordInput2=findViewById(R.id.passwordInput2);
        otp_view=findViewById(R.id.otp_view);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Creating Your Profile!");
        progressDialog.setCancelable(true);

//        otp_view.getEditText().addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(s.toString().length()==6)
//                {
//                    verifyPhoneNumberWithCode(mVerificationId,otp_view.getEditText().getText().toString());
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile="+88"+phoneNumberInput.getEditText().getText().toString();
                startPhoneNumberVerification(mobile);

            }
        });
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to contact or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                // [START_EXCLUDE silent]

                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                // [END_EXCLUDE]
                String code = credential.getSmsCode();
                Log.d("onVerificationCompleted", code + "");
                otp_view.getEditText().setText(code);
                phoneNumberInput.setVisibility(View.GONE);
                otp_view.setVisibility(View.VISIBLE);
                verifyPhoneNumberWithCode(credential);
               // signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]


                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    signInState.setError("Invalid phone number");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    signInState.setError("Quota exceeded");
                    // [END_EXCLUDE]
                }
                else if (e instanceof FirebaseNetworkException)
                {
                    signInState.setError("Network Error!");
                }

                // Show a message and update the UI
                // [START_EXCLUDE]

                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                phoneNumberInput.setVisibility(View.GONE);
                otp_view.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.GONE);
                // [START_EXCLUDE]
                // Update UI

                // [END_EXCLUDE]
            }
        };
        // [END phone_auth_callbacks]
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(phoneNumberInput.getEditText().getText().toString());
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }
    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(PhoneAuthCredential credential) {
        // [START verify_with_code]

        try {
            signInState.setText("Phone Verified! Create Email & Password!");
            cardView.setVisibility(View.GONE);
            emailPassLayout.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.VISIBLE);
            signInButton.setText("Create Account");
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signInWithEmailPass(credential);
                }
            });
            //signInWithPhoneAuthCredential(credential);
        }
        catch (Exception e)
        {
            //mobile="+88"+phoneNumberInput.getEditText().getText().toString();
            //resendVerificationCode(mobile,mResendToken);
            e.printStackTrace();
            signInState.setText(e.getLocalizedMessage());
        }
        // [END verify_with_code]

    }

    private void signInWithEmailPass(PhoneAuthCredential credential) {
        String email=emailInput.getEditText().getText().toString();
        String pass=passwordInput1.getEditText().getText().toString();
        String confirmPass=passwordInput2.getEditText().getText().toString();

        if (email.isEmpty())
        {
            emailInput.setErrorEnabled(true);
            emailInput.setError("Enter Your Email Please!");
        }
        else if (pass.isEmpty())
        {
            passwordInput1.setErrorEnabled(true);
            passwordInput1.setError("Enter Your Password Please!");
        }
        else if (confirmPass.isEmpty())
        {
            passwordInput2.setErrorEnabled(true);
            passwordInput2.setError("Enter Your Password Again!");
        }
        else if (!pass.equals(confirmPass))
        {
            passwordInput2.setErrorEnabled(true);
            passwordInput2.setError("Password Doesn't Match!");
        }
        else
        {
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            Log.d("Success", "Linked!");
                                        }
                                    });
                                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("user").child("UserProfile").child(task.getResult().getUser().getUid());
                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()) {
                                                startActivity(new Intent(SignInActivity.this,HomeActivity.class));
                                            } else {
                                                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
                                                intent.putExtra("mobile",mobile);
                                                intent.putExtra("email",email);
                                                startActivity(intent);
                                            }
                                            progressDialog.dismiss();
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignInActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(SignInActivity.this, "Server May Busy! Try Again Later Or Contact Support", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignInActivity.this, "Server May Busy! Try Again Later Or Contact Support", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//
//
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, task.getResult().getUser().getUid());
//
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                    progressDialog.dismiss();
//                                }
//                            });
//
//
//                            // [START_EXCLUDE]
//                            // [END_EXCLUDE]
//                        } else {
//                            // Sign in failed, display a message and update the UI
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                // The verification code entered was invalid
//                                // [START_EXCLUDE silent]
//                                signInState.setError("Invalid code.");
//                                // [END_EXCLUDE]
//                            }
//                            // [START_EXCLUDE silent]
//                            // Update UI
//
//                            // [END_EXCLUDE]
//                        }
//                    }
//                });
//    }
    // [END sign_in_with_phone]

    private void signOut() {
        mAuth.signOut();
    }
    private boolean validatePhoneNumber() {
        String phoneNumber = phoneNumberInput.getEditText().getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            signInState.setError("Invalid phone number.");
            return false;
        }

        return true;
    }
}
