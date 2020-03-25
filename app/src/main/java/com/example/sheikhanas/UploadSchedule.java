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

import com.example.sheikhanas.ScheduleModel.ScheduleModel;
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

public class UploadSchedule extends AppCompatActivity {

    private ImageView setFileSchedule, scheduleBack;
    private TextInputEditText editText;
    private MaterialButton uploadFile, chooseFile;
    private Uri fileData;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dRef;
    private FirebaseStorage fStorage;
    private StorageReference sRef;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_schedule);
        setFileSchedule = findViewById(R.id.set_file_schedule);
        scheduleBack = findViewById(R.id.schedule_back);
        chooseFile = findViewById(R.id.pick_file_schedule);
        editText = findViewById(R.id.schedule_disc);
        uploadFile = findViewById(R.id.upload_schedule);
        progressBar = findViewById(R.id.schedule_progress_circular);
        fDatabase = FirebaseDatabase.getInstance();
        dRef = fDatabase.getReference("Schedule");
        fStorage = FirebaseStorage.getInstance();
        sRef = fStorage.getReference("ScheduleImages/"+ UUID.randomUUID());
        fileData = null;
        scheduleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseFile();
            }
        });
        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidationCheck();
            }
        });
    }

    private void ChooseFile() {
        Intent imagePick = new Intent();
        imagePick.setType("image/*");
        imagePick.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(imagePick, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            assert data != null;
            fileData = data.getData();
            setFileSchedule.setImageURI(fileData);
        }
    }

    private void ValidationCheck() {
        if (editText != null && fileData !=null){
            String scheduleDisc = Objects.requireNonNull(editText.getText()).toString().trim();
            if (fileData == null){
                Toast.makeText(this, "Please Choose File", Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(scheduleDisc)){
                editText.setError("Please Enter schedule Discription");
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
                SendFileStorage(scheduleDisc, fileData);
            }
        }
    }

    private void SendFileStorage(final String scheduleDisc, Uri fileData) {
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
                    Toast.makeText(UploadSchedule.this, "Url Are Genrated", Toast.LENGTH_SHORT).show();
                    assert downloadUrl != null;
                    InsertRealTimeDatabase(scheduleDisc, downloadUrl);
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadSchedule.this, "Download File Url Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void InsertRealTimeDatabase(String scheduleDisc, Uri downloadUrl) {
        ScheduleModel scheduleModel = new ScheduleModel(scheduleDisc, downloadUrl.toString());
        dRef.push().setValue(scheduleModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadSchedule.this, "Data Are Upload", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UploadSchedule.this, DashboardMain.class));
                    finish();
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadSchedule.this, "Data Uploaded Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
