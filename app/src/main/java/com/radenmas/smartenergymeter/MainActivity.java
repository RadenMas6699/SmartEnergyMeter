package com.radenmas.smartenergymeter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
//    private ViewPager viewPager;
    private TextView tvRoom, voltLivingRoom, ampereLivingRoom, wattLivingRoom,
            voltKitchen, ampereKitchen, wattKitchen,
            voltBedroom, ampereBedroom, wattBedroom,
            statusLivingRoom, statusKitchen, statusBedroom;
    private LinearLayout dotsLayout;
    private int[] layouts;
    private ImageView switchLivingRoom, switchKitchen, switchBedroom, stateLivingRoom, stateKitchen, stateBedroom;
    private ImageButton imgInfo;
    private LineChart chart;
    int a = 0, b = 0, c = 0;


    LineDataSet lineDataSet = new LineDataSet(null,null);
    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
    LineData lineData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.main);

        initView();
        onClick();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("pesan");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Toast.makeText(MainActivity.this, ""+value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        if (a == 1) {
            a = 0;

            switchLivingRoom.setImageResource(R.drawable.ic_power_on);
            stateLivingRoom.setVisibility(View.VISIBLE);
            statusLivingRoom.setText(R.string.status_on);
        } else {
            a = 1;

            switchLivingRoom.setImageResource(R.drawable.ic_power_off);
            stateLivingRoom.setVisibility(View.INVISIBLE);
            statusLivingRoom.setText(R.string.status_off);
        }
        if (b == 1) {
            b = 0;

            switchKitchen.setImageResource(R.drawable.ic_power_on);
            stateKitchen.setVisibility(View.VISIBLE);
            statusKitchen.setText(R.string.status_on);
        } else {
            b = 1;

            switchKitchen.setImageResource(R.drawable.ic_power_off);
            stateKitchen.setVisibility(View.INVISIBLE);
            statusKitchen.setText(R.string.status_off);
        }
        if (c == 1) {
            c = 0;

            switchBedroom.setImageResource(R.drawable.ic_power_on);
            stateBedroom.setVisibility(View.VISIBLE);
            statusBedroom.setText(R.string.status_on);
        } else {
            c = 1;
            switchBedroom.setImageResource(R.drawable.ic_power_off);
            stateBedroom.setVisibility(View.INVISIBLE);
            statusBedroom.setText(R.string.status_off);
        }


        tvRoom.setText(R.string.living_room);

        // layout xml slide 1 sampai 3
        layouts = new int[]{
                R.layout.fragment_chart_living_room,
                R.layout.fragment_chart_kitchen,
                R.layout.fragment_chart_bedroom};

        // tombol dots (lingkaran kecil perpindahan slide)
        addBottomDots(0);

//        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
//        viewPager.setAdapter(myViewPagerAdapter);
//        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);


        GraphSuhu(100);

        chart.getDescription().setEnabled(false);
        chart.setNoDataTextColor(getResources().getColor(R.color.white));
        chart.invalidate();
    }

    private void onClick() {
        imgInfo.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, InfoAppActivity.class));
        });
        switchLivingRoom.setOnClickListener(v -> {
            if (a == 1) {
                a = 0;
                voltLivingRoom.setText("220 V");
                ampereLivingRoom.setText("2.5 A");
                wattLivingRoom.setText("354 W");
                switchLivingRoom.setImageResource(R.drawable.ic_power_on);
                stateLivingRoom.setVisibility(View.VISIBLE);
                statusLivingRoom.setText(R.string.status_on);
            } else {
                a = 1;
                voltLivingRoom.setText(R.string.dash);
                ampereLivingRoom.setText(R.string.dash);
                wattLivingRoom.setText(R.string.dash);
                switchLivingRoom.setImageResource(R.drawable.ic_power_off);
                stateLivingRoom.setVisibility(View.INVISIBLE);
                statusLivingRoom.setText(R.string.status_off);
            }
        });

        switchKitchen.setOnClickListener(v -> {
            if (b == 1) {
                b = 0;
                voltKitchen.setText("220 V");
                ampereKitchen.setText("1 A");
                wattKitchen.setText("30 W");
                switchKitchen.setImageResource(R.drawable.ic_power_on);
                stateKitchen.setVisibility(View.VISIBLE);
                statusKitchen.setText(R.string.status_on);
            } else {
                b = 1;
                voltKitchen.setText(R.string.dash);
                ampereKitchen.setText(R.string.dash);
                wattKitchen.setText(R.string.dash);
                switchKitchen.setImageResource(R.drawable.ic_power_off);
                stateKitchen.setVisibility(View.INVISIBLE);
                statusKitchen.setText(R.string.status_off);
            }
        });

        switchBedroom.setOnClickListener(v -> {
            if (c == 1) {
                c = 0;
                voltBedroom.setText("220 V");
                ampereBedroom.setText("4 A");
                wattBedroom.setText("350 W");
                switchBedroom.setImageResource(R.drawable.ic_power_on);
                stateBedroom.setVisibility(View.VISIBLE);
                statusBedroom.setText(R.string.status_on);
            } else {
                c = 1;
                voltBedroom.setText(R.string.dash);
                ampereBedroom.setText(R.string.dash);
                wattBedroom.setText(R.string.dash);
                switchBedroom.setImageResource(R.drawable.ic_power_off);
                stateBedroom.setVisibility(View.INVISIBLE);
                statusBedroom.setText(R.string.status_off);
            }
        });
    }

    private void initView() {
        chart = findViewById(R.id.chart);
        tvRoom = findViewById(R.id.tv_room);
//        viewPager = findViewById(R.id.vp_chart);
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
    }

    public void GraphSuhu(final int limit) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("SmartEnergyMeter");
