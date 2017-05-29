package mobile.paccar.com.paccar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import mobile.paccar.com.paccar.common.logger.Log;

public class Notification extends AppCompatActivity {

    // DataModel list
    ArrayList<NotificationListDataModel> dataModels;
    ListView listView;

    private static NotificationListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setContentView(R.layout.activity_sensor_detail_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //bluetooth
        Intent intent = new Intent(this, DataServices.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

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
            if(mServices == null) {
                android.util.Log.e("mServices in the PA","is null");
            } else {
                android.util.Log.e("mServices in the PA","is not null");
            }
            mBound = true;

            IDataReceivedCallBack callBack = new IDataReceivedCallBack() {
                @Override
                public void DataReceived(MessageType id, final JSONObject jsonD) {

                    runOnUiThread(new Runnable() {
                        public void run() {

                            if (jsonD != null) {
                                android.util.Log.e("CallBack??worked??", jsonD + "");
                            } else {
                                android.util.Log.e("JSON", "No JSON received");
                            }

                            DataSerialization serializer = new DataSerialization();

                            List<DC_Notification> list = serializer.getNotification(jsonD);

                            populateNotificationList(list);

                            android.util.Log.d("UI thread", "I am the UI thread");

                        }

                    });


                }
            };

            //sample data
            String message;

            message = OutgoingJsonCreation.getNotifications();

            mServices.sendRequest(callBack, MessageType.GetNotifications, message);

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    private void populateNotificationList(List<DC_Notification> list){

        // Data List
        listView=(ListView)findViewById(R.id.notification_list);

        dataModels= new ArrayList<>();

        // Hard coded additions for testing
//        dataModels.add(new SensorListDataModel("Temp",     "1", "56 F",  0, 0));
//        dataModels.add(new SensorListDataModel("Humidity", "2", "10 F",  1, 1));
//        dataModels.add(new SensorListDataModel("Temp",     "3", "13 F",  2, 3));
//        dataModels.add(new SensorListDataModel("Humidity", "4", "300 C", 3, 2));
//        dataModels.add(new SensorListDataModel("Temp",     "5", "99F ",  4, 1));

        for (DC_Notification notification : list) {
            dataModels.add(new NotificationListDataModel(notification.sensorName, notification.data, notification.severity,
                    notification.time));
        }


        adapter= new NotificationListAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            //setting icon should lead me to the setting page... but which page is the setting page?
            case R.id.action_settings:
                Intent i = new Intent(getApplicationContext(), SettingListActivity.class);
                startActivity(i);
                return true;

            //link to the home page
            case R.id.action_home:
                //  startActivity(new Intent(this, ));
                startActivity(new Intent(this, sensorListActivity.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.notification_menu, menu);
        return true;
    }
}

