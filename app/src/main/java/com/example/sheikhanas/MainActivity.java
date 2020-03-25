package com.example.sheikhanas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
   private ImageView tabBack;
   private ViewPager viewPager;
   private TabLayout tabLayout;
   private SectionPagerAdapter sectionPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.view_pager);
        tabBack = findViewById(R.id.tab_back);
        tabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(sectionPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_home_tab);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.ic_event_tab);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.schedule);
        Objects.requireNonNull(tabLayout.getTabAt(3)).setIcon(R.drawable.projects);
        Objects.requireNonNull(tabLayout.getTabAt(4)).setIcon(R.drawable.ic_alerts_tab);
        Objects.requireNonNull(tabLayout.getTabAt(5)).setIcon(R.drawable.ic_menu_tab);
    }
}
