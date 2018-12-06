package com.example.imransk.bitmproject.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.imransk.bitmproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Log_in_Activity extends AppCompatActivity {

    EditText email_ET;
    EditText pass_ET;
    Button login_btn;


    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    String email;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Login Page");
        setContentView(R.layout.activity_log_in);
        email_ET = findViewById(R.id.email_ET_login);
        pass_ET = findViewById(R.id.pass_ET_login);
        login_btn = findViewById(R.id.login_Btn);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = email_ET.getText().toString();
                password = pass_ET.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    //animation
                    YoYo.with(Techniques.Shake).duration(1000).repeat(0).playOn(email_ET);

                    email_ET.setError("Enter E-mail");
                    email_ET.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    //animation

                    YoYo.with(Techniques.Shake).duration(1000).repeat(0).playOn(pass_ET);

                    pass_ET.setError("Enter Password");
                    pass_ET.requestFocus();

                    return;
                }
                final ProgressBar progressBar = findViewById(R.id.log_in_progress);
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            startActivity(new Intent(Log_in_Activity.this, MainActivity.class));
                            progressBar.setVisibility(View.GONE);
                            finish();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            YoYo.with(Techniques.Shake).duration(1000).repeat(0).playOn(email_ET);
                            YoYo.with(Techniques.Shake).duration(1000).repeat(0).playOn(pass_ET);

                            Toast.makeText(Log_in_Activity.this, "having Problem", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }


    public void go_Register(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
