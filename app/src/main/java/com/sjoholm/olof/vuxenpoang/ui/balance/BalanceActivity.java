package com.sjoholm.olof.vuxenpoang.ui.balance;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sjoholm.olof.vuxenpoang.R;
import com.sjoholm.olof.vuxenpoang.ui.purchase.BuyActivity;
import com.sjoholm.olof.vuxenpoang.ui.sale.SellActivity;

public class BalanceActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance_main);

        findViewById(R.id.btn_buy).setOnClickListener(this);
        findViewById(R.id.btn_sell).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_buy:
                startActivity(new Intent(this, BuyActivity.class));
                break;
            case R.id.btn_sell:
                startActivity(new Intent(this, SellActivity.class));
                break;
        }
    }
}
