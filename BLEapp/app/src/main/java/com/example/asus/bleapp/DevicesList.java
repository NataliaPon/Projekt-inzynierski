package com.example.asus.bleapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.Manifest.permission.*;

public class DevicesList extends AppCompatActivity
{

    BluetoothManager btManager;
    BluetoothAdapter btAdapter;
    BluetoothLeScanner btScanner;
    BluetoothDevice btDevice;
    private ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
    private MyAdapter arrayAdapter;
    Handler btHandler = new Handler();
    TextView name;
    private ListView devicesList;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    public static final String addrRobot = "C8:A0:30:F8:BF:C0";
    public static final String addrBluno = "20:CD:39:87:4D:6D";

    ////Plytka Romeo adres: C8:A0:30:F8:BF:C0
    ///Plytka Bluno adres: 20:CD:39:87:4D:6D

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_list);

        devicesList = (ListView) findViewById(R.id.device);
        name = (TextView) findViewById(R.id.name);

        btManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();

        if(!btAdapter.isEnabled())
        {
            btAdapter.enable();
        }
        if(!(checkSelfPermission(ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED))
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
        }
        devices.clear();
        btScanner.startScan(leScanCallback);
        btHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btScanner.stopScan(leScanCallback);
                arrayAdapter.notifyDataSetChanged();
            }
        }, 50000);


        arrayAdapter = new MyAdapter(this, devices);
        devicesList.setAdapter(arrayAdapter);

        devicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                btDevice = devices.get(i);
                if(btDevice.getAddress().toString().equals(addrRobot))
                {
                    Intent connectDev = new Intent(getApplicationContext(), RobotRomeo.class);
                    connectDev.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    connectDev.putExtra("btDevice",btDevice);//wyslanie wyniku
                    getApplicationContext().startActivity(connectDev);
                    btScanner.stopScan(leScanCallback);
                }
                else if (btDevice.getAddress().toString().equals(addrBluno))
                {
                    Intent connectDev = new Intent(getApplicationContext(), Nucleo.class);
                    connectDev.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    connectDev.putExtra("btDevice", btDevice);//wyslanie wyniku
                    getApplicationContext().startActivity(connectDev);
                    btScanner.stopScan(leScanCallback);
                }
                else
                {
                    Intent connectDev = new Intent(getApplicationContext(), ConnectDevice.class);
                    connectDev.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    connectDev.putExtra("btDevice", btDevice);//wyslanie wyniku
                    getApplicationContext().startActivity(connectDev);
                    btScanner.stopScan(leScanCallback);
                }
            }
        });

    }

    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

            if(!devices.contains(result.getDevice()))
            {
                devices.add(result.getDevice());
                arrayAdapter.notifyDataSetChanged();
            }
        }
    };
}

