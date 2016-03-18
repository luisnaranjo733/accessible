package jnaranj0.uw.edu.accessible;


import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveConnectionFragment extends Fragment {

    public static final String TAG = "**activeConnFrag";
    public static final int UI_UPDATE_PERIOD = 100;
    private OnSSIDSavedListener callback;

    private TextView currentNetworkState;
    private TextView currentSSID;
    private TextView currentBSSID;
    private TextView currentRssi;
    private Button rememberWAPButton;

    private WifiManager wifiManager;
    private boolean activityStopped;
    private Handler handler;
    private Runnable runnable;

    public interface OnSSIDSavedListener {
        public void onSSIDSaved(SSID ssid);
    }


    public ActiveConnectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try {
            callback = (OnSSIDSavedListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnSSIDSavedListener");
        }
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
        if (wifiManager == null) {
            wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        }

        rememberWAPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    String ssidString = wifiInfo.getSSID();
                    ssidString = ssidString.replaceAll("\"", "");
                    String bssidString = wifiInfo.getBSSID();

                    List<SSID> ssidResults = SSID.find(SSID.class, "ssid = ?", ssidString);
                    boolean ssidCreated = false;
                    if (ssidResults.size() == 0) {
                        SSID ssid = new SSID(ssidString);
                        ssid.save();
                        ssidResults.add(ssid);
                        ssidCreated = true;
                    }
                    SSID ssid = ssidResults.get(0);

                    // and with our ssid?
                    List<BSSID> bssidResults = BSSID.find(BSSID.class, "bssid = ?", bssidString);

                    if (bssidResults.size() == 0) {
                        BSSID bssid = new BSSID("nickname", bssidString, ssid);
                        bssid.save();
                        Toast.makeText(getActivity(), "Saved bssid: " + bssidString, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Nothing to do!", Toast.LENGTH_SHORT).show();
                    }

                    if (ssidCreated) {
                        ((OnSSIDSavedListener) getActivity()).onSSIDSaved(ssid);
                    }

                    // if this bssid is not already stored
                    // store it with this ssid
                    // else, tell the user there is nothing to do



                } else {
                    Toast.makeText(getActivity(), "Wifi is not enabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                updateUI();
                if (!activityStopped) {
                    handler.postDelayed(this, UI_UPDATE_PERIOD);
                }

            }
        };

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        activityStopped = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        activityStopped = false;
        handler = new Handler();
        handler.postDelayed(runnable, 500);

    }

    public void updateUI() {
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
