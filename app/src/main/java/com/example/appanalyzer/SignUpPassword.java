package com.example.appanalyzer;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SignUpPassword extends AppCompatActivity {
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;
    private Button signUpButton;
    private Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_password);
        passwordEditText = findViewById(R.id.password_edit_text_sign_up_password);
        repeatPasswordEditText = findViewById(R.id.repeat_password_edit_text_sign_up_page);
        toolbar = findViewById(R.id.toolbar_sign_up_password);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_button_13x23);
        actionBar.setTitle("Sign up");

        repeatPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // if the passwords match  and the password
                // is valid  set the sign up
                // button to be clickable
                String password = charSequence.toString().trim();
                if(!password.isEmpty() && passwordEditText.getText().toString().trim().equals(password)){
                    if(passwordIsValid(password)){
                        signUpButton.setClickable(true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            signUpButton.setTextColor(getColor(R.color.white));
                        }
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private boolean passwordIsValid(String password) {
        // for checking if password length
        // is more than 8 characters
        if (password.length() <= 8) {
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
