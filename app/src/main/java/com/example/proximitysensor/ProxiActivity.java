package com.example.proximitysensor;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ProxiActivity extends AppCompatActivity {

    SensorManager sensorManager;
    Sensor proxiSensor;
    TextView textValues;
    String SQLiteQuery;
    Button View;
    float proxValue;
    SQLiteDatabase sqLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textValues = (TextView) findViewById(R.id.textValues);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (proxiSensor == null) {
            if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {
                createDatabase();
                int MINUTES = 1; // The delay in minutes
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        addData(); // If the function you wanted was static
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(
                                new Runnable()
                                {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ProxiActivity.this, "Data berhasil ditambah", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
                    }
                }, 0, 1000 * 120 * MINUTES);
                proxiSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            } else {
                textValues.setText("SmartPhone anda tidak mendukung");
            }
        }
        View = findViewById(R.id.view);
        View.setOnClickListener(new android.view.View.OnClickListener(){
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(ProxiActivity.this, ListViewActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onResume(){
        super.onResume();
        sensorManager.registerListener(proxiListener,proxiSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onStop() {
        super.onStop();

        sensorManager.unregisterListener(proxiListener);
    }
    SensorEventListener proxiListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) { }
        @SuppressLint("SetTextI18n")
        public void onSensorChanged(SensorEvent event) {
            proxValue = event.values[0];

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 10 seconds
                    textValues.setText("Value: "+ proxValue + " cm");
                    handler.postDelayed(this, 2000);
                }
            }, 2000);  //the time is in miliseconds
            if(event.values[0] < proxiSensor.getMaximumRange()){
                getWindow().getDecorView().setBackgroundColor(Color.RED);
            }else{
                getWindow().getDecorView().setBackgroundColor(Color.GREEN);
            }
        }
    };

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();

        return dateFormat.format(date);
    }

    private void createDatabase() {
        sqLiteDatabase = openOrCreateDatabase("Nama_Database_Baru", Context.MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Nama_Tabel (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title VARCHAR, value VARCHAR);");
    }

    private void addData() {
        SQLiteQuery = "INSERT INTO Nama_Tabel (title,value) VALUES ('"+ getCurrentDate() +"', '"+ proxValue +"');";
        sqLiteDatabase.execSQL(SQLiteQuery);
    }

}
