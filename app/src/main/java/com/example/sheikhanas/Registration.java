package com.example.sheikhanas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sheikhanas.UserModel.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Registration extends AppCompatActivity {

   private TextInputEditText reg_name, reg_pass, roll_no, contact, email, depart, conf_pass;
   private MaterialButton reg_btn;
   private FirebaseAuth auth;
   private FirebaseDatabase fDatabase;
   private DatabaseReference fReference;
   private ProgressBar progressBar;
   private ImageView regBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        regBack = findViewById(R.id.reg_back);
        reg_name = findViewById(R.id.reg_full_name);
        reg_pass = findViewById(R.id.reg_pass);
        roll_no = findViewById(R.id.reg_roll_no);
        contact = findViewById(R.id.reg_contact);
        email = findViewById(R.id.reg_email);
        depart = findViewById(R.id.reg_department);
        conf_pass = findViewById(R.id.reg_con_pass);
        reg_btn = findViewById(R.id.reg_btn);
        progressBar = findViewById(R.id.reg_progress_circular);
        auth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        fReference = fDatabase.getReference("Users");
        regBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidationCheck();
            }
        });
    }

    private void ValidationCheck() {
        if (reg_name.getText() != null && reg_pass.getText() != null && conf_pass.getText() != null && roll_no.getText() != null && depart.getText() != null && contact.getText() != null && email.getText() != null){
            String userName = reg_name.getText().toString().trim();
            String pass = reg_pass.getText().toString().trim();
            String confPass = conf_pass.getText().toString().trim();
            String regRollNo = roll_no.getText().toString().trim();
            String department = depart.getText().toString().trim();
            String regContact = contact.getText().toString().trim();
            String emailReg = email.getText().toString().trim();
            String uid = null;
            if (TextUtils.isEmpty(userName)) {
                reg_name.setError("Enter User Name");
            } else if (TextUtils.isEmpty(pass)) {
                reg_pass.setError("Enter Password");
            } else if (TextUtils.isEmpty(confPass)) {
                this.conf_pass.setError("Enter Confirm Password");
            } else if (!(pass.equals(confPass))) {
                Toast.makeText(Registration.this, "pass or confirm pass is not matched", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(regRollNo)) {
                roll_no.setError("Enter Roll No");
            } else if (TextUtils.isEmpty(regContact)) {
                contact.setError("Enter contact");
            } else if (!Patterns.PHONE.matcher(regContact).matches()) {
                contact.setError("Enter Valid Phone Number");
            } else if (TextUtils.isEmpty(department)) {
                depart.setError("Enter Department Name");
            } else if (TextUtils.isEmpty(emailReg)) {
                email.setError("Enter Your Email");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailReg).matches()) {
                email.setError("Enter Correct Email");
            } else {
                progressBar.setVisibility(View.VISIBLE);
                InsertDataAuthentication(userName, pass, regRollNo, department, regContact, emailReg, null);
            }
        }
    }

    private void InsertDataAuthentication(final String userName, final String pass, final String regRollNo, final String department, final String regContact, final String emailReg, final String uid) {
        auth.createUserWithEmailAndPassword(emailReg, pass).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Registration.this, "Authentication complete", Toast.LENGTH_SHORT).show();
                    InsertRealTimeDatabase(userName, regRollNo, regContact, department, emailReg);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Registration.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void InsertRealTimeDatabase(String userName, String regRollNo, String regContact, String department, String emailReg) {
        String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        UserModel userModel = new UserModel(userName, regRollNo, regContact, department, emailReg, uid);
        fReference.child(auth.getCurrentUser().getUid()).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Registration.this, "User Register", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Registration.this, Login.class));
                    finish();
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Registration.this, "Register Error? Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
