package jnaranj0.uw.edu.accessible;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HierarchyFragment extends Fragment {
    public static final String TAG = "**HierarchyFrag";

    SSIDAdapter ssidAdapter;

    public HierarchyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_hierarchy, container, false);

        // create array adapter for ssids
        List<SSID> ssids = SSID.listAll(SSID.class);
        Log.v(TAG, "Viewing ssids");
        for (int i=0; i < ssids.size(); i++) {
            SSID item = ssids.get(i);
            Log.v(TAG, "" + item.ssid);
        }
        // wouldn't it always be null in this method?
        if (ssidAdapter == null) {
            ssidAdapter = new SSIDAdapter(getActivity(), ssids);
        }

        ListView listView = (ListView) rootView.findViewById(R.id.listViewSSID);
        listView.setAdapter(ssidAdapter);

        //set alarm item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v(TAG, "Item clicked!");
                SSID ssid = (SSID) parent.getItemAtPosition(position);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

}
