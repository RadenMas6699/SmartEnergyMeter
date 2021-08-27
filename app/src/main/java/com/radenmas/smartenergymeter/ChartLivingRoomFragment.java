package com.radenmas.smartenergymeter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class ChartLivingRoomFragment extends Fragment {

    public ChartLivingRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart_living_room, container, false);

        Toast.makeText(getContext(), "living", Toast.LENGTH_SHORT).show();

        return view;
    }
}