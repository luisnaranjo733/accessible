package jnaranj0.uw.edu.accessible;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class BSSIDAdapter extends ArrayAdapter<BSSID> {
    public static final String TAG = "**BBSID adapter";
    public BSSIDAdapter(Context context, List<BSSID> bssids) {
        super(context, 0, bssids);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BSSID bssid = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bssid_item, parent, false);
        }

        TextView textViewBSSID = (TextView) convertView.findViewById(R.id.textViewBSSIDName);
        TextView textViewBSSIDNickname = (TextView) convertView.findViewById(R.id.textViewBSSIDNickname);
        TextView textViewBSSIDBand = (TextView) convertView.findViewById(R.id.bssidItemBand);
        TextView textViewBSSIDChannel = (TextView) convertView.findViewById(R.id.textViewBSSIDChannel);

        textViewBSSID.setText(bssid.bssid);
        textViewBSSIDNickname.setText(bssid.nickname);
        textViewBSSIDBand.setText("5 ghz");
        textViewBSSIDChannel.setText("36");

        // Return the completed view to render on screen
        return convertView;
    }
}