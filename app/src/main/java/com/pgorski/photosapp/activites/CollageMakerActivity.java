package com.pgorski.photosapp.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bikomobile.multipart.Multipart;
import com.pgorski.photosapp.R;
import com.pgorski.photosapp.models.ImageData;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CollageMakerActivity extends AppCompatActivity {

    private int currentImage;
    private Editable ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage_maker);
        ArrayList<ImageData> list = (ArrayList<ImageData>) getIntent().getExtras().getSerializable("list");
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 100);
        Log.d("DEBUG", "onCreate: "+ list.toString());
        FrameLayout frame = findViewById(R.id.collageMakerFrame);
        for (ImageData image: list) {
            ImageView iv = new ImageView(this);
            iv.setX(image.getX());
            iv.setY(image.getY());
            iv.setLayoutParams(new FrameLayout.LayoutParams(image.getW(), image.getH()));
            iv.setClickable(true);
            iv.setFocusable(true);
            iv.setImageResource(R.drawable.add);
            iv.setId(View.generateViewId());
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    currentImage = view.getId();
                    startActivityForResult(intent, 100); // 100 - stała wartość, która później posłuży do identyfikacji tej akcji
                }
            });
            frame.addView(iv);
        }
        ImageView next = findViewById(R.id.generate);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frame.setDrawingCacheEnabled(true);
                Bitmap b = frame.getDrawingCache(true);
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String d = df.format(new Date());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, stream); // kompresja, typ pliku jpg, png
                byte[] byteArray = stream.toByteArray();
                FileOutputStream fs = null;
                File pic = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES );
                File dir = new File(pic, "collages");
                if(!dir.exists())
                    dir.mkdir();
                File collage = new File(dir, d);
                try {
                    fs = new FileOutputStream(collage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    assert fs != null;
                    fs.write(byteArray);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Multipart multipart = new Multipart(CollageMakerActivity.this);
                multipart.addFile("image/jpeg", "file", "plik.jpg", byteArray);
                multipart.launchRequest(ip+":3000/upload",
                        response -> {
                            Log.d("xxx", "success");
                        },
                        error -> {
                            Log.d("xxx", "error");
                        });
            }
        });
//        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawerLayout.openDrawer(GravityCompat.START);
        EditText ipText = findViewById(R.id.Ip);
        ip = ipText.getText();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Uri imgData = data.getData();
                ImageView imageView = findViewById(currentImage);
                InputStream stream = null;
                try {
                    stream = getContentResolver().openInputStream(imgData);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap b = BitmapFactory.decodeStream(stream);
//                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//                b.compress(Bitmap.CompressFormat.JPEG, 100, byteStream); // kompresja, typ pliku jpg, png
                imageView.setImageBitmap(b);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //tak
                } else {
                    //nie
                }
                break;
            case 101 :

                break;
        }

    }
    public void checkPermission(String permission, int requestCode) {
        // jeśli nie jest przyznane to zażądaj
        if (ContextCompat.checkSelfPermission(CollageMakerActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(CollageMakerActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(CollageMakerActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }
}