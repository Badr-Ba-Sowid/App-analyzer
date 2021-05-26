package com.example.appanalyzer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpPassword extends AppCompatActivity {
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;
    private Button signUpButton;
    private Toolbar toolbar;
    private String username, email;
    private CustomProgressBar signInProgressBar;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_password);
        passwordEditText = findViewById(R.id.password_edit_text_sign_up_password);
        repeatPasswordEditText = findViewById(R.id.repeat_password_edit_text_sign_up_page);
        toolbar = findViewById(R.id.toolbar_sign_up_password);
        signUpButton = findViewById(R.id.sign_up_button_sign_in_page);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_button_13x23);
        actionBar.setTitle("Sign up");
        email = getIntent().getStringExtra("EMAIL");
        username = getIntent().getStringExtra("USERNAME");
        repeatPasswordEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // if the passwords match  and the password
                // is valid  set the sign up
                // button to be clickable
                String password = charSequence.toString().trim();
                if(!password.isEmpty() && passwordEditText.getText().toString().trim().equals(password)){
                    if(passwordIsValid(password)){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            signUpButton.setTextColor(getColor(R.color.white));
                        }
                        signUpButton.setClickable(true);
                    }else{
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            signUpButton.setTextColor(getColor(R.color.silver));
                        }
                        signUpButton.setClickable(false);
                    }
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        signUpButton.setTextColor(getColor(R.color.silver));
                    }
                    signUpButton.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

    }

    private void signUp() {
        //show the progress bar
        signInProgressBar = new CustomProgressBar(SignUpPassword.this,"Creating your account..."  , R.raw.creating_your_account);
        signInProgressBar.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        signInProgressBar.show();
        String password = passwordEditText.getText().toString().trim();
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String, Object> userDetails = new HashMap<>();
                userDetails.put("email", email);
                userDetails.put("username", username);
                userDetails.put("ID", auth.getCurrentUser().getUid());
                FirebaseFirestore usersDatabase = FirebaseFirestore.getInstance();
                usersDatabase.collection("Users").document(auth.getCurrentUser().getUid())
                        .set(userDetails)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                sendEmailVerification();
                                Intent intent = new Intent(SignUpPassword.this, LoginPagePassword.class);
                                intent.putExtra("EMAIL", email);
                                signInProgressBar.dismiss();
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //TODO handle error
                                signInProgressBar.dismiss();
                                Toast.makeText(SignUpPassword.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO handle error
                signInProgressBar.dismiss();
            }
        });
    }

        private void sendEmailVerification(){
            final FirebaseUser user = auth.getCurrentUser();
            user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Registered Successfully. Verification email sent to " + user.getEmail(), Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Failed to send verification email", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

    private boolean passwordIsValid(String password) {
        // for checking if password length
        // is more than 8 characters
        if (password.length() < 8) {
            return false;
        }
        // to check space
        if (password.contains(" ")) {
            return false;
        }
        if (true) {
            int count = 0;

            // check digits from 0 to 9
            for (int i = 0; i <= 9; i++) {

                // to convert int to string
                String str1 = Integer.toString(i);

                if (password.contains(str1)) {
                    count = 1;
                }
            }
            if (count == 0) {
                return false;
            }
        }

        // for special characters
        if (!(password.contains("@") || password.contains("#")
                || password.contains("!") || password.contains("~")
                || password.contains("$") || password.contains("%")
                || password.contains("^") || password.contains("&")
                || password.contains("*") || password.contains("(")
                || password.contains(")") || password.contains("-")
                || password.contains("+") || password.contains("/")
                || password.contains(":") || password.contains(".")
                || password.contains(",") || password.contains("<")
                || password.contains(">") || password.contains("?")
                || password.contains("|"))) {
            return false;
        }

        if (true) {
            int count = 0;

            // checking capital letters
            for (int i = 65; i <= 90; i++) {

                // type casting
                char c = (char)i;

                String str1 = Character.toString(c);
                if (password.contains(str1)) {
                    count = 1;
                }
            }
            if (count == 0) {
                return false;
            }
        }

        if (true) {
            int count = 0;
            // checking small letters
            for (int i = 90; i <= 122; i++) {
                // type casting
                char c = (char)i;
                String str1 = Character.toString(c);
                if (password.contains(str1)) {
                    count = 1;
                }
            }
            if (count == 0) {
                return false;
            }
        }

        // if all conditions fails
        return true;
    }

}
