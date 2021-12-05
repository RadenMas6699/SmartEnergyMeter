package com.radenmas.smartpowermeter.ui;

import android.content.Intent;
import android.os.Handler;

import com.radenmas.smartpowermeter.R;
import com.radenmas.smartpowermeter.base.BaseActivity;

public class SplashAct extends BaseActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.act_splash;
    }

    @Override
    protected void myCodeHere() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashAct.this, MainAct.class));
            finish();
        }, 1500);
    }
}