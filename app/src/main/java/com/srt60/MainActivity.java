package com.srt60;

import android.os.Bundle;
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

        txtAmount = findViewById(R.id.tvAmount);

        setupNumber(R.id.btn0);
        setupNumber(R.id.btn1);
        setupNumber(R.id.btn2);
        setupNumber(R.id.btn3);
        setupNumber(R.id.btn4);
        setupNumber(R.id.btn5);
        setupNumber(R.id.btn6);
        setupNumber(R.id.btn7);
        setupNumber(R.id.btn8);
        setupNumber(R.id.btn9);

        findViewById(R.id.btnDot).setOnClickListener(v -> {
            if (!currentAmount.toString().contains(".")) {
                currentAmount.append(".");
                updateAmount();
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            if (currentAmount.length() > 1) {
                currentAmount.deleteCharAt(currentAmount.length() - 1);
            } else {
                currentAmount.setLength(0);
                currentAmount.append("0");
            }
            updateAmount();
        });

        findViewById(R.id.btnPay).setOnClickListener(v -> {
            Toast.makeText(this, "Pay $" + currentAmount, Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnRequest).setOnClickListener(v -> {
            Toast.makeText(this, "Request $" + currentAmount, Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnPool).setOnClickListener(v -> {
            Toast.makeText(this, "Pool $" + currentAmount, Toast.LENGTH_SHORT).show();
        });
    }

    private void setupNumber(int id) {
        Button button = findViewById(id);
        button.setOnClickListener(v -> {
            String value = button.getText().toString();

            if (currentAmount.toString().equals("0")) {
                currentAmount.setLength(0);
            }

            currentAmount.append(value);
            updateAmount();
        });
    }

    private void updateAmount() {
        txtAmount.setText("$" + currentAmount);
    }
}
