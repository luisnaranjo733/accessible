package jnaranj0.uw.edu.accessible;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements HierarchyFragment.OnSSIDClickedListener, ActiveConnectionFragment.OnSSIDSavedListener {
    public static final String TAG = "**ACC_MAIN";

    public HierarchyFragment hierarchyFragment;
    public ActiveConnectionFragment activeConnectionFragment;
    public DetailSSIDFragment detailSSIDFragment;

    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (savedInstanceState != null) {
            // The Activity IS being re-created so we don't need to instantiate the Fragment or add it,
            // but if we need a reference to it, we can use the tag we passed to .replace

            activeConnectionFragment =  (ActiveConnectionFragment) getSupportFragmentManager()
                    .findFragmentByTag(ActiveConnectionFragment.TAG);
            hierarchyFragment = (HierarchyFragment) getSupportFragmentManager()
                    .findFragmentByTag(HierarchyFragment.TAG);

        } else {
            // The Activity is NOT being re-created so we can instantiate a new Fragment
            // and add it to the Activity

            activeConnectionFragment = new ActiveConnectionFragment();
            hierarchyFragment = new HierarchyFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.topPane, activeConnectionFragment, ActiveConnectionFragment.TAG)
                    .replace(R.id.bottomPane, hierarchyFragment, HierarchyFragment.TAG)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        MenuItem settingsItem = menu.findItem(R.id.reconnect_wifi_option);
        settingsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(MainActivity.this, R.string.menu_option_forced_wifi_handoff, Toast.LENGTH_SHORT).show();
                wifiManager.reassociate();
                // Return true to consume this click and prevent others from executing.
                return true;
            }
        });
        return true;
    }

    /*
    Called when a user clicks on an SSID's listview element.
    Triggers a fragment transaction which leads to the detail fragment
     */
    @Override
    public void onSSIDClicked(SSID ssid) {
        DetailSSIDFragment detailSSIDFragment = new DetailSSIDFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(DetailSSIDFragment.BUNDLE_ARG_SSID_PK, ssid.getId());
                detailSSIDFragment.setArguments(bundle);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.bottomPane, detailSSIDFragment, DetailSSIDFragment.TAG);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onSwitchToDetail(SSID ssid) {
        hierarchyFragment.ssidAdapter.add(ssid);
        onSSIDClicked(ssid);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}