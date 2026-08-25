package com.yourname.cashappclone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvAmount;
    private StringBuilder currentAmount = new StringBuilder("0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvAmount = findViewById(R.id.tvAmount);

        // Array of button IDs for numbers 0 through 9
        int[] numberButtonIds = {
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };

        View.OnClickListener numberListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                String digit = b.getText().toString();

                if (currentAmount.toString().equals("0")) {
                    currentAmount.setLength(0);
                }
                
                // Limit length to prevent overflow
                if (currentAmount.length() < 7) {
                    currentAmount.append(digit);
                    updateDisplay();
                }
            }
        };

        // Wire up number buttons (Note: ensure your XML buttons have corresponding IDs like android:id="@+id/btn1")
        for (int id : numberButtonIds) {
            Button btn = findViewById(id);
            if (btn != null) {
                btn.setOnClickListener(numberListener);
            }
        }

        // Backspace button logic
        Button btnDelete = findViewById(R.id.btnDelete);
        if (btnDelete != null) {
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentAmount.length() > 1) {
                        currentAmount.deleteCharAt(currentAmount.length() - 1);
                    } else {
                        currentAmount.setLength(0);
                        currentAmount.append("0");
                    }
                    updateDisplay();
                }
            });
        }
    }        // History navigation icon click logic
        TextView btnHistoryNav = findViewById(R.id.btnHistoryNav);
        if (btnHistoryNav != null) {
            btnHistoryNav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.content.Intent intent = new android.content.Intent(MainActivity.this, HistoryActivity.class);
                    startActivity(intent);
                }
            });
        }
                // Pay button click logic
        Button btnPay = findViewById(R.id.btnPay);
        if (btnPay != null) {
            btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String amount = currentAmount.toString();
                    android.content.Intent intent = new android.content.Intent(MainActivity.this, HistoryActivity.class);
                    intent.putExtra("TRANSACTION_AMOUNT", amount);
                    intent.putExtra("TRANSACTION_TYPE", "Paid");
                    startActivity(intent);
                }
            });
        }

        // Request button click logic
        Button btnRequest = findViewById(R.id.btnRequest);
        if (btnRequest != null) {
            btnRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String amount = currentAmount.toString();
                    android.content.Intent intent = new android.content.Intent(MainActivity.this, HistoryActivity.class);
                    intent.putExtra("TRANSACTION_AMOUNT", amount);
                    intent.putExtra("TRANSACTION_TYPE", "Requested");
                    startActivity(intent);
                }
            });
        }

            });
        }


    private void updateDisplay() {
        tvAmount.setText("$" + currentAmount.toString());
    }
}
