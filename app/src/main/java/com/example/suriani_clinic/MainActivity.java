package com.example.suriani_clinic;

import android.content.Intent;
import android.content.SharedPreferences; // Import 1
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate; // Import 2
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;
    FloatingActionButton btnAddNew;
    ImageButton btnHistory, btnTheme; // Added btnTheme

    // Key for saving preference
    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_THEME = "isDarkMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. LOAD SAVED THEME (Must be before setContentView)
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = settings.getBoolean(KEY_THEME, false);

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        btnAddNew = findViewById(R.id.btnAddNew);
        btnHistory = findViewById(R.id.btnViewHistory);
        btnTheme = findViewById(R.id.btnTheme); // Initialize

        // 2. THEME BUTTON LOGIC
        btnTheme.setOnClickListener(v -> {
            SharedPreferences.Editor editor = settings.edit();
            if (isDarkMode) {
                // Switch to Light
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean(KEY_THEME, false);
            } else {
                // Switch to Dark
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean(KEY_THEME, true);
            }
            editor.apply();
            // Note: The activity will automatically recreate itself to apply changes
        });

        // Setup ViewPager
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Home");
                tab.setIcon(android.R.drawable.ic_menu_day);
            } else {
                tab.setText("Manage");
                tab.setIcon(android.R.drawable.ic_menu_edit);
            }
        }).attach();

        btnAddNew.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddMedicationActivity.class);
            startActivity(intent);
        });

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }
}