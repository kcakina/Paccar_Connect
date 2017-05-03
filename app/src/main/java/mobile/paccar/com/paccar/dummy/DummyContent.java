package mobile.paccar.com.paccar.dummy;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.util.Log;


import org.json.JSONObject;

import mobile.paccar.com.paccar.DC_Profile;
import mobile.paccar.com.paccar.DataSerialization;
import mobile.paccar.com.paccar.ICallBack;
import mobile.paccar.com.paccar.MessageType;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public List<DummyItem> ITEMS = new ArrayList<DummyItem>();

   // public String[] myStringArray = new String[]{"sensor1","sensor2","sensor3"};
    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;
    private static String[] myStringArray = new String[]{"temperature ","gas","Door Status","weight", "tire pressure"};
    public static String[] myStringArray2 = new String[]{"gas","Door Status","weight", "tire pressure"};
    public static String[] myStringArray3 = new String[]{"gas","weight", "tire pressure"};
    public static String[] mySensorData = new String[]{"Temperature is really hight!", "You run out of gas", "Door is Open!", "Over weight!", "Please check your tire pressure"};

    DataSerialization dataSerialization;
    Context contextData;

    public DummyContent() {
        dataSerialization = new DataSerialization();

        ICallBack callBack = new ICallBack() {
            @Override
            public void callBack(MessageType id, JSONObject jsonD) {
                List<DC_Profile> list = dataSerialization.getProfileList(jsonD);
                if(list != null) {
                    Log.d("YourTag", "YourOutput");
                    for (int i = 0; i < list.size(); i++) {
                        DC_Profile profile = list.get(i);
                        addItem(createDummyItem(i, profile.name));
                    }
                } else {
                    Log.d("YourTag", "YourOutput");
                }
            }
        };
       // dataService.sendRequest(myContext, callBack, MessageType.GetProfileList, "");

    }

    private void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position, String sensorname) {

        return new DummyItem(String.valueOf(position), sensorname, makeDetails(position));

    }

    private static String makeDetails(int position) {


        StringBuilder builder = new StringBuilder();

        builder.append("\nMore details information about ." + mySensorData[position]);
       for (int i = 0; i < position; i++) {

           Log.d("makeDetails", "YourOutput");
            builder.append("\nMore details information about ." + position);
        }
        return builder.toString();

    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = contextData.getAssets().open("profileData.json");
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
