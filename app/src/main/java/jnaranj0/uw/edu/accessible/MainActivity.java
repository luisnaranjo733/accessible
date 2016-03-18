package jnaranj0.uw.edu.accessible;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements HierarchyFragment.OnSSIDClickedListener, ActiveConnectionFragment.OnSSIDSavedListener {
    public static final String TAG = "**accessible";

    FrameLayout topPane;
    FrameLayout bottomPane;
    HierarchyFragment hierarchyFragment;
    ActiveConnectionFragment activeConnectionFragment;
    DetailSSIDFragment detailSSIDFragment;

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
        ft.add(R.id.bottomPane, hierarchyFragment, null);
        ft.commit();
    }

    /*
    Called when a user clicks on an SSID's listview element.
    Triggers a fragment transaction which leads to the detail fragment
     */
    @Override
    public void onSSIDClicked(SSID ssid) {
        Log.v(TAG, "Clicked: " + ssid.toString());

        detailSSIDFragment = new DetailSSIDFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(DetailSSIDFragment.BUNDLE_ARG_SSID_PK, ssid.getId());
                detailSSIDFragment.setArguments(bundle);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.bottomPane, detailSSIDFragment, null);
        ft.addToBackStack(null);
        ft.commit();
    }

    /*
    Called when the user long presses on an SSID's listview item.
    Results in the ssid and all related bssids from being deleted
    from the database and from the array adapters
     */
    @Override
    public void onSSIDLongPressed(SSID ssid) {
        Log.v(TAG, "Long pressed: " + ssid.toString());
        hierarchyFragment.ssidAdapter.remove(ssid);
        for (BSSID bssid : ssid.getBSSIDs()) {
            bssid.delete();
            detailSSIDFragment.bssidAdapter.remove(bssid);
        }
        ssid.delete();

    }

    /*
    Called when a new BSSID and SSID are stored
    Results in the appropriate array adapters being updated
     */
    @Override
    public void onRememberSSID(SSID ssid) {
        hierarchyFragment.ssidAdapter.add(ssid);
    }

    @Override
    public void onRememberBSSID(BSSID bssid) {
        if (detailSSIDFragment.bssidAdapter != null) {
            detailSSIDFragment.bssidAdapter.add(bssid);
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}