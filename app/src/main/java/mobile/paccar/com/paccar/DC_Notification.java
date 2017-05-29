package mobile.paccar.com.paccar;

/**
 * Created by shiyizhang on 5/25/16.
 */
public class DC_Notification {
    //Jason data example:
    //[{"severity":"HIGH","value":80,"sensorType":"TEMP","sensorID":1,"times":1.4608267767230086E9},
    //{"severity":"LOW","value":30,"sensorType":"HUMID","sensorID":2,"times":1.4608267768031628E9}]
    //,"messageID":5}

    String severity;
    String data;
    String sensorName;
    String time;
}
