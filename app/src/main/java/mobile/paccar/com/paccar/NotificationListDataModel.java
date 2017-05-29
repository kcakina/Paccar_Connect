package mobile.paccar.com.paccar;

public class NotificationListDataModel {

    String severity;
    String data;
    String sensorName;
    String time;



    public NotificationListDataModel(String severity, String name, String current_data, String time){
        this.severity=severity;
        this.sensorName=name;
        this.data=current_data;
        this.time=time;

    }


    public String getName() { return sensorName; }


    public String getData() {
        return data;
    }


    public String getSeverity() {
        return severity;
    }


    public String getTime() {
        return time;
    }

}
