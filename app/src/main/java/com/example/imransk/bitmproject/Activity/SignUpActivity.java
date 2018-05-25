package com.example.imransk.bitmproject.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.imransk.bitmproject.ModelClass.SignUpClass;
import com.example.imransk.bitmproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText name_ET;
    EditText email_ET;
    EditText pass_ET;

    String name;
    String email;
    String pass;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("SignUp Page");
        setContentView(R.layout.activity_sign_up);

        name_ET = findViewById(R.id.name_ET_register);
        email_ET = findViewById(R.id.email_ET_register);
        pass_ET = findViewById(R.id.pass_ET_register);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void Sign_Up_Success(View view) {

        name = name_ET.getText().toString();
        email = email_ET.getText().toString();
        pass = pass_ET.getText().toString();

        if (TextUtils.isEmpty(name)) {
            name_ET.setError("Enter name");
            name_ET.requestFocus();
            return;

        }
        if (TextUtils.isEmpty(email)) {
            email_ET.setError("Enter e-mail");
            email_ET.requestFocus();
            return;

        }
        if (TextUtils.isEmpty(pass)) {
            pass_ET.setError("Enter password");
            pass_ET.requestFocus();
            return;
        }

        ProgressBar progressBar=findViewById(R.id.sign_up_progress);
        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseUser = firebaseAuth.getCurrentUser();

                    String user_id=firebaseUser.getUid().toString();

                    SignUpClass signUpClass = new SignUpClass(name, email, pass,user_id);
                    databaseReference.child("User").child(user_id).setValue(signUpClass);

                    startActivity(new Intent(SignUpActivity.this, Log_in_Activity.class));
                    finish();

                } else {
                    Toast.makeText(SignUpActivity.this, "having some problem", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
