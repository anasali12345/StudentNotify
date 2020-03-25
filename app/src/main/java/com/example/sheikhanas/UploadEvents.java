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

import com.example.sheikhanas.EventModel.EventModel;
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

public class UploadEvents extends AppCompatActivity {

   private ImageView  setImgEvent,eventBack;
   private TextInputEditText editText;
   private MaterialButton uploadEvent, pickImgEvents;
   private Uri fileData;
   private FirebaseStorage fStorage;
   private FirebaseDatabase fDatabase;
   private StorageReference sRef;
   private DatabaseReference dRef;
   private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_events);
        setImgEvent = findViewById(R.id.set_img_events);
        eventBack = findViewById(R.id.event_back);
        pickImgEvents = findViewById(R.id.pick_img_events);
        editText = findViewById(R.id.event_disc);
        uploadEvent = findViewById(R.id.upload_event);
        fileData = null;
        progressBar = findViewById(R.id.Event_progress_circular);
        fDatabase = FirebaseDatabase.getInstance();
        dRef = fDatabase.getReference("Events");
        fStorage = FirebaseStorage.getInstance();
        sRef = fStorage.getReference("EventsImage/"+ UUID.randomUUID().toString());
        eventBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadEvents.this, DashboardMain.class));

            }
        });
        pickImgEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage();
            }
        });
        uploadEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationCheck();
            }
        });
    }

    private void ChooseImage() {
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
            setImgEvent.setImageURI(fileData);
        }
    }

    private void validationCheck() {
        if (editText != null && fileData != null){
            String eventDiscription = Objects.requireNonNull(editText.getText()).toString().trim();

            if (fileData == null){
                Toast.makeText(this, "Please Choose Image", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(eventDiscription)){
                editText.setError("Please Enter Event Discription");
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
                SendImageStorage(eventDiscription, fileData);
            }
        }
    }

    private void SendImageStorage(final String eventDiscription, Uri fileData) {
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
                    Toast.makeText(UploadEvents.this, "Image Url genrated", Toast.LENGTH_SHORT).show();
                    assert downloadUrl != null;
                    InsertRealTimeDataBase(eventDiscription, downloadUrl);
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadEvents.this, "Url Not Download", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void InsertRealTimeDataBase(String eventDiscription, Uri downloadUrl) {
        EventModel eventModel = new EventModel(eventDiscription, downloadUrl.toString());
        dRef.push().setValue(eventModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadEvents.this, "Data Insert In Database", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UploadEvents.this, DashboardMain.class));
                    finish();
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadEvents.this, "Inserted Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
