package com.sjoholm.olof.vuxenpoang.storage;

import android.support.annotation.NonNull;

import com.sjoholm.olof.vuxenpoang.model.Purchase;
import com.sjoholm.olof.vuxenpoang.model.Sale;

import java.util.List;

public interface Repository {

    Sale createSale(String name, int cost);

    void syncSales(@NonNull List<Sale> sales);

    List<Sale> fetchSales();

    Purchase createPurchase(String name, int cost);

    void syncPurchases(@NonNull List<Purchase> purchases);

    List<Purchase> fetchPurchases();

    void setBalance(int balance);

    int fetchBalance();
}