//        Query query = databaseReference.orderByKey().limitToLast(limit);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Entry> data = new ArrayList<>();
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        DataChart dataChart = child.getValue(DataChart.class);
                        data.add(new Entry(dataChart.getTime(), dataChart.getLampu1()));

                        Toast.makeText(MainActivity.this, ""+dataChart.lampu1, Toast.LENGTH_SHORT).show();
                    }
                    showChart(data);
                    lineDataSet.setDrawCircles(false);
                }
                chart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showChart(ArrayList<Entry> data) {
        lineDataSet.setValues(data);
        lineDataSet.setLabel("DataSet 1");
        lineDataSet.setDrawFilled(true);

        lineDataSet.setLineWidth(1.5f);
        lineDataSet.setMode(LineDataSet.Mode.STEPPED);
//        lineDataSet.setCubicIntensity(0.05f);
        lineDataSet.setDrawValues(false);
        iLineDataSets.clear();
        iLineDataSets.add(lineDataSet);
        lineData = new LineData(iLineDataSets);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(0f);//45
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(false);
        xAxis.setLabelCount(3, true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                Date date = new Date((long) value);
                SimpleDateFormat fmt;
                fmt = new SimpleDateFormat("HH:mm zz");
                fmt.setTimeZone(TimeZone.getDefault());
                String s = fmt.format(date);
                return s;
            }
        });

        YAxis yAxisL = chart.getAxis(YAxis.AxisDependency.LEFT);
        yAxisL.setDrawGridLines(false);
        yAxisL.setDrawLabels(false);
        yAxisL.setAxisMinimum(15);
        yAxisL.setAxisMaximum(45);

//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//        mv.setChartView(chart);
//        chart.setMarker(mv);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getXAxis().setEnabled(false);
        chart.notifyDataSetChanged();
        chart.clear();
        chart.setData(lineData);
        chart.invalidate();
        chart.moveViewTo(lineData.getEntryCount(),50L, YAxis.AxisDependency.LEFT);
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

//    private int getItem() {
//        return viewPager.getCurrentItem() + 1;
//    }
//
//    final ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
//
//        @Override
//        public void onPageSelected(int position) {
//            addBottomDots(position);
//
//            int nameRoom;
//
//            switch (position) {
//                case 1:
//                    nameRoom = R.string.kitchen;
//                    break;
//                case 2:
//                    nameRoom = R.string.bedroom;
//                    break;
//                default:
//                    nameRoom = R.string.living_room;
//                    break;
//            }
//            tvRoom.setText(nameRoom);
//        }
//
//        @Override
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int arg0) {
//
//        }
//    };
//
//    public class MyViewPagerAdapter extends PagerAdapter {
//
//        public MyViewPagerAdapter() {
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            View view = layoutInflater.inflate(layouts[position], container, false);
//            container.addView(view);
//
//            return view;
//        }
//
//        @Override
//        public int getCount() {
//            return layouts.length;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object obj) {
//            return view == obj;
//        }
//
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            View view = (View) object;
//            container.removeView(view);
//        }
//    }
}