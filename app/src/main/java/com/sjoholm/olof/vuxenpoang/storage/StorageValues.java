package com.sjoholm.olof.vuxenpoang.storage;

public class StorageValues {
    private static final String KEY_BALANCE = "balance";
    private KeyValueTable keyValueTable;

    public StorageValues(KeyValueTable keyValueTable) {
        this.keyValueTable = keyValueTable;
    }

    public void setBalance(int balance) {
        keyValueTable.putInt(KEY_BALANCE, balance);
    }

    public int getBalance() {
        return keyValueTable.getInt(KEY_BALANCE, 0);
    }
}
