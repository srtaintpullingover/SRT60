package com.srt60;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private TextView txtBalance;
    private LinearLayout transactionContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        txtBalance = findViewById(R.id.txtBalance);
        transactionContainer = findViewById(R.id.transactionContainer);

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

        findViewById(R.id.btnProfile).setOnClickListener(v ->
                startActivity(
                        new Intent(HomeActivity.this,
                                ProfileActivity.class)
                )
        );

        findViewById(R.id.btnSearch).setOnClickListener(v ->
                startActivity(
                        new Intent(HomeActivity.this,
                                SendActivity.class)
                )
        );

        findViewById(R.id.btnAddMoney).setOnClickListener(v ->
                editBalance()
        );

        findViewById(R.id.rowCashBalance).setOnClickListener(v ->
                editBalance()
        );

        findViewById(R.id.btnSend).setOnClickListener(v ->
                startActivity(
                        new Intent(HomeActivity.this,
                                SendActivity.class)
                )
        );

        findViewById(R.id.btnPool).setOnClickListener(v ->
                startActivity(
                        new Intent(HomeActivity.this,
                                PoolActivity.class)
                )
        );

        findViewById(R.id.navHome).setOnClickListener(v -> {
            // Already here.
        });

        findViewById(R.id.navKeypad).setOnClickListener(v ->
                startActivity(
                        new Intent(HomeActivity.this,
                                MainActivity.class)
                )
        );

        findViewById(R.id.navHistory).setOnClickListener(v ->
                startActivity(
                        new Intent(HomeActivity.this,
                                HistoryActivity.class)
                )
        );
    }

    private void refreshHome() {

        double balance = AppData.getBalance(this);

        txtBalance.setText(
                AppData.formatMoney(balance)
        );

        transactionContainer.removeAllViews();

        ArrayList<AppData.Transaction> transactions =
                AppData.getTransactions(this);

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

        for (int i = transactions.size() - 1; i >= 0; i--) {

            AppData.Transaction transaction =
                    transactions.get(i);

            addTransactionRow(transaction);
        }
    }

    private void addTransactionRow(
            AppData.Transaction transaction
    ) {

        View row = LayoutInflater.from(this)
                .inflate(
                        R.layout.item_transaction,
                        transactionContainer,
                        false
                );

        TextView avatar = row.findViewById(R.id.txtAvatar);
        TextView name = row.findViewById(R.id.txtName);
        TextView detail = row.findViewById(R.id.txtDetail);
        TextView amount = row.findViewById(R.id.txtAmount);

        String initials = getInitials(transaction.person);

        avatar.setText(initials);

        name.setText(transaction.person);

        String type = transaction.type == null
                ? "Payment"
                : transaction.type;

        detail.setText(
                type + " • " +
                        AppData.formatDate(transaction.timestamp)
        );

        boolean outgoing =
                "PAY".equalsIgnoreCase(transaction.type)
                        || "POOL".equalsIgnoreCase(transaction.type);

        if (outgoing) {
            amount.setText(
                    "-" + AppData.formatMoney(transaction.amount)
            );
            amount.setTextColor(0xFFFFFFFF);
        } else {
            amount.setText(
                    "+" + AppData.formatMoney(transaction.amount)
            );
            amount.setTextColor(0xFF00D632);
        }

        row.setOnClickListener(v -> {

            Intent intent = new Intent(
                    HomeActivity.this,
                    TransactionDetailActivity.class
            );

            intent.putExtra(
                    "TRANSACTION_ID",
                    transaction.id
            );

            startActivity(intent);
        });

        transactionContainer.addView(row);
    }

    private String getInitials(String name) {

        if (name == null || name.trim().isEmpty()) {
            return "?";
        }

        String[] pieces = name.trim().split("\\s+");

        if (pieces.length == 1) {
            return pieces[0].substring(0, 1)
                    .toUpperCase(Locale.US);
        }

        return (
                pieces[0].substring(0, 1) +
                pieces[pieces.length - 1]
                        .substring(0, 1)
        ).toUpperCase(Locale.US);
    }

    private void editBalance() {

        EditText input = new EditText(this);

        input.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        input.setSingleLine(true);

        input.setText(
                String.format(
                        Locale.US,
                        "%.2f",
                        AppData.getBalance(this)
                )
        );

        input.selectAll();

        new AlertDialog.Builder(this)
                .setTitle("Edit Cash Balance")
                .setMessage(
                        "Set the demo balance shown on Home."
                )
                .setView(input)
                .setNegativeButton("Cancel", null)
                .setPositiveButton(
                        "Save",
                        (dialog, which) -> {

                            String value =
                                    input.getText()
                                            .toString()
                                            .trim();

                            try {

                                double balance =
                                        Double.parseDouble(value);

                                if (balance < 0) {
                                    throw new NumberFormatException();
                                }

                                AppData.setBalance(
                                        this,
                                        balance
                                );

                                refreshHome();

                                Toast.makeText(
                                        this,
                                        "Balance updated",
                                        Toast.LENGTH_SHORT
                                ).show();

                            } catch (Exception e) {

                                Toast.makeText(
                                        this,
                                        "Enter a valid amount",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                )
                .show();
    }
    }
