package mobile.paccar.com.paccar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONObject;

import mobile.paccar.com.paccar.dummy.ConfigDummy;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Setting. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SettingDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class SettingListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.setting_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.setting_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

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

                            List<DC_SensorCofig> list = serializer.getSensorConfigData(jsonD);

                            //PopulateSettingList(list);

                            Log.d("UI thread", "I am the UI thread");

                        }

                    });
                }
            };

            //sample data
            String message;

            message = OutgoingJsonCreation.getProfileList();

            mServices.sendRequest(callBack, MessageType.GetProfileList, message);

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    private void populateSensorList(List<DC_Sensor> list){

        // Data List
//        listView=(ListView)findViewById(R.id.list);
//
//        dataModels= new ArrayList<>();
//
//        for (DC_Sensor sensor : list) {
//            dataModels.add(new SensorListDataModel(sensor.sensorType, sensor.sensorName, sensor.sensorId, sensor.sensorData,
//                    sensor.sensorNum, sensor.sensorSeverity));
//        }
//
//
//        adapter= new SensorListAdapter(dataModels,getApplicationContext());
//
//        listView.setAdapter(adapter);

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

        switch (item.getItemId()) {

            //link to the home page
            case R.id.action_home:
                //  startActivity(new Intent(this, ));
                Intent i=new Intent(getApplicationContext(),sensorListActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
    int count = 0;
    TextView txtViewCount;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_sensor, menu);
        MenuItem settingItems = menu.findItem(R.id.action_notification);
        //setNotificationIcon(currentSeverityLevel,settingItems);

        // Adding badge to icon
        final View notifications = menu.findItem(R.id.action_notification).getActionView();
        txtViewCount = (TextView) notifications.findViewById(R.id.txtCount);

        updateHotCount(count);
        // this is where the number is grabbed from the datahub. inputted into the updatehotcount


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
                    SettingListActivity.this.invalidateOptionsMenu();
                }
            }
        });
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        ConfigDummy dummy = new ConfigDummy(recyclerView.getContext());

        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(dummy.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<ConfigDummy.SensorItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<ConfigDummy.SensorItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.setting_list_content, parent, false);
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
                        Bundle arguments = new Bundle();
                        arguments.putString(SettingDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        SettingDetailFragment fragment = new SettingDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.setting_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, SettingDetailActivity.class);
                        intent.putExtra(SettingDetailFragment.ARG_ITEM_ID, holder.mItem.id);

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
            public ConfigDummy.SensorItem mItem;

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
