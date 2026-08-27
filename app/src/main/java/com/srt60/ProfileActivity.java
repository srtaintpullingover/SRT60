package com.srt60;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView txtName, txtCashtag;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        prefs = getSharedPreferences("CashAppClone", MODE_PRIVATE);

        txtName = findViewById(R.id.txtName);
        txtCashtag = findViewById(R.id.txtCashtag);

        txtName.setText(prefs.getString("profile_name", "Diego Diego Logan"));
        txtCashtag.setText(prefs.getString("profile_cashtag", "$iranmoneyyy"));

        findViewById(R.id.btnClose).setOnClickListener(v -> finish());

        // Edit name
        txtName.setOnClickListener(v -> editText("profile_name", "Edit Name", txtName));
        findViewById(R.id.btnEditName).setOnClickListener(v -> editText("profile_name", "Edit Name", txtName));

        // Edit cashtag
        txtCashtag.setOnClickListener(v -> editText("profile_cashtag", "Edit $Cashtag", txtCashtag));
        findViewById(R.id.btnEditCashtag).setOnClickListener(v -> editText("profile_cashtag", "Edit $Cashtag", txtCashtag));

        findViewById(R.id.btnEditProfile).setOnClickListener(v -> editText("profile_name", "Edit Name", txtName));
    }

    private void editText(String key, String title, TextView target) {
        EditText input = new EditText(this);
        input.setText(target.getText().toString());
        input.setSelectAllOnFocus(true);
        input.setPadding(50, 40, 50, 40);

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(input)
                .setPositiveButton("Save", (d, w) -> {
                    String value = input.getText().toString().trim();
                    if (value.isEmpty()) return;
                    if (key.equals("profile_cashtag") && !value.startsWith("$")) {
                        value = "$" + value;
                    }
                    prefs.edit().putString(key, value).apply();
                    target.setText(value);
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
