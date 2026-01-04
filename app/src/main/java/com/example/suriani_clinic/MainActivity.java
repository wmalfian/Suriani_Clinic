package com.example.suriani_clinic;

import android.content.Intent;
import android.content.SharedPreferences; // Import 1
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.text.SimpleDateFormat; // Import 2
import java.util.Date; // Import 3
import java.util.Locale; // Import 4

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;
    FloatingActionButton btnAddNew;
    ImageButton btnHistory, btnTheme;
    DatabaseHelper myDb; // Add DB Helper

    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_THEME = "isDarkMode";
    private static final String KEY_LAST_DATE = "lastOpenDate"; // New Key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Theme Logic
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = settings.getBoolean(KEY_THEME, false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        myDb = new DatabaseHelper(this); // Initialize DB

        // --- NEW: DAILY RESET LOGIC ---
        checkNewDayReset(settings);
        // ------------------------------

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        btnAddNew = findViewById(R.id.btnAddNew);
        btnHistory = findViewById(R.id.btnViewHistory);
        btnTheme = findViewById(R.id.btnTheme);

        // Theme Button Logic
        if (isDarkMode) btnTheme.setImageResource(android.R.drawable.ic_menu_view);
        else btnTheme.setImageResource(android.R.drawable.ic_menu_view);

        btnTheme.setOnClickListener(v -> {
            SharedPreferences.Editor editor = settings.edit();
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean(KEY_THEME, false);
                Toast.makeText(MainActivity.this, "Light Mode Enabled", Toast.LENGTH_SHORT).show();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean(KEY_THEME, true);
                Toast.makeText(MainActivity.this, "Dark Mode Enabled", Toast.LENGTH_SHORT).show();
            }
            editor.apply();
        });

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

    // --- Helper Method for Daily Reset ---
    private void checkNewDayReset(SharedPreferences settings) {
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String lastDate = settings.getString(KEY_LAST_DATE, "");

        if (!todayDate.equals(lastDate)) {
            // It's a new day! Reset status.
            myDb.resetDailySchedule();

            // Save today as the new last opened date
            settings.edit().putString(KEY_LAST_DATE, todayDate).apply();

            Toast.makeText(this, "New Day! Schedule Reset.", Toast.LENGTH_LONG).show();
        }
    }
}