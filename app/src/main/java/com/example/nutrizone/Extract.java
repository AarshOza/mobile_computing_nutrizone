package com.example.nutrizone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;

public class Extract extends AppCompatActivity {

    private CameraSource camera;
    private TextRecognizer text_recognizer;
    private SurfaceView camera_view;
    private TextView extracted_text;
    private LinearLayout toggle_button;
    private Boolean surface_flag = true;
    private String extracted_data;
    public ArrayList<String> data_array = new ArrayList<String>();;
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract);

        camera_view = (SurfaceView) findViewById(R.id.camera_view);
        extracted_text = (TextView) findViewById(R.id.extracted_text);
        toggle_button = (LinearLayout) findViewById(R.id.toggle_button);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {
            startTextRecognizer();
        } else {
            camera_permission();
        }


        toggle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SurfaceView surface_view = findViewById(R.id.camera_view);
                Log.d("LOGIN_DETAILS", "data on click ==> " +extracted_data);
//                Log.d("LOGIN_DETAILS", "array1 on click " + data_array);
                Toast.makeText(getApplicationContext(), "Recognized your Text",
                        Toast.LENGTH_LONG).show();
                onBackPressed();
                surface_view.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surface_view = findViewById(R.id.camera_view);
        if (surface_flag) {
            surface_view.setVisibility(View.VISIBLE);
        } else {
            surface_view.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.release();
    }

    private void startTextRecognizer() {

        text_recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!text_recognizer.isOperational()) {
            Toast.makeText(getApplicationContext(), "Cannot start the text recognizer " +
                    "please try again later", Toast.LENGTH_LONG).show();
        }
        else {
            camera = new CameraSource.Builder(getApplicationContext(), text_recognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(15.0f)
                    .setAutoFocusEnabled(true)
                    .build();

            camera_view.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            camera.start(camera_view.getHolder());
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                            Log.d("LOGIN_DETAILS", String.valueOf(e));
                        }
                    }
                    else {
                        camera_permission();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    camera.stop();
                }
            });

            text_recognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    SparseArray<TextBlock> items = detections.getDetectedItems();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < items.size(); ++i) {
                        TextBlock item = items.valueAt(i);
                        if (item != null && item.getValue() != null) {
                            stringBuilder.append(item.getValue() + "-");
                            String new_item = item.getValue();
                            data_array.add(new_item);
                        }
                    }

                    final String fullText = stringBuilder.toString();
                    extracted_data = fullText;
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            extracted_text.setText(fullText);
                        }
                    });

                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startTextRecognizer();
            return;
        }

    }

    private void camera_permission() {

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

    }
}