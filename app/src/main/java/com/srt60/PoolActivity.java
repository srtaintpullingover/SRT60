package com.srt60;

import android.content.Intent;
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
import java.util.Locale;

public class PoolActivity extends AppCompatActivity {

    private LinearLayout poolList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pool);

        poolList = findViewById(R.id.poolList);

        findViewById(R.id.btnClose)
                .setOnClickListener(v -> finish());

        findViewById(R.id.btnCreatePool)
                .setOnClickListener(v -> createPool());

        refreshPools();
    }

    private void refreshPools() {

        poolList.removeAllViews();

        ArrayList<AppData.Pool> pools =
                AppData.getPools(this);

        if (pools.isEmpty()) {

            TextView empty = new TextView(this);

            empty.setText(
                    "No pools yet.\nCreate one to start collecting."
            );

            empty.setTextColor(0xFF888888);
            empty.setTextSize(16);
            empty.setGravity(17);
            empty.setPadding(20, 60, 20, 60);

            poolList.addView(empty);

            return;
        }

        for (AppData.Pool pool : pools) {

            View row = LayoutInflater.from(this)
                    .inflate(
                            R.layout.item_pool,
                            poolList,
                            false
                    );

            TextView name =
                    row.findViewById(R.id.txtPoolName);

            TextView description =
                    row.findViewById(R.id.txtPoolDescription);

            TextView balance =
                    row.findViewById(R.id.txtPoolBalance);

            TextView members =
                    row.findViewById(R.id.txtPoolMembers);

            name.setText(pool.name);
            description.setText(pool.description);

            balance.setText(
                    String.format(
                            Locale.US,
                            "$%.2f / $%.2f",
                            pool.balance,
                            pool.goal
                    )
            );

            members.setText(
                    pool.members.size() +
                            " member" +
                            (pool.members.size() == 1
                                    ? ""
                                    : "s")
            );

            row.setOnClickListener(v -> {

                Intent intent =
                        new Intent(
                                this,
                                PoolMemberActivity.class
                        );

                intent.putExtra(
                        "POOL_ID",
                        pool.id
                );

                startActivity(intent);
            });

            poolList.addView(row);
        }
    }

    private void createPool() {

        LinearLayout form = new LinearLayout(this);

        form.setOrientation(
                LinearLayout.VERTICAL
        );

        form.setPadding(30, 10, 30, 0);

        EditText name = new EditText(this);
        name.setHint("Pool name");

        EditText description = new EditText(this);
        description.setHint("Description");

        EditText goal = new EditText(this);
        goal.setHint("Goal amount");
        goal.setInputType(2 | 8192);

        form.addView(name);
        form.addView(description);
        form.addView(goal);

        new AlertDialog.Builder(this)
                .setTitle("Create pool")
                .setView(form)
                .setNegativeButton("Cancel", null)
                .setPositiveButton(
                        "Create",
                        (dialog, which) -> {

                            try {

                                String poolName =
                                        name.getText()
                                                .toString()
                                                .trim();

                                String poolDescription =
                                        description.getText()
                                                .toString()
                                                .trim();

                                double goalAmount =
                                        Double.parseDouble(
                                                goal.getText()
                                                        .toString()
                                                        .trim()
                                        );

                                if (poolName.isEmpty()
                                        || goalAmount <= 0) {
                                    throw new Exception();
                                }

                                AppData.Pool pool =
                                        new AppData.Pool(
                                                poolName,
                                                poolDescription,
                                                goalAmount
                                        );

                                pool.members.add(
                                        "You"
                                );

                                AppData.savePool(
                                        this,
                                        pool
                                );

                                refreshPools();

                            } catch (Exception e) {

                                Toast.makeText(
                                        this,
                                        "Enter a pool name and valid goal",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                )
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (poolList != null) {
            refreshPools();
        }
    }
}
