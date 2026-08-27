package com.srt60;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    private LinearLayout historyContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);

        historyContainer =
                findViewById(
                        R.id.historyContainer
                );

        setupNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadHistory();
    }

    private void loadHistory() {

        historyContainer.removeAllViews();

        List<TransactionStore.Transaction>
                transactions =
                TransactionStore
                        .getTransactions(this);

        if (transactions.isEmpty()) {

            TextView empty =
                    new TextView(this);

            empty.setText(
                    "No transactions yet.\n\n" +
                            "Payments, requests, and pools " +
                            "will appear here."
            );

            empty.setGravity(
                    Gravity.CENTER
            );

            empty.setTextColor(
                    Color.GRAY
            );

            empty.setTextSize(16);

            empty.setPadding(
                    20,
                    80,
                    20,
                    80
            );

            historyContainer.addView(
                    empty
            );

            return;
        }

        for (
                TransactionStore.Transaction transaction
                : transactions
        ) {

            historyContainer.addView(
                    createHistoryRow(
                            transaction
                    )
            );
        }
    }

    private View createHistoryRow(
            TransactionStore.Transaction transaction
    ) {

        LinearLayout row =
                new LinearLayout(this);

        row.setOrientation(
                LinearLayout.HORIZONTAL
        );

        row.setGravity(
                Gravity.CENTER_VERTICAL
        );

        row.setPadding(
                0,
                14,
                0,
                14
        );

        TextView icon =
                new TextView(this);

        icon.setGravity(
                Gravity.CENTER
        );

        icon.setTextSize(18);

        icon.setTextColor(
                Color.WHITE
        );

        if (transaction.type.equals("PAY")) {
            icon.setText("↑");
        } else if (
                transaction.type.equals("REQUEST")
        ) {
            icon.setText("↓");
        } else {
            icon.setText("$");
        }

        android.graphics.drawable.GradientDrawable
                background =
                new android.graphics.drawable
                        .GradientDrawable();

        background.setShape(
                android.graphics.drawable
                        .GradientDrawable.OVAL
        );

        background.setColor(
                Color.rgb(45, 45, 45)
        );

        icon.setBackground(background);

        row.addView(
                icon,
                new LinearLayout.LayoutParams(
                        dp(50),
                        dp(50)
                )
        );

        LinearLayout middle =
                new LinearLayout(this);

        middle.setOrientation(
                LinearLayout.VERTICAL
        );

        LinearLayout.LayoutParams
                middleParams =
                new LinearLayout.LayoutParams(
                        0,
                        -2,
                        1
                );

        middleParams.leftMargin = dp(14);

        TextView title =
                new TextView(this);

        title.setText(
                titleFor(transaction)
        );

        title.setTextColor(
                Color.WHITE
        );

        title.setTextSize(16);

        title.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        middle.addView(title);

        TextView subtitle =
                new TextView(this);

        String date =
                new SimpleDateFormat(
                        "MMM d, yyyy • h:mm a",
                        Locale.US
                ).format(
                        new Date(
                                transaction.timestamp
                        )
                );

        String note =
                transaction.note;

        if (note.isEmpty()) {
            subtitle.setText(date);
        } else {
            subtitle.setText(
                    note + "\n" + date
            );
        }

        subtitle.setTextColor(
                Color.rgb(165, 165, 165)
        );

        subtitle.setTextSize(13);

        middle.addView(subtitle);

        row.addView(
                middle,
                middleParams
        );

        TextView amount =
                new TextView(this);

        if (transaction.type.equals("PAY")) {

            amount.setText(
                    "-" +
                            TransactionStore.money(
                                    transaction.amountCents
                            )
            );

        } else if (
                transaction.type.equals("REQUEST")
        ) {

            amount.setText(
                    "Request\n" +
                            TransactionStore.money(
                                    transaction.amountCents
                            )
            );

        } else {

            amount.setText(
                    "Pool\n" +
                            TransactionStore.money(
                                    transaction.amountCents
                            )
            );
        }

        amount.setTextColor(
                transaction.type.equals("PAY")
                        ? Color.WHITE
                        : Color.rgb(
                                0,
                                214,
                                50
                        )
        );

        amount.setGravity(
                Gravity.CENTER
        );

        amount.setTextSize(15);

        row.addView(amount);

        return row;
    }

    private String titleFor(
            TransactionStore.Transaction transaction
    ) {

        if (transaction.type.equals("PAY")) {
            return "Paid " + transaction.person;
        }

        if (transaction.type.equals("REQUEST")) {
            return "Requested from "
                    + transaction.person;
        }

        if (transaction.type.equals("POOL")) {
            return "Pool: "
                    + transaction.person;
        }

        return transaction.person;
    }

    private void setupNavigation() {

        View home =
                findViewById(R.id.navHome);

        View keypad =
                findViewById(R.id.navKeypad);

        View history =
                findViewById(R.id.navHistory);

        if (home != null) {

            home.setOnClickListener(v -> {

                startActivity(
                        new Intent(
                                this,
                                HomeActivity.class
                        )
                );

                finish();
            });
        }

        if (keypad != null) {

            keypad.setOnClickListener(v -> {

                startActivity(
                        new Intent(
                                this,
                                MainActivity.class
                        )
                );

                finish();
            });
        }

        if (history != null) {

            history.setOnClickListener(
                    v -> {
                        // Already here.
                    }
            );
        }
    }

    private int dp(int value) {

        return Math.round(
                value *
                        getResources()
                                .getDisplayMetrics()
                                .density
        );
    }
                             }
