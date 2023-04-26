package com.kerrrusha.lab_3.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import com.kerrrusha.lab_3.R;
import com.kerrrusha.lab_3.activity.db.DbActivity;

public class MainActivity extends FragmentActivity {

    Button openDbButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openDbButton = findViewById(R.id.openDbButton);
        openDbButton.setOnClickListener(v -> openDbActivity());
    }

    private void openDbActivity() {
        Intent intent = new Intent(MainActivity.this, DbActivity.class);
        startActivity(intent);
    }

}
