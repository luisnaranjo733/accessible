package jnaranj0.uw.edu.accessible;


import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveConnectionFragment extends Fragment {

    public static final String TAG = "**activeConnFrag";

    private TextView currentNetworkState;
    private TextView currentSSID;
    private TextView currentBSSID;
    private TextView currentRssi;
    private Button rememberWAPButton;

    public ActiveConnectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_active_connection, container, false);
        if (currentNetworkState == null) {
            currentNetworkState = (TextView) rootView.findViewById(R.id.textViewCurrentNetworkState);
            currentSSID = (TextView) rootView.findViewById(R.id.textViewCurrentSSIDName);
            currentBSSID = (TextView) rootView.findViewById(R.id.textViewCurrentBSSID);
            currentRssi = (TextView) rootView.findViewById(R.id.textViewCurrentRssi);
            rememberWAPButton = (Button) rootView.findViewById(R.id.rememberWAPButton);

        }

        WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        int wifiState = wifiManager.getWifiState();

        String wifiStateRepr = "";
        if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
            wifiStateRepr = "disabled";
        } else if (wifiState == WifiManager.WIFI_STATE_DISABLING) {
            wifiStateRepr = "disabling";
        } else if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
            wifiStateRepr = "enabled";
        } else if (wifiState == WifiManager.WIFI_STATE_ENABLING) {
            wifiStateRepr = "enabling";
        } else if (wifiState == WifiManager.WIFI_STATE_UNKNOWN) {
            wifiStateRepr = "unknown";
        }
        currentNetworkState.setText(wifiStateRepr);

        if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            currentSSID.setText(wifiInfo.getSSID());
            currentBSSID.setText(wifiInfo.getBSSID());
            String signalStrength = "" + wifiInfo.getRssi();
            currentRssi.setText("" + signalStrength);

        } else {
            Log.v(TAG, "wifi disabled");
            currentSSID.setText("");
            currentBSSID.setText("");
            currentRssi.setText("");
        }
        return rootView;
    }

}

/*
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("44:d9:e7:02:33:92", "AP_luis_room_?");
        hashMap.put("44:d9:e7:03:33:92", "AP_luis_room_n");
        hashMap.put("46:d9:e7:04:33:92", "AP_luis_room_ac");
        hashMap.put("44:d9:e7:f6:02:e2", "AP_nakagawa_room_?");
        hashMap.put("44:d9:e7:f9:6d:ce", "AP_joes_room_?");
        hashMap.put("44:d9:e7:f9:7e:39", "AP_dions_room_?");
        hashMap.put("44:d9:e7:f9:79:3c", "AP_andys_room_?");
        hashMap.put("44:d9:e7:f9:7a:0b", "AP_red_room_?");
 */
