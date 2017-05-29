package mobile.paccar.com.paccar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class NotificationListAdapter extends ArrayAdapter<NotificationListDataModel> {

    private ArrayList<NotificationListDataModel> dataSet;
    Context mContext;
    int count;

    // View lookup cache
    private static class ViewHolder {
        TextView txtNumber;
        TextView txtSensorName;
        TextView txtData;
        TextView txtTime;
        ImageView severity;
    }


    public NotificationListAdapter(ArrayList<NotificationListDataModel> data, Context context) {
        super(context, R.layout.notification_row, data);
        this.dataSet = data;
        this.mContext=context;
        count = 0;

    }

//    public void onClick(View v) {
//
////        int position=(Integer) v.getTag();
////        Object object= getItem(position);
////        SensorListDataModel dataModel=(SensorListDataModel) object;
//
//        // go to sensor page?
//        Intent i=new Intent(v.getContext(), sensorDetailActivity.class);
//        v.getContext().startActivity(i);
//
//
//    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        NotificationListDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.notification_row, parent, false);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        // Update the data!

        viewHolder.txtNumber = (TextView) convertView.findViewById(R.id.number);
        viewHolder.txtSensorName = (TextView) convertView.findViewById(R.id.sensor_name);
        viewHolder.txtData = (TextView) convertView.findViewById(R.id.current_data);
        viewHolder.txtTime = (TextView) convertView.findViewById(R.id.notification_time);


        switch (dataModel.getSeverity()) {
            case "low":
                viewHolder.severity.setImageResource(R.drawable.ic_action_lnotification);
                break;
            case "medium":
                viewHolder.severity.setImageResource(R.drawable.ic_action_mnotification);
                break;
            case "high":
                viewHolder.severity.setImageResource(R.drawable.ic_action_hnotification);
                break;
        }

        // Set on click for individual sensor pages - not working...
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(mContext, sensorDetailActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mContext.startActivity(i);
//            }
//        });

        convertView.setTag(viewHolder);

        result=convertView;

        viewHolder.txtNumber.setText(String.valueOf(count));
        count++;
        viewHolder.txtSensorName.setText(dataModel.getName());
        viewHolder.txtData.setText(dataModel.getData());
        viewHolder.txtTime.setText(dataModel.getTime());

        // Return the completed view to render on screen
        return result;
    }


}

