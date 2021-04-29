package com.example.appanalyzer;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class LoginPageUsername extends AppCompatActivity {
    private Button signUpButton;
    private ImageButton continueImageButton;
    private EditText usernameEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page_username);
        signUpButton = findViewById(R.id.sign_up_button_login_page_username);

        continueImageButton =  findViewById(R.id.continue_image_button_login_page_username);
        usernameEditText = findViewById(R.id.username_edit_text_login_page_username);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to the signup page
                startActivity(new Intent(getApplicationContext() , SignUpUserName.class));
            }
        });
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().isEmpty()){
                    continueImageButton.setClickable(true);
                    continueImageButton.setImageDrawable(getDrawable(R.drawable.ic_next_button_white_20x20));

                }else{
                    continueImageButton.setClickable(false);
                    continueImageButton.setImageDrawable(getDrawable(R.drawable.ic_next_button_silver_20x20));

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        continueImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // trim the username to remove extra spaces
                // and check if the name is not
                // null
                // add the username to a new intent and
                // got to the login page password
                String username = usernameEditText.getText().toString().trim();
                Intent intent =  new Intent(getApplicationContext() , LoginPagePassword.class);
                intent.putExtra("USERNAME" , username);
                startActivity(intent);

            }
        });


    }
}