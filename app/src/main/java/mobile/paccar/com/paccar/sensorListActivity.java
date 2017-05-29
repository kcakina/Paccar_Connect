package mobile.paccar.com.paccar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import org.json.JSONObject;

import mobile.paccar.com.paccar.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of sensors. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link sensorDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class sensorListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    String message;
    IDataReceivedCallBack notificationCountCallBack;
    IDataReceivedCallBack getSensorDataCallBack;
    SeverityLevel currentSeverityLevel;

    // DataModel list
    ArrayList<SensorListDataModel> dataModels;
    ListView listView;
    LinearLayout mainHome;
    TextView textViewName;
    TextView textViewData;
    private static SensorListAdapter adapter;

    private Handler handler = new Handler();

//    private Runnable runnable = new Runnable(){
//
//
//        public void run() {
//            // findViewById(R.id.action_notificatio);
//            Log.e("runnnnnn","worked");
//            /*if(mServices != null)
//            {
//                mServices.sendRequest(notificationCallBack, MessageType.GetNotifications, message);
//            }
//            handler.postDelayed(this,1000);
//
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_list);

        currentSeverityLevel = SeverityLevel.NotSet;

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setTitle(getTitle());

        //bluetooth
        Intent intent = new Intent(this, DataServices.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

//        View recyclerView = findViewById(R.id.sensor_list);
//        assert recyclerView != null;
//        setupRecyclerView((RecyclerView) recyclerView);


        getSensorDataCallBack = new IDataReceivedCallBack() {
            @Override
            public void DataReceived(MessageType id, final JSONObject jsonD) {
                //TODO send the data to addItem(),
                Log.e("getSensorDataCallBack","WORKED!");

                runOnUiThread(new Runnable() {
                    public void run() {

                        if (jsonD != null) {
                            Log.e("CallBack??worked??", jsonD + "");
                        } else {
                            Log.e("JSON", "No JSON received");
                        }

                        DataSerialization serializer = new DataSerialization();

                        List<DC_SensorData> list = serializer.getSensorData(jsonD);

                        updateSensorData(list);

                        Log.d("UI thread", "Give me data");

                    }

                });
            }
        };

        notificationCountCallBack = new IDataReceivedCallBack() {
            @Override
            public void DataReceived(MessageType id, final JSONObject jsonD) {
                Log.e("I'm in the CallBack","SLA");

                runOnUiThread(new Runnable() {
                    public void run() {

                        DataSerialization jsonSerializer = new DataSerialization();
                        DC_NotificationCount counts = jsonSerializer.getNotificationCount(jsonD);


                        // Updating Notification icon
                        updateNotificationIcon(counts);

                        // Updating notification count
                        int totalCount = counts.low + counts.medium + counts.high;

                        updateHotCount(totalCount);

                        Log.d("UI thread", "Give me notifications");
                    }

                });
            }
        };


        if (findViewById(R.id.sensor_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.

            mTwoPane = true;
        }
        // runnable.run();


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
                Log.e("mServices in the PA","is null");
            } else {
                Log.e("mServices in the PA","is not null");
            }
            mBound = true;

            //wire the callbacks
            mServices.setNotificationCountCallback(notificationCountCallBack);
            mServices.setSensorDataCallback(getSensorDataCallBack);


            IDataReceivedCallBack callBack = new IDataReceivedCallBack() {
                @Override
                public void DataReceived(MessageType id, final JSONObject jsonD) {

                    runOnUiThread(new Runnable() {
                        public void run() {

                            if (jsonD != null) {
                                Log.e("CallBack??worked??", jsonD + "");
                            } else {
                                Log.e("JSON", "No JSON received");
                            }

                            DataSerialization serializer = new DataSerialization();

                            List<DC_Sensor> list = serializer.getSensorList(jsonD);

                            populateSensorList(list);

                            Log.d("UI thread", "I am the UI thread");

                        }

                    });
                }
            };

//            Log.e("Momo message",message);


//            IDataReceivedCallBack callBack = new IDataReceivedCallBack() {
//                @Override
//                public void DataReceived(MessageType id, JSONObject jsonD) {
//                    Log.e("I'm in the CallBack","SLA");
//                    DataSerialization myService = new DataSerialization();
//                    List<DC_Notification> list = myService.getNotification(jsonD);
//
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
//            Log.e("Momo message",message);

            String message;

            message = OutgoingJsonCreation.getSensorList();

            mServices.sendRequest(callBack, MessageType.GetSensorList, message);

        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private void populateSensorList(List<DC_Sensor> list){

        // Data List
        listView=(ListView)findViewById(R.id.list);
        mainHome=(LinearLayout)findViewById(R.id.sensor_main);
        textViewName=(TextView)findViewById(R.id.homeDis_SensorName);
        textViewData=(TextView)findViewById(R.id.homeDis_SensorData);

        dataModels= new ArrayList<>();

        // Hard coded additions for testing
//        dataModels.add(new SensorListDataModel("Temp",     "1", "56 F",  0, 0));
//        dataModels.add(new SensorListDataModel("Humidity", "2", "10 F",  1, 1));
//        dataModels.add(new SensorListDataModel("Temp",     "3", "13 F",  2, 3));
//        dataModels.add(new SensorListDataModel("Humidity", "4", "300 C", 3, 2));
//        dataModels.add(new SensorListDataModel("Temp",     "5", "99F ",  4, 1));

        for (DC_Sensor sensor : list) {
            dataModels.add(new SensorListDataModel(sensor.sensorType, sensor.sensorName, sensor.sensorId, sensor.sensorData,
                    sensor.sensorNum, sensor.sensorSeverity, sensor.upperThreshold, sensor.lowerThreshold));
        }


        // Main display of home screen
        textViewName.setText(dataModels.get(0).getName());
        textViewData.setText(dataModels.get(0).getCurrentData());

        switch (dataModels.get(0).getSeverityStatus()) {
            case 0:
                mainHome.setBackgroundResource(R.drawable.no_severity_mainborder);
                break;
            case 1:
                mainHome.setBackgroundResource(R.drawable.low_severity_mainborder);
                break;
            case 2:
                mainHome.setBackgroundResource(R.drawable.medium_severity_mainborder);
                break;
            case 3:
                mainHome.setBackgroundResource(R.drawable.high_severity_mainborder);
                break;
        }

        adapter= new SensorListAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);

    }


    private void updateSensorData(List<DC_SensorData> list){

        // Updates the Current Values in the array to populate the home page
        for (DC_SensorData sensorData : list) {
            for (SensorListDataModel sensor : dataModels) {
                if (sensor.getID().equalsIgnoreCase(sensorData.sensorId)) {
                    sensor.currentData = sensorData.sensorData;         // Data update
                    sensor.sensorSeverity = sensorData.sensorSeverity;  // Severity update
                }
            }
        }

        adapter.notifyDataSetChanged();
        updateHomeMain(list);

    }

    private void updateHomeMain(List<DC_SensorData> list) {

        mainHome=(LinearLayout)findViewById(R.id.sensor_main);         // linear layout
        textViewName=(TextView)findViewById(R.id.homeDis_SensorName);  // text view
        textViewData=(TextView)findViewById(R.id.homeDis_SensorData);  // text view

        // Main display of home screen

        textViewData.setText(list.get(1).sensorData);

        switch (list.get(1).sensorSeverity) {
            case 0:
                mainHome.setBackgroundResource(R.drawable.no_severity_mainborder);
                break;
            case 1:
                mainHome.setBackgroundResource(R.drawable.low_severity_mainborder);
                break;
            case 2:
                mainHome.setBackgroundResource(R.drawable.medium_severity_mainborder);
                break;
            case 3:
                mainHome.setBackgroundResource(R.drawable.high_severity_mainborder);
                break;
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            // This ID represents the Home or Up button. In the case of this
//            // activity, the Up button is shown. For
//            // more details, see the Navigation pattern on Android Design:
//            //
//            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
//            //
//            navigateUpTo(new Intent(this, sensorListActivity.class));
//            return true;
//        }

        Menu menu = null;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        switch (item.getItemId()) {

            //setting icon should lead me to the setting page... but which page is the setting page?
            case R.id.action_settings:
                Intent i=new Intent(getApplicationContext(),SettingListActivity.class);
                startActivity(i);
                return true;

            case R.id.action_notification:
                // notification page
                i=new Intent(getApplicationContext(),Notification.class);
                startActivity(i);
                return true;

        }

        final MenuItem mItem = menu.findItem(R.id.notification_icon);
        mItem.getActionView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Notification.class);
                startActivity(i);
            }
        });

        return super.onOptionsItemSelected(item);
    }

    //adding the menu to sensorbar.
    // Menu testing 1 2 3...1 2 3

    TextView txtViewCount;
    int count = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        // Doesn't work/either this or the badge
        //MenuItem notificationIcon = menu.findItem(R.id.action_notification);
        //setNotificationIcon(currentSeverityLevel,notificationIcon);

        // Adding badge to icon
        final View notifications = menu.findItem(R.id.action_notification).getActionView();
        txtViewCount = (TextView) notifications.findViewById(R.id.txtCount);


