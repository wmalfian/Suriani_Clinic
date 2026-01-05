package com.example.suriani_clinic;

import android.content.Intent;
import android.content.SharedPreferences; // Import for using SharedPreferences to save settings
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.text.SimpleDateFormat; // Import for formatting dates
import java.util.Date; // Import for getting the current date
import java.util.Locale; // Import for specifying the locale for date formatting

// The main activity of the application, which hosts the main user interface.
public class MainActivity extends AppCompatActivity {

    // UI elements
    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;
    FloatingActionButton btnAddNew;
    ImageButton btnHistory, btnTheme;
    // Database helper to interact with the database
    DatabaseHelper myDb;

    // Constants for SharedPreferences
    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_THEME = "isDarkMode";
    private static final String KEY_LAST_DATE = "lastOpenDate"; // New Key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Theme Logic
        // Get the SharedPreferences object for this application.
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        // Retrieve the saved theme setting, defaulting to light mode (false).
        boolean isDarkMode = settings.getBoolean(KEY_THEME, false);
        // Apply the selected theme.
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Set the content view to the main activity layout.
        setContentView(R.layout.activity_main);

        // Hide the action bar if it exists.
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        // Initialize the database helper.
        myDb = new DatabaseHelper(this);

        // --- NEW: DAILY RESET LOGIC ---
        // Check if the date has changed since the last launch and reset the schedule if needed.
        checkNewDayReset(settings);
        // ------------------------------

        // Initialize the UI elements from the layout.
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        btnAddNew = findViewById(R.id.btnAddNew);
        btnHistory = findViewById(R.id.btnViewHistory);
        btnTheme = findViewById(R.id.btnTheme);

        // Theme Button Logic
        // Set the icon for the theme button (currently the same for both light and dark mode).
        if (isDarkMode) btnTheme.setImageResource(android.R.drawable.ic_menu_view);
        else btnTheme.setImageResource(android.R.drawable.ic_menu_view);

        // Set a click listener for the theme button to toggle between light and dark modes.
        btnTheme.setOnClickListener(v -> {
            SharedPreferences.Editor editor = settings.edit();
            if (isDarkMode) {
                // Switch to light mode.
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean(KEY_THEME, false);
                Toast.makeText(MainActivity.this, "Light Mode Enabled", Toast.LENGTH_SHORT).show();
            } else {
                // Switch to dark mode.
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean(KEY_THEME, true);
                Toast.makeText(MainActivity.this, "Dark Mode Enabled", Toast.LENGTH_SHORT).show();
            }
            // Save the new theme setting.
            editor.apply();
        });

        // Initialize the ViewPager adapter and set it to the ViewPager.
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        // Connect the TabLayout to the ViewPager and set the tab titles and icons.
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Home");
                tab.setIcon(android.R.drawable.ic_menu_day);
            } else {
                tab.setText("Manage");
                tab.setIcon(android.R.drawable.ic_menu_edit);
            }
        }).attach();

        // Set a click listener for the "Add New" button to open the AddMedicationActivity.
        btnAddNew.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddMedicationActivity.class);
            startActivity(intent);
        });

        // Set a click listener for the "History" button to open the HistoryActivity.
        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    // --- Helper Method for Daily Reset ---
    // This method checks if a new day has started and resets the daily medication schedule if so.
    private void checkNewDayReset(SharedPreferences settings) {
        // Get the current date in the format "yyyy-MM-dd".
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        // Get the last date the app was opened from SharedPreferences.
        String lastDate = settings.getString(KEY_LAST_DATE, "");

        // If the current date is different from the last saved date, it's a new day.
        if (!todayDate.equals(lastDate)) {
            // It's a new day! Reset the status of all medications to "Pending".
            myDb.resetDailySchedule();

            // Save today as the new last opened date.
            settings.edit().putString(KEY_LAST_DATE, todayDate).apply();

            // Inform the user that the schedule has been reset.
            Toast.makeText(this, "New Day! Schedule Reset.", Toast.LENGTH_LONG).show();
        }
    }
}
