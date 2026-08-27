package com.srt60;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView txtAmount;
    private StringBuilder currentAmount = new StringBuilder("0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the amount display text view dynamically
        txtAmount = findTextViewRecursive(getWindow().getDecorView());

        // Universal listener for keypad and action buttons
        View.OnClickListener keypadListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof Button) {
                    Button b = (Button) v;
                    String val = b.getText().toString();

                    if (val.equalsIgnoreCase("PAY") || val.equalsIgnoreCase("POOL") || val.equalsIgnoreCase("REQUEST")) {
                        // Handle action buttons here if needed
                        return;
                    }

                    if (val.equals("<") || val.equals("DEL")) {
                        if (currentAmount.length() > 1) {
                            currentAmount.deleteCharAt(currentAmount.length() - 1);
                        } else {
                            currentAmount.setLength(0);
                            currentAmount.append("0");
                        }
                    } else {
                        if (currentAmount.toString().equals("0") && !val.equals(".")) {
                            currentAmount.setLength(0);
                        }
                        currentAmount.append(val);
                    }

                    if (txtAmount != null) {
                        txtAmount.setText("$" + currentAmount.toString());
                    }
                }
            }
        };

        setupButtonListenersRecursive(getWindow().getDecorView(), keypadListener);

        // Handle top navigation icons/buttons if present
        setupNavigationIcons();
    }

    private TextView findTextViewRecursive(View view) {
        if (view instanceof TextView) {
            String text = ((TextView) view).getText().toString();
            if (text.contains("$") || text.equals("0")) {
                return (TextView) view;
            }
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                TextView found = findTextViewRecursive(group.getChildAt(i));
                if (found != null) return found;
            }
        }
        return null;
    }

    private void setupButtonListenersRecursive(View view, View.OnClickListener listener) {
        if (view instanceof Button) {
            view.setOnClickListener(listener);
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                setupButtonListenersRecursive(group.getChildAt(i), listener);
            }
        }
    }

    private void setupNavigationIcons() {
        // Automatically hook up top navigation elements to switch views or check balance/history
        int[] possibleNavIds = {
            getResources().getIdentifier("navHome", "id", getPackageName()),
            getResources().getIdentifier("navHistory", "id", getPackageName()),
            getResources().getIdentifier("histNavHome", "id", getPackageName()),
            getResources().getIdentifier("homeNavHome", "id", getPackageName())
        };

        for (int id : possibleNavIds) {
            if (id != 0) {
                View navView = findViewById(id);
                if (navView != null) {
                    navView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Open History or Home Activity when top nav icons are tapped
                            try {
                                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                                startActivity(intent);
                            } catch (Exception e) {
                                // Fallback if activity isn't registered yet
                            }
                        }
                    });
                }
            }
        }
    }
}
