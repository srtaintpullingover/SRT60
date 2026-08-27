package com.srt60;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BalanceActivity extends AppCompatActivity {

    private TextView txtCashBalance;
    private TextView txtSavings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_balance);

        txtCashBalance =
                findViewById(R.id.txtCashBalance);

        txtSavings =
                findViewById(R.id.txtSavings);

        refresh();

        findViewById(R.id.cardCashBalance)
                .setOnClickListener(
                        v -> editCashBalance()
                );

        findViewById(R.id.btnEditCash)
                .setOnClickListener(
                        v -> editCashBalance()
                );

        findViewById(R.id.cardSavings)
                .setOnClickListener(
                        v -> editSavings()
                );

        findViewById(R.id.btnEditSavings)
                .setOnClickListener(
                        v -> editSavings()
                );

        findViewById(R.id.btnProfile)
                .setOnClickListener(
                        v -> startActivity(
                                new Intent(
                                        this,
                                        ProfileActivity.class
                                )
                        )
                );
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {

        txtCashBalance.setText(
                TransactionStore.money(
                        TransactionStore
                                .getCashBalance(this)
                )
        );

        txtSavings.setText(
                TransactionStore.money(
                        TransactionStore
                                .getSavings(this)
                )
        );
    }

    private void editCashBalance() {

        showAmountEditor(
                "Edit Cash Balance",
                TransactionStore.getCashBalance(this),
                cents -> {

                    TransactionStore
                            .setCashBalance(
                                    this,
                                    cents
                            );

                    refresh();

                    Toast.makeText(
                            this,
                            "Cash balance updated",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );
    }

    private void editSavings() {

        showAmountEditor(
                "Edit Savings",
                TransactionStore.getSavings(this),
                cents -> {

                    TransactionStore
                            .setSavings(
                                    this,
                                    cents
                            );

                    refresh();

                    Toast.makeText(
                            this,
                            "Savings updated",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );
    }

    private interface AmountCallback {
        void onSaved(long cents);
    }

    private void showAmountEditor(
            String title,
            long currentCents,
            AmountCallback callback
    ) {

        EditText input =
                new EditText(this);

        input.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        input.setText(
                String.format(
                        java.util.Locale.US,
                        "%.2f",
                        currentCents / 100.0
                )
        );

        input.setSelectAllOnFocus(true);

        input.setPadding(
                40,
                20,
                40,
                20
        );

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(
                        "Enter the new amount"
                )
                .setView(input)
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .setPositiveButton(
                        "Save",
                        (dialog, which) -> {

                            try {

                                double amount =
                                        Double.parseDouble(
                                                input.getText()
                                                        .toString()
                                                        .trim()
                                        );

                                if (amount < 0) {
                                    throw new Exception();
                                }

                                long cents =
                                        Math.round(
                                                amount * 100
                                        );

                                callback.onSaved(
                                        cents
                                );

                            } catch (Exception e) {

                                Toast.makeText(
                                        this,
                                        "Enter a valid amount.",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                )
                .show();
    }
}
