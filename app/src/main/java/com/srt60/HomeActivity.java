package com.srt60;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private TextView txtBalance;
    private TextView txtUsdValue;
    private ImageView imgProfile;
    private LinearLayout transactionContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtBalance = findViewById(R.id.txtBalance);
        txtUsdValue = findViewById(R.id.txtUsdValue);
        imgProfile = findViewById(R.id.imgProfile);
        transactionContainer = findViewById(R.id.transactionContainer);

        // Profile image click - goes to ProfileActivity
        imgProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        setupButtons();
        refreshHome();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (txtBalance != null) {
            refreshHome();
        }
    }

    private void setupButtons() {
        // Action buttons
        findViewById(R.id.btnAddMoney).setOnClickListener(v -> editBalance());
        findViewById(R.id.btnWithdraw).setOnClickListener(v -> withdrawMoney());
        findViewById(R.id.btnCashApp).setOnClickListener(v -> cashAppTransfer());
        findViewById(R.id.btnEarnStatus).setOnClickListener(v -> earnStatus());

        // Bottom navigation
        findViewById(R.id.navHome).setOnClickListener(v -> {
            // Already here
        });

        findViewById(R.id.navKeypad).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.navHistory).setOnClickListener(v -> 
            startActivity(new Intent(HomeActivity.this, HistoryActivity.class))
        );

        // Cash App Mobile promo click
        findViewById(R.id.rowCashAppMobile).setOnClickListener(v -> {
            Toast.makeText(this, "Cash App Mobile - $2 months interest", Toast.LENGTH_SHORT).show();
        });
    }

    private void refreshHome() {
        double balance = AppData.getBalance(this);

        // Display balance
        txtBalance.setText(String.format(Locale.US, "%,d", (long) balance));

        // Display USD value ($8,300)
        if (txtUsdValue != null) {
            double usdValue = balance * 2.789;
            txtUsdValue.setText(String.format(Locale.US, "$%,.2f", usdValue));
        }

        // Load transactions
        transactionContainer.removeAllViews();
        ArrayList<AppData.Transaction> transactions = AppData.getTransactions(this);

        if (transactions.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No transactions yet");
            empty.setTextColor(0xFF888888);
            empty.setTextSize(16);
            empty.setGravity(View.TEXT_ALIGNMENT_CENTER);
            empty.setPadding(0, 60, 0, 60);
            transactionContainer.addView(empty);
            return;
        }

        // Show last 5 transactions
        int start = Math.max(0, transactions.size() - 5);
        for (int i = transactions.size() - 1; i >= start; i--) {
            addTransactionRow(transactions.get(i));
        }
    }

    private void addTransactionRow(AppData.Transaction transaction) {
        View row = LayoutInflater.from(this)
                .inflate(R.layout.item_transaction, transactionContainer, false);

        TextView avatar = row.findViewById(R.id.txtAvatar);
        TextView name = row.findViewById(R.id.txtName);
        TextView detail = row.findViewById(R.id.txtDetail);
        TextView amount = row.findViewById(R.id.txtAmount);

        avatar.setText(getInitials(transaction.person));
        name.setText(transaction.person);
        detail.setText(transaction.type + " • " + AppData.formatDate(transaction.timestamp));

        boolean outgoing = "PAY".equalsIgnoreCase(transaction.type) || 
                          "POOL".equalsIgnoreCase(transaction.type);

        if (outgoing) {
            amount.setText("-" + AppData.formatMoney(transaction.amount));
            amount.setTextColor(0xFFFFFFFF);
        } else {
            amount.setText("+" + AppData.formatMoney(transaction.amount));
            amount.setTextColor(0xFF00D632);
        }

        transactionContainer.addView(row);
    }

    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "?";
        String[] pieces = name.trim().split("\\s+");
        if (pieces.length == 1) {
            return pieces[0].substring(0, 1).toUpperCase(Locale.US);
        }
        return (pieces[0].substring(0, 1) + pieces[pieces.length - 1].substring(0, 1))
                .toUpperCase(Locale.US);
    }

    private void editBalance() {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setSingleLine(true);
        input.setText(String.format(Locale.US, "%.2f", AppData.getBalance(this)));
        input.selectAll();

        new AlertDialog.Builder(this)
                .setTitle("Edit Cash Balance")
                .setView(input)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialog, which) -> {
                    try {
                        double balance = Double.parseDouble(input.getText().toString().trim());
                        if (balance < 0) throw new NumberFormatException();
                        AppData.setBalance(this, balance);
                        refreshHome();
                        Toast.makeText(this, "Balance updated", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void withdrawMoney() {
        double balance = AppData.getBalance(this);
        if (balance <= 0) {
            Toast.makeText(this, "No funds to withdraw", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setSingleLine(true);
        input.setText(String.format(Locale.US, "%.2f", balance));
        input.selectAll();

        new AlertDialog.Builder(this)
                .setTitle("Withdraw Money")
                .setView(input)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Withdraw", (dialog, which) -> {
                    try {
                        double amount = Double.parseDouble(input.getText().toString().trim());
                        if (amount <= 0 || amount > balance) {
                            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        AppData.setBalance(this, balance - amount);
                        
                        AppData.Transaction transaction = new AppData.Transaction();
                        transaction.id = System.currentTimeMillis();
                        transaction.person = "Withdrawal";
                        transaction.amount = amount;
                        transaction.type = "WITHDRAW";
                        transaction.timestamp = System.currentTimeMillis();
                        AppData.addTransaction(this, transaction);
                        
                        refreshHome();
                        Toast.makeText(this, "Withdrew $" + String.format(Locale.US, "%.2f", amount), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void cashAppTransfer() {
        Toast.makeText(this, "Cash App Transfer", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(HomeActivity.this, SendActivity.class));
    }

    private void earnStatus() {
        int points = (int) (Math.random() * 50) + 10;
        Toast.makeText(this, "🎉 Earned " + points + " status points!", Toast.LENGTH_SHORT).show();
        
        AppData.Transaction transaction = new AppData.Transaction();
        transaction.id = System.currentTimeMillis();
        transaction.person = "Status Points";
        transaction.amount = points;
        transaction.type = "STATUS";
        transaction.timestamp = System.currentTimeMillis();
        AppData.addTransaction(this, transaction);
        refreshHome();
    }
}
