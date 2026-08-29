package com.srt60;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PoolActivity extends AppCompatActivity {

    private LinearLayout poolContainer;
    private double poolAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool);

        poolContainer = findViewById(R.id.poolContainer);
        
        // Get amount from intent
        poolAmount = getIntent().getDoubleExtra("AMOUNT", 0);

        setupButtons();
        loadPools();
    }

    private void setupButtons() {
        findViewById(R.id.btnCreatePool).setOnClickListener(v -> createPoolDialog());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        findViewById(R.id.navHome).setOnClickListener(v -> {
            Intent intent = new Intent(PoolActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.navKeypad).setOnClickListener(v -> {
            Intent intent = new Intent(PoolActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.navHistory).setOnClickListener(v -> 
            startActivity(new Intent(PoolActivity.this, HistoryActivity.class))
        );
    }

    private void loadPools() {
        poolContainer.removeAllViews();
        ArrayList<AppData.Pool> pools = AppData.getPools(this);

        if (pools.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No pools yet.\nCreate one by tapping the + button");
            empty.setTextColor(0xFF888888);
            empty.setTextSize(16);
            empty.setGravity(View.TEXT_ALIGNMENT_CENTER);
            empty.setPadding(0, 60, 0, 60);
            poolContainer.addView(empty);
            return;
        }

        for (AppData.Pool pool : pools) {
            addPoolRow(pool);
        }
    }

    private void addPoolRow(AppData.Pool pool) {
        View row = LayoutInflater.from(this)
                .inflate(R.layout.item_pool, poolContainer, false);

        TextView name = row.findViewById(R.id.poolName);
        TextView description = row.findViewById(R.id.poolDescription);
        TextView amount = row.findViewById(R.id.poolAmount);
        TextView members = row.findViewById(R.id.poolMembers);

        name.setText(pool.name);
        description.setText(pool.description != null && !pool.description.isEmpty() ? 
            pool.description : "No description");
        amount.setText(AppData.formatMoney(pool.balance) + " / " + AppData.formatMoney(pool.goal));
        members.setText(pool.members.size() + " members");

        row.setOnClickListener(v -> {
            Intent intent = new Intent(PoolActivity.this, PoolMemberActivity.class);
            intent.putExtra("POOL_ID", pool.id);
            startActivity(intent);
        });

        poolContainer.addView(row);
    }

    private void createPoolDialog() {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_create_pool, null);

        EditText poolName = dialogView.findViewById(R.id.etPoolName);
        EditText poolDescription = dialogView.findViewById(R.id.etPoolDescription);
        EditText poolAmountInput = dialogView.findViewById(R.id.etPoolAmount);

        if (poolAmount > 0) {
            poolAmountInput.setText(String.valueOf((int) poolAmount));
            poolAmountInput.setEnabled(false);
        }

        new AlertDialog.Builder(this)
                .setTitle("Create New Pool")
                .setView(dialogView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Create", (dialog, which) -> {
                    String name = poolName.getText().toString().trim();
                    String description = poolDescription.getText().toString().trim();
                    String amountStr = poolAmountInput.getText().toString().trim();

                    if (name.isEmpty() || amountStr.isEmpty()) {
                        Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        double amount = Double.parseDouble(amountStr);
                        if (amount <= 0) {
                            Toast.makeText(this, "Amount must be greater than 0", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String currentUser = AppData.getUsername(this);
                        
                        // Create pool with name, description, amount, creator
                        AppData.Pool pool = new AppData.Pool(name, description, amount, currentUser);
                        AppData.createPool(this, pool);
                        
                        Toast.makeText(this, "Pool created successfully!", Toast.LENGTH_SHORT).show();
                        loadPools();

                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
}
