package com.example.sheikhanas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {

   private TextInputEditText log_email, password;
   private MaterialButton btn;
   private MaterialTextView goToReg;
   private ProgressBar progressBar;
   private FirebaseAuth auth;
   private FirebaseDatabase fDatabase;
   private DatabaseReference dRef;
   private String user = null;
   private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        back = findViewById(R.id.log_back);
        log_email = findViewById(R.id.log_user);
        password = findViewById(R.id.log_pass);
        btn = findViewById(R.id.log_btn);
        goToReg = findViewById(R.id.go_to_Reg);
        auth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        dRef = fDatabase.getReference("Users");
        progressBar = findViewById(R.id.progress_circular);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        goToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registration.class));
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationCheck();
            }
        });
    }

    private void validationCheck() {
        if (log_email.getText() != null && password.getText() != null) {
            String logEmail = log_email.getText().toString().trim();
            String logPassword = password.getText().toString().trim();
            if (TextUtils.isEmpty(logEmail)) {
                log_email.setError("Please Enter Email");

            } else if (!Patterns.EMAIL_ADDRESS.matcher(logEmail).matches()) {
                log_email.setError("Please Enter Correct Email");
            } else if (TextUtils.isEmpty(logPassword)) {
                password.setError("please Enter Passeord");
            } else {
                Authentication(logEmail, logPassword);
            }
        }
    }

    private void Authentication(final String logEmail, final String logPassword) {
        progressBar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(logEmail, logPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                    dRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String userRole = String.valueOf(dataSnapshot.child(user).child("Role").getValue());

                            Log.d("student", userRole);

                            if (userRole.equals("Admin")) {
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(Login.this, DashboardMain.class));
                                finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(Login.this, MainActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Login.this, "Get User Role Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Email And Password is Incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}