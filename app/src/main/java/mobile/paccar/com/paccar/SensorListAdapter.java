package mobile.paccar.com.paccar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class SensorListAdapter extends ArrayAdapter<SensorListDataModel> {

    private ArrayList<SensorListDataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtNumber;
        TextView txtSensorName;
        TextView txtCurrentData;
    }


    public SensorListAdapter(ArrayList<SensorListDataModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

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
        SensorListDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        // Update the data!

        viewHolder.txtNumber = (TextView) convertView.findViewById(R.id.number);
        viewHolder.txtSensorName = (TextView) convertView.findViewById(R.id.sensor_name);

        TextView CurrentDataControl = (TextView) convertView.findViewById(R.id.current_data);
        viewHolder.txtCurrentData = CurrentDataControl;


        switch (dataModel.getSeverityStatus()) {
            case 0:
                viewHolder.txtCurrentData.setBackgroundResource(R.drawable.no_severity_border);
                break;
            case 1:
                viewHolder.txtCurrentData.setBackgroundResource(R.drawable.low_severity_border);
                break;
            case 2:
                viewHolder.txtCurrentData.setBackgroundResource(R.drawable.medium_severity_border);
                break;
            case 3:
                viewHolder.txtCurrentData.setBackgroundResource(R.drawable.high_severity_border);
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

        viewHolder.txtNumber.setText(String.valueOf(dataModel.getID()));
        viewHolder.txtSensorName.setText(dataModel.getName());
        viewHolder.txtCurrentData.setText(dataModel.getCurrentData());

        // Return the completed view to render on screen
        return result;
    }


}
