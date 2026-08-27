package com.srt60;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView txtAmount;
    private StringBuilder currentAmount = new StringBuilder("0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtAmount = findViewById(R.id.txtAmount);

        // Number pad
        int[] numberIds = {
                R.id.btn1, R.id.btn2, R.id.btn3,
                R.id.btn4, R.id.btn5, R.id.btn6,
                R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btn0, R.id.btnDot, R.id.btnBack
        };

        View.OnClickListener numberListener = v -> {
            String text = ((Button) v).getText().toString();

            if (text.equals("<")) {
                if (currentAmount.length() > 1) {
                    currentAmount.deleteCharAt(currentAmount.length() - 1);
                } else {
                    currentAmount.setLength(0);
                    currentAmount.append("0");
                }
            } else {
                if (currentAmount.toString().equals("0") && !text.equals(".")) {
                    currentAmount.setLength(0);
                }
                // Prevent multiple decimals
                if (text.equals(".") && currentAmount.toString().contains(".")) return;
                currentAmount.append(text);
            }
            txtAmount.setText("$" + currentAmount.toString());
        };

        for (int id : numberIds) {
            findViewById(id).setOnClickListener(numberListener);
        }

        // Action buttons
        findViewById(R.id.btnPay).setOnClickListener(v -> {
            Toast.makeText(this, "Pay $" + currentAmount + " (demo)", Toast.LENGTH_SHORT).show();
            // You can launch a confirmation screen here later
        });

        findViewById(R.id.btnPool).setOnClickListener(v ->
                Toast.makeText(this, "Pool $" + currentAmount, Toast.LENGTH_SHORT).show());

        findViewById(R.id.btnRequest).setOnClickListener(v ->
                Toast.makeText(this, "Request $" + currentAmount, Toast.LENGTH_SHORT).show());

        // Bottom nav
        findViewById(R.id.navHome).setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
        findViewById(R.id.navKeypad).setOnClickListener(v -> { /* already here */ });
        findViewById(R.id.navHistory).setOnClickListener(v -> {
            startActivity(new Intent(this, HistoryActivity.class));
            finish();
        });

        // Top profile circle → Profile
        findViewById(R.id.btnProfile).setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
    }
}