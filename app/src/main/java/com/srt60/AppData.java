package com.srt60;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppData {

    private static final String PREFS_NAME = "app_data";
    private static final String KEY_BALANCE = "balance";

    // ========== TRANSACTION CLASS ==========
    public static class Transaction {
        public long id;
        public String person;
        public double amount;
        public String type;
        public long timestamp;
        public String username;
        public String note;
        public boolean isIncoming;

        public Transaction() {
            this.id = System.currentTimeMillis();
            this.timestamp = System.currentTimeMillis();
        }

        public Transaction(String person, double amount, String type) {
            this.id = System.currentTimeMillis();
            this.person = person;
            this.amount = amount;
            this.type = type;
            this.timestamp = System.currentTimeMillis();
        }
    }

    // ========== CONTACT CLASS ==========
    public static class Contact {
        public String name;
        public String username;
        public String avatar;
        public boolean isSelected;

        public Contact(String name, String username, String avatar) {
            this.name = name;
            this.username = username;
            this.avatar = avatar;
            this.isSelected = false;
        }
    }

    // ========== POOL CLASS ==========
    public static class Pool {
        public String id;
        public String name;
        public double amount;
        public String creator;
        public long timestamp;
        public List<String> members;
        public boolean isActive;

        public Pool() {
            this.id = String.valueOf(System.currentTimeMillis());
            this.timestamp = System.currentTimeMillis();
            this.members = new ArrayList<>();
            this.isActive = true;
        }

        public Pool(String name, double amount, String creator) {
            this.id = String.valueOf(System.currentTimeMillis());
            this.name = name;
            this.amount = amount;
            this.creator = creator;
            this.timestamp = System.currentTimeMillis();
            this.members = new ArrayList<>();
            this.members.add(creator);
            this.isActive = true;
        }
    }

    // ========== BALANCE METHODS ==========
    public static double getBalance(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getFloat(KEY_BALANCE, 2976f);
    }

    public static void setBalance(Context context, double balance) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putFloat(KEY_BALANCE, (float) balance).apply();
    }

    public static boolean subtractBalance(Context context, double amount) {
        double balance = getBalance(context);
        if (balance >= amount) {
            setBalance(context, balance - amount);
            return true;
        }
        return false;
    }

    public static void addBalance(Context context, double amount) {
        double balance = getBalance(context);
        setBalance(context, balance + amount);
    }

    // ========== TRANSACTION METHODS ==========
    public static ArrayList<Transaction> getTransactions(Context context) {
        // For demo, return some sample transactions
        ArrayList<Transaction> transactions = new ArrayList<>();
        
        // Sample transactions
        Transaction t1 = new Transaction();
        t1.id = 1;
        t1.person = "River Lee";
        t1.amount = 20;
        t1.type = "PAY";
        t1.timestamp = System.currentTimeMillis() - 86400000; // Yesterday
        t1.username = "@river";
        t1.note = "Snack Wagon 🥣";
        transactions.add(t1);

        Transaction t2 = new Transaction();
        t2.id = 2;
        t2.person = "Alyssa Smith";
        t2.amount = 15;
        t2.type = "REQUEST";
        t2.timestamp = System.currentTimeMillis() - 172800000; // 2 days ago
        t2.username = "@alyssa";
        transactions.add(t2);

        Transaction t3 = new Transaction();
        t3.id = 3;
        t3.person = "Snack Wagon";
        t3.amount = 20;
        t3.type = "PAY";
        t3.timestamp = System.currentTimeMillis() - 259200000; // 3 days ago
        t3.note = "🥣 Lunch";
        transactions.add(t3);

        return transactions;
    }

    public static void addTransaction(Context context, Transaction transaction) {
        ArrayList<Transaction> transactions = getTransactions(context);
        transactions.add(transaction);
        // In a real app, save to SharedPreferences or SQLite
        saveTransactions(context, transactions);
    }

    public static Transaction getTransactionById(Context context, long id) {
        ArrayList<Transaction> transactions = getTransactions(context);
        for (Transaction t : transactions) {
            if (t.id == id) {
                return t;
            }
        }
        return null;
    }

    private static void saveTransactions(Context context, ArrayList<Transaction> transactions) {
        // For demo, we'll just store in SharedPreferences as a string
        // In a real app, use Gson or SQLite
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // Simple save - just store count
        prefs.edit().putInt("transaction_count", transactions.size()).apply();
    }

    // ========== CONTACT METHODS ==========
    public static List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("Alexander", "@alex", "A"));
        contacts.add(new Contact("River Lee", "@river", "R"));
        contacts.add(new Contact("Alyssa Smith", "@alyssa", "A"));
        contacts.add(new Contact("KoKo", "@koko", "K"));
        contacts.add(new Contact("Sava", "@sava", "S"));
        contacts.add(new Contact("Snack Wagon", "@snackwagon", "S"));
        contacts.add(new Contact("Sarah Johnson", "@sarahj", "S"));
        contacts.add(new Contact("Mike Chen", "@mikec", "M"));
        return contacts;
    }

    public static Contact getContactByUsername(String username) {
        List<Contact> contacts = getContacts();
        for (Contact contact : contacts) {
            if (contact.username.equals(username)) {
                return contact;
            }
        }
        return null;
    }

    // ========== POOL METHODS ==========
    public static ArrayList<Pool> getPools(Context context) {
        ArrayList<Pool> pools = new ArrayList<>();
        
        // Sample pools
        Pool p1 = new Pool("Dinner Group", 50, "@river");
        p1.members.add("@alex");
        p1.members.add("@alyssa");
        pools.add(p1);

        Pool p2 = new Pool("Weekend Trip", 100, "@alex");
        p2.members.add("@sarahj");
        p2.members.add("@mikec");
        pools.add(p2);

        return pools;
    }

    public static Pool getPoolById(Context context, String id) {
        ArrayList<Pool> pools = getPools(context);
        for (Pool pool : pools) {
            if (pool.id.equals(id)) {
                return pool;
            }
        }
        return null;
    }

    public static void createPool(Context context, Pool pool) {
        ArrayList<Pool> pools = getPools(context);
        pools.add(pool);
        savePools(context, pools);
    }

    public static void addMemberToPool(Context context, String poolId, String member) {
        ArrayList<Pool> pools = getPools(context);
        for (Pool pool : pools) {
            if (pool.id.equals(poolId)) {
                if (!pool.members.contains(member)) {
                    pool.members.add(member);
                }
                break;
            }
        }
        savePools(context, pools);
    }

    private static void savePools(Context context, ArrayList<Pool> pools) {
        // In a real app, save to SharedPreferences or SQLite
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt("pool_count", pools.size()).apply();
    }

    // ========== HELPER METHODS ==========
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

    public static String formatDateFull(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        return sdf.format(new Date(timestamp));
    }

    public static String formatTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
        return sdf.format(new Date(timestamp));
    }

    public static String getUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString("username", "$iranmoneyyy");
    }

    public static void setUsername(Context context, String username) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString("username", username).apply();
    }
}
