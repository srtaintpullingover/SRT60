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

        txtAmount = findTextViewRecursive(getWindow().getDecorView());

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonText = "";
                if (v instanceof Button) {
                    buttonText = ((Button) v).getText().toString();
                } else if (v instanceof TextView) {
                    buttonText = ((TextView) v).getText().toString();
                }

                if (buttonText.equalsIgnoreCase("PAY") || 
                    buttonText.equalsIgnoreCase("POOL") || 
                    buttonText.equalsIgnoreCase("REQUEST")) {
                    
                    Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                    intent.putExtra("ACTION_TYPE", buttonText);
                    intent.putExtra("AMOUNT", currentAmount.toString());
                    startActivity(intent);
                    return;
                }

                if (buttonText.equals("<") || buttonText.equals("DEL") || buttonText.equalsIgnoreCase("Back")) {
                    if (currentAmount.length() > 1) {
                        currentAmount.deleteCharAt(currentAmount.length() - 1);
                    } else {
                        currentAmount.setLength(0);
                        currentAmount.append("0");
                    }
                } else if (!buttonText.isEmpty() && (Character.isDigit(buttonText.charAt(0)) || buttonText.equals("."))) {
                    if (currentAmount.toString().equals("0") && !buttonText.equals(".")) {
                        currentAmount.setLength(0);
                    }
                    currentAmount.append(buttonText);
                }

                if (txtAmount != null) {
                    txtAmount.setText("$" + currentAmount.toString());
                }
            }
        };

        attachListenersRecursive(getWindow().getDecorView(), clickListener);
    }

    private TextView findTextViewRecursive(View view) {
        if (view instanceof TextView && !(view instanceof Button)) {
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

    private void attachListenersRecursive(View view, View.OnClickListener listener) {
        if (view instanceof Button || view instanceof TextView) {
            view.setOnClickListener(listener);
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                attachListenersRecursive(group.getChildAt(i), listener);
            }
        }
    }
}

