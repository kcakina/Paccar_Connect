package mobile.paccar.com.paccar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter<SensorListDataModel> implements View.OnClickListener{

    private ArrayList<SensorListDataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtNumber;
        TextView txtSensorName;
        TextView txtCurrentData;
    }

    public CustomAdapter(ArrayList<SensorListDataModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {


        int position=(Integer) v.getTag();
        Object object= getItem(position);
        SensorListDataModel dataModel=(SensorListDataModel) object;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SensorListDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtNumber = (TextView) convertView.findViewById(R.id.number);
            viewHolder.txtSensorName = (TextView) convertView.findViewById(R.id.sensor_name);
            //viewHolder.txtCurrentData = (TextView) convertView.findViewById(R.id.current_data);

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


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
//        lastPosition = position;


        viewHolder.txtNumber.setText(String.valueOf(dataModel.getNumber()));
        viewHolder.txtSensorName.setText(dataModel.getName());
        viewHolder.txtCurrentData.setText(dataModel.getCurrentData());

        // Return the completed view to render on screen
        return convertView;
    }


}