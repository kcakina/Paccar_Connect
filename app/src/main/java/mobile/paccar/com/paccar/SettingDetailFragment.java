package mobile.paccar.com.paccar;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mobile.paccar.com.paccar.dummy.ConfigDummy;
import mobile.paccar.com.paccar.dummy.DummyContent;
import org.florescu.android.rangeseekbar.RangeSeekBar;

/**
 * A fragment representing a single Setting detail screen.
 * This fragment is either contained in a {@link SettingListActivity}
 * in two-pane mode (on tablets) or a {@link SettingDetailActivity}
 * on handsets.
 */
public class SettingDetailFragment extends Fragment {

    RangeSeekBar seekBarInteger, seekBarDouble;
    TextView minTextInt, maxtextInt, minTextDouble, maxTextDouble;
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private ConfigDummy.SensorItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SettingDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = ConfigDummy.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }

        //day and night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);


    }


        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.setting_detail, container, false);
            //setHasOptionsMenu(true);

                if(rootView != null) {
                    seekBarInteger = (RangeSeekBar) rootView.findViewById(R.id.seekbar);
                    if(seekBarInteger == null) {
                        Log.e("seekBar is ","NULLLLLLLLL");
                    } else  {
                        minTextInt = (TextView) rootView.findViewById(R.id.seekValuemin);
                        maxtextInt = (TextView) rootView.findViewById(R.id.seekValuemax);
                        seekBarInteger.setRangeValues(0, 30); // if we want to set progrmmatically set range of seekbar
                        seekBarInteger.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
                            @Override
                            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                                Log.e("value", minValue + "  " + maxValue);
                                minTextInt.setText("Min Value " + minValue);
                                maxtextInt.setText("Max value " + maxValue);
                            }



                        });
                    }
                }
            return rootView;
        }


    }