//        final MenuItem notificationIcon = menu.findItem(R.id.action_notification);
//
//        notificationIcon.getActionView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onOptionsItemSelected(notificationIcon);
//            }
//        });


        //initialize notification count
        updateHotCount(count);


        return true;
    }

    public void updateHotCount(final int new_hot_number) {
        count = new_hot_number;
        if (count < 0) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (count == 0)
                    txtViewCount.setVisibility(View.GONE);
                else {
                    txtViewCount.setVisibility(View.VISIBLE);
                    txtViewCount.setText(Integer.toString(count));
                    sensorListActivity.this.invalidateOptionsMenu();
                }
            }
        });
    }

    public void updateNotificationIcon(DC_NotificationCount counts){


        if (counts.low > 0) {

            if ((counts.low > counts.medium) && (counts.low > counts.high)) {
                Log.e("notificationItem", "low");
                currentSeverityLevel = SeverityLevel.Low;
            }

        }

        if (counts.medium > 0) {
            if ((counts.medium >= counts.low) && (counts.medium > counts.high)) {
                Log.e("notificationItem", "medium");
                currentSeverityLevel = SeverityLevel.Medium;
            }
        }

        if (counts.high > 0) {
            if ((counts.high >= counts.low) && (counts.high >= counts.medium)) {
                Log.e("notificationItem", "high");
                currentSeverityLevel = SeverityLevel.High;
            }
        }

        invalidateOptionsMenu();
    }


//    public void setNotificationIcon(SeverityLevel level, MenuItem menuItem) {
//
//
//        switch (level) {
//            case Low:
//                menuItem.setIcon(R.drawable.ic_action_lnotification);
//                break;
//
//            case Medium:
//                menuItem.setIcon(R.drawable.ic_action_mnotification);
//                break;
//
//            case High:
//                menuItem.setIcon(R.drawable.ic_action_hnotification);
////                menuItem.setImageResource(R.drawable.ic_action_hnotification);
//                break;
//        }
//    }


    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sensor_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Log.d("onBindViewHolder", "YourOutput_if");
                        Bundle arguments = new Bundle();
                        arguments.putString(sensorDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        sensorDetailFragment fragment = new sensorDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.sensor_detail_container, fragment)
                                .commit();
                    } else {
                        Log.d("onBindViewHolder", "YourOutput_else");
                        Context context = v.getContext();
                        Intent intent = new Intent(context, sensorDetailActivity.class);
                        intent.putExtra(sensorDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }

    }
}
