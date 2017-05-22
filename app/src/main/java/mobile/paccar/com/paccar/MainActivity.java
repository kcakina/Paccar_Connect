package mobile.paccar.com.paccar;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Checks for day/night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); // Night Mode test

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Intent deviceListIntent = new Intent(this, DeviceList.class);
        startActivityForResult(deviceListIntent, REQUEST_CONNECT_DEVICE_SECURE); // this gives us address
    }

    DataServices mServices;
    boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DataServices.LocalBinder binder = (DataServices.LocalBinder) service;
            mServices = binder.getService();
            if (mServices == null) {
                Log.e("mServices in the PA", "is null");
            } else {
                Log.e("mServices in the PA", "is not null");
            }
            mBound = true;
            connectDevice(address, true);

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    private void connectDevice(String address, boolean secure) {
        // Get the BluetoothDevice object
        //BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mServices.connect(address);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
                case REQUEST_CONNECT_DEVICE_SECURE:
                    // When DeviceListActivity returns with a device to connect
                    if (resultCode == Activity.RESULT_OK) {
                        address = data.getExtras()
                             .getString(DeviceList.EXTRA_DEVICE_ADDRESS);
                        String msg = "CONNECTED!";
                        getService();
                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                        navigateMain();
                    }
                    break;
                case REQUEST_CONNECT_DEVICE_INSECURE:
                    // When DeviceListActivity returns with a device to connect
                   // if (resultCode == Activity.RESULT_OK) {
                     //   connectDevice(data, false);
                    //}
                    break;
                case REQUEST_ENABLE_BT:
                    // When the request to enable Bluetooth returns
                   // if (resultCode == Activity.RESULT_OK) {
                        // Bluetooth is now enabled, so set up a chat session
                     //   setupChat();
                    //} else {
                        // User did not enable Bluetooth or an error occurred
                      //  Log.d(TAG, "BT not enabled");
                        //Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                          //      Toast.LENGTH_SHORT).show();
                        //getActivity().finish();
                    }
            }

    public void getService() { // Method gets the Bluetooth Service
        Intent intent = new Intent(getApplicationContext(), DataServices.class); //
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public void navigateMain(){
        Intent sensorListIntent = new Intent(this, ProfileActivity.class);
        startActivity(sensorListIntent);
    }

    public void sendDummyData(){
        Intent sendDummyDataIntent = new Intent(this,SendDummyData.class);
        bindService(sendDummyDataIntent,mConnection, Context.BIND_AUTO_CREATE);
    }
}
