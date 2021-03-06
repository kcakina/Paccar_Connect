package mobile.paccar.com.paccar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.os.Handler;
import android.widget.TextView;

/**
 * An activity representing a single sensor detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link sensorListActivity}.
 */
public class sensorDetailActivity extends AppCompatActivity {

    private Handler handler = new Handler();
/*    private Runnable runnable = new Runnable(){
        public void run() {
           // findViewById(R.id.action_notificatio);
            Log.e("runnnnnn","worked");

            handler.postDelayed(this,1000);

        }
    };*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(sensorDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(sensorDetailFragment.ARG_ITEM_ID));
            sensorDetailFragment fragment = new sensorDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sensor_detail_container, fragment)
                    .commit();
        }
        //Log.e("I'm in the onCreate","in SDA, RUN?");
        //runnable.run();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            //setting icon should lead me to the setting page... but which page is the setting page?
            case R.id.action_settings:
                Intent i=new Intent(getApplicationContext(),SettingListActivity.class);
                startActivity(i);

                return true;

/*            //link to the home page
            case R.id.action_home:
                //  startActivity(new Intent(this, ));
                startActivity(new Intent(this,sensorListActivity.class));
                return true;*/

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
        menuInflater.inflate(R.menu.menu_main, menu);
        MenuItem settingItems = menu.findItem(R.id.action_notification);
        //setNotificationIcon(currentSeverityLevel,settingItems);

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
                    sensorDetailActivity.this.invalidateOptionsMenu();
                }
            }
        });
    }

}
