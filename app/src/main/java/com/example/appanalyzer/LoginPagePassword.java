package com.example.appanalyzer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginPagePassword extends AppCompatActivity {
    private TextView userEmailTextView;
    private Toolbar toolbar;
    private TextInputEditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page_password);
        userEmailTextView = findViewById(R.id.username_text_view_login_page_password);
        toolbar = findViewById(R.id.toolbar_login_page_password);
        passwordEditText = findViewById(R.id.password_text_input_edit_text_login_page_password);
        loginButton = findViewById(R.id.login_button_login_page_password);

        // get the name from the intent and set
        // the text view with user's username
        String username = getIntent().getStringExtra("EMAIL");
        userEmailTextView.setText(username);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_button_13x23);
        actionBar.setTitle("Password");

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //on text changed make the login button clickable
                if (!charSequence.toString().trim().isEmpty()) {
                    loginButton.setClickable(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        loginButton.setTextColor(getApplication().getColor(R.color.white));

                    }
                } else {
                    loginButton.setClickable(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        loginButton.setTextColor(getApplication().getColor(R.color.silver));
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String username = userEmailTextView.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (auth.getCurrentUser().isEmailVerified()) {
                        String userEmail = auth.getCurrentUser().getEmail();
                        Intent intent = new Intent(LoginPagePassword.this, MainActivity.class);
                        intent.putExtra("EMAIL", userEmail);
                        startActivity(intent);
                    }
                    if (!(auth.getCurrentUser().isEmailVerified())) {
                        Toast.makeText(LoginPagePassword.this, "Please verify your email address", Toast.LENGTH_LONG).show();
                        final FirebaseUser user = auth.getCurrentUser();
                        user.sendEmailVerification().addOnCompleteListener(LoginPagePassword.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginPagePassword.this, "A new verification email has been sent. Please verify your email" + user.getEmail(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(LoginPagePassword.this, "Failed to send verification email", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                }
            }
        }). addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(LoginPagePassword.this, "Invalid password", Toast.LENGTH_LONG).show();
                } else if (e instanceof FirebaseAuthInvalidUserException) {
                    String errorCode = ((FirebaseAuthInvalidUserException) e).getErrorCode();
                    if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                        Toast.makeText(LoginPagePassword.this, "This email does not belong to a registered account. Please proceed to Sign Up.", Toast.LENGTH_SHORT).show();
                    } else if (errorCode.equals("ERROR_USER_DISABLED")) {
                        Toast.makeText(LoginPagePassword.this, "User account has been disabled", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginPagePassword.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }
}

