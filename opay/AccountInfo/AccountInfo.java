package com.pay.opay.AccountInfo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Singleton class for managing account information with persistent storage.
 * Must be initialized once in the Application class before use.
 */
public class AccountInfo {
    private static final String PREFS_NAME = "AccountInfoPrefs";
    private static final String TAG = "AccountInfo";

    // Singleton instance - thread-safe lazy initialization
    private static volatile AccountInfo instance;
    private static SharedPreferences sharedPreferences;

    // Account data fields
    private String amount;
    private String userAccount;
    private String userNumber;
    private String rootAccount;
    private String rootNumber;
    private String userBank;
    private String rootBank;
    private int Response;
    private int bankUpdate;
    private int activebank;
    private String accountType;
    private String longdatetime;
    private String shortdatetime;

    // Private constructor to prevent direct instantiation
    private AccountInfo() {
        // Initialize with default values if needed
    }

    /**
     * Initialize the singleton instance. Must be called once in Application class.
     * @param context Application context
     */
    public static void initialize(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }

        synchronized (AccountInfo.class) {
            if (sharedPreferences == null) {
                sharedPreferences = context.getApplicationContext()
                        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

                // Load data after SharedPreferences is initialized
                if (instance != null) {
                    instance.loadFromPreferences();
                }
            }
        }
    }

    /**
     * Get the singleton instance.
     * @return AccountInfo instance
     * @throws IllegalStateException if not initialized
     */
    public static AccountInfo getInstance() {
        if (sharedPreferences == null) {
            throw new IllegalStateException(
                    "AccountInfo must be initialized first. Call AccountInfo.initialize(context) in your Application class."
            );
        }

        if (instance == null) {
            synchronized (AccountInfo.class) {
                if (instance == null) {
                    instance = new AccountInfo();
                    instance.loadFromPreferences();
                }
            }
        }
        return instance;
    }

    /**
     * Load all data from SharedPreferences
     */
    private void loadFromPreferences() {
        if (sharedPreferences == null) return;

        amount = sharedPreferences.getString("amount", null);
        userAccount = sharedPreferences.getString("userAccount", null);
        userNumber = sharedPreferences.getString("userNumber", null);
        rootAccount = sharedPreferences.getString("rootAccount", null);
        rootNumber = sharedPreferences.getString("rootNumber", null);
        userBank = sharedPreferences.getString("userBank", null);
        rootBank = sharedPreferences.getString("rootBank", null);
        Response = sharedPreferences.getInt("Response", 0);
        bankUpdate = sharedPreferences.getInt("bankUpdate", 0);
        activebank = sharedPreferences.getInt("activebank", 0);
        accountType = sharedPreferences.getString("accountType", null);
        longdatetime = sharedPreferences.getString("longdatetime", null);
        shortdatetime = sharedPreferences.getString("shortdatetime", null);
    }

    /**
     * Save all current data to SharedPreferences
     */
    private void saveToPreferences() {
        if (sharedPreferences == null) return;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("amount", amount);
        editor.putString("userAccount", userAccount);
        editor.putString("userNumber", userNumber);
        editor.putString("rootAccount", rootAccount);
        editor.putString("rootNumber", rootNumber);
        editor.putString("userBank", userBank);
        editor.putString("rootBank", rootBank);
        editor.putInt("Response", Response);
        editor.putInt("bankUpdate", bankUpdate);
        editor.putInt("activebank", activebank);
        editor.putString("accountType", accountType);
        editor.putString("longdatetime", longdatetime);
        editor.putString("shortdatetime", shortdatetime);
        editor.apply(); // Use apply() instead of commit() for better performance
    }

    /**
     * Reset all account data to default values and clear from storage
     */
    public synchronized void reset() {
        amount = null;
        userAccount = null;
        userNumber = null;
        rootAccount = null;
        rootNumber = null;
        userBank = null;
        rootBank = null;
        accountType = null;
        shortdatetime = null;
        longdatetime = null;
        Response = 0;
        bankUpdate = 0;
        activebank = 0;

        saveToPreferences();
    }

    /**
     * Clear all data from SharedPreferences (alternative to reset)
     */
    public synchronized void clearAllData() {
        if (sharedPreferences != null) {
            sharedPreferences.edit().clear().apply();
        }
        reset();
    }

    // Getters and Setters - keeping your exact naming conventions

    public String getAmount() {
        return amount;
    }

    public synchronized void setAmount(String amount) {
        this.amount = amount;
        saveToPreferences();
    }

    public String getUserAccount() {
        return userAccount;
    }

    public synchronized void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
        saveToPreferences();
    }

    public String getUserNumber() {
        return userNumber;
    }

    public synchronized void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
        saveToPreferences();
    }

    public String getRootAccount() {
        return rootAccount;
    }

    public synchronized void setRootAccount(String rootAccount) {
        this.rootAccount = rootAccount;
        saveToPreferences();
    }

    public String getRootNumber() {
        return rootNumber;
    }

    public synchronized void setRootNumber(String rootNumber) {
        this.rootNumber = rootNumber;
        saveToPreferences();
    }

    public String getUserBank() {
        return userBank;
    }

    public synchronized void setUserBank(String userBank) {
        this.userBank = userBank;
        saveToPreferences();
    }

    public String getRootBank() {
        return rootBank;
    }

    public synchronized void setRootBank(String rootBank) {
        this.rootBank = rootBank;
        saveToPreferences();
    }

    public int getResponse() {
        return Response;
    }

    public synchronized void setResponse(int Response) {
        this.Response = Response;
        saveToPreferences();
    }

    public int getBankUpdate() {
        return bankUpdate;
    }

    public synchronized void setBankUpdate(int bankUpdate) {
        this.bankUpdate = bankUpdate;
        saveToPreferences();
    }

    public int getActivebank() {
        return activebank;
    }

    public synchronized void setActivebank(int activebank) {
        this.activebank = activebank;
        saveToPreferences();
    }

    public String getAccountType() {
        return accountType;
    }

    public synchronized void setAccountType(String accountType) {
        this.accountType = accountType;
        saveToPreferences();
    }

    public String getLongDateTime() {
        return longdatetime;
    }

    public synchronized void setLongDateTime(String longdatetime) {
        this.longdatetime = longdatetime;
        saveToPreferences();
    }

    public String getShortDateTime() {
        return shortdatetime;
    }

    public synchronized void setShortDateTime(String shortdatetime) {
        this.shortdatetime = shortdatetime;
        saveToPreferences();
    }
}