package mobile.paccar.com.paccar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import org.json.JSONObject;

import mobile.paccar.com.paccar.dummy.DummyContent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    IDataReceivedCallBack notificationCallBack;
    IDataReceivedCallBack getSensorDataCallBack;
    SeverityLevel currentSeverityLevel;

    private Handler handler = new Handler();
    /*
    private Runnable runnable = new Runnable(){


        public void run() {
            // findViewById(R.id.action_notificatio);
            Log.e("runnnnnn","worked");
            /*if(mServices != null)
            {
                mServices.sendRequest(notificationCallBack, MessageType.GetNotifications, message);
            }
            handler.postDelayed(this,1000);

        }
    };
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_list);
        setTitle("HOME PAGE Sensor List Activity");
        currentSeverityLevel = SeverityLevel.NotSet;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        //bluetooth
        Intent intent = new Intent(this, DataServices.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        View recyclerView = findViewById(R.id.sensor_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        //day and night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);

        DataSerialization mDataSerialization = new DataSerialization();

        //sample data
        Map<String, String> datalist = new HashMap<String, String>();
        datalist.put("messageID", "1");
        Log.e("MessageID","1");
        message = mDataSerialization.convertToJSON(datalist);

        getSensorDataCallBack = new IDataReceivedCallBack() {
            @Override
            public void DataReceived(MessageType id, JSONObject jsonD) {
                //TODO send the data to addItem(),
                Log.e("getSensorDataCallBack","WORKED!");
            }
        };

        notificationCallBack = new IDataReceivedCallBack() {
            @Override
            public void DataReceived(MessageType id, JSONObject jsonD) {
                Log.e("I'm in the CallBack","SLA");
                DataSerialization jsonSerializer = new DataSerialization();
                DC_NotificationCount counts = jsonSerializer.getNotificationCount(jsonD);


                if(counts.low > 0) {
                    Log.e("notificationItem","low");
                    //TODO update the icon of notification, and invalidate the menu item.
                    //this.invalidateOptionsMenu();
                    currentSeverityLevel = SeverityLevel.Low;
                    sensorListActivity.this.invalidateOptionsMenu();

                }

                if(counts.medium > 0) {
                    Log.e("notificationItem","medium");
                    //TODO update the icon of notification, and invalidate the menu item.
                    currentSeverityLevel = SeverityLevel.Medium;
                    sensorListActivity.this.invalidateOptionsMenu();
                }

                if(counts.high > 0) {
                    Log.e("notificationItem","high");
                    //TODO update the icon of notification, and invalidate the menu item.
                    currentSeverityLevel = SeverityLevel.High;
                    sensorListActivity.this.invalidateOptionsMenu();
                }

/*               // Log.e("list size", counts.size() + "");
                for (int i = 0; i < list.size(); i++) {
                    //String notificationItem =  list.get(i).sensorID + list.get(i).sensorType + list.get(i).value;
                    //notificationListAL.add(notificationItem);
                    if(list.get(i).severity == "HIGH") {
                        Log.e("notificationItem",list.get(i).severity);
                        //TODO update the icon of notification, and invalidate the menu item.
                        break;
                    }
                }*/
            }
        };

        Log.e("Momo message",message);

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
            //DataService mDataService = new DataService();
            //wire the
            mServices.setNotificationCountCallback(notificationCallBack);
            mServices.setSensorDataCallback(getSensorDataCallBack);

            //sample data
            Map<String, String> datalist = new HashMap<String, String>();
            datalist.put("messageID", "3");
            Log.d("MessageID","3 + momo");
//            String message = mDataService.convertToJSON(datalist);

            IDataReceivedCallBack callBack = new IDataReceivedCallBack() {
                @Override
                public void DataReceived(MessageType id, JSONObject jsonD) {
                    Log.e("I'm in the CallBack","SLA");
                    DataSerialization myService = new DataSerialization();
                    List<DC_Notification> list = myService.getNotification(jsonD);
               /*     if(jsonD != null) {
                        Log.e("CallBack??worked??",jsonD + "");
                    }*/
                   // Log.e("list size", list.size() + "");
                    for (int i = 0; i < list.size(); i++) {
                        //String notificationItem =  list.get(i).sensorID + list.get(i).sensorType + list.get(i).value;
                        //notificationListAL.add(notificationItem);
                        if(list.get(i).severity == "HIGH") {
                            //TODO DO NOT USE == to check String
                            Log.e("notificationItem",list.get(i).severity);
                            break;
                        }
                    }
                }
            };
            Log.e("Momo message",message);

//            mServices.sendRequest(callBack, MessageType.GetSensorList, message);
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


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
        switch (item.getItemId()) {

            //setting icon should lead me to the setting page... but which page is the setting page?
            case R.id.action_settings:
                Intent i=new Intent(getApplicationContext(),SettingListActivity.class);
                startActivity(i);
                return true;

            case R.id.action_notification:
                //need notification page
                startActivity(new Intent(this,Notification.class));
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    //adding the menu to sensorbar.
    // Menu testing 1 2 3...1 2 3

    int count = 0;
    TextView txtViewCount;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        MenuItem settingItems = menu.findItem(R.id.action_notification);
        setNotificationIcon(currentSeverityLevel,settingItems);

        // Adding badge to icon
        final View notificaitons = menu.findItem(R.id.action_notification).getActionView();
        txtViewCount = (TextView) notificaitons.findViewById(R.id.txtCount);
        updateHotCount(count);
        // this is where the number is grabbed from the datahub. inputted into the updatehotcount
//        Button button = (Button) findViewById(R.id.buttonpress);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                count++;
//                updateHotCount(count);
//            }
//        });

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


    public void setNotificationIcon(SeverityLevel level, MenuItem menuItem) {

        switch (level) {
            case Low:
                menuItem.setIcon(R.drawable.ic_action_lnotification);
                break;

            case Medium:
                menuItem.setIcon(R.drawable.ic_action_mnotification);
                break;

            case High:
                menuItem.setIcon(R.drawable.ic_action_hnotification);
                break;
        }
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {


        DummyContent dummy = new DummyContent();

        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(dummy.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sensor_list_content, parent, false);
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
