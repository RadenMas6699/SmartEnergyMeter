package com.radenmas.smartpowermeter.ui;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.radenmas.smartpowermeter.R;
import com.radenmas.smartpowermeter.base.BaseActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainAct extends BaseActivity {

    private TextView tvTime;
    private ImageView imgProfile, imgInfo;
    private ViewPager viewPager, viewpagerRoom;
    private TabLayout tabLayout;

    @Override
    protected int getLayoutResource() {
        return R.layout.act_main;
    }

    @Override
    protected void myCodeHere() {

        initView();
        setupViewPager(viewPager);
        setupViewPagerRoom(viewpagerRoom);
        tabLayout.setupWithViewPager(viewpagerRoom);

        onClick();

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

    private void setupViewPagerRoom(ViewPager viewpagerRoom) {
        ViewPagerAdapter adapterRoom = new ViewPagerAdapter(getSupportFragmentManager());
        adapterRoom.addFragment(new ControlFrag(), "Kamar Satu");
        adapterRoom.addFragment(new ControlFrag(), "Kamar Dua");
        adapterRoom.addFragment(new ControlFrag(), "Kamar Tiga");
        adapterRoom.addFragment(new ControlFrag(), "Kamar Empat");
        adapterRoom.addFragment(new ControlFrag(), "Kamar Lima");
        adapterRoom.addFragment(new ControlFrag(), "Kamar Enam");
        viewpagerRoom.setAdapter(adapterRoom);
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


    private void onClick() {
        imgProfile.setOnClickListener(v -> {
            startActivity(new Intent(MainAct.this, InfoAppAct.class));
        });
        imgInfo.setOnClickListener(v -> {
            startActivity(new Intent(MainAct.this, SettingsAct.class));
        });
    }

    private void initView() {
        viewPager = findViewById(R.id.viewpager);

        tvTime = findViewById(R.id.tvTime);

        viewpagerRoom = findViewById(R.id.viewpagerRoom);
        tabLayout = findViewById(R.id.tabs);

        imgInfo = findViewById(R.id.imgSettings);
        imgProfile = findViewById(R.id.imgProfile);
    }
}