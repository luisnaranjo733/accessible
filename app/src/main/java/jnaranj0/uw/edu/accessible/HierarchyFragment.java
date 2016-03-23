package jnaranj0.uw.edu.accessible;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class HierarchyFragment extends Fragment implements ConfirmDeleteDialogFragment.OnConfirmDeleteListener {
    public static final String TAG = "**ACC_HIERARCHY";
    public static final String STATE_SSIDS = "ssids";

    public SSIDAdapter ssidAdapter;
    public ArrayList<SSID> ssids;
    public ListView listView;

    Bundle instanceState;

    private OnSSIDClickedListener callback;

    public interface OnSSIDClickedListener {
        void onSSIDClicked(SSID ssid);
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

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            ArrayList<Long> data = (ArrayList<Long>) savedInstanceState.getSerializable(STATE_SSIDS);
            if (data != null && !data.isEmpty()) {
                instanceState = savedInstanceState;
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_hierarchy, container, false);
        listView = (ListView) rootView.findViewById(R.id.listViewSSID);
        listView.setEmptyView(rootView.findViewById(R.id.emptyElement));

        // Inflate the layout for this fragment
        return rootView;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ArrayList<Long> serialized;
            if (instanceState ==null) {
                // if the user rotates the screen twice, this savedInstanceState will be empty
                // because android only calls onCreate
                serialized = (ArrayList<Long>) savedInstanceState.getSerializable(STATE_SSIDS);
            } else {
                // instanceState is intercepted and saved in onCreate in the above case occurs
                serialized = (ArrayList<Long>) instanceState.getSerializable(STATE_SSIDS);
            }
            ssids = SSID.unserialize(serialized);

        } else {
            ssids = new ArrayList<>();
            // converting from List to ArrayList
            for (SSID ssid : SSID.listAll(SSID.class)) {
                ssids.add(ssid);
            }
        }

        ssidAdapter = new SSIDAdapter(getActivity(), ssids);
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
                ConfirmDeleteDialogFragment confirmDeleteFragment = new ConfirmDeleteDialogFragment();

                Bundle bundle = new Bundle();
                bundle.putLong(ConfirmDeleteDialogFragment.BUNDLE_SSID_PK, ssid.getId());

                confirmDeleteFragment.setArguments(bundle);
                confirmDeleteFragment.setTargetFragment(HierarchyFragment.this,
                        ConfirmDeleteDialogFragment.DIALOG_FRAGMENT);
                confirmDeleteFragment.show(getChildFragmentManager(), null);
                return true;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_SSIDS, SSID.serialize(ssids));
    }


    @Override
    public void onConfirmDelete(ConfirmDeleteDialogFragment dialog) {
        ssids.remove(dialog.ssid);
        ssidAdapter.notifyDataSetChanged();
        for (BSSID bssid : dialog.ssid.getBSSIDs()) {
            bssid.delete();
        }
        dialog.ssid.delete();
    }

}
