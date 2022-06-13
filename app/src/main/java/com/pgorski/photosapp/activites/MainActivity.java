package com.pgorski.photosapp.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.pgorski.photosapp.R;
import com.pgorski.photosapp.activites.CollageTypeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout collageButton = findViewById(R.id.collageButton);
        collageButton.setOnClickListener(view -> {
            Intent collageSwitch = new Intent(view.getContext(), CollageTypeActivity.class);
            startActivity(collageSwitch);
        });
    }
}