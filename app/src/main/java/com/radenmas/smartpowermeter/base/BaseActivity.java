package com.radenmas.smartpowermeter.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.radenmas.smartpowermeter.R;

public abstract class BaseActivity extends AppCompatActivity {
    protected abstract int getLayoutResource();
    protected abstract void myCodeHere();

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    public FirebaseDatabase database;
    public DatabaseReference dbReff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(getLayoutResource());

        database = FirebaseDatabase.getInstance();
        dbReff = database.getReference();

        sharedPreferences = getSharedPreferences(getResources().getString(R.string.prefAccount), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        myCodeHere();
    }

}
