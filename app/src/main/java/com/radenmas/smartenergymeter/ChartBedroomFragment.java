package com.radenmas.smartenergymeter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class ChartBedroomFragment extends Fragment {

    public ChartBedroomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart_bedroom, container, false);

        Toast.makeText(getContext(), "living", Toast.LENGTH_SHORT).show();

        return view;
    }
}