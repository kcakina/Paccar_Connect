package mobile.paccar.com.paccar;

import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutgoingJsonCreation {

    public static String getSensorData(){
        Map<String, String> datalist = new HashMap<String, String>();
        datalist.put("messageID", "1"); // Get sensor data
        Log.e("MessageID", "1");
        String message = convertToJSON(datalist);

        return message;
    }

    public static String getProfileList() {
        Map<String, String> datalist = new HashMap<String, String>();
        datalist.put("messageID", "2"); // Get sensor list
        Log.e("messageID", "2");
        String message = convertToJSON(datalist);

        return message;
    }

    public static String getSensorList() {
        Map<String, String> datalist = new HashMap<String, String>();
        datalist.put("messageID", "3"); // Get sensor list
        Log.e("messageID", "3");
        String message = convertToJSON(datalist);

        return message;
    }

    public static String getNotificationCount() {
        Map<String, String> datalist = new HashMap<String, String>();
        datalist.put("messageID", "4");
        Log.e("MessageID", "4,DataServices");//notification count
        String message = convertToJSON(datalist);

        return message;
    }

    public static String getNotifications() {
        Map<String, String> datalist = new HashMap<String, String>();
        datalist.put("messageID", "5");
        Log.e("MessageID", "5,DataServices");//notifications
        String message = convertToJSON(datalist);

        return message;
    }


    public static String convertToJSON(Map<String, String> finalList) {

        List<Map<String, String>> finalData = new ArrayList<>();
        finalData.add(0, finalList);
        JSONArray mJSONArray = new JSONArray(finalData);

        return mJSONArray + "~";
    }

}
