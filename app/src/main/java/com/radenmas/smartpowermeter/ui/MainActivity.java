package com.radenmas.smartpowermeter.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.radenmas.smartpowermeter.DataChart;
import com.radenmas.smartpowermeter.InfoAppActivity;
import com.radenmas.smartpowermeter.R;
import com.radenmas.smartpowermeter.base.BaseActivity;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class MainActivity extends BaseActivity {
    private TextView voltLampOne, ampereLampOne, wattLampOne,
            voltLampTwo, ampereLampTwo, wattLampTwo,
            voltPlug, amperePlug, wattPlug,
            statusLampOne, statusLampTwo, statusPlug;
    private ImageView switchLampOne, switchLampTwo, switchPlug, stateLampOne, stateLampTwo, statePlug;
    private ImageButton imgInfo;
    private RadioGroup radioGroup;
    private RadioButton radioVolt, radioLampOne, radioLampTwo, radioPlug;
    int iWatt1, iWatt2, iWatt3;
    float fAmpere1, fAmpere2, fAmpere3;
    int a, b, c;

    DecimalFormat koma = new DecimalFormat("#.##");

    private static final String ID = "Notif";
    private static final String NAME = "Smart Energy Meter";


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void myCodeHere() {
        initView();
        onClick();
        cekStatus();
        getData();

        selectTextView(radioVolt, radioLampOne, radioLampTwo, radioPlug);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_chart, new VoltFragment()).commit();
    }

    private void getData() {
        DatabaseReference dbLast = FirebaseDatabase.getInstance().getReference("SmartEnergyMeter");
        Query query = dbLast.orderByKey().limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    DataChart dataChart = child.getValue(DataChart.class);

                    voltLampOne.setText("V : " + dataChart.getVolt() + " V");
                    voltLampTwo.setText("V : " + dataChart.getVolt() + " V");
                    voltPlug.setText("V : " + dataChart.getVolt() + " V");

                    fAmpere1 = dataChart.getArus1();
                    fAmpere2 = dataChart.getArus2();
                    fAmpere3 = dataChart.getArus3();

                    iWatt1 = dataChart.getWatt1();
                    iWatt2 = dataChart.getWatt2();
                    iWatt3 = dataChart.getWatt3();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void cekStatus() {
        dbReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String strA = snapshot.child("lampu_1").getValue().toString();
                String strB = snapshot.child("lampu_2").getValue().toString();
                String strC = snapshot.child("lampu_3").getValue().toString();
                a = Integer.parseInt(strA);
                b = Integer.parseInt(strB);
                c = Integer.parseInt(strC);

                if (a == 1) {
                    a = 0;
                    setOnPower(fAmpere1, iWatt1, ampereLampOne, wattLampOne,
                            switchLampOne, stateLampOne, statusLampOne);
                    notif("Lampu Satu");
                } else {
                    a = 1;
                    setOffPower(ampereLampOne, wattLampOne,
                            switchLampOne, stateLampOne, statusLampOne);
                }
                if (b == 1) {
                    b = 0;
                    setOnPower(fAmpere2, iWatt2, ampereLampTwo, wattLampTwo,
                            switchLampTwo, stateLampTwo, statusLampTwo);
                    notif("Lampu Dua");
                } else {
                    b = 1;
                    setOffPower(ampereLampTwo, wattLampTwo,
                            switchLampTwo, stateLampTwo, statusLampTwo);
                }
                if (c == 1) {
                    c = 0;
                    setOnPower(fAmpere3, iWatt3, amperePlug, wattPlug,
                            switchPlug, statePlug, statusPlug);
                    notif("Stop Kontak");
                } else {
                    c = 1;
                    setOffPower(amperePlug, wattPlug,
                            switchPlug, statePlug, statusPlug);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    private void onClick() {
        imgInfo.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, InfoAppActivity.class));
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.radioLampOne:
                        selectTextView(radioLampOne, radioVolt, radioLampTwo, radioPlug);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_chart, new LampOneFragment()).commit();
                        break;
                    case R.id.radioLampTwo:
                        selectTextView(radioLampTwo, radioLampOne, radioVolt, radioPlug);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_chart, new LampTwoFragment()).commit();
                        break;
                    case R.id.radioPlug:
                        selectTextView(radioPlug, radioVolt, radioLampOne, radioLampTwo);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_chart, new PlugFragment()).commit();
                        break;
                    default:
                        selectTextView(radioVolt, radioLampOne, radioLampTwo, radioPlug);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_chart, new VoltFragment()).commit();
                        break;
                }
            }
        });

        switchLampOne.setOnClickListener(v -> {
            if (a == 1) {
                a = 0;
                dbReff.child("lampu_1").setValue(1);
                setOnPower(fAmpere1, iWatt1, ampereLampOne, wattLampOne,
                        switchLampOne, stateLampOne, statusLampOne);
                notif("Lampu Satu");
            } else {
                a = 1;
                dbReff.child("lampu_1").setValue(0);
                setOffPower(ampereLampOne, wattLampOne,
                        switchLampOne, stateLampOne, statusLampOne);
            }
        });

        switchLampTwo.setOnClickListener(v -> {
            if (b == 1) {
                b = 0;
                dbReff.child("lampu_2").setValue(1);
                setOnPower(fAmpere2, iWatt2, ampereLampTwo, wattLampTwo,
                        switchLampTwo, stateLampTwo, statusLampTwo);
                notif("Lampu Dua");
            } else {
                b = 1;
                dbReff.child("lampu_2").setValue(0);
                setOffPower(ampereLampTwo, wattLampTwo,
                        switchLampTwo, stateLampTwo, statusLampTwo);
            }
        });

        switchPlug.setOnClickListener(v -> {
            if (c == 1) {
                c = 0;
                dbReff.child("lampu_3").setValue(1);
                setOnPower(fAmpere3, iWatt3, amperePlug, wattPlug,
                        switchPlug, statePlug, statusPlug);
                notif("Stop Kontak");
            } else {
                c = 1;
                dbReff.child("lampu_3").setValue(0);
                setOffPower(amperePlug, wattPlug,
                        switchPlug, statePlug, statusPlug);
            }
        });
    }

    private void setOffPower(TextView ampere, TextView watt, ImageView switc, ImageView state, TextView status) {
        ampere.setText(R.string.value_a);
        watt.setText(R.string.value_w);
        switc.setImageResource(R.drawable.ic_power_off);
        state.setVisibility(View.INVISIBLE);
        status.setText(R.string.status_off);
    }

    private void setOnPower(float strampere, float strwatt, TextView ampere, TextView watt, ImageView switc, ImageView state, TextView status) {
        ampere.setText("A : " + strampere + " A");
        watt.setText("W: " + koma.format(strampere) + " W");
        switc.setImageResource(R.drawable.ic_power_on);
        state.setVisibility(View.VISIBLE);
        status.setText(R.string.status_on);
    }

    private void initView() {
        radioGroup = findViewById(R.id.radioGroup);
        radioVolt = findViewById(R.id.radioVolt);
        radioLampOne = findViewById(R.id.radioLampOne);
        radioLampTwo = findViewById(R.id.radioLampTwo);
        radioPlug = findViewById(R.id.radioPlug);

        voltLampOne = findViewById(R.id.voltLampOne);
        ampereLampOne = findViewById(R.id.ampereLampOne);
        wattLampOne = findViewById(R.id.wattLampOne);

        voltPlug = findViewById(R.id.voltPlug);
        amperePlug = findViewById(R.id.amperePlug);
        wattPlug = findViewById(R.id.wattPlug);

        voltLampTwo = findViewById(R.id.voltLampTwo);
        ampereLampTwo = findViewById(R.id.ampereLampTwo);
        wattLampTwo = findViewById(R.id.wattLampTwo);

        switchLampOne = findViewById(R.id.imgSwitchLampOne);
        switchPlug = findViewById(R.id.imgSwitchPlug);
        switchLampTwo = findViewById(R.id.imgSwitchLampTwo);

        stateLampOne = findViewById(R.id.imgStateLampOne);
        statePlug = findViewById(R.id.imgStatePlug);
        stateLampTwo = findViewById(R.id.imgStateLampTwo);

        statusLampOne = findViewById(R.id.tvStatusLampOne);
        statusPlug = findViewById(R.id.tvStatusPlug);
        statusLampTwo = findViewById(R.id.tvStatusLampTwo);

        imgInfo = findViewById(R.id.imgInfo);
    }

    private void selectTextView(TextView tvSelected, TextView tvUnselected1, TextView tvUnselected2, TextView tvUnselected3) {
        tvSelected.setBackgroundResource(R.drawable.bg_btn_selected);
        tvUnselected1.setBackgroundResource(R.drawable.bg_btn_unselected);
        tvUnselected2.setBackgroundResource(R.drawable.bg_btn_unselected);
        tvUnselected3.setBackgroundResource(R.drawable.bg_btn_unselected);

        tvSelected.setTextColor(getResources().getColor(R.color.black));
        tvUnselected1.setTextColor(getResources().getColor(android.R.color.white));
        tvUnselected2.setTextColor(getResources().getColor(android.R.color.white));
        tvUnselected3.setTextColor(getResources().getColor(android.R.color.white));
    }

    private void notif(String device) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    ID, NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(device + " menyala");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        long[] v = {500, 1000};
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, ID)
                        .setSmallIcon(R.drawable.ic_wireless_energy_color)
                        .setContentTitle(NAME)
                        .setContentText(device + " menyala")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setVibrate(v)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, mBuilder.build());
    }
}