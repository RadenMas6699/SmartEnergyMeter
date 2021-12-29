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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.radenmas.smartpowermeter.DataChart;
import com.radenmas.smartpowermeter.R;
import com.radenmas.smartpowermeter.base.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class ControlFrag extends BaseFragment {
    private TextView ampereLampOne, wattLampOne,
            ampereLampTwo, wattLampTwo,
            amperePlug, wattPlug,
            statusLampOne, statusLampTwo, statusPlug;
    private ImageView imgLiving, imgBedroom, imgKitchen,
            switchLampOne, switchLampTwo, switchPlug,
            stateLampOne, stateLampTwo, statePlug;

    int iWatt1, iWatt2, iWatt3;
    float fAmpere1, fAmpere2, fAmpere3;
    int a, b, c;
    int maxVolt ;
    int maxDaya ;

    DecimalFormat koma = new DecimalFormat("#.##");

    private static final String ID = "Notif";
    private static final String NAME = "Smart Power Meter";

    @Override
    protected int getLayoutResource() {
        return R.layout.frag_control;
    }

    @Override
    protected void myCodeHere(View view) {
        maxVolt = sharedPreferences.getInt(getResources().getString(R.string.prefVolt), 230);
        maxDaya = sharedPreferences.getInt(getResources().getString(R.string.prefDaya), 900);

        initView(view);
        onClick();
        cekStatus();
        getData();
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
                        notifMax("Voltage melebihi batas maksimum dari yang telah ditentukan");
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
        ampere.setText("I\t\t: " + koma.format(strampere) + " A");
        watt.setText("P\t: " + koma.format(strwatt) + " W");
        switc.setImageResource(R.drawable.ic_power_on);
        state.setVisibility(View.VISIBLE);
        status.setText(R.string.status_on);

        if (strampere > maxDaya && strampere != 0) {
            notifMax("Penggunaan Daya melebihi maksimum penggunaan dari yang telah ditentukan");
        }
    }

    private void notifOn(String device) {
        Intent intent = new Intent(getContext(), MainAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    ID, NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(device + " menyala");
            NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        long[] v = {500, 1000};
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext(), ID)
                        .setSmallIcon(R.drawable.ic_wireless_energy_color)
                        .setContentTitle(NAME)
                        .setContentText(device + " menyala")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setVibrate(v)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, mBuilder.build());
    }

    private void notifMax(String device) {
        Intent intent = new Intent(getContext(), MainAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    ID, NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(device);
            NotificationManager manager = getContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        long[] v = {500, 1000};
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext(), ID)
                        .setSmallIcon(R.drawable.ic_wireless_energy_color)
                        .setContentTitle(NAME)
                        .setContentText(device)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setVibrate(v)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, mBuilder.build());
    }

    private void onClick() {
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

    private void initView(View view) {
        ampereLampOne = view.findViewById(R.id.ampereLampOne);
        wattLampOne = view.findViewById(R.id.wattLampOne);

        amperePlug = view.findViewById(R.id.amperePlug);
        wattPlug = view.findViewById(R.id.wattPlug);

        ampereLampTwo = view.findViewById(R.id.ampereLampTwo);
        wattLampTwo = view.findViewById(R.id.wattLampTwo);

        switchLampOne = view.findViewById(R.id.imgSwitchLampOne);
        switchPlug = view.findViewById(R.id.imgSwitchPlug);
        switchLampTwo = view.findViewById(R.id.imgSwitchLampTwo);

        stateLampOne = view.findViewById(R.id.imgStateLampOne);
        statePlug = view.findViewById(R.id.imgStatePlug);
        stateLampTwo = view.findViewById(R.id.imgStateLampTwo);

        statusLampOne = view.findViewById(R.id.tvStatusLampOne);
        statusPlug = view.findViewById(R.id.tvStatusPlug);
        statusLampTwo = view.findViewById(R.id.tvStatusLampTwo);

        imgLiving = view.findViewById(R.id.img_living_room);
        imgBedroom = view.findViewById(R.id.img_bedroom);
        imgKitchen = view.findViewById(R.id.img_kitchen);
    }
}
