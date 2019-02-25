package com.example.asus.bleapp;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RobotRomeo extends AppCompatActivity {

    BluetoothDevice btDevice;
    BluetoothGatt btGatt;
    BluetoothGattCharacteristic tx, rx, SerialPortUUIDchar, CommandUUIDchar, ModelNumberStringUUIDchar;
    private Button bt;
    String str;
    private ImageButton f,fr,fl,l,stop,r,b,bl,br;
    byte[] strBytes = new byte[1];
    public static final String SerialPortUUID="0000dfb1-0000-1000-8000-00805f9b34fb";//wyslanie i odczytanie znaku
    public static final String CommandUUID="0000dfb2-0000-1000-8000-00805f9b34fb";
    public static final String ModelNumberStringUUID="00002a24-0000-1000-8000-00805f9b34fb";//nazwa urzadzenia

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_romeo);

        bt = (Button) findViewById(R.id.b1);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rx != null ) {
                    str = "0";
                    strBytes = str.getBytes();
                    rx.setValue(strBytes);
                    btGatt.writeCharacteristic(rx);
                    btGatt.setCharacteristicNotification(rx,true);

                }
                SystemClock.sleep(200);
                Intent mainIntent = new Intent(RobotRomeo.this, MainActivity.class);
                startActivity(mainIntent);
                btGatt.disconnect();
                btGatt.close();
                btGatt=null;
            }
        });

        f = (ImageButton) findViewById(R.id.f);
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rx != null ) {
                    str = "1";
                    strBytes = str.getBytes();
                    rx.setValue(strBytes);
                    btGatt.writeCharacteristic(rx);
                    btGatt.setCharacteristicNotification(rx,true);

                }

            }
        });
        b = (ImageButton) findViewById(R.id.b);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rx != null ) {
                    str = "2";
                    strBytes = str.getBytes();
                    rx.setValue(strBytes);
                    btGatt.writeCharacteristic(rx);
                    btGatt.setCharacteristicNotification(rx,true);

                }

            }
        });
        l = (ImageButton) findViewById(R.id.l);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rx != null ) {
                    str = "3";
                    strBytes = str.getBytes();
                    rx.setValue(strBytes);
                    btGatt.writeCharacteristic(rx);
                    btGatt.setCharacteristicNotification(rx,true);

                }

            }
        });
        r = (ImageButton) findViewById(R.id.r);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rx != null ) {
                    str = "4";
                    strBytes = str.getBytes();
                    rx.setValue(strBytes);
                    btGatt.writeCharacteristic(rx);
                    btGatt.setCharacteristicNotification(rx,true);

                }

            }
        });
        fr = (ImageButton) findViewById(R.id.fr);
        fr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rx != null ) {
                    str = "5";
                    strBytes = str.getBytes();
                    rx.setValue(strBytes);
                    btGatt.writeCharacteristic(rx);
                    btGatt.setCharacteristicNotification(rx,true);

                }

            }
        });
        fl = (ImageButton) findViewById(R.id.fl);
        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rx != null ) {
                    str = "6";
                    strBytes = str.getBytes();
                    rx.setValue(strBytes);
                    btGatt.writeCharacteristic(rx);
                    btGatt.setCharacteristicNotification(rx,true);

                }

            }
        });
        bl = (ImageButton) findViewById(R.id.bl);
        bl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rx != null ) {
                    str = "7";
                    strBytes = str.getBytes();
                    rx.setValue(strBytes);
                    btGatt.writeCharacteristic(rx);
                    btGatt.setCharacteristicNotification(rx,true);

                }

            }
        });
        br = (ImageButton) findViewById(R.id.br);
        br.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rx != null ) {
                    str = "8";
                    strBytes = str.getBytes();
                    rx.setValue(strBytes);
                    btGatt.writeCharacteristic(rx);
                    btGatt.setCharacteristicNotification(rx,true);

                }

            }
        });
        stop = (ImageButton) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rx != null ) {
                    str = "0";
                    strBytes = str.getBytes();
                    rx.setValue(strBytes);
                    btGatt.writeCharacteristic(rx);
                    btGatt.setCharacteristicNotification(rx,true);
                }

            }
        });

        btDevice = getIntent().getExtras().getParcelable("btDevice");

        btGatt = btDevice.connectGatt(this, false, bluetoothGattCallback);
    }

    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            System.out.println("BluetoothGattCallback----onConnectionStateChange"+newState);
            switch (newState) {
                case 0:
                    RobotRomeo.this.runOnUiThread(new Runnable() {
                        public void run() {

                        }
                    });
                    break;
                case 2:
                    RobotRomeo.this.runOnUiThread(new Runnable() {
                        public void run() {

                        }
                    });

                    // discover services and characteristics for this device
                    btGatt.discoverServices();

                    break;
                default:
                    RobotRomeo.this.runOnUiThread(new Runnable() {
                        public void run() {

                        }
                    });
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            RobotRomeo.this.runOnUiThread(new Runnable() {
                public void run() {
                    // alert.append("device services have been discovered\n");
                }
            });
            displayGattServices(btGatt.getServices());
        }


        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            synchronized (this)
            {
                if(characteristic.equals(SerialPortUUIDchar)) {
                    System.out.println("onCharacteristicWrite success:" + new String(characteristic.getValue()));
                    characteristic.setValue(strBytes);
                }
            }
        }
    };

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {

            final String uuid = gattService.getUuid().toString();
            System.out.println("Service discovered: " + uuid);

            new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic :
                    gattCharacteristics) {

                final String charUuid = gattCharacteristic.getUuid().toString();
                System.out.println("Characteristic discovered for service: " + charUuid);

                if(charUuid.equals(ModelNumberStringUUID)){
                    ModelNumberStringUUIDchar=gattCharacteristic;//nazwa
                    System.out.println("ModelNumberCharacteristic  "+ModelNumberStringUUIDchar.getUuid().toString());

                }
                else if(charUuid.equals(SerialPortUUID)){
                    SerialPortUUIDchar = gattCharacteristic;
                    System.out.println("SerialPortCharacteristic  "+SerialPortUUIDchar.getUuid().toString());

                }
                else if(charUuid.equals(CommandUUID)){
                    CommandUUIDchar = gattCharacteristic;
                    System.out.println("CommandUUIDchar   "+CommandUUIDchar.getUuid().toString());

                }
            }
            rx = SerialPortUUIDchar;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (btGatt == null) {
            return;
        }

        btGatt.close();
        btGatt = null;
    }

}
