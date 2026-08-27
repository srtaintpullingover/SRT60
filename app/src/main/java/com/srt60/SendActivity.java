package com.srt60;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

public class SendActivity extends AppCompatActivity {

    private EditText search;
    private EditText amount;
    private EditText note;
    private LinearLayout contactList;

    private AppData.Contact selectedContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_send);

        search = findViewById(R.id.txtSearch);
        amount = findViewById(R.id.txtAmount);
        note = findViewById(R.id.txtNote);
        contactList = findViewById(R.id.contactList);

        findViewById(R.id.btnClose)
                .setOnClickListener(v -> finish());

        findViewById(R.id.btnSendMoney)
                .setOnClickListener(v -> sendMoney());

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after
            ) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count
            ) {
                showContacts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        showContacts("");
    }

    private void showContacts(String query) {

        contactList.removeAllViews();

        List<AppData.Contact> contacts =
                AppData.getContacts();

        String lower =
                query.trim().toLowerCase(Locale.US);

        for (AppData.Contact contact : contacts) {

            if (!lower.isEmpty()
                    && !contact.name
                    .toLowerCase(Locale.US)
                    .contains(lower)
                    && !contact.username
                    .toLowerCase(Locale.US)
                    .contains(lower)
                    && !contact.phone
                    .contains(lower)) {

                continue;
            }

            View row = LayoutInflater.from(this)
                    .inflate(
                            R.layout.item_contact,
                            contactList,
                            false
                    );

            TextView avatar =
                    row.findViewById(R.id.txtAvatar);

            TextView name =
                    row.findViewById(R.id.txtName);

            TextView username =
                    row.findViewById(R.id.txtUsername);

            avatar.setText(contact.initials);
            name.setText(contact.name);
            username.setText(
                    contact.username +
                            (contact.phone.isEmpty()
                                    ? ""
                                    : " • " + contact.phone)
            );

            row.setOnClickListener(v -> {

                selectedContact = contact;

                findViewById(R.id.selectedContact)
                        .setVisibility(View.VISIBLE);

                TextView selected =
                        findViewById(R.id.txtSelectedContact);

                selected.setText(
                        contact.name +
                                "  " +
                                contact.username
                );

                Toast.makeText(
                        this,
                        "Selected " + contact.name,
                        Toast.LENGTH_SHORT
                ).show();
            });

            contactList.addView(row);
        }
    }

    private void sendMoney() {

        if (selectedContact == null) {

            Toast.makeText(
                    this,
                    "Choose a contact first",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String rawAmount =
                amount.getText().toString().trim();

        if (rawAmount.isEmpty()) {

            Toast.makeText(
                    this,
                    "Enter an amount",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        double value;

        try {
            value = Double.parseDouble(rawAmount);
        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Enter a valid amount",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (value <= 0) {

            Toast.makeText(
                    this,
                    "Amount must be greater than $0",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (value > AppData.getBalance(this)) {

            Toast.makeText(
                    this,
                    "Insufficient demo balance",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String message =
                note.getText().toString().trim();

        if (!AppData.subtractBalance(this, value)) {
            return;
        }

        AppData.Transaction transaction =
                new AppData.Transaction(
                        "PAY",
                        selectedContact.name,
                        selectedContact.username,
                        value,
                        message
                );

        AppData.addTransaction(
                this,
                transaction
        );

        Toast.makeText(
                this,
                String.format(
                        Locale.US,
                        "Sent $%.2f to %s",
                        value,
                        selectedContact.name
                ),
                Toast.LENGTH_LONG
        ).show();

        finish();
    }
              }
