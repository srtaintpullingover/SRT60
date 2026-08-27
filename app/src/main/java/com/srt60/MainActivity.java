package com.srt60;

import android.os.Bundle;
import android.view.View;
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

        // Safe initialization with fallback if XML IDs differ
        int resId =getResources().getIdentifier("txtAmount", "id", getPackageName());
        if (resId != 0) {
            txtAmount = (TextView) findViewById(resId);
        }

        // Setup generic click listener for keypad buttons safely by name
        View.OnClickListener numListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof Button) {
                    Button b = (Button) v;
                    String val = b.getText().toString();

                    if (currentAmount.toString().equals("0") && !val.equals(".")) {
                        currentAmount.setLength(0);
                    }
                    currentAmount.append(val);
                    if (txtAmount != null) {
                        txtAmount.setText("$" + currentAmount.toString());
                    }
                }
            }
        };

        // Bind numbers 0-9 and dot dynamically without hardcoding missing R.id references
        String[] btnNames = {"btn0", "btn1", "btn2", "btn3", "btn4", "btn5", "btn6", "btn7", "btn8", "btn9", "btnDot"};
        for (int i = 0; i < btnNames.length; i++) {
            int id = getResources().getIdentifier(btnNames[i], "id", getPackageName());
            if (id != 0) {
                View btn = findViewById(id);
                if (btn != null) {
                    btn.setOnClickListener(numListener);
                }
            }
        }

        // Safe delete button bind
        int delId = getResources().getIdentifier("btnDel", "id", getPackageName());
        if (delId != 0) {
            View btnDel = findViewById(delId);
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
                        if (txtAmount != null) {
                            txtAmount.setText("$" + currentAmount.toString());
                        }
                    }
                });
            }
        }
    }
}
