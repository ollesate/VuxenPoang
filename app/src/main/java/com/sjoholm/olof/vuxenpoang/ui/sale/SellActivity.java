package com.sjoholm.olof.vuxenpoang.ui.sale;

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
import com.sjoholm.olof.vuxenpoang.model.Sale;
import com.sjoholm.olof.vuxenpoang.ui.purchase.PurchaseItemsActivity;
import com.sjoholm.olof.vuxenpoang.ui.shared.GridPagerAdapter;
import com.sjoholm.olof.vuxenpoang.ui.shared.ReceiptView;

import java.util.List;

public class SellActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private TextView currentPageText;
    private GridPagerAdapter gridAdapter;
    private ReceiptView receipt;
    private int balance;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_main);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.addOnPageChangeListener(this);

        currentPageText = (TextView) findViewById(R.id.pager_progress);

        receipt = new ReceiptView((ViewGroup) findViewById(R.id.receipt));
        balance = Injection.getRepository(this).fetchBalance();
        receipt.setBalance(balance);

        final Button button = (Button) findViewById(R.id.btn_sell);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = receipt.getTotal();
                if (total == 0) {
                    return;
                }
                balance += total;
                receipt.clear();
                receipt.setBalance(balance);
                Injection.getRepository(SellActivity.this).setBalance(balance);
                String msg = getResources().getString(R.string.sell_toast_sold, total);
                Toast.makeText(SellActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.add_income).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellActivity.this, SalesItemsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Sale> sales = Injection.getRepository(this).fetchSales();
        gridAdapter = new IncomeGridPagerAdapter(sales);
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

    private class IncomeGridPagerAdapter extends GridPagerAdapter<Sale> {

        public IncomeGridPagerAdapter(List<Sale> list) {
            super(list);
        }

        @Override
        public void onItemClicked(Sale sale) {
            receipt.add(sale);
        }

        @Override
        public void populateChild(View view, Sale item) {
            ((TextView) view.findViewById(R.id.title)).setText(item.name);
            ((TextView) view.findViewById(R.id.cost)).setText(String.valueOf(item.cost));
        }
    }
}
