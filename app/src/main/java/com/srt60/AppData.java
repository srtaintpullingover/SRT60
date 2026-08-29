package com.srt60;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AppData {

    private static final String PREFS_NAME = "app_data";
    private static final String KEY_BALANCE = "balance";
    private static final String KEY_TRANSACTIONS = "transactions";

    public static class Transaction {
        public long id;
        public String person;
        public double amount;
        public String type;
        public long timestamp;
    }

    public static double getBalance(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getFloat(KEY_BALANCE, 2976f);
    }

    public static void setBalance(Context context, double balance) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putFloat(KEY_BALANCE, (float) balance).apply();
    }

    public static ArrayList<Transaction> getTransactions(Context context) {
        // For demo purposes, return empty list
        // You can implement actual storage here
        return new ArrayList<>();
    }

    public static void addTransaction(Context context, Transaction transaction) {
        // For demo purposes, just show toast
        // You can implement actual storage here
        ArrayList<Transaction> transactions = getTransactions(context);
        transactions.add(transaction);
        // Save to SharedPreferences or SQLite
    }

    public static String formatMoney(double amount) {
        if (amount == (long) amount) {
            return String.format(Locale.US, "$%,d", (long) amount);
        } else {
            return String.format(Locale.US, "$%,.2f", amount);
        }
    }

    public static String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d", Locale.US);
        return sdf.format(new Date(timestamp));
    }
}
