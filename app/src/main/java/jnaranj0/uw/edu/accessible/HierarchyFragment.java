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

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HierarchyFragment extends Fragment implements ConfirmDeleteDialogFragment.OnConfirmDeleteListener {
    public static final String TAG = "**HierarchyFrag";

    public SSIDAdapter ssidAdapter;
    public List<SSID> ssids;
    private OnSSIDClickedListener callback;

    public interface OnSSIDClickedListener {
        void onSSIDClicked(SSID ssid);
        void onSSIDLongPressed(SSID ssid);
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
        ssids = SSID.listAll(SSID.class);

        ssidAdapter = new SSIDAdapter(getActivity(), ssids);

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

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onConfirmDelete(ConfirmDeleteDialogFragment dialog) {
        Log.v(TAG, "On confirm delete: " + dialog.ssid.ssid);
        ((OnSSIDClickedListener) getActivity()).onSSIDLongPressed(dialog.ssid);
    }

    @Override
    public void onCancelDelete(ConfirmDeleteDialogFragment dialog) {

    }

}
