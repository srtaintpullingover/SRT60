package com.srt60;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public final class AppData {

    private static final String PREFS = "CashAppClone";
    private static final String KEY_BALANCE = "cash_balance";
    private static final String KEY_TRANSACTIONS = "transactions";
    private static final String KEY_POOLS = "pools";

    private AppData() {}

    public static class Contact {
        public String id;
        public String name;
        public String username;
        public String phone;
        public String initials;

        public Contact(String id, String name, String username,
                       String phone, String initials) {
            this.id = id;
            this.name = name;
            this.username = username;
            this.phone = phone;
            this.initials = initials;
        }
    }

    public static class Transaction {
        public String id;
        public String type;
        public String person;
        public String username;
        public String note;
        public double amount;
        public long timestamp;
        public boolean completed;

        public Transaction(String type, String person, String username,
                           double amount, String note) {
            this.id = UUID.randomUUID().toString();
            this.type = type;
            this.person = person;
            this.username = username;
            this.amount = amount;
            this.note = note;
            this.timestamp = System.currentTimeMillis();
            this.completed = true;
        }
    }

    public static class Pool {
        public String id;
        public String name;
        public String description;
        public double goal;
        public double balance;
        public ArrayList<String> members = new ArrayList<>();

        public Pool(String name, String description, double goal) {
            this.id = UUID.randomUUID().toString();
            this.name = name;
            this.description = description;
            this.goal = goal;
            this.balance = 0;
        }
    }

    public static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static double getBalance(Context context) {
        return Double.longBitsToDouble(
                prefs(context).getLong(
                        KEY_BALANCE,
                        Double.doubleToLongBits(5.00)
                )
        );
    }

    public static void setBalance(Context context, double balance) {
        prefs(context).edit()
                .putLong(KEY_BALANCE, Double.doubleToLongBits(balance))
                .apply();
    }

    public static void addBalance(Context context, double amount) {
        setBalance(context, getBalance(context) + amount);
    }

    public static boolean subtractBalance(Context context, double amount) {
        double balance = getBalance(context);

        if (amount < 0 || amount > balance) {
            return false;
        }

        setBalance(context, balance - amount);
        return true;
    }

    public static List<Contact> getContacts() {
        ArrayList<Contact> contacts = new ArrayList<>();

        contacts.add(new Contact(
                "alexus",
                "Alexus Sims",
                "$alexus",
                "555-0101",
                "A"
        ));

        contacts.add(new Contact(
                "koko",
                "KoKo",
                "$koko",
                "555-0102",
                "K"
        ));

        contacts.add(new Contact(
                "jaiwain",
                "Jaiwain Small",
                "$jaiwain",
                "555-0103",
                "J"
        ));

        contacts.add(new Contact(
                "savage",
                "Savage",
                "$savage",
                "555-0104",
                "S"
        ));

        contacts.add(new Contact(
                "cash",
                "Savings",
                "$savings",
                "",
                "$"
        ));

        return contacts;
    }

    public static void addTransaction(
            Context context,
            Transaction transaction
    ) {
        JSONArray array = getTransactionsJson(context);

        JSONObject object = new JSONObject();

        try {
            object.put("id", transaction.id);
            object.put("type", transaction.type);
            object.put("person", transaction.person);
            object.put("username", transaction.username);
            object.put("note", transaction.note);
            object.put("amount", transaction.amount);
            object.put("timestamp", transaction.timestamp);
            object.put("completed", transaction.completed);

            array.put(object);

            prefs(context).edit()
                    .putString(KEY_TRANSACTIONS, array.toString())
                    .apply();

        } catch (JSONException ignored) {
        }
    }

    public static ArrayList<Transaction> getTransactions(Context context) {
        ArrayList<Transaction> result = new ArrayList<>();
        JSONArray array = getTransactionsJson(context);

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);

                Transaction transaction = new Transaction(
                        object.optString("type"),
                        object.optString("person"),
                        object.optString("username"),
                        object.optDouble("amount"),
                        object.optString("note")
                );

                transaction.id = object.optString("id");
                transaction.timestamp = object.optLong("timestamp");
                transaction.completed =
                        object.optBoolean("completed", true);

                result.add(transaction);

            } catch (JSONException ignored) {
            }
        }

        return result;
    }

    private static JSONArray getTransactionsJson(Context context) {
        String saved = prefs(context).getString(KEY_TRANSACTIONS, "");

        if (saved.isEmpty()) {
            return new JSONArray();
        }

        try {
            return new JSONArray(saved);
        } catch (JSONException e) {
            return new JSONArray();
        }
    }

    public static String formatMoney(double amount) {
        return String.format(Locale.US, "$%.2f", amount);
    }

    public static String formatDate(long timestamp) {
        return DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM,
                DateFormat.SHORT,
                Locale.US
        ).format(new Date(timestamp));
    }

    public static void savePool(Context context, Pool pool) {
        JSONArray array = getPoolsJson(context);

        JSONObject object = new JSONObject();

        try {
            object.put("id", pool.id);
            object.put("name", pool.name);
            object.put("description", pool.description);
            object.put("goal", pool.goal);
            object.put("balance", pool.balance);

            JSONArray members = new JSONArray();

            for (String member : pool.members) {
                members.put(member);
            }

            object.put("members", members);

            array.put(object);

            prefs(context).edit()
                    .putString(KEY_POOLS, array.toString())
                    .apply();

        } catch (JSONException ignored) {
        }
    }

    public static ArrayList<Pool> getPools(Context context) {
        ArrayList<Pool> result = new ArrayList<>();
        JSONArray array = getPoolsJson(context);

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);

                Pool pool = new Pool(
                        object.optString("name"),
                        object.optString("description"),
                        object.optDouble("goal")
                );

                pool.id = object.optString("id");
                pool.balance = object.optDouble("balance");

                JSONArray members = object.optJSONArray("members");

                if (members != null) {
                    for (int j = 0; j < members.length(); j++) {
                        pool.members.add(members.optString(j));
                    }
                }

                result.add(pool);

            } catch (JSONException ignored) {
            }
        }

        return result;
    }

    private static JSONArray getPoolsJson(Context context) {
        String saved = prefs(context).getString(KEY_POOLS, "");

        if (saved.isEmpty()) {
            return new JSONArray();
        }

        try {
            return new JSONArray(saved);
        } catch (JSONException e) {
            return new JSONArray();
        }
    }
              }
