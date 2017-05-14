package mobile.paccar.com.paccar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.IBinder;

/**
 * Created by shiyizhang on 4/29/16.
 */

//Not from a jason file

    //HIDDEN

public class DataSerialization {

    Context contextData;
    DataServices mServices;
    boolean mBound = false;

    public DataSerialization() {
    }

    public String convertToJSON(Map<String, String> finalList) {

        List<Map<String, String>> finalData = new ArrayList<>();
        finalData.add(0, finalList);
        JSONArray mJSONArray = new JSONArray(finalData);

        return mJSONArray + "~";
    }

    public void sendRequestTest(IDataReceivedCallBack callBack, MessageType messageId, String jsonD){
        //String jsonD: data in json format.

        JSONObject localdata = null;
        int test = 0;
        String fileName = "";

        switch (messageId) {
            case GetProfileList:
                fileName = "profileData.json";
                test = 1;
                break;

            case GetSensorData:
                fileName = "ConfigurationData.json";
                test = 1;
                break;
        }

        localdata = getJson(fileName);

        callBack.DataReceived(messageId,localdata);
    }

    public DC_NotificationCount getNotificationCount(JSONObject data) {
        //List<DC_NotificationCount> listNotificationCount = new ArrayList<DC_NotificationCount>();
        DC_NotificationCount notificationCount = null;
        // DC_Profile profile = null;
        //JSONObject data = null;
        //data = sendRequest(MessageType.GetProfileList, "");

        try {
            Log.d("Notification ", "Count");
            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = data.optJSONArray("data");


            System.out.print(" jsonObject  " + jsonArray.toString());

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                Log.d("Notification momo ", jsonArray.toString());

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                System.out.print(" jsonObject  " + jsonObject.toString());

                int high = Integer.parseInt(jsonObject.optString("high").toString());
                int medium = Integer.parseInt(jsonObject.optString("medium").toString());
                int low = Integer.parseInt(jsonObject.optString("low").toString());
                //String medium = jsonObject.optString("medium").toString();
                //String low = jsonObject.optString("low").toString();

                notificationCount = new DC_NotificationCount();
                notificationCount.high = high;
                notificationCount.medium = medium;
                notificationCount.low = low;
            }

        } catch (JSONException e) {e.printStackTrace();}

        return notificationCount;
    }

    public List<DC_Notification> getNotification(JSONObject data) {
        List<DC_Notification> listNotification = new ArrayList<DC_Notification>();
        DC_Notification notification = null;
       // DC_Profile profile = null;
        //JSONObject data = null;
        //data = sendRequest(MessageType.GetProfileList, "");

        try {
            Log.d("YourTag", "YourOutput");
            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = data.optJSONArray("data");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                Log.d("YourTag", "YourOutput");
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String severity = jsonObject.optString("severity").toString();
                String value = jsonObject.optString("value").toString();
                String sensorType = jsonObject.optString("sensorType").toString();
                String sensorID = jsonObject.optString("sensorID").toString();

                notification = new DC_Notification();
                notification.severity = severity;
                notification.value = value;
                notification.sensorType = sensorType;
                notification.sensorID = sensorID;

                listNotification.add(notification);
            }

        } catch (JSONException e) {e.printStackTrace();}

        return listNotification;
    }


    public List<DC_Profile> getProfileList(JSONObject data) {
        List<DC_Profile> list = new ArrayList<DC_Profile>();
        DC_Profile profile = null;
        //JSONObject data = null;
        //data = sendRequest(MessageType.GetProfileList, "");

        try {
            Log.d("YourTag", "YourOutput");
            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = data.optJSONArray("Profile");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                Log.d("YourTag", "YourOutput");
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String id = jsonObject.optString("id").toString();
                String name = jsonObject.optString("name").toString();
                String type = jsonObject.optString("type").toString();;

                profile = new DC_Profile();
                profile.id = id;
                profile.name = name;
                profile.type = type;

                list.add(profile);
            }

        } catch (JSONException e) {e.printStackTrace();}

         return list;
        }



    private JSONObject getJson(String fileName) {
        JSONObject data = null;

        try {
            data= new JSONObject(loadJSONFromAsset(fileName));

        } catch (JSONException e) {e.printStackTrace();}

        return data;
    }

    private String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            Log.d("YourTag", "YourOutput");
            InputStream is = contextData.getAssets().open(fileName);
            Log.d("YourTag", "YourOutput");
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
}


