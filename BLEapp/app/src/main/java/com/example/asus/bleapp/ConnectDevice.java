package com.example.asus.bleapp;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConnectDevice extends AppCompatActivity {


    BluetoothDevice btDevice;
    BluetoothGatt btGatt;
    BluetoothGattCharacteristic tx, rx, SerialPortUUIDchar, CommandUUIDchar, ModelNumberStringUUIDchar;
    BluetoothGattService btGattService;
    private TextView textView, alert, textRead;
    private Button bt,send;
    private String str;
    private EditText dataToSend;
    byte[] data;
    byte[] strBytes = new byte[1];
    public static final String SerialPortUUID="0000dfb1-0000-1000-8000-00805f9b34fb";//wyslanie i odczytanie znaku
    public static final String CommandUUID="0000dfb2-0000-1000-8000-00805f9b34fb";
    public static final String ModelNumberStringUUID="00002a24-0000-1000-8000-00805f9b34fb";//nazwa urzadzenia


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_device);

        textView = (TextView) findViewById(R.id.textView);
        alert = (TextView) findViewById(R.id.textView2);
        textRead = (TextView) findViewById(R.id.textRead);
        dataToSend = (EditText) findViewById(R.id.dataText);
        bt = (Button) findViewById(R.id.b1);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(ConnectDevice.this, MainActivity.class);
                startActivity(mainIntent);
                btGatt.disconnect();
                btGatt.close();
                btGatt=null;
            }
        });
        send = (Button) findViewById(R.id.send);
        send.setEnabled(false);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str = dataToSend.getText().toString();
                strBytes = str.getBytes();

                if(rx != null ) {
                    rx.setValue(strBytes);
                    btGatt.writeCharacteristic(rx);
                    btGatt.setCharacteristicNotification(rx,true);

                }
                else textRead.setText("nie odnaleziono odpowiedniego UUID");

            }
        });

        btDevice = getIntent().getExtras().getParcelable("btDevice");

        if(!(btDevice==null)) {
            if (btDevice.getName() == null) {
                textView.setText("Wybrano: \nNieznane urzadzenie" + "\n" + "(" + btDevice.getAddress() + ") ");
            } else {
                textView.setText("Wybrano: \n" + btDevice.getName() + "\n" + "(" + btDevice.getAddress() + ")");
            }
        }else {
            textView.setText("Blad");
        }

        btGatt = btDevice.connectGatt(this, false, bluetoothGattCallback);

    }

    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);

            System.out.println("onCharacteristicChanged  "+characteristic.getUuid().toString());
            System.out.println("onCharacteristicChanged  " + new String(characteristic.getValue()));
            data = characteristic.getValue();

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    textRead.setText("Odczytano: "+new String(data));
                }
            });
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);

            System.out.println("onCharacteristicRead  " + characteristic.getUuid().toString());
            System.out.println("onCharacteristicRead  " + new String(characteristic.getValue()));

            data = characteristic.getValue();

        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            System.out.println("BluetoothGattCallback----onConnectionStateChange"+newState);
            switch (newState) {
                case 0:
                    ConnectDevice.this.runOnUiThread(new Runnable() {
                        public void run() {
                            alert.append("Brak polaczenia\n");
                            send.setEnabled(false);
                        }
                    });
                    break;
                case 2:
                    ConnectDevice.this.runOnUiThread(new Runnable() {
                        public void run() {
                            alert.append("Polaczono\n");
                            send.setEnabled(true);
                        }
                    });

                    // discover services and characteristics for this device
                    btGatt.discoverServices();

                    break;
                default:
                    ConnectDevice.this.runOnUiThread(new Runnable() {
                        public void run() {
                            alert.append("...\n");
                        }
                    });
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            ConnectDevice.this.runOnUiThread(new Runnable() {
                public void run() {
                    alert.append("Wykryto uslugi. \n");
                }
            });
            displayGattServices(btGatt.getServices());
            send.setEnabled(true);
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
        ModelNumberStringUUIDchar=null;
        SerialPortUUIDchar=null;
        CommandUUIDchar=null;
        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {

            final String uuid = gattService.getUuid().toString();
            System.out.println("Service discovered: " + uuid);


            ConnectDevice.this.runOnUiThread(new Runnable() {
                public void run() {
                    alert.append("Wykryto usluge: "+uuid+"\n");
                }
            });
            if(gattService.getUuid().toString().equals(SerialPortUUID)){
                btGattService=gattService;
            }
            new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic :
                    gattCharacteristics) {

                final String charUuid = gattCharacteristic.getUuid().toString();
                System.out.println("Characteristic discovered for service: " + charUuid);

                ConnectDevice.this.runOnUiThread(new Runnable() {
                    public void run() {
                        alert.append("Wykryto charakterystyke dla uslugi: "+charUuid+"\n");
                    }
                });

                if(charUuid.equals(ModelNumberStringUUID)){
                    ModelNumberStringUUIDchar=gattCharacteristic;//nazwa
                    System.out.println("ModelNumberCharacteristic  "+ModelNumberStringUUIDchar.getUuid().toString());
                    alert.append("Wykryto ModelNumberString: "+ModelNumberStringUUIDchar.getUuid().toString()+"\n");
                }
                else if(charUuid.equals(SerialPortUUID)){//wysylanie/odbieranie danych
                    SerialPortUUIDchar = gattCharacteristic;
                    System.out.println("SerialPortCharacteristic  "+SerialPortUUIDchar.getUuid().toString());
                    alert.append("Wykryto SerialPortUUID: "+SerialPortUUIDchar.getUuid().toString()+"\n");
                }
                else if(charUuid.equals(CommandUUID)){
                    CommandUUIDchar = gattCharacteristic;
                    System.out.println("CommandUUIDchar   "+CommandUUIDchar.getUuid().toString());
                    alert.append("Wykryto CommandUUID: "+CommandUUIDchar.getUuid().toString()+"\n");
                }
            }
            rx = SerialPortUUIDchar;
        }
    }
}

