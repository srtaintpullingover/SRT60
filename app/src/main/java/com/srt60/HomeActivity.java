package com.srt60;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private TextView txtBalance;
    private TextView txtUsdValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtBalance = findViewById(R.id.txtBalance);
        txtUsdValue = findViewById(R.id.txtUsdValue);

        // Set balance to 2976
        txtBalance.setText("2976");
        // Set USD value to $8,300
        txtUsdValue.setText("$8,300");
    }
}
