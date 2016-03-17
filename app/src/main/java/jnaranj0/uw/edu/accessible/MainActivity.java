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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements HierarchyFragment.OnSSIDClickedListener {
    public static final String TAG = "**accessible";

    FrameLayout topPane;
    FrameLayout bottomPane;
    HierarchyFragment hierarchyFragment;
    ActiveConnectionFragment activeConnectionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        topPane = (FrameLayout) findViewById(R.id.topPane);
        bottomPane = (FrameLayout) findViewById(R.id.bottomPane);

        hierarchyFragment = new HierarchyFragment();
        activeConnectionFragment = new ActiveConnectionFragment();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.topPane, activeConnectionFragment, null);
        ft.add(R.id.bottomPane, hierarchyFragment , null);
        ft.commit();
    }

    @Override
    public void onSSIDClicked(SSID ssid) {
        Log.v(TAG, "Clicked: " + ssid.toString());
    }

    @Override
    public void onSSIDLongPressed(SSID ssid) {
        Log.v(TAG, "Long pressed: " + ssid.toString());
        hierarchyFragment.ssidAdapter.remove(ssid);
        ssid.delete();
    }
}