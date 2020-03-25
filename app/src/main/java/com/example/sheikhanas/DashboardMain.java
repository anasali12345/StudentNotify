package com.example.sheikhanas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class DashboardMain extends AppCompatActivity {

    private ImageView dashSchedule;
    private ImageView dashProject;
    private ImageView dashPublication;
    private ImageView dashResearch;
    private ImageView dashBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_main);
        dashBack = findViewById(R.id.dash_back);
        ImageView dashEvents = findViewById(R.id.dash_event_img);
        dashSchedule = findViewById(R.id.dash_schedule_img);
        dashProject = findViewById(R.id.dash_project_img);
        dashPublication = findViewById(R.id.dash_publication_img);
        dashResearch = findViewById(R.id.dash_research_img);
        dashBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dashEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardMain.this, UploadEvents.class));
            }
        });
        dashSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardMain.this, UploadSchedule.class));
            }
        });
        dashProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardMain.this, UploadProjects.class));
            }
        });
        dashPublication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardMain.this, UploadPublication.class));
            }
        });
        dashResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardMain.this, UploadResearch.class));
            }
        });
    }
}