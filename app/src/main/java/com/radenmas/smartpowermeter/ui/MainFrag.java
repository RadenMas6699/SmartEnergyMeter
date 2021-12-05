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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainFrag extends BaseFragment {
    private TextView tvCalender, tvVolt, tvTotalDaya;
    int total = 0;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    @Override
    protected int getLayoutResource() {
        return R.layout.frag_main;
    }

    @Override
    protected void myCodeHere(View view) {
        initView(view);
        getDataVolt();
        getTotalDaya();

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd MMM yyyy");
        date = dateFormat.format(calendar.getTime());
        tvCalender.setText(date);
    }

    private void getDataVolt() {
        DatabaseReference dbLast = FirebaseDatabase.getInstance().getReference("SmartEnergyMeter");
        Query query = dbLast.orderByKey().limitToLast(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    DataChart dataChart = child.getValue(DataChart.class);
                    tvVolt.setText(dataChart.getVolt() + " V");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getTotalDaya() {
        dbReff.child("SmartEnergyMeter").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    DataChart dataChart = ds.getValue(DataChart.class);

                    int watt1 = (int) dataChart.getWatt1();
                    int watt2 = (int) dataChart.getWatt2();
                    int watt3 = (int) dataChart.getWatt3();
                    int wattTotal = watt1 + watt2 + watt3;
                    total = total + wattTotal;

                    tvTotalDaya.setText(total + " W");
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
    }
}
