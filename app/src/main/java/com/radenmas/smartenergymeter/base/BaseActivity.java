package com.radenmas.smartenergymeter.base;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class BaseActivity extends AppCompatActivity {
    protected abstract int getLayoutResource();
    protected abstract void myCodeHere();

    public FirebaseDatabase database;
    public DatabaseReference dbReff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(getLayoutResource());

        database = FirebaseDatabase.getInstance();
        dbReff = database.getReference();

        myCodeHere();
    }

}
