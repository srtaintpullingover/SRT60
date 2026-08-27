package com.srt60;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TransactionStore {

    private static final String PREFS = "CashAppClone";
    private static final String TRANSACTIONS = "transactions";
    private static final String CASH_BALANCE = "cash_balance_cents";
    private static final String SAVINGS = "savings_cents";

    public static class Transaction {
        public String type;
        public String person;
        public String note;
        public long amountCents;
        public long timestamp;

        public Transaction(
                String type,
                String person,
                String note,
                long amountCents,
                long timestamp
        ) {
            this.type = type;
            this.person = person;
            this.note = note;
            this.amountCents = amountCents;
            this.timestamp = timestamp;
        }
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static long getCashBalance(Context context) {
        SharedPreferences p = prefs(context);

        if (p.contains(CASH_BALANCE)) {
            return p.getLong(CASH_BALANCE, 0);
        }

        // Migrate the old version's cash_balance string if it exists.
        String old = p.getString("cash_balance", null);

        if (old != null) {
            try {
                double amount = Double.parseDouble(old);
                long cents = Math.round(amount * 100.0);

                p.edit()
                        .putLong(CASH_BALANCE, cents)
                        .apply();

                return cents;

            } catch (Exception ignored) {
            }
        }

        return 0;
    }

    public static void setCashBalance(Context context, long cents) {
        prefs(context)
                .edit()
                .putLong(CASH_BALANCE, Math.max(0, cents))
                .apply();
    }

    public static long getSavings(Context context) {
        SharedPreferences p = prefs(context);

        if (p.contains(SAVINGS)) {
            return p.getLong(SAVINGS, 0);
        }

        String old = p.getString("savings", null);

        if (old != null) {
            try {
                double amount = Double.parseDouble(old);
                long cents = Math.round(amount * 100.0);

                p.edit()
                        .putLong(SAVINGS, cents)
                        .apply();

                return cents;

            } catch (Exception ignored) {
            }
        }

        return 0;
    }

    public static void setSavings(Context context, long cents) {
        prefs(context)
                .edit()
                .putLong(SAVINGS, Math.max(0, cents))
                .apply();
    }

    public static boolean subtractCash(Context context, long amountCents) {
        long current = getCashBalance(context);

        if (amountCents <= 0) {
            return false;
        }

        if (current < amountCents) {
            return false;
        }

        setCashBalance(context, current - amountCents);
        return true;
    }

    public static void addCash(Context context, long amountCents) {
        long current = getCashBalance(context);
        setCashBalance(context, current + Math.max(0, amountCents));
    }

    public static void addTransaction(
            Context context,
            String type,
            String person,
            String note,
            long amountCents
    ) {
        try {
            SharedPreferences p = prefs(context);

            JSONArray array;

            String existing = p.getString(TRANSACTIONS, "[]");

            try {
                array = new JSONArray(existing);
            } catch (Exception e) {
                array = new JSONArray();
            }

            JSONObject object = new JSONObject();

            object.put("type", type);
            object.put("person", person == null ? "" : person);
            object.put("note", note == null ? "" : note);
            object.put("amount", amountCents);
            object.put("timestamp", System.currentTimeMillis());

            // Newest first.
            array.put(0, object);

            p.edit()
                    .putString(TRANSACTIONS, array.toString())
                    .apply();

        } catch (Exception ignored) {
        }
    }

    public static List<Transaction> getTransactions(Context context) {

        List<Transaction> result = new ArrayList<>();

        String saved = prefs(context)
                .getString(TRANSACTIONS, "[]");

        try {
            JSONArray array = new JSONArray(saved);

            for (int i = 0; i < array.length(); i++) {

                JSONObject object = array.getJSONObject(i);

                result.add(
                        new Transaction(
                                object.optString("type", "PAY"),
                                object.optString("person", ""),
                                object.optString("note", ""),
                                object.optLong("amount", 0),
                                object.optLong("timestamp", 0)
                        )
                );
            }

        } catch (Exception ignored) {
        }

        Collections.sort(
                result,
                new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction a, Transaction b) {
                        return Long.compare(
                                b.timestamp,
                                a.timestamp
                        );
                    }
                }
        );

        return result;
    }

    public static void clearTransactions(Context context) {
        prefs(context)
                .edit()
                .remove(TRANSACTIONS)
                .apply();
    }

    public static String money(long cents) {
        return String.format(
                java.util.Locale.US,
                "$%.2f",
                cents / 100.0
        );
    }
}
