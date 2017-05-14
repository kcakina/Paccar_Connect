package mobile.paccar.com.paccar.dummy;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import mobile.paccar.com.paccar.DataServices;

import mobile.paccar.com.paccar.MessageType;

//import org.florescu.android.rangeseekbar.RangeSeekBar;

/**
 * Created by shiyizhang on 4/17/16.
 */
public class ConfigDummy {

  //  RangeSeekBar seekBarInteger, seekBarDouble;
   // TextView minTextInt, maxtextInt, minTextDouble, maxTextDouble;
    /**
     * An array of sample (dummy) items.
     */
    public static final List<SensorItem> ITEMS = new ArrayList<SensorItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, SensorItem> ITEM_MAP = new HashMap<String, SensorItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

   // DataService dataService;
//    DataServices mServices;

    public ConfigDummy(Context myContext) {
       // mServices = new DataServices(myContext);


        Map<String, String> datalist = new HashMap<String, String>();
        datalist.put("messageID", "9");

        List<Map<String, String>> finalData = new ArrayList<>();
        finalData.add(0, datalist);


        System.out.println(finalData);

        String sendData;

        JSONArray mJSONArray = new JSONArray(finalData);

        sendData = mJSONArray + "~";

/*        ICallBack callBack = new ICallBack() {
            @Override
            public void callBack(MessageType id, JSONObject jsonD) {
*//*                       List<DC_Profile> list = dataService.getProfileList(jsonD);
                        if(list != null) {
                            Log.d("YourTag", "YourOutput");
                            for (int i = 0; i < list.size(); i++) {
                                DC_Profile profile = list.get(i);
                                Log.e(" profile.name",  profile.name);
                                addItem(createDummyItem(i));
                            }
                        } else {
                            Log.d("YourTag", "YourOutput");
                        }*//**//*
            }
        };
        Log.e("Momo",sendData);
        mServices.sendRequest(callBack, MessageType.GetProfileList, sendData);*//*
            };
        };*/
    }

    private static void addItem(SensorItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static SensorItem createDummyItem(int position) {
        return new SensorItem(String.valueOf(position), "Item_dummy_config " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("You can do more setting on this page!").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A stupid item representing a piece of chocolate.
     */
    public static class SensorItem {
        public final String id;
        public final String content;
        public final String details;

        public SensorItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }

}
