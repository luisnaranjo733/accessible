package jnaranj0.uw.edu.accessible;


import android.app.DialogFragment;
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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HierarchyFragment extends Fragment implements ConfirmDeleteDialogFragment.OnConfirmDeleteListener {
    public static final String TAG = "**ACC_HIERARCHY";
    public static final String STATE_SSIDS = "ssids";

    public SSIDAdapter ssidAdapter;
    public ArrayList<SSID> ssids;
    public ListView listView;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if (isVisible()) {
                Log.i(TAG, "onCreate should restore state here");
            } else {
                Log.i(TAG, "onCreate should NOT restore state here");
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
            ArrayList<Long> serialized = (ArrayList<Long>) savedInstanceState.getSerializable(STATE_SSIDS);
            ssids = SSID.unserialize(serialized);
            Log.i(TAG, "Restoring state");

        } else {
            ssids = new ArrayList<>();
            // converting from List to ArrayList
            for (SSID ssid : SSID.listAll(SSID.class)) {
                ssids.add(ssid);
            }
            Log.i(TAG, "Creating state");
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
                Log.v(TAG, "" + ssid.getId() + "Clicked on " + ssid.ssid);
                DialogFragment confirmDeleteFragment = new ConfirmDeleteDialogFragment();

                Bundle bundle = new Bundle();
                bundle.putLong(ConfirmDeleteDialogFragment.BUNDLE_SSID_PK, ssid.getId());

                confirmDeleteFragment.setArguments(bundle);
                confirmDeleteFragment.setTargetFragment(HierarchyFragment.this,
                        ConfirmDeleteDialogFragment.DIALOG_FRAGMENT);
                confirmDeleteFragment.show(getFragmentManager(), null);

                return true;
            }
        });
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore some state that needs to happen after our own views have had
            // their state restored
            // DON'T try to restore ListViews here because their scroll position will
            // not be restored properly
            Log.i(TAG, "onViewStateRestored");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_SSIDS, SSID.serialize(ssids));
        if (ssids != null) {
            Log.i(TAG, "Saving state: " + ssids.size());
        } else {
            Log.i(TAG, "Saving state");
        }
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
