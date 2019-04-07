package com.example.yuejiao.gyroscopeControlledCar;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    ImageButton servoL;
    ImageButton servoR;
    ImageButton upButton;
    ImageButton downButton;
    ImageButton leftButton;
    ImageButton rightButton;
    BluetoothSPP bt;
    BluetoothAdapter adapter;
    private boolean shake;

    private SensorManager sensorManager;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        servoL = (ImageButton) findViewById(R.id.lookLeft);
        servoR = (ImageButton) findViewById(R.id.lookRight);
        upButton = (ImageButton) findViewById(R.id.upButton);
        downButton = (ImageButton) findViewById(R.id.downButton);
        leftButton = (ImageButton) findViewById(R.id.leftButton);
        rightButton = (ImageButton) findViewById(R.id.rightButton);

        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null){
            //Toast.makeText(getApplicationContext(), "adapter not null", Toast.LENGTH_SHORT).show();
            bt = new BluetoothSPP(getApplicationContext());

        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);




        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 6000; i++){
                    forward();

                }
            }



        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 6000; i++) {
                    backward();

                }
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 3000; i++){
                    left();

                }
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 3000; i++){
                    right();

                }
            }
        });

        servoL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 30; i++){
                    lookLeft();
                }
            }
        });
        servoR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 30; i++){
                    lookRight();
                }
            }
        });
    }
    public void toggleShake(View view){
        if (shake){ shake = false; }
        else { shake = true; }
    }

    public void onSensorChanged(SensorEvent event) {
        // we received a sensor event. it is a good practice to check
        // that we received the proper event
        if (event.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR && shake) {
            //Toast.makeText(getApplicationContext(), "sense" + event.values[0], Toast.LENGTH_SHORT).show();
            if (event.values[0]*10 > 1.5 && Math.abs(event.values[0]) > Math.abs(event.values[1])){
                forward();
                forward();
                forward();
            }
            if (event.values[0]*10 < -1.5 && Math.abs(event.values[0]) > Math.abs(event.values[1])) {
                backward();
                backward();
                backward();
            }
            if (event.values[1]*10 > 1.5 && Math.abs(event.values[1]) > Math.abs(event.values[0])) {
                right();
                right();
                right();
            }
            if (event.values[1]*10 < -1.5 && Math.abs(event.values[1]) > Math.abs(event.values[0])){
                left();
                left();
                left();
            }
            stop();
            stop();
            stop();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d("main", "on Start");
        Toast.makeText(getApplicationContext(), "onStart", Toast.LENGTH_SHORT).show();
        bTCtrl();
        sensorManager.registerListener(this, sensor, 30);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d("main", "on ReStart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("main", "on Resume");
       sensorManager.registerListener(this, sensor, 30);
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("main", "on Pause");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d("main", "on Stop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("main", "on Destroy");
        bt.stopService();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * send hex over bluetooth
     */
    public void forward(){
        Log.d("control", "forward");

        bt.send(new byte[]{0x77}, false);
    }
    public void backward(){
        Log.d("control", "back");

        bt.send(new byte[]{0x73}, false);
    }
    public void left(){
        Log.d("control", "left");

        bt.send(new byte[]{0x64}, false);
    }
    public void right(){
        Log.d("control", "right");

        bt.send(new byte[]{0x61}, false);
    }
    public void stop(){
        bt.send(new byte[]{0x11}, false);
    }

    public void lookLeft(){
        bt.send(new byte[]{0x71}, false);
    }

    public void lookRight(){
        bt.send(new byte[]{0x65},false);
    }

    public void bTCtrl(){
        if (!bt.isBluetoothAvailable()){
            Log.d("Main","Lack bluetooth functionality");
            Toast.makeText(getApplicationContext(), "not available", Toast.LENGTH_SHORT).show();
            return;
        }
        if (! bt.isBluetoothEnabled()){
            bt.enable();
            bt.setupService();
            bt.startService(BluetoothState.DEVICE_OTHER);
            Toast.makeText(getApplicationContext(), "enable", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "startService1", Toast.LENGTH_SHORT).show();
            bt.setupService();
            bt.startService(BluetoothState.DEVICE_OTHER);
        }

        bt.connect("00:21:13:05:4B:54");
        //Toast.makeText(getApplicationContext(), intent.getExtras().getString(BluetoothState.EXTRA_DEVICE_ADDRESS), Toast.LENGTH_LONG).show();
        //bt.connect(intent);
        //startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
        //Toast.makeText(getApplicationContext(), "startService2", Toast.LENGTH_SHORT).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getApplicationContext(), "onActivity", Toast.LENGTH_SHORT).show();
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK) {
                Toast.makeText(getApplicationContext(), data.getDataString(), Toast.LENGTH_SHORT).show();
                bt.connect(data);
                //setContentView(R.layout.activity_main);
            }
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                //bt.enable();
                //bt.setupService();
                //bt.startService(BluetoothState.DEVICE_OTHER);

                Toast.makeText(getApplicationContext(), "re", Toast.LENGTH_SHORT).show();
            } else {
                // Do something if user doesn't choose any device (Pressed back)
            }
        }
    }


}
