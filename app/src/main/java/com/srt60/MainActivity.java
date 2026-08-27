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

        txtAmount = (TextView) findViewById(R.id.txtAmount);

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
                if (txtAmount != null) {
                    txtAmount.setText("$" + currentAmount.toString());
                }
            }
        };

        for (int i = 0; i < numButtonIds.length; i++) {
            Button btn = (Button) findViewById(numButtonIds[i]);
            if (btn != null) {
                btn.setOnClickListener(numListener);
            }
        }

        Button btnDel = (Button) findViewById(R.id.btnDel);
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
