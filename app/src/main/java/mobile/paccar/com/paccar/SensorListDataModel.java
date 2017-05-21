package mobile.paccar.com.paccar;

public class SensorListDataModel {

    String sensorID;
    String sensorName;
    int sensorNumber;
    String currentData;
    int sensorSeverity;


    public SensorListDataModel(String name, String id, String current_data, int sensor_number, int severity) {
        this.sensorName=name;
        this.sensorID=id;
        this.currentData=current_data;
        this.sensorNumber=sensor_number;
        this.sensorSeverity=severity;
    }


    public String getName() {
        return sensorName;
    }


    public String getID() {
        return sensorID;
    }


    public int getNumber() {
        return sensorNumber;
    }


    public String getCurrentData() {
        return currentData;
    }


    public int getSeverityStatus() {
        return sensorSeverity;
    }

}
