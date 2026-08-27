package com.srt60;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TransactionDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_transaction_detail
        );

        findViewById(R.id.btnClose)
                .setOnClickListener(v -> finish());

        String id =
                getIntent().getStringExtra("TRANSACTION_ID");

        if (id == null) {
            finish();
            return;
        }

        AppData.Transaction found = null;

        ArrayList<AppData.Transaction> transactions =
                AppData.getTransactions(this);

        for (AppData.Transaction transaction : transactions) {

            if (id.equals(transaction.id)) {
                found = transaction;
                break;
            }
        }

        if (found == null) {
            finish();
            return;
        }

        TextView avatar =
                findViewById(R.id.txtAvatar);

        TextView amount =
                findViewById(R.id.txtAmount);

        TextView name =
                findViewById(R.id.txtName);

        TextView username =
                findViewById(R.id.txtUsername);

        TextView type =
                findViewById(R.id.txtType);

        TextView date =
                findViewById(R.id.txtDate);

        TextView note =
                findViewById(R.id.txtNote);

        avatar.setText(
                found.person.substring(0, 1)
                        .toUpperCase()
        );

        boolean outgoing =
                "PAY".equalsIgnoreCase(found.type)
                        || "POOL".equalsIgnoreCase(found.type);

        amount.setText(
                (outgoing ? "-" : "+") +
                        AppData.formatMoney(found.amount)
        );

        amount.setTextColor(
                outgoing
                        ? 0xFFFFFFFF
                        : 0xFF00D632
        );

        name.setText(found.person);
        username.setText(found.username);
        type.setText(found.type);
        date.setText(
                AppData.formatDate(found.timestamp)
        );

        if (found.note == null
                || found.note.isEmpty()) {

            note.setText("No note");

        } else {

            note.setText(found.note);
        }
    }
}
