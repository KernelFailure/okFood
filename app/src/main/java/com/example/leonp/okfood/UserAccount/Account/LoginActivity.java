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
import com.example.leonp.okfood.UserAccount.Account.MainActivity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by leonp on 4/18/2018.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // Widgets
    private EditText etUsernameOrEmail, etPassword;
    private Button btnLogin;
    private TextView tvGoToRegister, tvLogginIn;
    private ProgressBar mProgressBar;

    // vars
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String usernameOrEmail, password;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsernameOrEmail = (EditText) findViewById(R.id.etUsernameOrEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvGoToRegister = (TextView) findViewById(R.id.tvGoToRegister);
        tvLogginIn = (TextView) findViewById(R.id.tvLogginIn);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        init();

        setupFirebaseAuth();

    }

    private void init() {
        Log.d(TAG, "init: Starting");

        hideProgressBar();

        setListnerToLoginButton();

        setListenerToRegisterLink();

    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
        tvLogginIn.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        tvLogginIn.setVisibility(View.VISIBLE);
    }

    private void setListnerToLoginButton() {
        Log.d(TAG, "setListnerToLoginButton: Starting");
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Login Button clicked ");

                showProgressBar();

                usernameOrEmail = etUsernameOrEmail.getText().toString();
                password = etPassword.getText().toString();

                if (usernameOrEmail.equals("") || password.equals("")) {
                    Log.d(TAG, "setListnerToLoginButton: Not all fields filled out");
                    Toast.makeText(LoginActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    hideProgressBar();
                } else {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(usernameOrEmail, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    if (task.isSuccessful()) {
                                        try {
                                            if (user.isEmailVerified()) {
                                                Log.d(TAG, "onComplete: Login Sucess. Email already verified");
                                                hideProgressBar();
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Log.d(TAG, "onComplete: Failed to login.  Email not verified");
                                                hideProgressBar();
                                                Toast.makeText(LoginActivity.this, "Email not verified", Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().signOut();
                                            }
                                        } catch (NullPointerException e) {
                                            Log.e(TAG, "onComplete: NullPointerException: " + e.getMessage());
                                        }
                                    } else {
                                        Log.d(TAG, "onComplete: Authentication Failed");
                                        Toast.makeText(LoginActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                                        hideProgressBar();
                                    }
                                }
                            });
                }
                
            }
        });

        

    }

    private void setListenerToRegisterLink() {
        Log.d(TAG, "setListenerToRegisterLink: Starting");
        
        tvGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Register Link Clicked");
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /*
       ----------------------------- Firebase setup ---------------------------------
    */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: started");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    //check if email is verified
                    if(user.isEmailVerified()){
                        Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());


                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }else{
                        Toast.makeText(LoginActivity.this, "Email is not Verified\nCheck your Inbox", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }

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
