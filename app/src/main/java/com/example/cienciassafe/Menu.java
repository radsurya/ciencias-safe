package com.example.cienciassafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void goToScreen(View view) {
        Intent intent = new Intent(this, Classrooms.class);
        startActivity(intent);
    }

    public void goToScreen1(View view) {
        Intent intent = new Intent(this, ReportResource.class);
        startActivity(intent);
    }
}