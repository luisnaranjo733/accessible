package jnaranj0.uw.edu.accessible;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SSIDAdapter extends ArrayAdapter<SSID> {
    public static final String TAG = "**SSID.Adapter";
    public SSIDAdapter(Context context, List<SSID> ssids) {
        super(context, 0, ssids);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SSID ssid = getItem(position);


        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ssid_item, parent, false);
        }

        TextView ssidName = (TextView) convertView.findViewById(R.id.textViewSSIDName);
        ssidName.setText("tester");

        // Return the completed view to render on screen
        return convertView;
    }
}