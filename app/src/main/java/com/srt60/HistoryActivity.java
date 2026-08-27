package com.example.srt60;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        View navHome = findViewById(R.id.histNavHome);
        View navKeypad = findViewById(R.id.histNavKeypad);
        View navHistory = findViewById(R.id.histNavHistory);

        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                startActivity(new Intent(HistoryActivity.this, HomeActivity.class));
                finish();
            });
        }

        if (navKeypad != null) {
            navKeypad.setOnClickListener(v -> {
                startActivity(new Intent(HistoryActivity.this, MainActivity.class));
                finish();
            });
        }
    }
}
