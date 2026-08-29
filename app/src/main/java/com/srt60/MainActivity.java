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

        txtAmount = findViewById(R.id.tvAmount);

        // Setup number buttons
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

        // Decimal point
        findViewById(R.id.btnDot).setOnClickListener(v -> {
            if (!currentAmount.toString().contains(".")) {
                currentAmount.append(".");
                updateAmount();
            }
        });

        // Backspace
        findViewById(R.id.btnBack).setOnClickListener(v -> {
            if (currentAmount.length() > 1) {
                currentAmount.deleteCharAt(currentAmount.length() - 1);
            } else {
                currentAmount.setLength(0);
                currentAmount.append("0");
            }
            updateAmount();
        });

        // POOL Button
        findViewById(R.id.btnPool).setOnClickListener(v -> {
            double amount = getCurrentAmount();
            if (amount <= 0) {
                Toast.makeText(this, "Enter an amount first", Toast.LENGTH_SHORT).show();
                return;
            }
            
            Intent intent = new Intent(MainActivity.this, PoolActivity.class);
            intent.putExtra("AMOUNT", amount);
            startActivity(intent);
        });

        // REQUEST Button
        findViewById(R.id.btnRequest).setOnClickListener(v -> {
            double amount = getCurrentAmount();
            if (amount <= 0) {
                Toast.makeText(this, "Enter an amount first", Toast.LENGTH_SHORT).show();
                return;
            }
            
            AppData.Transaction transaction = new AppData.Transaction();
            transaction.id = System.currentTimeMillis();
            transaction.person = "Request";
            transaction.amount = amount;
            transaction.type = "REQUEST";
            transaction.timestamp = System.currentTimeMillis();
            AppData.addTransaction(this, transaction);
            
            Toast.makeText(this, "📨 Requested $" + formatAmount(amount), Toast.LENGTH_SHORT).show();
            resetAmount();
        });

        // PAY Button
        findViewById(R.id.btnPay).setOnClickListener(v -> {
            double amount = getCurrentAmount();
            if (amount <= 0) {
                Toast.makeText(this, "Enter an amount first", Toast.LENGTH_SHORT).show();
                return;
            }

            double balance = AppData.getBalance(this);
            if (amount > balance) {
                Toast.makeText(this, "Insufficient balance!", Toast.LENGTH_LONG).show();
                return;
            }

            AppData.setBalance(this, balance - amount);

            AppData.Transaction transaction = new AppData.Transaction();
            transaction.id = System.currentTimeMillis();
            transaction.person = "Payment";
            transaction.amount = amount;
            transaction.type = "PAY";
            transaction.timestamp = System.currentTimeMillis();
            AppData.addTransaction(this, transaction);

            Toast.makeText(this, "✅ Paid $" + formatAmount(amount), Toast.LENGTH_SHORT).show();
            resetAmount();
        });

        // $ Button - Go to Home/Dashboard
        findViewById(R.id.btnHome).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        // ⚙️ Settings Button
        findViewById(R.id.btnSettings).setOnClickListener(v -> {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupNumber(int id) {
        Button button = findViewById(id);
        button.setOnClickListener(v -> {
            String value = button.getText().toString();

            if (currentAmount.toString().equals("0")) {
                currentAmount.setLength(0);
            }

            if (currentAmount.length() < 10) {
                currentAmount.append(value);
                updateAmount();
            }
        });
    }

    private void updateAmount() {
        txtAmount.setText("$" + currentAmount);
    }

    private double getCurrentAmount() {
        try {
            return Double.parseDouble(currentAmount.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String formatAmount(double amount) {
        if (amount == (long) amount) {
            return String.format("%d", (long) amount);
        } else {
            return String.format("%.2f", amount);
        }
    }

    private void resetAmount() {
        currentAmount.setLength(0);
        currentAmount.append("0");
        updateAmount();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
        }
