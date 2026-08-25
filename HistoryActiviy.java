package com.yourname.cashappclone;
import java.util.ArrayList;
import java.util.Arrays;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    private ListView listViewTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listViewTransactions = findViewById(R.id.listViewTransactions);
String amount = getIntent().getStringExtra("TRANSACTION_AMOUNT");
String type = getIntent().getStringExtra("TRANSACTION_TYPE");


        ArrayList<String> transactionList = new ArrayList<>(Arrays.asList(
            "Paid $45.00 to John Doe",
            "Received $120.00 from Jane Smith",
            "Cash Out -$50.00 to Bank",
            "Paid $12.50 to Coffee Shop",
            "Received $15.00 from Alex"
        ));

        if (amount != null && !amount.isEmpty() && type != null) {
            String newTransaction = type + " $" + amount;
            transactionList.add(0, newTransaction);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_list_item_1,
            transactionList
        );

        listViewTransactions.setAdapter(adapter);


        
}
