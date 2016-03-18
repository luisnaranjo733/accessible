package jnaranj0.uw.edu.accessible;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailSSIDFragment extends Fragment {
    public static final String TAG = "**DetailFrag";
    public static final String BUNDLE_ARG_SSID_PK = "SSID_PK";;

    public BSSIDAdapter bssidAdapter;

    public DetailSSIDFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_detail_ssid, container, false);

        Bundle bundle = getArguments();
        long pk = bundle.getLong(BUNDLE_ARG_SSID_PK);
        List<SSID> results = SSID.find(SSID.class, "" + pk);
        if (results.size() > 0) {
            SSID ssid = results.get(0);
            List<BSSID> bssidList = ssid.getBSSIDs();

            if (bssidAdapter == null) {
                bssidAdapter = new BSSIDAdapter(getActivity(), bssidList);
                ListView listView = (ListView) rootView.findViewById(R.id.listViewBSSID);
                listView.setAdapter(bssidAdapter);
            }

        }

        return rootView;
    }

}
