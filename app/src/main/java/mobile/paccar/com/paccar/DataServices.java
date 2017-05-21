package mobile.paccar.com.paccar;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import java.sql.Timestamp;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataServices extends Service {
    DataServices mServices;
    MessageComm messageComm;
    boolean mBound = false;
    private GoogleApiClient mClient;

    private Handler handler = new Handler();//?!?!?!?!?!?!
    private Long getCurrentTime() {
        return System.currentTimeMillis()/1000;
    }

    public DataServices() {
        messageComm = new MessageComm();
    }

    public void connect(String address){
        messageComm.connect(address);
    }

    public void setNotificationCountCallback(IDataReceivedCallBack notificationCallBack){
    }

    public void setSensorDataCallback(IDataReceivedCallBack getSensorDataCallBack) {
    }

    public void sendRequest(IDataReceivedCallBack callBack, MessageType messageID, String jsonD){
        messageComm.sendRequest(callBack,messageID,jsonD);
    }

    //specific to Android service
    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");

        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String messaged = "Serivce Running ";
        Toast.makeText(getApplicationContext(), messaged, Toast.LENGTH_LONG).show();
        String address = "";
        if(intent == null){
            Log.e("DataS, intent", "is null");
        } else {
            Log.e("DataS, intent", "is not null");
        }

//        Toast.makeText(getApplicationContext(), address, Toast.LENGTH_LONG).show();

        return START_STICKY;
    }

    public class LocalBinder extends Binder{
        DataServices getService(){
            return DataServices.this;
        }
    }

    //bluetooth
    @Override
    public void onCreate() {
        super.onCreate();
    }

/*
        * Please Read this*
        * This will tell you what you are getting back from the BT and which messageID it turns to
        * Error happens when the request arrives within one second.
        * You can see the MessageID doesn't match the jsonResponse when the time difference < 1
        * For a quick fix, we delayed the sendRequest by 1.5s for now.
        * Highly recommend to implement queue to handle sendRequest, or use different
        * executors for Async
        * Useful link ->
        * http://stackoverflow.com/questions/10480599/how-asynctask-works-in-android
        * */

    @Override
    public void onDestroy() {
        super.onDestroy();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://mobile.paccar.com.paccar/http/host/path")
        );

        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }

        mClient.disconnect();
        AppIndex.AppIndexApi.end(mClient, viewAction);
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DataServices.LocalBinder binder = (DataServices.LocalBinder) service;
            mServices = binder.getService();
            mBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
