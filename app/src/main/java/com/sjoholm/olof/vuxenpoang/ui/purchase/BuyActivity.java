package com.sjoholm.olof.vuxenpoang.ui.purchase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sjoholm.olof.vuxenpoang.Injection;
import com.sjoholm.olof.vuxenpoang.R;
import com.sjoholm.olof.vuxenpoang.model.Purchase;
import com.sjoholm.olof.vuxenpoang.ui.shared.GridPagerAdapter;
import com.sjoholm.olof.vuxenpoang.ui.shared.ReceiptView;

import java.util.List;

public class BuyActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private TextView currentPageText;
    private GridPagerAdapter gridAdapter;
    private ReceiptView receipt;
    private int balance;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_main);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.addOnPageChangeListener(this);

        currentPageText = (TextView) findViewById(R.id.pager_progress);

        receipt = new ReceiptView((ViewGroup) findViewById(R.id.receipt));
        balance = Injection.getRepository(this).fetchBalance();
        receipt.setBalance(balance);

        final Button button = (Button) findViewById(R.id.btn_buy);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = receipt.getTotal();
                if (total == 0) {
                    return;
                }
                balance -= total;
                receipt.clear();
                receipt.setBalance(balance);
                Injection.getRepository(BuyActivity.this).setBalance(balance);
                String msg = getResources().getString(R.string.buy_toast_sold, total);
                Toast.makeText(BuyActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.add_purchase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyActivity.this, PurchaseItemsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Purchase> sales = Injection.getRepository(this).fetchPurchases();
        gridAdapter = new PurchasesGridPagerAdapter(sales);
        viewPager.setAdapter(gridAdapter);
        currentPageText.setText(pagerProgressText(1, gridAdapter.getCount()));
    }

    private String pagerProgressText(int currentPage, int totalPages) {
        return String.format("%s/%s", currentPage, totalPages);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPageText.setText(pagerProgressText(position + 1, gridAdapter.getCount()));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class PurchasesGridPagerAdapter extends GridPagerAdapter<Purchase> {

        public PurchasesGridPagerAdapter(List<Purchase> list) {
            super(list);
        }

        @Override
        public void onItemClicked(Purchase expense) {
            receipt.add(expense);
        }

        @Override
        public void populateChild(View view, Purchase item) {
            ((TextView) view.findViewById(R.id.title)).setText(item.name);
            ((TextView) view.findViewById(R.id.cost)).setText(String.valueOf(item.cost));
        }
    }
}