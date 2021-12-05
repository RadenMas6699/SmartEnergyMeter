package com.radenmas.smartpowermeter.ui;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import com.radenmas.smartpowermeter.R;
import com.radenmas.smartpowermeter.base.BaseActivity;

public class InfoAppAct extends BaseActivity {
    private TextView app_version;

    @Override
    protected int getLayoutResource() {
        return R.layout.act_info_app;
    }

    @Override
    protected void myCodeHere() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Context context = getApplicationContext();

        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = info.versionName;

        app_version = findViewById(R.id.app_version);
        app_version.setText("Version " + version);
    }

    public void Back(View view) {
        onBackPressed();
    }
}