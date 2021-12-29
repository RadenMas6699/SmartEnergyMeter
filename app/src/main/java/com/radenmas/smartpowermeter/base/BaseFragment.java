package com.radenmas.smartpowermeter.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.radenmas.smartpowermeter.R;

public abstract class BaseFragment extends Fragment {
    protected abstract int getLayoutResource();

    protected abstract void myCodeHere(View view);

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    public FirebaseDatabase database;
    public DatabaseReference dbReff;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        database = FirebaseDatabase.getInstance();
        dbReff = database.getReference();

        sharedPreferences = getActivity().getSharedPreferences(getResources().getString(R.string.prefAccount), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        myCodeHere(view);

        return view;
    }
}
