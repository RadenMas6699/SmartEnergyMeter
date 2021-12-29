package com.radenmas.smartpowermeter.ui;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.radenmas.smartpowermeter.R;
import com.radenmas.smartpowermeter.base.BaseActivity;

public class SettingsAct extends BaseActivity {
    private TextView valWattMax, valVoltMax;
    private RelativeLayout rlWatt, rlVolt;
    private int maxDaya = 0;
    private int maxVolt = 0;

    @Override
    protected int getLayoutResource() {
        return R.layout.act_setting;
    }

    @Override
    protected void myCodeHere() {
        initView();
        onClick();
        getData();
    }

    private void getData() {
        maxVolt = sharedPreferences.getInt(getResources().getString(R.string.prefVolt), 230);
        maxDaya = sharedPreferences.getInt(getResources().getString(R.string.prefDaya), 900);

        valWattMax.setText(maxDaya + " W");
        valVoltMax.setText(maxVolt + " V");
    }

    private void onClick() {
        rlWatt.setOnClickListener(view -> {
            showDialog(getResources().getString(R.string.daya), getResources().getString(R.string.desc_daya), getResources().getString(R.string.prefDaya));

        });

        rlVolt.setOnClickListener(view -> {
            showDialog(getResources().getString(R.string.voltage), getResources().getString(R.string.desc_voltage), getResources().getString(R.string.prefVolt));
        });
    }

    private void showDialog(String title, String desc, String preff) {
        final Dialog dialog = new Dialog(SettingsAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_settings);

        final TextView tvTitleSettings = dialog.findViewById(R.id.tvTitleSettings);
        final TextView tvDescSettings = dialog.findViewById(R.id.tvDescSettings);
        final EditText etValueSettings = dialog.findViewById(R.id.etValueSettings);
        MaterialButton btnSaveSettings = dialog.findViewById(R.id.btnSaveSettings);

        tvTitleSettings.setText(title);
        tvDescSettings.setText(desc);

        btnSaveSettings.setOnClickListener(v -> {
            String valueMax = etValueSettings.getText().toString();
            if (valueMax.isEmpty()) {
                Toast.makeText(this, "Masukkan nilai maksimum", Toast.LENGTH_SHORT).show();
            } else {
                int value = Integer.parseInt(valueMax);
                editor.putInt(preff, value);
                editor.apply();
                dialog.dismiss();
                Toast.makeText(this, "Berhasil disimpan", Toast.LENGTH_SHORT).show();
                getData();
            }
        });

        dialog.show();
    }

    private void initView() {
        valWattMax = findViewById(R.id.valWattMax);
        valVoltMax = findViewById(R.id.valVoltMax);
        rlWatt = findViewById(R.id.rlSetWatt);
        rlVolt = findViewById(R.id.rlSetVolt);
    }

    public void Back(View view) {
        onBackPressed();
    }
}
