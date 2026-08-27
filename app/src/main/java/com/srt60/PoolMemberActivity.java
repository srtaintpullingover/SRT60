package com.srt60;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PoolMemberActivity extends AppCompatActivity {

    private AppData.Pool pool;
    private LinearLayout memberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_pool_member
        );

        memberList =
                findViewById(R.id.memberList);

        findViewById(R.id.btnClose)
                .setOnClickListener(v -> finish());

        findViewById(R.id.btnAddMember)
                .setOnClickListener(v -> addMember());

        String id =
                getIntent().getStringExtra("POOL_ID");

        ArrayList<AppData.Pool> pools =
                AppData.getPools(this);

        for (AppData.Pool item : pools) {

            if (item.id.equals(id)) {
                pool = item;
                break;
            }
        }

        if (pool == null) {
            finish();
            return;
        }

        refresh();
    }

    private void refresh() {

        ((TextView) findViewById(R.id.txtPoolName))
                .setText(pool.name);

        ((TextView) findViewById(R.id.txtPoolDescription))
                .setText(pool.description);

        ((TextView) findViewById(R.id.txtPoolBalance))
                .setText(
                        AppData.formatMoney(pool.balance)
                                + " / "
                                + AppData.formatMoney(pool.goal)
                );

        memberList.removeAllViews();

        for (String member : pool.members) {

            TextView row = new TextView(this);

            row.setText(member);
            row.setTextColor(0xFFFFFFFF);
            row.setTextSize(17);
            row.setPadding(18, 20, 18, 20);

            memberList.addView(row);
        }
    }

    private void addMember() {

        EditText input = new EditText(this);

        input.setHint(
                "Name or $cashtag"
        );

        new AlertDialog.Builder(this)
                .setTitle("Add member")
                .setView(input)
                .setNegativeButton("Cancel", null)
                .setPositiveButton(
                        "Add",
                        (dialog, which) -> {

                            String member =
                                    input.getText()
                                            .toString()
                                            .trim();

                            if (member.isEmpty()) {
                                return;
                            }

                            pool.members.add(member);

                            AppData.savePool(
                                    this,
                                    pool
                            );

                            refresh();

                            Toast.makeText(
                                    this,
                                    member + " added",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                )
                .show();
    }
}
