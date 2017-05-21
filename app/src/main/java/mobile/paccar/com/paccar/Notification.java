package mobile.paccar.com.paccar;
import android.content.ComponentName;
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
import java.util.Map;

import android.view.Menu;

public class Notification extends AppCompatActivity {

    DataSerialization myService;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> notificationListAL = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("NOTIFICATION Notification");
        setContentView(R.layout.activity_notification);
        setContentView(R.layout.activity_sensor_detail_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//         arrayAdapter =
//                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, notificationListAL);

        //bluetooth
//        Intent intent = new Intent(this, DataServices.class);
//        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        // Get list of notifications
        //            Map<String, String> datalist = new HashMap<String, String>();
//            datalist.put("messageID", "3");
//            Log.d("MessageID","3 + momo");
//            String message = mDataService.convertToJSON(datalist);

//            IDataReceivedCallBack callBack = new IDataReceivedCallBack() {
//                @Override
//                public void DataReceived(MessageType id, JSONObject jsonD) {
//                    Log.e("I'm in the CallBack","SLA");
//                    DataSerialization myService = new DataSerialization();
//                    List<DC_Notification> list = myService.getNotification(jsonD);
//               /*     if(jsonD != null) {
//                        Log.e("CallBack??worked??",jsonD + "");
//                    }*/
//                   // Log.e("list size", list.size() + "");
//                    for (int i = 0; i < list.size(); i++) {
//                        //String notificationItem =  list.get(i).sensorID + list.get(i).sensorType + list.get(i).value;
//                        //notificationListAL.add(notificationItem);
//                        if(list.get(i).severity == "HIGH") {
//                            //TODO DO NOT USE == to check String
//                            Log.e("notificationItem",list.get(i).severity);
//                            break;
//                        }
//                    }
//                }
//            };
    }

    private HashMap<String, String>createEmployee(String name,String number){
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            //setting icon should lead me to the setting page... but which page is the setting page?
            case R.id.action_settings:
                Intent i=new Intent(getApplicationContext(),SettingListActivity.class);
                startActivity(i);
                return true;

            //link to the home page
            case R.id.action_home:
                //  startActivity(new Intent(this, ));
                startActivity(new Intent(this,sensorListActivity.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.notification_menu,menu);
        return true;
    }


//    DataServices mServices;
    boolean mBound = false;


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Map<String, String> datalist = new HashMap<String, String>();
            datalist.put("messageID", "5");

            // We've bound to LocalService, cast the IBinder and get LocalService instance
//            DataServices.LocalBinder binder = (DataServices.LocalBinder) service;
//            mServices = binder.getService();
            mBound = true;
            myService = new DataSerialization();
            final String json = myService.convertToJSON(datalist);


        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}

