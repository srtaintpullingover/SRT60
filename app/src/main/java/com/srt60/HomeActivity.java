package com.example.srt60;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Point to your home balance XML layout

        View navHome = findViewById(R.id.homeNavHome);
        View navKeypad = findViewById(R.id.homeNavKeypad);
        View navHistory = findViewById(R.id.homeNavHistory);

        if (navKeypad != null) {
            navKeypad.setOnClickListener(v -> {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finish();
            });
        }

        if (navHistory != null) {
            navHistory.setOnClickListener(v -> {
                startActivity(new Intent(HomeActivity.this, HistoryActivity.class));
                finish();
            });
        }
    }
}
