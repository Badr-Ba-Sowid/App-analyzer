package com.example.appanalyzer;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class LoginPageUsername extends AppCompatActivity {
    private Button signUpButton, GoogleSignUpButton;
    private ImageButton continueImageButton;
    private EditText emailEditText;
    private final int RC_SIGN_IN = 7;
    private ImageView appLogo;
    GoogleSignInClient signInGoogle;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    RelativeLayout afterAnimationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page_username);
        signUpButton = findViewById(R.id.sign_up_button_login_page_username);
        GoogleSignUpButton = findViewById(R.id.google_signing_button_login_page_username);
        continueImageButton = findViewById(R.id.continue_image_button_login_page_username);
        emailEditText = findViewById(R.id.email_edit_text_login_page);
        afterAnimationView = findViewById(R.id.afterAnimationView);
        appLogo = findViewById(R.id.app_logo_image_view_login_page_username);

        appLogo.animate().yBy(100f).setDuration(500).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                afterAnimationView.animate().alpha(1.0f).setDuration(500).start();
                afterAnimationView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        signInGoogle = GoogleSignIn.getClient(this, gso);
        GoogleSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to the signup page
                startActivity(new Intent(getApplicationContext(), SignUpUserName.class));
            }
        });
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    continueImageButton.setClickable(true);
                    continueImageButton.setImageDrawable(getDrawable(R.drawable.ic_next_button_white_20x20));

                } else {
                    continueImageButton.setClickable(false);
                    continueImageButton.setImageDrawable(getDrawable(R.drawable.ic_next_button_mine_shaft_20x20));

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
                String email = emailEditText.getText().toString().trim();
                Intent intent = new Intent(getApplicationContext(), LoginPagePassword.class);
                intent.putExtra("EMAIL", email);
                startActivity(intent);

            }
        });


    }

    private void signInWithGoogle() {
        Intent signInIntent = signInGoogle.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {

            }
            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(LoginPageUsername.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                        HashMap<String, Object> userDetails = new HashMap<>();
                        userDetails.put("email", user.getEmail());
                        userDetails.put("ID", auth.getCurrentUser().getUid());
                        FirebaseFirestore usersDatabase = FirebaseFirestore.getInstance();
                        usersDatabase.collection("Users").document(auth.getCurrentUser().getUid())
                                .set(userDetails)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        String userEmail = auth.getCurrentUser().getEmail();
                                        Intent intent = new Intent(LoginPageUsername.this, MainActivity.class);
                                        intent.putExtra("EMAIL", userEmail);
                                        startActivity(intent);
                                        Toast.makeText(LoginPageUsername.this, "Signed in successfully with Google account " + userEmail, Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LoginPageUsername.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else{
                        String userID = auth.getCurrentUser().getUid();
                        String userEmail = auth.getCurrentUser().getEmail();
                        Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Toast.makeText(LoginPageUsername.this, "Signed in with Google account " + userEmail, Toast.LENGTH_LONG).show();
                    }
                } else{
                    Toast.makeText(LoginPageUsername.this, "Authentication Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
