package com.example.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public ListView lv;
    public TextView tt;
    Button b;
    BluetoothAdapter bluetoothAdapter;
    ArrayList<String> address;
    ArrayList<String> devices;
    ArrayAdapter<String> ad;
    private void searchClick(View view)
    {
           tt.setText("Searching...");
           b.setEnabled(false);
        bluetoothAdapter.startDiscovery();
    }
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction()))
            {
                tt.setText("Finished");
                b.setEnabled(true);
                devices.clear();
                address.clear();
            }
            else if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction()))
            {
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name=device.getName();
                String a=device.getAddress();
                String s;
                String rssi=Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE));
                if(name==null)
                    s=a+"  "+rssi;
                else
                    s=name+" "+a+" "+rssi;
                if(!address.contains(a)){
                    devices.add(s);
                ad.notifyDataSetChanged();}
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b=(Button)findViewById(R.id.button);
        tt=(TextView) findViewById(R.id.textView);
        lv=(ListView) findViewById(R.id.listView);
        address=new ArrayList<>();
        devices=new ArrayList<>();
        ad=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,devices);
        lv.setAdapter(ad);



        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(broadcastReceiver,intentFilter);

    }
}
