package jnaranj0.uw.edu.accessible;


import android.content.Context;
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

    public SSIDAdapter ssidAdapter;
    private OnSSIDClickedListener callback;

    public interface OnSSIDClickedListener {
        public void onSSIDClicked(SSID ssid);
        public void onSSIDLongPressed(SSID ssid);
    }

    public HierarchyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try {
            callback = (OnSSIDClickedListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnSSIDClickedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_hierarchy, container, false);

        // create array adapter for ssids
        List<SSID> ssids = SSID.listAll(SSID.class);

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
                SSID ssid = (SSID) parent.getItemAtPosition(position);
                ((OnSSIDClickedListener) getActivity()).onSSIDClicked(ssid);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SSID ssid = (SSID) parent.getItemAtPosition(position);
                ((OnSSIDClickedListener) getActivity()).onSSIDLongPressed(ssid);
                return true;
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

}
