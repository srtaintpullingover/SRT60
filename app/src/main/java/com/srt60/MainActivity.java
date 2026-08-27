package com.example.srt60;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView txtAmount;
    private StringBuilder currentAmount = new StringBuilder("0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtAmount = findViewById(R.id.txtAmount);

        // Keypad Number Click Logic
        int[] numButtonIds = {
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot
        };

        View.OnClickListener numListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                String val = b.getText().toString();

                if (currentAmount.toString().equals("0") && !val.equals(".")) {
                    currentAmount.setLength(0);
                }
                currentAmount.append(val);
                txtAmount.setText("$" + currentAmount.toString());
            }
        };

        for (int id : numButtonIds) {
            Button btn = findViewById(id);
            if (btn != null) {
                btn.setOnClickListener(numListener);
            }
        }

        // Backspace Button Logic
        Button btnDel = findViewById(R.id.btnDel);
        if (btnDel != null) {
            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentAmount.length() > 1) {
                        currentAmount.deleteCharAt(currentAmount.length() - 1);
                    } else {
                        currentAmount.setLength(0);
                        currentAmount.append("0");
                    }
                    txtAmount.setText("$" + currentAmount.toString());
                }
            });
        }

        // Bottom Navigation Bar Switching
        View navHome = findViewById(R.id.navHome);
        View navKeypad = findViewById(R.id.navKeypad);
        View navHistory = findViewById(R.id.navHistory);

        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            });
        }

        if (navHistory != null) {
            navHistory.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                finish();
            });
        }
    }
}
