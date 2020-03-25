package com.example.sheikhanas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sheikhanas.ProjectModel.ProjectModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.UUID;

public class UploadProjects extends AppCompatActivity {
    private ImageView  setImgProjects, projectBack;
    private TextInputEditText editText;
    private MaterialButton uploadProject, chooseImage;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dRef;
    private FirebaseStorage fStorage;
    private StorageReference sRef;
    private Uri fileData;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_projects);
        setImgProjects = findViewById(R.id.set_img_projects);
        projectBack = findViewById(R.id.project_back);
        chooseImage = findViewById(R.id.pick_img_projects);
        editText = findViewById(R.id.project_disc);
        uploadProject = findViewById(R.id.upload_Projects);
        progressBar = findViewById(R.id.project_progress_circular);
        fDatabase = FirebaseDatabase.getInstance();
        dRef = fDatabase.getReference("Projects");
        fStorage = FirebaseStorage.getInstance();
        sRef = fStorage.getReference("ProjectImages/"+ UUID.randomUUID());
        fileData = null;
        projectBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadProjects.this, DashboardMain.class));

            }
        });
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoosePick();
            }
        });
        uploadProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckValidation();
            }
        });
    }

    private void CheckValidation() {
        if (editText != null && fileData != null){
            String projectDiscription = Objects.requireNonNull(editText.getText()).toString().trim();
            if (fileData == null){
                Toast.makeText(this, "Please choose Image", Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(projectDiscription)){
                editText.setError("Please Enter Project Discription");
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
                SendImageStorage(projectDiscription, fileData);
            }
        }
    }

    private void SendImageStorage(final String projectDiscription, Uri fileData) {
        sRef.putFile(fileData).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw Objects.requireNonNull(task.getException());
                }
                return sRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri downloadUrl = task.getResult();
                    Toast.makeText(UploadProjects.this, "Url Are Genrated", Toast.LENGTH_SHORT).show();
                    assert downloadUrl != null;
                    InsertRealTimeDatabase(projectDiscription, downloadUrl);
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadProjects.this, "Image Url downloaded Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void InsertRealTimeDatabase(String projectDiscription, Uri downloadUrl) {
        final ProjectModel projectModel = new ProjectModel(projectDiscription, downloadUrl.toString());
        dRef.push().setValue(projectModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadProjects.this, "Data Uploaded", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UploadProjects.this, DashboardMain.class));
                    finish();
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadProjects.this, "Uploaded Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ChoosePick() {
        Intent imagePick = new Intent();
        imagePick.setType("image/*");
        imagePick.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(imagePick , 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            assert data != null;
            fileData = data.getData();
            setImgProjects.setImageURI(fileData);
        }
    }
}
