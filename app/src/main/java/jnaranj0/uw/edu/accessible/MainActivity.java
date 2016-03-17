package jnaranj0.uw.edu.accessible;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "**accessible";

    FrameLayout topPane;
    FrameLayout bottomPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        topPane = (FrameLayout) findViewById(R.id.topPane);
        bottomPane = (FrameLayout) findViewById(R.id.bottomPane);;

        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.topPane, new ActiveConnectionFragment(), null);
        ft.add(R.id.bottomPane, new HierarchyFragment(), null);
        ft.commit();

    }
}


/*
        final HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("44:d9:e7:02:33:92", "AP_luis_room_?");
        hashMap.put("44:d9:e7:03:33:92", "AP_luis_room_n");
        hashMap.put("46:d9:e7:04:33:92", "AP_luis_room_ac");
        hashMap.put("44:d9:e7:f6:02:e2", "AP_nakagawa_room_?");
        hashMap.put("44:d9:e7:f9:6d:ce", "AP_joes_room_?");
        hashMap.put("44:d9:e7:f9:7e:39", "AP_dions_room_?");
        hashMap.put("44:d9:e7:f9:79:3c", "AP_andys_room_?");
        hashMap.put("44:d9:e7:f9:7a:0b", "AP_red_room_?");

                wifiManager.startScan();

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        // may be null
        String bssid = wifiInfo.getBSSID();
        String nickname = hashMap.get(bssid);
        Log.v(TAG, "" + bssid + " " + nickname);
        List<ScanResult> results = wifiManager.getScanResults();
        for (int i=0; i < results.size(); i++) {
            ScanResult result = results.get(i);
            Log.v(TAG, "" + i + ": " + result.BSSID + " " + hashMap.get(result.BSSID));
        }
 */