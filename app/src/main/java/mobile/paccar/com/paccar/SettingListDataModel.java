package mobile.paccar.com.paccar;

public class SettingListDataModel {

    String sensorType;
    int sensorID;
    String sensorName;
    String profileID;
    int upperThreshold;
    int lowerThreshold;



    public SettingListDataModel(String sType, String name, int id, String profileID,
                               int upThreshold, int lowThreshold) {
        this.sensorType=sType;
        this.sensorName=name;
        this.sensorID=id;
        this.profileID=profileID;
        this.upperThreshold=upThreshold;
        this.lowerThreshold=lowThreshold;
    }


    public String getName() { return sensorName; }


    public int getID() {
        return sensorID;
    }


    public String getSensorType() {
        return sensorType;
    }


    public String getProfileID() {
        return profileID;
    }


    public int getUpperThreshold() {
        return upperThreshold;
    }


    public int getLowerThreshold() {
        return lowerThreshold;
    }

}
