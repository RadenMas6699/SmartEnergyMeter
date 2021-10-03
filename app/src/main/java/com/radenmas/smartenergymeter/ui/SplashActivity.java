package com.radenmas.smartenergymeter.ui;

import android.content.Intent;
import android.os.Handler;

import com.radenmas.smartenergymeter.R;
import com.radenmas.smartenergymeter.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void myCodeHere() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 1500);
    }
}