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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Nucleo extends AppCompatActivity {


    BluetoothDevice btDevice;
    BluetoothGatt btGatt;
    BluetoothGattCharacteristic tx, rx, SerialPortUUIDchar, CommandUUIDchar, ModelNumberStringUUIDchar;
    BluetoothGattService btGattService;
    private TextView textView, alert, textRead, tView,pView,pView2,pView3,lView,hView;
    private Button bt,send;
    private String str, pomiar;
    byte[] data;
    byte[] strBytes = new byte[1];
    public static final String SerialPortUUID="0000dfb1-0000-1000-8000-00805f9b34fb";//wyslanie i odczytanie znaku
    public static final String CommandUUID="0000dfb2-0000-1000-8000-00805f9b34fb";
    public static final String ModelNumberStringUUID="00002a24-0000-1000-8000-00805f9b34fb";//nazwa urzadzenia



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nucleo);

        textView = (TextView) findViewById(R.id.textView4);
        textView.setText("Wybrano:\n"+"Czujnik Nucleo Weather");
        tView = (TextView) findViewById(R.id.term);
        pView = (TextView) findViewById(R.id.pres);
        pView2 = (TextView) findViewById(R.id.pres2);
        pView3 = (TextView) findViewById(R.id.pres3);
        hView = (TextView) findViewById(R.id.hum);
        lView = (TextView) findViewById(R.id.light);
        alert = (TextView) findViewById(R.id.textView6);
        textRead = (TextView) findViewById(R.id.textView5);
        //dataToSend = (EditText) findViewById(R.id.dataText);
        bt = (Button) findViewById(R.id.b1);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(Nucleo.this, MainActivity.class);
                startActivity(mainIntent);
                btGatt.disconnect();
                btGatt.close();
                btGatt=null;
            }
        });
        send = (Button) findViewById(R.id.pom);
        send.setEnabled(false);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str = "0";
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

        btGatt = btDevice.connectGatt(this, false, bluetoothGattCallback);

    }

    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);

            data = characteristic.getValue();
            System.out.println("onCharacteristicChanged  "+characteristic.getUuid().toString());
            System.out.println("onCharacteristicChanged  " + new String(data));

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    pomiar = new String(data);
                    if(pomiar.startsWith("A"))
                    {
                        pomiar = pomiar.substring(1);
                        textRead.setText("Wilgotnosc: "+pomiar+" %");
                    }
                    else if(pomiar.startsWith("B"))
                    {
                        pomiar = pomiar.substring(1);
                        hView.setText("Temperatura: "+pomiar+" st C");
                    }
                    else if(pomiar.startsWith("C"))
                    {
                        pomiar = pomiar.substring(1);
                        tView.setText("Temperatura: "+pomiar+" st C");
                    }
                    else if(pomiar.startsWith("D"))
                    {
                        pomiar = pomiar.substring(1);
                        pView2.setText("Wysokosc: "+pomiar+" m");
                    }
                    else if(pomiar.startsWith("E"))
                    {
                        pomiar = pomiar.substring(1);
                        pView3.setText("Temperatura: "+pomiar+" st C");
                    }
                    else if(pomiar.startsWith("F"))
                    {
                        pomiar = pomiar.substring(1);
                        lView.setText("Natezenie swiatla: "+pomiar+" lx(Luks)");
                    }
                    else
                    {
                        pView.setText("Cisnienie: "+pomiar+" hPa");
                    }
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
                    System.out.println("Urzadzenie rozlaczone\n");

                    break;
                case 2:
                    System.out.println("Polaczono z urzadzeniem\n");

                    // discover services and characteristics for this device
                    btGatt.discoverServices();

                    break;
                default:
                    System.out.println("...\n");

                    break;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Nucleo.this.runOnUiThread(new Runnable() {
                public void run() {

                        alert.setText("Urzadzenie gotowe do pomiaru.");
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


                if(charUuid.equals(ModelNumberStringUUID)){
                    ModelNumberStringUUIDchar=gattCharacteristic;//nazwa
                    System.out.println("ModelNumberCharacteristic  "+ModelNumberStringUUIDchar.getUuid().toString());

                }
                else if(charUuid.equals(SerialPortUUID)){//wysylanie/odbieranie danych
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
}

