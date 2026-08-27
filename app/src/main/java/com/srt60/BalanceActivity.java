package com.srt60;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BalanceActivity extends AppCompatActivity {

    private TextView txtCashBalance, txtSavings;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        prefs = getSharedPreferences("CashAppClone", MODE_PRIVATE);

        txtCashBalance = findViewById(R.id.txtCashBalance);
        txtSavings = findViewById(R.id.txtSavings);

        // Load saved values (default to the ones in your screenshot)
        String cash = prefs.getString("cash_balance", "0.75");
        String savings = prefs.getString("savings", "5.00");
        txtCashBalance.setText("$" + cash);
        txtSavings.setText("$" + savings);

        // Make Cash Balance editable
        findViewById(R.id.cardCashBalance).setOnClickListener(v -> editAmount("cash_balance", "Edit Cash Balance", txtCashBalance));
        findViewById(R.id.btnEditCash).setOnClickListener(v -> editAmount("cash_balance", "Edit Cash Balance", txtCashBalance));

        // Make Savings editable
        findViewById(R.id.cardSavings).setOnClickListener(v -> editAmount("savings", "Edit Savings", txtSavings));
        findViewById(R.id.btnEditSavings).setOnClickListener(v -> editAmount("savings", "Edit Savings", txtSavings));

        // Bottom navigation
        findViewById(R.id.navHome).setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
        findViewById(R.id.navKeypad).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        findViewById(R.id.navHistory).setOnClickListener(v -> {
            startActivity(new Intent(this, HistoryActivity.class));
            finish();
        });

        // Profile button
        findViewById(R.id.btnProfile).setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
    }

    private void editAmount(String key, String title, TextView target) {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setText(target.getText().toString().replace("$", ""));
        input.setSelectAllOnFocus(true);
        input.setPadding(50, 30, 50, 30);

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage("Enter new amount")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) -> {
                    String value = input.getText().toString().trim();
                    if (value.isEmpty()) value = "0.00";
                    // Keep max 2 decimal places
                    try {
                        double d = Double.parseDouble(value);
                        value = String.format("%.2f", d);
                    } catch (Exception e) {
                        value = "0.00";
                    }
                    prefs.edit().putString(key, value).apply();
                    target.setText("$" + value);
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
