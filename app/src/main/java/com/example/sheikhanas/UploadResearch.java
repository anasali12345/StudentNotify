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

import com.example.sheikhanas.ResearchModel.ResearchModel;
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

public class UploadResearch extends AppCompatActivity {

    private ImageView setImgResearch, researchBack;
    private TextInputEditText editText;
    private MaterialButton uploadResearch, chooseImg;
    private Uri fileData;
    private FirebaseStorage fStorage;
    private StorageReference sRef;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dRef;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_research);
        researchBack = findViewById(R.id.research_back);
        setImgResearch = findViewById(R.id.set_img_research);
        chooseImg = findViewById(R.id.pick_img_research);
        editText = findViewById(R.id.research_disc);
        uploadResearch = findViewById(R.id.upload_research);
        fileData = null;
        progressBar = findViewById(R.id.research_progress_circular);
        fDatabase = FirebaseDatabase.getInstance();
        dRef = fDatabase.getReference("Researches");
        fStorage = FirebaseStorage.getInstance();
        sRef = fStorage.getReference("ResearchImages/"+ UUID.randomUUID());
        researchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadResearch.this, DashboardMain.class));

            }
        });
        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImage();
            }
        });
        uploadResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidationCheck();
            }
        });

    }

    private void PickImage() {
        Intent pickImage = new Intent();
        pickImage.setType("image/*");
        pickImage.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pickImage, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            assert data != null;
            fileData = data.getData();
            setImgResearch.setImageURI(fileData);
        }
    }

    private void ValidationCheck() {
        if (fileData != null && editText != null){
            String researchDisc = Objects.requireNonNull(editText.getText()).toString();
            if (fileData == null){
                Toast.makeText(this, "Pleasse choose Image", Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(researchDisc)){
                editText.setError("Please Enter Disc");
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
                SendImageStorage(researchDisc, fileData);
            }
        }
    }

    private void SendImageStorage(final String researchDisc, Uri fileData) {
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
                    Toast.makeText(UploadResearch.this, "Url Are Genrated", Toast.LENGTH_SHORT).show();

                    assert downloadUrl != null;
                    InsertRealTimeDataBase(researchDisc, downloadUrl);
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadResearch.this, "Image Url Downloaded Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void InsertRealTimeDataBase(String researchDisc, Uri downloadUrl) {
        ResearchModel researchModel = new ResearchModel(researchDisc, downloadUrl.toString());
        dRef.push().setValue(researchModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadResearch.this, "Data Is Uploaded", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UploadResearch.this, DashboardMain.class));
                    finish();
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadResearch.this, "Data Uploaded Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
