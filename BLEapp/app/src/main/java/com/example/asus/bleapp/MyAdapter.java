package com.example.asus.bleapp;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Asus on 2018-11-02.
 */

public class MyAdapter extends ArrayAdapter<BluetoothDevice> {

    private final Context context;
    private final ArrayList<BluetoothDevice> values;
    public static final String addrRobot = "C8:A0:30:F8:BF:C0";
    public static final String addrBluno = "20:CD:39:87:4D:6D";

    public MyAdapter(@NonNull Context context, ArrayList<BluetoothDevice> values) {
        super(context, R.layout.activity_devices_list,values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_devices_list, parent,false);
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView add = (TextView) rowView.findViewById(R.id.address);
        BluetoothDevice devices = values.get(position);
        if(devices.getName()==null)
        {
            name.setText("Nieznane urzadzenie ");
            add.setText( " (" + devices.getAddress() + ") ");
        }
        else
        {
            if(devices.getAddress().toString().equals(addrRobot))
            {
                name.setText("Robot Romeo");
                add.setText( " (" + devices.getAddress() + ") ");
            }
            else if (devices.getAddress().toString().equals(addrBluno))
            {
                name.setText("Czujnik Nucleo Weather");
                add.setText( " (" + devices.getAddress() + ") ");
            }
            else {
                name.setText(devices.getName());
                add.setText( " (" + devices.getAddress() + ") ");
            }
        }

        return rowView;
    }
}
