package mobile.paccar.com.paccar;

import org.json.JSONObject;

/**
 * Created by oshiancoates on 3/21/17.
 */

public interface IDataReceivedCallBack {
    void DataReceived(MessageType id, JSONObject jsonD);
}
