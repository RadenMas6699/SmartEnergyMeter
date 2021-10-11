package com.radenmas.smartenergymeter.ui;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.radenmas.smartenergymeter.DataChart;
import com.radenmas.smartenergymeter.InfoAppActivity;
import com.radenmas.smartenergymeter.R;
import com.radenmas.smartenergymeter.base.BaseActivity;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class MainActivity extends BaseActivity {
    private TextView tvAppName, voltLivingRoom, ampereLivingRoom, wattLivingRoom,
            voltKitchen, ampereKitchen, wattKitchen,
            voltBedroom, ampereBedroom, wattBedroom,
            statusLivingRoom, statusKitchen, statusBedroom;
    private ImageView switchLivingRoom, switchKitchen, switchBedroom, stateLivingRoom, stateKitchen, stateBedroom;
    private ImageButton imgInfo;
    private RadioGroup radioGroup;
    private RadioButton radioVolt, radioLivingRoom, radioBedroom, radioKitchen;
    int iWatt1, iWatt2, iWatt3;
    float fAmpere1, fAmpere2, fAmpere3;
    int a, b, c;

    DecimalFormat koma = new DecimalFormat("#.##");


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

        tvAppName.setText(R.string.app_name);

        selectTextView(radioVolt, radioLivingRoom, radioBedroom, radioKitchen);

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

                    voltLivingRoom.setText("V : " + dataChart.getVolt() + " V");
                    voltBedroom.setText("V : " + dataChart.getVolt() + " V");
                    voltKitchen.setText("V : " + dataChart.getVolt() + " V");

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
                    setOnPower(fAmpere1, iWatt1, ampereLivingRoom, wattLivingRoom,
                            switchLivingRoom, stateLivingRoom, statusLivingRoom);
                } else {
                    a = 1;
                    setOffPower(ampereLivingRoom, wattLivingRoom,
                            switchLivingRoom, stateLivingRoom, statusLivingRoom);
                }
                if (b == 1) {
                    b = 0;
                    setOnPower(fAmpere2, iWatt2, ampereBedroom, wattBedroom,
                            switchBedroom, stateBedroom, statusBedroom);
                } else {
                    b = 1;
                    setOffPower(ampereBedroom, wattBedroom,
                            switchBedroom, stateBedroom, statusBedroom);
                }
                if (c == 1) {
                    c = 0;
                    setOnPower(fAmpere3, iWatt3, ampereKitchen, wattKitchen,
                            switchKitchen, stateKitchen, statusKitchen);
                } else {
                    c = 1;
                    setOffPower(ampereKitchen, wattKitchen,
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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.radioLivingRoom:
                        selectTextView(radioLivingRoom, radioVolt, radioBedroom, radioKitchen);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_chart, new LivingRoomFragment()).commit();
                        break;
                    case R.id.radioBedroom:
                        selectTextView(radioBedroom, radioLivingRoom, radioVolt, radioKitchen);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_chart, new BedroomFragment()).commit();
                        break;
                    case R.id.radioKitchen:
                        selectTextView(radioKitchen, radioVolt, radioLivingRoom, radioBedroom);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_chart, new KitchenFragment()).commit();
                        break;
                    default:
                        selectTextView(radioVolt, radioLivingRoom, radioBedroom, radioKitchen);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_chart, new VoltFragment()).commit();
                        break;
                }
            }
        });

        switchLivingRoom.setOnClickListener(v -> {
            if (a == 1) {
                a = 0;
                dbReff.child("lampu_1").setValue(1);
                setOnPower(fAmpere1, iWatt1, ampereLivingRoom, wattLivingRoom,
                        switchLivingRoom, stateLivingRoom, statusLivingRoom);
            } else {
                a = 1;
                dbReff.child("lampu_1").setValue(0);
                setOffPower(ampereLivingRoom, wattLivingRoom,
                        switchLivingRoom, stateLivingRoom, statusLivingRoom);
            }
        });

        switchBedroom.setOnClickListener(v -> {
            if (b == 1) {
                b = 0;
                dbReff.child("lampu_2").setValue(1);
                setOnPower(fAmpere2, iWatt2, ampereBedroom, wattBedroom,
                        switchBedroom, stateBedroom, statusBedroom);
            } else {
                b = 1;
                dbReff.child("lampu_2").setValue(0);
                setOffPower(ampereBedroom, wattBedroom,
                        switchBedroom, stateBedroom, statusBedroom);
            }
        });

        switchKitchen.setOnClickListener(v -> {
            if (c == 1) {
                c = 0;
                dbReff.child("lampu_3").setValue(1);
                setOnPower(fAmpere3, iWatt3, ampereKitchen, wattKitchen,
                        switchKitchen, stateKitchen, statusKitchen);
            } else {
                c = 1;
                dbReff.child("lampu_3").setValue(0);
                setOffPower(ampereKitchen, wattKitchen,
                        switchKitchen, stateKitchen, statusKitchen);
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

    private void changeChart(int count) {
        Fragment selectedFragment = null;
        switch (count) {
            case 0:
                selectedFragment = new VoltFragment();
                tvAppName.setText(R.string.app_name);
                break;
            case 1:
                selectedFragment = new LivingRoomFragment();
                tvAppName.setText(R.string.app_name);
                break;
            case 2:
                selectedFragment = new BedroomFragment();
                tvAppName.setText(R.string.app_name);
                break;
            case 3:
                selectedFragment = new KitchenFragment();
                tvAppName.setText(R.string.app_name);
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_chart, selectedFragment).commit();
        return;
    }

    private void initView() {
        tvAppName = findViewById(R.id.tv_app_name);
        radioGroup = findViewById(R.id.radioGroup);
        radioVolt = findViewById(R.id.radioVolt);
        radioLivingRoom = findViewById(R.id.radioLivingRoom);
        radioBedroom = findViewById(R.id.radioBedroom);
        radioKitchen = findViewById(R.id.radioKitchen);

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
}