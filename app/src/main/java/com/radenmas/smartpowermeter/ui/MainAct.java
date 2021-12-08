package com.radenmas.smartpowermeter.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.radenmas.smartpowermeter.DataChart;
import com.radenmas.smartpowermeter.R;
import com.radenmas.smartpowermeter.base.BaseActivity;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainAct extends BaseActivity {

    TextView tvTime,
            ampereLampOne, wattLampOne,
            ampereLampTwo, wattLampTwo,
            amperePlug, wattPlug,
            statusLampOne, statusLampTwo, statusPlug;
    ImageView imgProfile, imgInfo, imgLiving, imgBedroom, imgKitchen,
            switchLampOne, switchLampTwo, switchPlug,
            stateLampOne, stateLampTwo, statePlug;
    ViewPager viewPager;
    int iWatt1, iWatt2, iWatt3;
    float fAmpere1, fAmpere2, fAmpere3;
    int a, b, c;

    int maxVolt = 0;
    int maxDaya = 0;


    DecimalFormat koma = new DecimalFormat("#.##");

    private static final String ID = "Notif";
    private static final String NAME = "Smart Power Meter";

    @Override
    protected int getLayoutResource() {
        return R.layout.act_main;
    }

    @Override
    protected void myCodeHere() {
        maxVolt = sharedPreferences.getInt(getResources().getString(R.string.prefVolt), 0);
        maxDaya = sharedPreferences.getInt(getResources().getString(R.string.prefDaya), 0);

        initView();
        setupViewPager(viewPager);
        onClick();
        cekStatus();
        getData();

        Date dt = new Date();
        int hours = dt.getHours();
        if (hours >= 1 && hours <= 11) {
            tvTime.setText("Selamat Pagi");
        } else if (hours > 11 && hours <= 15) {
            tvTime.setText("Selamat Siang");
        } else if (hours > 15 && hours <= 18) {
            tvTime.setText("Selamat Sore");
        } else if (hours > 18 && hours <= 24) {
            tvTime.setText("Selamat Malam");
        } else {
            tvTime.setText("-");
        }


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainFrag(), "Header");
        adapter.addFragment(new VoltFrag(), "Chart");
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    private void getData() {
        DatabaseReference dbLast = FirebaseDatabase.getInstance().getReference("SmartEnergyMeter");
        Query query = dbLast.orderByKey().limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    DataChart dataChart = child.getValue(DataChart.class);

                    if (dataChart.getVolt() > maxVolt && dataChart.getVolt() != 0) {
                        notifMax("Voltase melebihi batas maksimum dari yang telah ditentukan");
                    }

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
                    setOnPower(imgLiving, fAmpere1, iWatt1, ampereLampOne, wattLampOne,
                            switchLampOne, stateLampOne, statusLampOne);
                    notifOn("Lampu Satu");
                } else {
                    a = 1;
                    setOffPower(imgLiving, ampereLampOne, wattLampOne,
                            switchLampOne, stateLampOne, statusLampOne);
                }
                if (b == 1) {
                    b = 0;
                    setOnPower(imgBedroom, fAmpere2, iWatt2, ampereLampTwo, wattLampTwo,
                            switchLampTwo, stateLampTwo, statusLampTwo);
                    notifOn("Lampu Dua");
                } else {
                    b = 1;
                    setOffPower(imgBedroom, ampereLampTwo, wattLampTwo,
                            switchLampTwo, stateLampTwo, statusLampTwo);
                }
                if (c == 1) {
                    c = 0;
                    setOnPower(imgKitchen, fAmpere3, iWatt3, amperePlug, wattPlug,
                            switchPlug, statePlug, statusPlug);
                    notifOn("Stop Kontak");
                } else {
                    c = 1;
                    setOffPower(imgKitchen, amperePlug, wattPlug,
                            switchPlug, statePlug, statusPlug);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void onClick() {
        imgProfile.setOnClickListener(v -> {
            startActivity(new Intent(MainAct.this, InfoAppAct.class));
        });
        imgInfo.setOnClickListener(v -> {
            startActivity(new Intent(MainAct.this, SettingsAct.class));
        });

        switchLampOne.setOnClickListener(v -> {
            if (a == 1) {
                a = 0;
                dbReff.child("lampu_1").setValue(1);
                setOnPower(imgLiving, fAmpere1, iWatt1, ampereLampOne, wattLampOne,
                        switchLampOne, stateLampOne, statusLampOne);
                notifOn("Lampu Satu");
            } else {
                a = 1;
                dbReff.child("lampu_1").setValue(0);
                setOffPower(imgLiving, ampereLampOne, wattLampOne,
                        switchLampOne, stateLampOne, statusLampOne);
            }
        });

        switchLampTwo.setOnClickListener(v -> {
            if (b == 1) {
                b = 0;
                dbReff.child("lampu_2").setValue(1);
                setOnPower(imgBedroom, fAmpere2, iWatt2, ampereLampTwo, wattLampTwo,
                        switchLampTwo, stateLampTwo, statusLampTwo);
                notifOn("Lampu Dua");
            } else {
                b = 1;
                dbReff.child("lampu_2").setValue(0);
                setOffPower(imgBedroom, ampereLampTwo, wattLampTwo,
                        switchLampTwo, stateLampTwo, statusLampTwo);
            }
        });

        switchPlug.setOnClickListener(v -> {
            if (c == 1) {
                c = 0;
                dbReff.child("lampu_3").setValue(1);
                setOnPower(imgKitchen, fAmpere3, iWatt3, amperePlug, wattPlug,
                        switchPlug, statePlug, statusPlug);
                notifOn("Stop Kontak");
            } else {
                c = 1;
                dbReff.child("lampu_3").setValue(0);
                setOffPower(imgKitchen, amperePlug, wattPlug,
                        switchPlug, statePlug, statusPlug);
            }
        });
    }

    private void setOffPower(ImageView imgIcon, TextView ampere, TextView watt, ImageView switc, ImageView state, TextView status) {
        imgIcon.setColorFilter(Color.argb(255, 222, 222, 222));
        ampere.setText(R.string.value_a);
        watt.setText(R.string.value_p);
        switc.setImageResource(R.drawable.ic_power_off);
        state.setVisibility(View.INVISIBLE);
        status.setText(R.string.status_off);
    }

    private void setOnPower(ImageView imgIcon, float strampere, float strwatt, TextView ampere, TextView watt, ImageView switc, ImageView state, TextView status) {
        imgIcon.setColorFilter(Color.argb(255, 3, 169, 244));
        ampere.setText("A : " + koma.format(strampere) + " A");
        watt.setText("W: " + koma.format(strwatt) + " W");
        switc.setImageResource(R.drawable.ic_power_on);
        state.setVisibility(View.VISIBLE);
        status.setText(R.string.status_on);

        if (strampere > maxDaya && strampere != 0) {
            notifMax("Penggunaan daya melebihi maksimum penggunaan dari yang telah ditentukan");
        }
    }

    private void initView() {
        viewPager = findViewById(R.id.viewpager);

        tvTime = findViewById(R.id.tvTime);

        ampereLampOne = findViewById(R.id.ampereLampOne);
        wattLampOne = findViewById(R.id.wattLampOne);

        amperePlug = findViewById(R.id.amperePlug);
        wattPlug = findViewById(R.id.wattPlug);

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

        imgInfo = findViewById(R.id.imgSettings);
        imgProfile = findViewById(R.id.imgProfile);
        imgLiving = findViewById(R.id.img_living_room);
        imgBedroom = findViewById(R.id.img_bedroom);
        imgKitchen = findViewById(R.id.img_kitchen);
    }

    private void notifOn(String device) {
        Intent intent = new Intent(this, MainAct.class);
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

    private void notifMax(String device) {
        Intent intent = new Intent(this, MainAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    ID, NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(device);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        long[] v = {500, 1000};
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, ID)
                        .setSmallIcon(R.drawable.ic_wireless_energy_color)
                        .setContentTitle(NAME)
                        .setContentText(device)
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