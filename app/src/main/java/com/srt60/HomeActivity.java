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

public class HomeActivity extends AppCompatActivity {

    private TextView txtBalance;
    private LinearLayout transactionContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        txtBalance =
                findViewById(R.id.txtHomeBalance);

        transactionContainer =
                findViewById(
                        R.id.transactionContainer
                );

        setupNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshHome();
    }

    private void refreshHome() {

        long balance =
                TransactionStore
                        .getCashBalance(this);

        if (txtBalance != null) {
            txtBalance.setText(
                    TransactionStore.money(balance)
            );
        }

        renderTransactions();
    }

    private void renderTransactions() {

        if (transactionContainer == null) {
            return;
        }

        transactionContainer.removeAllViews();

        List<TransactionStore.Transaction>
                transactions =
                TransactionStore
                        .getTransactions(this);

        if (transactions.isEmpty()) {

            TextView empty =
                    new TextView(this);

            empty.setText(
                    "No transactions yet"
            );

            empty.setTextColor(
                    Color.GRAY
            );

            empty.setTextSize(16);

            empty.setGravity(
                    Gravity.CENTER
            );

            empty.setPadding(
                    0,
                    dp(40),
                    0,
                    dp(40)
            );

            transactionContainer.addView(
                    empty
            );

            return;
        }

        int count = 0;

        for (
                TransactionStore.Transaction transaction
                : transactions
        ) {

            if (count >= 20) {
                break;
            }

            transactionContainer.addView(
                    createTransactionRow(
                            transaction
                    )
            );

            count++;
        }
    }

    private View createTransactionRow(
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
                dp(12),
                0,
                dp(12)
        );

        TextView avatar =
                new TextView(this);

        avatar.setGravity(
                Gravity.CENTER
        );

        avatar.setTextSize(18);

        avatar.setTextColor(
                Color.WHITE
        );

        avatar.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        String person =
                transaction.person;

        String letter =
                person.isEmpty()
                        ? "$"
                        : person.substring(
                                0,
                                1
                        ).toUpperCase();

        avatar.setText(letter);

        android.graphics.drawable.GradientDrawable
                avatarBackground =
                new android.graphics.drawable
                        .GradientDrawable();

        avatarBackground.setColor(
                Color.rgb(45, 45, 45)
        );

        avatarBackground.setShape(
                android.graphics.drawable
                        .GradientDrawable.OVAL
        );

        avatar.setBackground(
                avatarBackground
        );

        LinearLayout.LayoutParams
                avatarParams =
                new LinearLayout.LayoutParams(
                        dp(50),
                        dp(50)
                );

        row.addView(
                avatar,
                avatarParams
        );

        LinearLayout details =
                new LinearLayout(this);

        details.setOrientation(
                LinearLayout.VERTICAL
        );

        LinearLayout.LayoutParams
                detailsParams =
                new LinearLayout.LayoutParams(
                        0,
                        -2,
                        1
                );

        detailsParams.leftMargin =
                dp(14);

        TextView title =
                new TextView(this);

        title.setText(
                getTransactionTitle(
                        transaction
                )
        );

        title.setTextColor(
                Color.WHITE
        );

        title.setTextSize(16);

        title.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        details.addView(title);

        TextView subtitle =
                new TextView(this);

        String note =
                transaction.note;

        String date =
                new SimpleDateFormat(
                        "MMM d • h:mm a",
                        Locale.US
                ).format(
                        new Date(
                                transaction.timestamp
                        )
                );

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

        details.addView(subtitle);

        row.addView(
                details,
                detailsParams
        );

        TextView amount =
                new TextView(this);

        boolean outgoing =
                transaction.type.equals("PAY");

        amount.setText(
                (outgoing ? "-" : "+")
                        +
                        TransactionStore.money(
                                transaction.amountCents
                        )
        );

        amount.setTextColor(
                outgoing
                        ? Color.WHITE
                        : Color.rgb(
                                0,
                                214,
                                50
                        )
        );

        amount.setTextSize(16);

        amount.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        row.addView(amount);

        return row;
    }

    private String getTransactionTitle(
            TransactionStore.Transaction transaction
    ) {

        if (transaction.type.equals("PAY")) {
            return transaction.person;
        }

        if (transaction.type.equals("REQUEST")) {
            return "Request from "
                    + transaction.person;
        }

        if (transaction.type.equals("POOL")) {
            return transaction.person;
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

        View profile =
                findViewById(R.id.btnProfile);

        View balance =
                findViewById(R.id.rowCashBalance);

        if (home != null) {
            home.setOnClickListener(
                    v -> {
                        // Already home.
                    }
            );
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

            history.setOnClickListener(v -> {

                startActivity(
                        new Intent(
                                this,
                                HistoryActivity.class
                        )
                );

                finish();
            });
        }

        if (profile != null) {

            profile.setOnClickListener(v ->
                    startActivity(
                            new Intent(
                                    this,
                                    ProfileActivity.class
                            )
                    )
            );
        }

        if (balance != null) {

            balance.setOnClickListener(v ->
                    startActivity(
                            new Intent(
                                    this,
                                    BalanceActivity.class
                            )
                    )
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
