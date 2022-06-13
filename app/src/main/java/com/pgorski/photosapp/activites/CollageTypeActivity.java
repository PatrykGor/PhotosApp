package com.pgorski.photosapp.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;

import com.pgorski.photosapp.R;
import com.pgorski.photosapp.activites.CollageActivity;
import com.pgorski.photosapp.models.ImageData;

import java.util.ArrayList;

public class CollageTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage_type);
        LinearLayout buttonOne = findViewById(R.id.collageTypeOneButton);
        LinearLayout buttonTwo = findViewById(R.id.collageTypeTwoButton);

        buttonOne.setOnClickListener(view -> {
            moveWithType(view, 1);
        });

        buttonTwo.setOnClickListener(view -> {
            moveWithType(view, 2);
        });
    }

    protected void moveWithType(View view, int type) {
        Intent intent = new Intent(view.getContext(), CollageMakerActivity.class);
        ArrayList<ImageData> list = new ArrayList<>();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        switch (type) {
            case 1: {
                list.add(new ImageData(0, 0, size.x, size.y/2));
                list.add(new ImageData(0, size.y/2, size.x/3, size.y/2));
                list.add(new ImageData(size.x/3, size.y/2, 2*size.x/3, size.y/2));
                break;
            }
            case 2: {
                list.add(new ImageData(0, 0, size.x/3, size.y));
                list.add(new ImageData(size.x/3, 0, 2*size.x/3, size.y/2));
                list.add(new ImageData(size.x/3, size.y/2, size.x/3, size.y/2));
                list.add(new ImageData(2*size.x/3, size.y/2, size.x/3, size.y/2));
                break;
            }
        }
        intent.putExtra("list", list);
        startActivity(intent);
    }
}