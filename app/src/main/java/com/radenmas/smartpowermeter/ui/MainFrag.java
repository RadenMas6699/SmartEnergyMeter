package com.radenmas.smartpowermeter.ui;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.radenmas.smartpowermeter.DataChart;
import com.radenmas.smartpowermeter.R;
import com.radenmas.smartpowermeter.base.BaseFragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainFrag extends BaseFragment {
    private TextView tvCalender, tvVolt, tvTotalDaya, tvTotalArus;
    int totalDaya = 0;
    float totalArus = 0;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    long timesPast, timesNow;
    DecimalFormat koma = new DecimalFormat("#.##");

    @Override
    protected int getLayoutResource() {
        return R.layout.frag_main;
    }

    @Override
    protected void myCodeHere(View view) {
        initView(view);
        getDataLast();

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        date = dateFormat.format(calendar.getTime());
        timesNow = calendar.getTimeInMillis() / 1000;
        timesPast = timesNow - 2592000;
        tvCalender.setText("" + date);

        getTotalDaya(timesPast, timesNow);
    }

    private void getDataLast() {
        DatabaseReference dbLast = FirebaseDatabase.getInstance().getReference("SmartEnergyMeter");
        Query query = dbLast.orderByKey().limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        DataChart dataChart = child.getValue(DataChart.class);
                        tvVolt.setText(dataChart.getVolt() + " V");
                        float arus1 = dataChart.getArus1();
                        float arus2 = dataChart.getArus2();
                        float arus3 = dataChart.getArus3();
                        float arusTotal = arus1 + arus2 + arus3;

                        tvTotalArus.setText(koma.format(arusTotal) + " A");

                    }
                } else {
                    tvTotalArus.setText("0 A");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTotalDaya(long timesPast, long timesNow) {
        dbReff.child("SmartEnergyMeter").orderByChild("time").startAt(timesPast).endAt(timesNow).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    DataChart dataChart = ds.getValue(DataChart.class);

                    int watt1 = dataChart.getWatt1();
                    int watt2 = dataChart.getWatt2();
                    int watt3 = dataChart.getWatt3();
                    int wattTotal = watt1 + watt2 + watt3;
                    totalDaya = totalDaya + wattTotal;
                    int rata = totalDaya / 30;

                    tvTotalDaya.setText(rata + " W");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView(View view) {
        tvCalender = view.findViewById(R.id.tvCalender);
        tvVolt = view.findViewById(R.id.tvVolt);
        tvTotalDaya = view.findViewById(R.id.tvTotalDaya);
        tvTotalArus = view.findViewById(R.id.tvTotalArus);
    }
}
