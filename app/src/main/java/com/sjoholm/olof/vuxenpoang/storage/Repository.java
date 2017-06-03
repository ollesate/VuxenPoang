package com.sjoholm.olof.vuxenpoang.storage;

import android.support.annotation.NonNull;

import com.sjoholm.olof.vuxenpoang.model.Purchase;
import com.sjoholm.olof.vuxenpoang.model.Sale;

import java.util.List;

public interface Repository {

    void syncSales(@NonNull List<Sale> sales);

    List<Sale> fetchSales();

    void syncPurchases(@NonNull List<Purchase> purchases);

    List<Purchase> fetchPurchases();

    void setBalance(int balance);

    int fetchBalance();
}
