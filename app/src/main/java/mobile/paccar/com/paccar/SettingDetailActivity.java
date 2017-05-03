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
import android.view.MenuItem;
import android.widget.TextView;

import org.florescu.android.rangeseekbar.RangeSeekBar;

/**
 * An activity representing a single Setting detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link SettingListActivity}.
 */


public class SettingDetailActivity extends AppCompatActivity {
    RangeSeekBar seekBarInteger, seekBarDouble;
    TextView minTextInt, maxtextInt, minTextDouble, maxTextDouble;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        //day and night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(SettingDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(SettingDetailFragment.ARG_ITEM_ID));
            SettingDetailFragment fragment = new SettingDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.setting_detail_container, fragment)
                    .commit();
        }

        /*{

            seekBarInteger = (RangeSeekBar) findViewById(R.id.seekbar);
            minTextInt = (TextView) findViewById(R.id.seekValuemin);
            maxtextInt = (TextView) findViewById(R.id.seekValuemax);
         *//*   seekBarDouble = (RangeSeekBar) findViewById(R.id.seekbarDouble);
            minTextDouble = (TextView) findViewById(R.id.seekValueminDouble);
            maxTextDouble = (TextView) findViewById(R.id.seekValuemaxDouble);
*//*


            seekBarInteger.setRangeValues(0, 30); // if we want to set progrmmatically set range of seekbar
            seekBarInteger.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {


                @Override
                public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                    Log.e("value", minValue + "  " + maxValue);
                    minTextInt.setText("Min Value " + minValue);
                    maxtextInt.setText("Max value " + maxValue);

                }

            });


        }*/
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            //setting icon should lead me to the setting page... but which page is the setting page?
            case R.id.action_settings:
                Intent i=new Intent(getApplicationContext(),SettingListActivity.class);
                startActivity(i);
                return true;

/*           //link to the home page
            case R.id.action_home:
                //  startActivity(new Intent(this, ));
                startActivity(new Intent(this,sensorListActivity.class));
                return true;*/

            //link to save changes
            case R.id.action_save:
                //  startActivity(new Intent(this, ));
                startActivity(new Intent(this,sensorListActivity.class));
                return true;

            // Link to the notifications page
            case R.id.action_notification:
                //need notification page
                startActivity(new Intent(this,Notification.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    //adding the menu to sensor bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);

        return true;
    }


}
