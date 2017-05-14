package mobile.paccar.com.paccar;

/**
 * Created by shiyizhang on 4/29/16.
 */

public enum MessageType {

    NotSet(0),
    GetSensorData(1),
    GetProfileList(2),
    GetSensorList(3),
    GetNotificationCount(4),
    GetNotifications(5),
    IsConnected(6),
    CreateProfile(7),
    GetHistory(8),
    GetSensorConfig(9),
    SaveSensorConfig(10);


    private int MessageIndex;

    private MessageType(int MessageIndex) { this.MessageIndex = MessageIndex; }

    public static MessageType getMessage(int messageIndex) {
        for (MessageType l : MessageType.values()) {
            if (l.MessageIndex == messageIndex) return l;
        }
        throw new IllegalArgumentException("Message not found. Resend");
    }

}
