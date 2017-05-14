package mobile.paccar.com.paccar;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oshiancoates on 3/21/17.
 */

public class MessageComm implements IDataReceivedCallBack {

    private class DataHolder {
        public ICallBack CallBack;
        public MessageType MessageID;
        public String JsonD;
    }

    DataSerialization mDataSerialization = new DataSerialization();
    BluetoothBoss boss;

    private Long mostRecentNotificationTime;
    private Long mostRecentSensorDataTime;
    private String sensorDataInputMessage;
    private String notificationCount;
    private DataHolder currentMessage;

    public ICallBack getSensorDataCallBack;
    public ICallBack getNotificationCount;
    private StringBuilder recDataString;


    public MessageComm() {
        boss = new BluetoothBoss(mHandler);
        sensorDataInputMessage = getSenosrDataInputMessage();
        notificationCount = getNotificationCountInputMessage();
        mostRecentSensorDataTime = getCurrentTime();
        mostRecentNotificationTime = getCurrentTime();
        runnable.run();
        recDataString = new StringBuilder();
    }

    public void connect(String address) {
        boss.connect(address);
    }

    private String getSenosrDataInputMessage() {
        Map<String, String> datalist = new HashMap<String, String>();
        datalist.put("messageID", "1");
        Log.e("MessageID", "1");//get notification
        String message = mDataSerialization.convertToJSON(datalist);

        return message;
    }

    private String getNotificationCountInputMessage() {
        Map<String, String> datalist = new HashMap<String, String>();
        datalist.put("messageID", "4");
        Log.e("MessageID", "4,DataServices");//notification count
        String message = mDataSerialization.convertToJSON(datalist);

        return message;
    }

    public void sendRequest(ICallBack callBack, MessageType messageID, String jsonD) {

        try {
            DataHolder holder = new DataHolder();
            holder.CallBack = callBack;
            holder.MessageID = messageID;
            holder.JsonD = jsonD;

            new getSensorDataTask().execute(holder);/// this will go into a queue

        } catch (MalformedParameterizedTypeException e) {
            e.printStackTrace();
        }
    }

    private class getSensorDataTask extends AsyncTask<DataHolder, String, DataHolder> {
        // this Integer below is the 3rd Integer in generic
        // the Integer in argument is the 1st Integer in generic
        @Override
        protected DataHolder doInBackground(DataHolder... params) {

            DataHolder data = params[0];
            boss.send(data.JsonD);

            System.out.println("message In DataServices ?" + data.JsonD);
            return data;
        }

        // why is this here
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(DataHolder data) {
            super.onPostExecute(data);

            currentMessage = data;
        }

        private void returnResponse(JSONObject jsonResponse) {
            currentMessage.CallBack.callBack(currentMessage.MessageID, jsonResponse);
        }
    }

    public void end(){

    }

    private Long getCurrentTime() {
        return System.currentTimeMillis()/1000;
    }
    //date time
    //get notification check time, GetMostRecentNotification
    private boolean checkSensorData(Long timeInterval) {
        Long cTime = getCurrentTime();
        Long sum = mostRecentSensorDataTime + timeInterval;
        boolean shouldCheck = false;

        if(sum < cTime) {
            shouldCheck = true;
            System.out.println("sum  " + sum + " cTime" + cTime + " checkSensorData" + shouldCheck);
        }
        return shouldCheck;
    }

    private boolean checkNotification(Long timeInterval) {
        Long cTime = getCurrentTime();
        Long sum = mostRecentNotificationTime + timeInterval;
        boolean shouldCheck = false;

        if(sum < cTime) {
            shouldCheck = true;
            Log.e("checkNotification","true");
            System.out.println("sum " + sum + " cTime" + cTime + " checkNotification" + shouldCheck);
        } else {
            System.out.println("sum " + sum + " cTime" + cTime + " checkNotification" + shouldCheck);
        }

        return shouldCheck;
    }

    private Runnable runnable = new Runnable(){

        public void run() {
            Log.e("runnnnnn","worked");
            if(checkSensorData(60L)){
                java.util.Date date= new java.util.Date();

                mostRecentSensorDataTime = getCurrentTime();
                System.out.println(" In side of the while loop mostRecentSensorDataTime " + mostRecentSensorDataTime);
                if(getSensorDataCallBack != null) {
                    //sendRequest(getSensorDataCallBack,MessageType.GetSensorData,sensorDataInputMessage);
                    //Log.e("DataServices","working");
                } else{
                    //Log.e("DataServices","NOT working");
                }

            }
            if(checkNotification(30L)){
                java.util.Date date= new java.util.Date();
                System.out.println(new Timestamp(date.getTime()));

                mostRecentNotificationTime = getCurrentTime();
                if(getNotificationCount != null) {
                    sendRequest(getNotificationCount,MessageType.GetNotificationCount,notificationCount);
                    Log.e("getNotificationCount","working");
                } else {
                    Log.e("getNotificationCount","not working");
                }
            }
            //handler.postDelayed(this,1000);
        }
    };

    public void DataReceived(String message){
        try {
            JSONObject json = new JSONObject(message);
            //returnResponse(json);

        } catch (JSONException e) {e.printStackTrace();}
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                     //       setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                         //   mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                           // setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                          //  setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                   // mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                   // byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    //String readMessage = new String(readBuf, 0, msg.arg1); // this handles the "~"
                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                   // if (msg.what == handlerState) {        //if message is what we want
                    String readMessage = (String) msg.obj;      // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);         //keep appending to string until ~
                        int endOfLineIndex = recDataString.indexOf("~");             // determine the end-of-line

                        if (endOfLineIndex > 0) {
                            // make sure there is data before ~
                            String data = recDataString.substring(0, endOfLineIndex);    // extract string
                            //receivedDataCallback.DataReceived(data);
                            DataReceived(data);
                            recDataString.delete(0, endOfLineIndex);                    //clear all string data
                        }

                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                   // mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
               //     if (null != activity) {
               //         Toast.makeText(activity, "Connected to "
                //                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
               //     }
                    break;
                case Constants.MESSAGE_TOAST:
                    //if (null != activity) {
                     //   Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                      //          Toast.LENGTH_SHORT).show();
                    //}
                    break;
            }
        }
    };

}


