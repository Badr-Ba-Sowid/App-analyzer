package com.example.appanalyzer;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

public class LoginPagePassword extends AppCompatActivity {
    private TextView userNameTextView;
    private Toolbar toolbar;
    private TextInputEditText passwordEditText;
    private Button loginButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page_password);
        userNameTextView = findViewById(R.id.username_text_view_login_page_password);
        toolbar = findViewById(R.id.toolbar_login_page_password);
        passwordEditText = findViewById(R.id.password_text_input_edit_text_login_page_password);
        loginButton = findViewById(R.id.login_button_login_page_password);

        // get the name from the intent and set
        // the text view with user's username
        String username = getIntent().getStringExtra("USERNAME");
        userNameTextView.setText(username);

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
                if(!charSequence.toString().trim().isEmpty()){
                    loginButton.setClickable(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        loginButton.setTextColor(getApplication().getColor(R.color.white));
                    }
                }else{
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
    }
}
