package mobile.paccar.com.paccar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.view.Gravity;
import android.widget.LinearLayout;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import android.app.Activity;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;


public class ProfileActivity extends AppCompatActivity {

    Button getdata, update;
    TextView tv;
    EditText ed1;
    String data;
    private String file = "mydata";
    private static final int size = 25;
    private List<String> list = new ArrayList<String>();

    ///bluetooth stuff
    Button btnOn, btnOff;
    TextView txtArduino, txtString, dataIn, txtStringLength, sensorView0, sensorView1, sensorView2, sensorView3;
    Handler bluetoothIn;

    final int handlerState = 0;                         //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address;
    private GoogleApiClient mClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //bluetooth
        Intent intent = new Intent(this, DataServices.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        //end bluetooth

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        // JSON data test read
        /*TextView output = (TextView) findViewById(R.id.textView1);

        try {
            JSONObject  jsonRootObject = new JSONObject(loadJSONFromAsset());

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("Profile");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String id = jsonObject.optString("id").toString();
                String name = jsonObject.optString("name").toString();
                String type = jsonObject.optString("type").toString();

                //data += "My Node"+i+" : \n My_id= "+ id +" \n My_Name= "+ name +" \n Salary= "+ type +" \n ";
            }
            output.setText(data);
        } catch (JSONException e) {e.printStackTrace();}*/


    }

    View.OnClickListener getOnClickDoSomething(final Button button)  {
        return new View.OnClickListener() {
            public void onClick(View v) {
                //button.setText("text now set.. ");
                // communicate with data hub & go to the next page
                Intent i=new Intent(getApplicationContext(),SettingListActivity.class);
                startActivity(i);
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        // Need to do something for the notifications here.
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


//    public String parsingType(String line) {
//        String rt = "";
//        profileList pl = new profileList("", "", "");
//        String delims = "[ ]+";
//        String[] tokens = line.split(delims);
//        //  for (int i = 0; i < tokens.length; i++)
//        //    System.out.println(tokens[i]);
//
//        pl.id = tokens[0];
//        pl.type = tokens[1];
//        pl.list = tokens[2];
//        rt = pl.type;
//
//        return rt;
//    }

//    public static class profileList {
//        public String id;
//        public String type;
//        public String list;
//
//        public profileList(String id, String type, String list) {
//            this.id = id;
//            this.type = type;
//            this.list = list;
//        }
//
//        @Override
//        public String toString() {
//            return type;
//        }
//    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("profileData.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
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

                            List<DC_Profile> list = serializer.getProfileList(jsonD);

                            PopulateProfiles(list);

                            Log.d("UI thread", "I am the UI thread");

                        }

                    });
                }
            };
//            Log.e("Momo message",message);

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

    private void PopulateProfiles(List<DC_Profile> list) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearlayout);
        //String[] mTextofButton = { "Perishables", "Non-perishables", "Frozen", "Furniture", "Electronics", "Miscellaneous"};

        int count = list.size();

        // How many rows
        int numRows = Math.round(count / 2);

        DC_Profile currentItem = null;

        int index = 0;

        for (int i = 0; i < numRows; i++) {

            currentItem = list.get(index);

            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));


            for (int j = 0; j < 2; j++ ){
                Button btnTag = new Button(this); // create a new button
                btnTag.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) btnTag.getLayoutParams();
                params.setMargins(15, 15, 15, 20); //left, top, right, bottom
                btnTag.setLayoutParams(params); // set button parameter
                btnTag.setText(currentItem.name); // set profile name
                btnTag.setAllCaps(true); // Text to all caps
                btnTag.setTextColor(getResources().getColor(R.color.textColorPrimaryNight)); // set text color
                btnTag.setTextSize(20f); // Set text size
                btnTag.setWidth(500); // button width
                btnTag.setHeight(120); // button height
                btnTag.setPadding(30, 30, 30, 30);
                btnTag.setId(Integer.parseInt(currentItem.id)); // button id
                row.addView(btnTag);
                btnTag.setOnClickListener(getOnClickDoSomething(btnTag)); // Setting it up for each click
                index++;
            }

            layout.addView(row);
        }
    }

}