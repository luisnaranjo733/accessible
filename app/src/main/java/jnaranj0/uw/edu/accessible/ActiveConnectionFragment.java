package jnaranj0.uw.edu.accessible;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveConnectionFragment extends Fragment {

    public static final String TAG = "**activeConnFrag";
    public static final int UI_UPDATE_PERIOD = 500;
    private OnSSIDSavedListener callback;

    private TextView currentNetworkState;
    private TextView currentSSID;
    private TextView currentBSSID;
    private TextView currentRssi;
    private TextView currentBand;
    private Button rememberWAPButton;

    private WifiManager wifiManager;
    private boolean activityStopped;
    private Handler handler;
    private Runnable runnable;

    public interface OnSSIDSavedListener {
        void onRememberSSID(SSID ssid);
        void onRememberBSSID(BSSID bssid);
        void onSwitchToDetail(SSID ssid);
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
            currentBand = (TextView) rootView.findViewById(R.id.textViewCurrentBand);
            rememberWAPButton = (Button) rootView.findViewById(R.id.rememberWAPButton);

        }
        if (wifiManager == null) {
            wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        }

        rememberWAPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.alert_remember_wap_title);

// Set up the input
                final EditText input = new EditText(getActivity());


// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setMessage(R.string.alert_remember_wap_message);

// Set up the buttons
                builder.setPositiveButton(R.string.alert_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nickname = input.getText().toString();
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
                                BSSID bssid = new BSSID(nickname, bssidString,
                                        wifiInfo.getFrequency(), ssid);
                                bssid.save();
                                ((OnSSIDSavedListener) getActivity()).onRememberBSSID(bssid);
                                Toast.makeText(getActivity(), R.string.alert_remember_wap_toast_bssid_not_exists + bssidString, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), R.string.alert_remember_wap_toast_bssid_exists, Toast.LENGTH_SHORT).show();
                            }

                            if (ssidCreated) {
                                ((OnSSIDSavedListener) getActivity()).onRememberSSID(ssid);
                            }

                            ((OnSSIDSavedListener) getActivity()).onSwitchToDetail(ssid);

                        } else {
                            Toast.makeText(getActivity(), R.string.alert_remember_wap_toast_wifi_disabled, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton(R.string.alert_negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
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
            String bssidRepr = wifiInfo.getBSSID();
            if (bssidRepr != null && !bssidRepr.equals("")) {
                List<BSSID> results = BSSID.find(BSSID.class, "bssid = ?" , bssidRepr);
                if (results.size() > 0) {
                    BSSID bssid = results.get(0);
                    currentBSSID.setText(bssid.nickname);
                    rememberWAPButton.setPaintFlags(rememberWAPButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    rememberWAPButton.setEnabled(false);
                } else {
                    currentBSSID.setText(bssidRepr);
                    rememberWAPButton.setPaintFlags(rememberWAPButton.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    rememberWAPButton.setEnabled(true);
                }
                String signalStrength = "" + wifiInfo.getRssi();
                currentRssi.setText("" + signalStrength);
                currentBand.setText("" + wifiInfo.getFrequency());
            } else {
                Log.v(TAG, "BSSID IS NULL :(");
                Toast.makeText(getActivity(), R.string.update_ui_bssid_null_toast, Toast.LENGTH_SHORT).show();
            }

        } else {
            Log.v(TAG, "wifi disabled");
            currentSSID.setText("");
            currentBSSID.setText("");
            currentRssi.setText("");
            rememberWAPButton.setEnabled(false);
        }
    }
}
