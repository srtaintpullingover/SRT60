package com.srt60;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        // Home
        View navHome = findViewById(R.id.navHome);
        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                // Already on Home.
            });
        }

        // Keypad
        View navKeypad = findViewById(R.id.navKeypad);
        if (navKeypad != null) {
            navKeypad.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // History
        View navHistory = findViewById(R.id.navHistory);
        if (navHistory != null) {
            navHistory.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // Profile
        View btnProfile = findViewById(R.id.btnProfile);
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            });
        }

        // Cash balance
        View rowCashBalance = findViewById(R.id.rowCashBalance);
        if (rowCashBalance != null) {
            rowCashBalance.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, BalanceActivity.class);
                startActivity(intent);
            });
        }
    }
}
