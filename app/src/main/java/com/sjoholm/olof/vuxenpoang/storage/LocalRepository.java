package com.sjoholm.olof.vuxenpoang.storage;

import android.content.Context;
import android.support.annotation.NonNull;

import com.sjoholm.olof.vuxenpoang.model.Purchase;
import com.sjoholm.olof.vuxenpoang.model.Sale;

import java.util.List;

public class LocalRepository implements Repository {
    private final SalesTable salesTable;
    private final StorageValues storageValues;
    private final PurchaseTable purchaseTable;

    public LocalRepository(@NonNull Context context) {
        Database database = new Database(context);
        salesTable = database.getSalesTable();
        purchaseTable = database.getPurchaseTable();
        storageValues = new StorageValues(database.getKeyValueTable());
    }

    @Override
    public Sale createSale(String name, int cost) {
        return salesTable.createSale(name, cost);
    }

    @Override
    public void syncSales(@NonNull List<Sale> expenses) {
        salesTable.update(expenses);
    }

    @Override
    public List<Sale> fetchSales() {
        return salesTable.getItems();
    }

    @Override
    public Purchase createPurchase(String name, int cost) {
        return purchaseTable.createPurchase(name, cost);
    }

    @Override
    public void syncPurchases(@NonNull List<Purchase> purchases) {
        purchaseTable.update(purchases);
    }

    @Override
    public List<Purchase> fetchPurchases() {
        return purchaseTable.getItems();
    }

    @Override
    public void setBalance(int balance) {
        storageValues.setBalance(balance);
    }

    @Override
    public int fetchBalance() {
        return storageValues.getBalance();
    }
}
