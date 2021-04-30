package com.example.appanalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.regex.Pattern;

public class SignUpUserName extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText usernameEditText , emailEditText;
    private ImageButton continueImageButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user_name);
        toolbar = findViewById(R.id.toolbar_sign_up_page);
        usernameEditText = findViewById(R.id.username_edit_text_sign_up_page);
        emailEditText = findViewById(R.id.email_edit_text_sign_up_page);
        continueImageButton = findViewById(R.id.continue_image_button_sign_up_page);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_button_13x23);
        actionBar.setTitle("Sign up");

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = charSequence.toString().trim();
                if(!email.isEmpty() && !emailEditText.getText().toString().trim().isEmpty()){
                    if(emailIsValid(email)){
                        continueImageButton.setClickable(true);
                        continueImageButton.setImageDrawable(getDrawable(R.drawable.ic_next_button_white_20x20));
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

        continueImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpPassword.class);
                intent.putExtra("EMAIL", emailEditText.getText().toString().trim());
                intent.putExtra("USERNAME", usernameEditText.getText().toString().trim());
                startActivity(intent);
            }
        });
    }

    public static boolean emailIsValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
