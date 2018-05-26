package com.example.leonp.okfood.UserAccount.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonp.okfood.R;
import com.example.leonp.okfood.UserAccount.Account.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by leonp on 4/21/2018.
 */

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";


    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;

    // widgets
    private EditText etEmail, etUsername, etPassword, etConfirmPassword;
    private Button btnRegister;
    private ProgressBar mProgressBar;
    private TextView tvRegistering;

    // vars
    private User mUser;
    private String email, username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvRegistering = (TextView) findViewById(R.id.tvRegistering);

        mUser = new User();

        init();
    }

    private void init(){

        setupFirebaseAuth();

        hideProgressBar();

        addListenerToRegisterButton();

    }

    private void addListenerToRegisterButton(){
        Log.d(TAG, "addListenerToRegisterButton: starting");
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Register Button clicked");
                email = etEmail.getText().toString();
                username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String confirmedPassword = etConfirmPassword.getText().toString();

                if (allFieldsFilledOut(email, username, password, confirmedPassword)) {
                    // try to login
                    if (password.equals(confirmedPassword)) {
                        registerNewUser(email, username, password);
                    } else {
                        Log.d(TAG, "onClick: Passwords don't match");
                        Toast.makeText(RegisterActivity.this, "Passwords don't match. Try again", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "onClick: Not all fields are filled out");
                    Toast.makeText(RegisterActivity.this, "All fields must be filled out", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean allFieldsFilledOut(String email, String username, String password, String confirmedPassword) {
        Log.d(TAG, "allFieldsFilledOut: Starting");
        if (email.equals("") || username.equals("") || password.equals("") || confirmedPassword.equals("")) {
            return false;
        }
        return true;
    }

    private void registerNewUser(final String email, String username, String password) {
        Log.d(TAG, "registerNewUser: Starting");

        showProgressBar();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Successfully registered email and password for: " + email);

                            hideProgressBar();

                            sendVerificationEmail();


                        } else {
                            Log.d(TAG, "onComplete: Failed to register email and password for: " + email);
                            hideProgressBar();
                            Toast.makeText(RegisterActivity.this, "Failed to register with this email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerificationEmail(){
        Log.d(TAG, "sendVerificationEmail: Starting");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: Email verification sent successfully");
                                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                Log.d(TAG, "onComplete: User ID: " + userID);

                            } else {
                                Log.d(TAG, "onComplete: Failed to send verification email");
                                Toast.makeText(RegisterActivity.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    private void addNewUser() {
        Log.d(TAG, "addNewUser: Starting");

        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mUser.setUser_id(user_id);
        mUser.setUsername(username);
        mUser.setEmail(email);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.child(getString(R.string.node_users))
                .child(user_id)
                .setValue(mUser);

        FirebaseAuth.getInstance().signOut();
        redirectLoginScreen();

    }

    private void redirectLoginScreen() {
        Log.d(TAG, "redirectLoginScreen: Starting");

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
        tvRegistering.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        tvRegistering.setVisibility(View.VISIBLE);
    }

    private void setupFirebaseAuth(){

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is authenticated
                    Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            addNewUser();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");

                }
                // ...
            }
        };

    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }
}
