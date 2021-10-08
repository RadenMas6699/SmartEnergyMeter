package com.radenmas.smartenergymeter.ui;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.radenmas.smartenergymeter.DataChart;
import com.radenmas.smartenergymeter.InfoAppActivity;
import com.radenmas.smartenergymeter.R;
import com.radenmas.smartenergymeter.base.BaseActivity;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class MainActivity extends BaseActivity {
    private TextView tvRoom, voltLivingRoom, ampereLivingRoom, wattLivingRoom,
            voltKitchen, ampereKitchen, wattKitchen,
            voltBedroom, ampereBedroom, wattBedroom,
            statusLivingRoom, statusKitchen, statusBedroom;
    private LinearLayout dotsLayout;
    private int[] layouts;
    private ImageView switchLivingRoom, switchKitchen, switchBedroom, stateLivingRoom, stateKitchen, stateBedroom;
    private ImageButton imgInfo, btnBackRound, btnNextRound;
    int a, b, c;
    int count = 0;

    @Override
    protected int getLayoutResource() {
        return R.layout.main;
    }

    @Override
    protected void myCodeHere() {
        initView();
        onClick();
        cekStatus();
        getData();

        tvRoom.setText("Voltase");

        layouts = new int[]{
                R.layout.fragment_chart,
                R.layout.fragment_chart,
                R.layout.fragment_chart,
                R.layout.fragment_chart};

        addBottomDots(0);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_chart, new VoltFragment()).commit();
    }

    private void getData() {

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
                    switchLivingRoom.setImageResource(R.drawable.ic_power_on);
                    stateLivingRoom.setVisibility(View.VISIBLE);
                    statusLivingRoom.setText(R.string.status_on);
                } else {
                    a = 1;
                    setOffPower(voltLivingRoom, ampereLivingRoom, wattLivingRoom,
                            switchLivingRoom, stateLivingRoom, statusLivingRoom);
                }
                if (b == 1) {
                    b = 0;
                    switchBedroom.setImageResource(R.drawable.ic_power_on);
                    stateBedroom.setVisibility(View.VISIBLE);
                    statusBedroom.setText(R.string.status_on);
                } else {
                    b = 1;
                    setOffPower(voltBedroom, ampereBedroom, wattBedroom,
                            switchBedroom, stateBedroom, statusBedroom);
                }
                if (c == 1) {
                    c = 0;
                    switchKitchen.setImageResource(R.drawable.ic_power_on);
                    stateKitchen.setVisibility(View.VISIBLE);
                    statusKitchen.setText(R.string.status_on);
                } else {
                    c = 1;
                    setOffPower(voltKitchen, ampereKitchen, wattKitchen,
                            switchKitchen, stateKitchen, statusKitchen);
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

        btnBackRound.setOnClickListener(view1 -> {
            if (count <= 0) {
                btnBackRound.setClickable(false);
                btnNextRound.setClickable(true);
            } else {
                btnBackRound.setClickable(true);
                count = count - 1;
                changeChart(count);
                addBottomDots(count);
            }
        });

        btnNextRound.setOnClickListener(view1 -> {
            if (count >= 3) {
                btnBackRound.setClickable(true);
                btnNextRound.setClickable(false);
            } else {
                btnNextRound.setClickable(true);
                count = count + 1;
                changeChart(count);
                addBottomDots(count);
            }
        });

        switchLivingRoom.setOnClickListener(v -> {
            if (a == 1) {
                a = 0;
                dbReff.child("lampu_1").setValue(1);
                voltLivingRoom.setText("220 V");
                ampereLivingRoom.setText("2.5 A");
                wattLivingRoom.setText("354 W");
                switchLivingRoom.setImageResource(R.drawable.ic_power_on);
                stateLivingRoom.setVisibility(View.VISIBLE);
                statusLivingRoom.setText(R.string.status_on);
            } else {
                a = 1;
                dbReff.child("lampu_1").setValue(0);
                setOffPower(voltLivingRoom, ampereLivingRoom, wattLivingRoom,
                        switchLivingRoom, stateLivingRoom, statusLivingRoom);
            }
        });

        switchBedroom.setOnClickListener(v -> {
            if (b == 1) {
                b = 0;
                dbReff.child("lampu_2").setValue(1);
                voltBedroom.setText("220 V");
                ampereBedroom.setText("4 A");
                wattBedroom.setText("350 W");
                switchBedroom.setImageResource(R.drawable.ic_power_on);
                stateBedroom.setVisibility(View.VISIBLE);
                statusBedroom.setText(R.string.status_on);
            } else {
                b = 1;
                dbReff.child("lampu_2").setValue(0);
                setOffPower(voltBedroom, ampereBedroom, wattBedroom,
                        switchBedroom, stateBedroom, statusBedroom);
            }
        });

        switchKitchen.setOnClickListener(v -> {
            if (c == 1) {
                c = 0;
                dbReff.child("lampu_3").setValue(1);
                voltKitchen.setText("220 V");
                ampereKitchen.setText("1 A");
                wattKitchen.setText("30 W");
                switchKitchen.setImageResource(R.drawable.ic_power_on);
                stateKitchen.setVisibility(View.VISIBLE);
                statusKitchen.setText(R.string.status_on);
            } else {
                c = 1;
                dbReff.child("lampu_3").setValue(0);
                setOffPower(voltKitchen, ampereKitchen, wattKitchen,
                        switchKitchen, stateKitchen, statusKitchen);
            }
        });
    }

    private void setOffPower(TextView volt, TextView ampere, TextView watt, ImageView switc, ImageView state, TextView status) {
        volt.setText(R.string.dash);
        ampere.setText(R.string.dash);
        watt.setText(R.string.dash);
        switc.setImageResource(R.drawable.ic_power_off);
        state.setVisibility(View.INVISIBLE);
        status.setText(R.string.status_off);
    }

    private void setOnPower(String path, TextView volt, TextView ampere, TextView watt, ImageView switc, ImageView state, TextView status) {
        dbReff.child("SmartEnergyMeter").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataChart data = dataSnapshot.getValue(DataChart.class);
                    DecimalFormat koma = new DecimalFormat("#.##");

                    volt.setText(data.getVolt() + " V");
                    ampere.setText("" + data.getArus1() + " A");
                    watt.setText("" + data.getWatt1()+" W");
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        volt.setText(R.string.dash);
        ampere.setText(R.string.dash);
        watt.setText(R.string.dash);
        switc.setImageResource(R.drawable.ic_power_on);
        state.setVisibility(View.VISIBLE);
        status.setText(R.string.status_on);
    }

    private void changeChart(int count) {
        Fragment selectedFragment = null;
        switch (count) {
            case 0:
                selectedFragment = new VoltFragment();
                tvRoom.setText("Voltase");
                break;
            case 1:
                selectedFragment = new LivingRoomFragment();
                tvRoom.setText("Living Room");
                break;
            case 2:
                selectedFragment = new BedroomFragment();
                tvRoom.setText("Bedroom");
                break;
            case 3:
                selectedFragment = new KitchenFragment();
                tvRoom.setText("Kitchen");
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_chart, selectedFragment).commit();
        return;
    }

    private void initView() {
        tvRoom = findViewById(R.id.tv_room);
        dotsLayout = findViewById(R.id.layout_dots);

        voltLivingRoom = findViewById(R.id.voltLivingRoom);
        ampereLivingRoom = findViewById(R.id.ampereLivingRoom);
        wattLivingRoom = findViewById(R.id.wattLivingRoom);

        voltKitchen = findViewById(R.id.voltKitchen);
        ampereKitchen = findViewById(R.id.ampereKitchen);
        wattKitchen = findViewById(R.id.wattKitchen);

        voltBedroom = findViewById(R.id.voltBedroom);
        ampereBedroom = findViewById(R.id.ampereBedroom);
        wattBedroom = findViewById(R.id.wattBedroom);

        switchLivingRoom = findViewById(R.id.imgSwitchLivingRoom);
        switchKitchen = findViewById(R.id.imgSwitchKitchen);
        switchBedroom = findViewById(R.id.imgSwitchBedroom);

        stateLivingRoom = findViewById(R.id.imgStateLivingRoom);
        stateKitchen = findViewById(R.id.imgStateKitchen);
        stateBedroom = findViewById(R.id.imgStateBedroom);

        statusLivingRoom = findViewById(R.id.tvStatusLivingRoom);
        statusKitchen = findViewById(R.id.tvStatusKitchen);
        statusBedroom = findViewById(R.id.tvStatusBedroom);

        imgInfo = findViewById(R.id.imgInfo);

        btnBackRound = findViewById(R.id.btn_back_round);
        btnNextRound = findViewById(R.id.btn_next_round);
    }

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }
}