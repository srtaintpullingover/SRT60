package com.srt60;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView txtAmount;

    private StringBuilder currentAmount =
            new StringBuilder("0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        txtAmount = findViewById(R.id.txtAmount);

        setupKeypad();
        setupActions();
        setupNavigation();

        updateAmount();
    }

    private void setupKeypad() {

        int[] numberIds = {
                R.id.btn0,
                R.id.btn1,
                R.id.btn2,
                R.id.btn3,
                R.id.btn4,
                R.id.btn5,
                R.id.btn6,
                R.id.btn7,
                R.id.btn8,
                R.id.btn9
        };

        for (int id : numberIds) {

            Button button = findViewById(id);

            if (button == null) {
                continue;
            }

            button.setOnClickListener(v -> {

                String digit =
                        ((Button) v).getText().toString();

                addDigit(digit);
            });
        }

        Button dot = findViewById(R.id.btnDot);

        if (dot != null) {
            dot.setOnClickListener(v -> addDecimal());
        }

        Button back = findViewById(R.id.btnBack);

        if (back != null) {
            back.setOnClickListener(v -> deleteLast());
        }
    }

    private void setupActions() {

        Button pay = findViewById(R.id.btnPay);
        Button request = findViewById(R.id.btnRequest);
        Button pool = findViewById(R.id.btnPool);

        if (pay != null) {
            pay.setOnClickListener(v -> showPaymentDialog());
        }

        if (request != null) {
            request.setOnClickListener(v -> showRequestDialog());
        }

        if (pool != null) {
            pool.setOnClickListener(v -> showPoolDialog());
        }
    }

    private void setupNavigation() {

        View home = findViewById(R.id.navHome);
        View keypad = findViewById(R.id.navKeypad);
        View history = findViewById(R.id.navHistory);
        View profile = findViewById(R.id.btnProfile);

        if (home != null) {
            home.setOnClickListener(v -> {
                startActivity(
                        new Intent(
                                MainActivity.this,
                                HomeActivity.class
                        )
                );
                finish();
            });
        }

        if (keypad != null) {
            keypad.setOnClickListener(v -> {
                // Already here.
            });
        }

        if (history != null) {
            history.setOnClickListener(v -> {
                startActivity(
                        new Intent(
                                MainActivity.this,
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
                                    MainActivity.this,
                                    ProfileActivity.class
                            )
                    )
            );
        }
    }

    private void addDigit(String digit) {

        if (digit.isEmpty()) {
            return;
        }

        if (currentAmount.toString().equals("0")) {
            currentAmount.setLength(0);
        }

        // Prevent more than two decimal places.
        int decimalIndex =
                currentAmount.indexOf(".");

        if (decimalIndex >= 0) {

            int decimalPlaces =
                    currentAmount.length()
                            - decimalIndex
                            - 1;

            if (decimalPlaces >= 2) {
                return;
            }
        }

        // Prevent an unnecessarily huge amount.
        if (currentAmount.length() >= 10) {
            return;
        }

        currentAmount.append(digit);

        updateAmount();
    }

    private void addDecimal() {

        if (currentAmount.indexOf(".") >= 0) {
            return;
        }

        currentAmount.append(".");
        updateAmount();
    }

    private void deleteLast() {

        if (currentAmount.length() <= 1) {
            currentAmount.setLength(0);
            currentAmount.append("0");
        } else {
            currentAmount.deleteCharAt(
                    currentAmount.length() - 1
            );

            if (currentAmount.toString().equals("")) {
                currentAmount.append("0");
            }
        }

        updateAmount();
    }

    private void updateAmount() {
        if (txtAmount != null) {
            txtAmount.setText(
                    "$" + currentAmount.toString()
            );
        }
    }

    private long getAmountCents() {

        try {

            double amount =
                    Double.parseDouble(
                            currentAmount.toString()
                    );

            if (amount <= 0) {
                return 0;
            }

            return Math.round(amount * 100.0);

        } catch (Exception e) {
            return 0;
        }
    }

    private void showPaymentDialog() {

        final long amount = getAmountCents();

        if (amount <= 0) {
            Toast.makeText(
                    this,
                    "Enter an amount first.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        final String[] contacts = {
                "Alexus Sims",
                "KoKo",
                "Jaiwain Small"
        };

        final int[] selected = {-1};

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        int padding = dp(20);

        layout.setPadding(
                padding,
                padding,
                padding,
                padding
        );

        TextView amountText =
                new TextView(this);

        amountText.setText(
                "Send " +
                        TransactionStore.money(amount)
        );

        amountText.setTextSize(24);
        amountText.setTextColor(
                android.graphics.Color.WHITE
        );

        layout.addView(amountText);

        TextView recipientLabel =
                new TextView(this);

        recipientLabel.setText(
                "Choose recipient"
        );

        recipientLabel.setTextSize(15);
        recipientLabel.setTextColor(
                android.graphics.Color.LTGRAY
        );

        LinearLayout.LayoutParams labelParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        labelParams.topMargin = dp(20);

        layout.addView(
                recipientLabel,
                labelParams
        );

        for (int i = 0; i < contacts.length; i++) {

            Button contact =
                    new Button(this);

            contact.setText(
                    contacts[i]
            );

            contact.setAllCaps(false);

            final int index = i;

            contact.setOnClickListener(v -> {

                selected[0] = index;

                for (int j = 0;
                     j < contacts.length;
                     j++) {

                    View child =
                            layout.getChildAt(
                                    2 + j
                            );

                    if (child instanceof Button) {

                        ((Button) child)
                                .setAlpha(
                                        j == selected[0]
                                                ? 1f
                                                : 0.65f
                                );
                    }
                }
            });

            layout.addView(contact);
        }

        EditText note =
                new EditText(this);

        note.setHint("What's it for?");
        note.setSingleLine(false);
        note.setTextColor(
                android.graphics.Color.WHITE
        );
        note.setHintTextColor(
                android.graphics.Color.GRAY
        );

        LinearLayout.LayoutParams noteParams =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                );

        noteParams.topMargin = dp(12);

        layout.addView(
                note,
                noteParams
        );

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("Pay")
                        .setView(layout)
                        .setNegativeButton(
                                "Cancel",
                                null
                        )
                        .setPositiveButton(
                                "Pay",
                                null
                        )
                        .create();

        dialog.setOnShowListener(d -> {

            Button positive =
                    dialog.getButton(
                            AlertDialog.BUTTON_POSITIVE
                    );

            positive.setOnClickListener(v -> {

                if (selected[0] < 0) {

                    Toast.makeText(
                            this,
                            "Choose a recipient.",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                long balance =
                        TransactionStore
                                .getCashBalance(this);

                if (balance < amount) {

                    Toast.makeText(
                            this,
                            "Not enough cash balance.",
                            Toast.LENGTH_LONG
                    ).show();

                    return;
                }

                boolean success =
                        TransactionStore
                                .subtractCash(
                                        this,
                                        amount
                                );

                if (!success) {
                    return;
                }

                String selectedPerson =
                        contacts[selected[0]];

                String noteText =
                        note.getText()
                                .toString()
                                .trim();

                TransactionStore.addTransaction(
                        this,
                        "PAY",
                        selectedPerson,
                        noteText,
                        amount
                );

                Toast.makeText(
                        this,
                        "Paid " +
                                TransactionStore.money(amount)
                                + " to "
                                + selectedPerson,
                        Toast.LENGTH_LONG
                ).show();

                resetAmount();

                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void showRequestDialog() {

        final long amount = getAmountCents();

        if (amount <= 0) {
            Toast.makeText(
                    this,
                    "Enter an amount first.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        final String[] contacts = {
                "Alexus Sims",
                "KoKo",
                "Jaiwain Small"
        };

        final int[] selected = {-1};

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        int padding = dp(20);

        layout.setPadding(
                padding,
                padding,
                padding,
                padding
        );

        TextView amountText =
                new TextView(this);

        amountText.setText(
                "Request " +
                        TransactionStore.money(amount)
        );

        amountText.setTextSize(24);
        amountText.setTextColor(
                android.graphics.Color.WHITE
        );

        layout.addView(amountText);

        TextView label =
                new TextView(this);

        label.setText(
                "Request from"
        );

        label.setTextColor(
                android.graphics.Color.LTGRAY
        );

        label.setTextSize(15);

        LinearLayout.LayoutParams labelParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        labelParams.topMargin = dp(20);

        layout.addView(
                label,
                labelParams
        );

        for (int i = 0; i < contacts.length; i++) {

            Button contact =
                    new Button(this);

            contact.setText(
                    contacts[i]
            );

            contact.setAllCaps(false);

            final int index = i;

            contact.setOnClickListener(v -> {

                selected[0] = index;

                for (int j = 0;
                     j < contacts.length;
                     j++) {

                    View child =
                            layout.getChildAt(
                                    2 + j
                            );

                    if (child instanceof Button) {

                        ((Button) child)
                                .setAlpha(
                                        j == selected[0]
                                                ? 1f
                                                : 0.65f
                                );
                    }
                }
            });

            layout.addView(contact);
        }

        EditText note =
                new EditText(this);

        note.setHint("What's it for?");
        note.setTextColor(
                android.graphics.Color.WHITE
        );
        note.setHintTextColor(
                android.graphics.Color.GRAY
        );

        layout.addView(
                note,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("Request")
                        .setView(layout)
                        .setNegativeButton(
                                "Cancel",
                                null
                        )
                        .setPositiveButton(
                                "Request",
                                null
                        )
                        .create();

        dialog.setOnShowListener(d -> {

            Button positive =
                    dialog.getButton(
                            AlertDialog.BUTTON_POSITIVE
                    );

            positive.setOnClickListener(v -> {

                if (selected[0] < 0) {

                    Toast.makeText(
                            this,
                            "Choose someone.",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                String person =
                        contacts[selected[0]];

                String noteText =
                        note.getText()
                                .toString()
                                .trim();

                TransactionStore.addTransaction(
                        this,
                        "REQUEST",
                        person,
                        noteText,
                        amount
                );

                Toast.makeText(
                        this,
                        "Request sent to " + person,
                        Toast.LENGTH_LONG
                ).show();

                resetAmount();

                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private void showPoolDialog() {

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        int padding = dp(20);

        layout.setPadding(
                padding,
                padding,
                padding,
                padding
        );

        EditText poolName =
                new EditText(this);

        poolName.setHint("Pool name");
        poolName.setTextColor(
                android.graphics.Color.WHITE
        );
        poolName.setHintTextColor(
                android.graphics.Color.GRAY
        );

        layout.addView(
                poolName,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        EditText goal =
                new EditText(this);

        goal.setHint("Goal amount");
        goal.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        goal.setTextColor(
                android.graphics.Color.WHITE
        );
        goal.setHintTextColor(
                android.graphics.Color.GRAY
        );

        layout.addView(
                goal,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        AlertDialog dialog =
                new AlertDialog.Builder(this)
                        .setTitle("Create Pool")
                        .setView(layout)
                        .setNegativeButton(
                                "Cancel",
                                null
                        )
                        .setPositiveButton(
                                "Create",
                                null
                        )
                        .create();

        dialog.setOnShowListener(d -> {

            Button positive =
                    dialog.getButton(
                            AlertDialog.BUTTON_POSITIVE
                    );

            positive.setOnClickListener(v -> {

                String name =
                        poolName.getText()
                                .toString()
                                .trim();

                String goalText =
                        goal.getText()
                                .toString()
                                .trim();

                if (name.isEmpty()) {
                    name = "New Pool";
                }

                if (goalText.isEmpty()) {

                    Toast.makeText(
                            this,
                            "Enter a goal amount.",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                try {

                    double goalAmount =
                            Double.parseDouble(
                                    goalText
                            );

                    long cents =
                            Math.round(
        
