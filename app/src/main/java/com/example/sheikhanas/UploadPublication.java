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

import com.example.sheikhanas.PublicationModel.PublicationModel;
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

public class UploadPublication extends AppCompatActivity {

    private ImageView setImagePublication, publicationBack;
    private TextInputEditText editText;
    private MaterialButton uploadPublication, chooseImage;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dRef;
    private FirebaseStorage fStorage;
    private StorageReference sRef;
    private Uri fileData;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_publication);
        setImagePublication = findViewById(R.id.set_img_publication);
        publicationBack = findViewById(R.id.publication_back);
        chooseImage = findViewById(R.id.pick_img_publication);
        editText = findViewById(R.id.publication_disc);
        uploadPublication = findViewById(R.id.upload_publication);
        progressBar = findViewById(R.id.publication_progress_circular);
        fDatabase = FirebaseDatabase.getInstance();
        dRef = fDatabase.getReference("Publication");
        fStorage = FirebaseStorage.getInstance();
        sRef = fStorage.getReference("PublicationImage/"+ UUID.randomUUID());
        publicationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadPublication.this, DashboardMain.class));

            }
        });
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoosePick();
            }
        });
        uploadPublication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckValidation();
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
            setImagePublication.setImageURI(fileData);
        }
    }

    private void CheckValidation() {
        if (editText != null && fileData != null){
            String  publicationDisc = Objects.requireNonNull(editText.getText()).toString().trim();
            if (fileData == null){
                Toast.makeText(this, "Please Choose Image", Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(publicationDisc)){
                editText.setError("Please Enter Publication Discription");
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
                SendImageStorage(publicationDisc, fileData);
            }
        }
    }

    private void SendImageStorage(final String publicationDisc, Uri fileData) {
        sRef.putFile(fileData).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful())
                {
                    throw Objects.requireNonNull(task.getException());
                }
                return sRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri downloadUrl = task.getResult();
                    Toast.makeText(UploadPublication.this, "Url Are Genrated", Toast.LENGTH_SHORT).show();
                    assert downloadUrl != null;
                    InsertRealTimeDatabase(publicationDisc, downloadUrl);
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadPublication.this, "Image Download Url Error", Toast.LENGTH_SHORT).show();        
                }
            }
        });
    }

    private void InsertRealTimeDatabase(String publicationDisc, Uri downloadUrl) {
        PublicationModel publicationModel = new PublicationModel(publicationDisc, downloadUrl.toString());
        dRef.push().setValue(publicationModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadPublication.this, "Data Will Uploaded", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UploadPublication.this, DashboardMain.class));
                    finish();
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadPublication.this, "Data Uploaded Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
