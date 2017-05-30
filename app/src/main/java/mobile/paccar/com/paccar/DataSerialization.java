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

//Not from a json file

    //HIDDEN

public class DataSerialization {

    Context contextData;
    DataServices mServices;
    boolean mBound = false;

    public DataSerialization() {
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
            JSONArray jsonArray = data.optJSONArray("messageID: 4");


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
            JSONArray jsonArray = data.optJSONArray("messageID: 5");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                Log.d("YourTag", "YourOutput");
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String severity = jsonObject.optString("severity");
                String sensorData = jsonObject.optString("data");
                String sensorName = jsonObject.optString("sensorName");
                String notificationTime = jsonObject.optString("time");

                notification = new DC_Notification();
                notification.severity = severity;
                notification.data = sensorData;
                notification.sensorName = sensorName;
                notification.time = notificationTime;

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
            JSONArray jsonArray = data.optJSONArray("messageID: 2");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                Log.d("YourTag", "YourOutput");
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String id = jsonObject.optString("profileID");
                String name = jsonObject.optString("profileName");
                String type = jsonObject.optString("profileType");
                boolean isActive = jsonObject.optBoolean("active");

                profile = new DC_Profile();
                profile.id = id;
                profile.name = name;
                profile.type = type;
                profile.isActive = isActive;

                list.add(profile);
            }

        } catch (JSONException e) {e.printStackTrace();}

         return list;
        }


    public List<DC_Sensor> getSensorList(JSONObject data) {
        List<DC_Sensor> list = new ArrayList<DC_Sensor>();
        DC_Sensor sensor = null;
        //JSONObject data = null;
        //data = sendRequest(MessageType.GetProfileList, "");

        try {
            Log.d("YourTag", "YourOutput");
            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = data.optJSONArray("messageID: 3");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                Log.d("YourTag", "YourOutput");
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String type = jsonObject.optString("sensorType");
                String id = jsonObject.optString("sensorID");
                String name = jsonObject.optString("sensorName");
                String currentData = jsonObject.optString("data");
                int number = jsonObject.optInt("sensorNumber");
                int severity = jsonObject.optInt("severity");

                sensor = new DC_Sensor();
                sensor.sensorType = type;
                sensor.sensorId = id;
                sensor.sensorName = name;
                sensor.sensorData = currentData;
                sensor.sensorNum = number;
                sensor.sensorSeverity = severity;

                list.add(sensor);
            }

        } catch (JSONException e) {e.printStackTrace();}

        return list;
    }


    public List<DC_SensorData> getSensorData(JSONObject data) {
        List<DC_SensorData> list = new ArrayList<DC_SensorData>();
        DC_SensorData sensorData = null;
        //JSONObject data = null;

        try {
            Log.d("YourTag", "YourOutput");
            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = data.optJSONArray("messageID: 1");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                Log.d("YourTag", "YourOutput");
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String id = jsonObject.optString("sensorID");
                String currentData = jsonObject.optString("data");
                int severity = jsonObject.optInt("severity");


                sensorData = new DC_SensorData();
                sensorData.sensorId = id;
                sensorData.sensorData = currentData;
                sensorData.sensorSeverity = severity;


                list.add(sensorData);
            }

        } catch (JSONException e) {e.printStackTrace();}

        return list;
    }


    public List<DC_SensorCofig> getSensorConfigData(JSONObject data) {
        List<DC_SensorCofig> list = new ArrayList<DC_SensorCofig>();
        DC_SensorCofig sensorConfig = null;
        //JSONObject data = null;

        try {
            Log.d("YourTag", "YourOutput");
            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = data.optJSONArray("messageID: 9");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                Log.d("YourTag", "YourOutput");
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String id = jsonObject.optString("sensorID");
                String name = jsonObject.optString("sensorName");
                int lThresh = jsonObject.optInt("lowThresh");
                int uThresh = jsonObject.optInt("upThresh");


                sensorConfig = new DC_SensorCofig();
                sensorConfig.sensorId = Integer.getInteger(id);
                sensorConfig.sensorName = name;
                sensorConfig.lowThresh = lThresh;
                sensorConfig.upThresh = uThresh;


                list.add(sensorConfig);
            }

        } catch (JSONException e) {e.printStackTrace();}

        return list;
    }


    public List<DC_isSaved> saveSensorConfigData(JSONObject data) {
        List<DC_isSaved> list = new ArrayList<DC_isSaved>();
        DC_isSaved sensorConfigSave = null;
        //JSONObject data = null;

        try {
            Log.d("YourTag", "YourOutput");
            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = data.optJSONArray("messageID: 10");

            //Iterate the jsonArray and print the info of JSONObjects
            Log.d("YourTag", "YourOutput");
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            Boolean isSaved = jsonObject.optBoolean("isSaved");

            sensorConfigSave = new DC_isSaved();
            sensorConfigSave.isSaved = isSaved;

            list.add(sensorConfigSave);

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


