package jnaranj0.uw.edu.accessible;

import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailSSIDFragment extends Fragment implements ConfirmDeleteDialogFragment.OnConfirmDeleteListener {
    public static final String TAG = "**DetailFrag";
    public static final String BUNDLE_ARG_SSID_PK = "SSID_PK";
    public static final int DIALOG_FRAGMENT = 1;

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
        List<SSID> results = SSID.find(SSID.class, "id = ?", "" + pk);
        if (results.size() > 0) {
            SSID ssid = results.get(0);

            TextView title = (TextView) rootView.findViewById(R.id.textViewSSIDDetailName);
            title.setText(ssid.ssid);

            List<BSSID> bssidList = ssid.getBSSIDs();

            bssidAdapter = new BSSIDAdapter(getActivity(), bssidList);
            ListView listView = (ListView) rootView.findViewById(R.id.listViewBSSID);
            listView.setAdapter(bssidAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BSSID bssid = (BSSID) parent.getItemAtPosition(position);
                    // open edit nickname dialog
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    BSSID bssid = (BSSID) parent.getItemAtPosition(position);
                    Log.v(TAG, "" + bssid.getId() + "Clicked on " + bssid.bssid);
                    DialogFragment confirmDeleteFragment = new ConfirmDeleteDialogFragment();

                    Bundle bundle = new Bundle();
                    bundle.putLong(BUNDLE_ARG_SSID_PK, bssid.getId());

                    confirmDeleteFragment.setArguments(bundle);
                    confirmDeleteFragment.setTargetFragment(DetailSSIDFragment.this, DIALOG_FRAGMENT);
                    confirmDeleteFragment.show(getFragmentManager(), null);
                    return true;
                }
            });


        }

        return rootView;
    }

    @Override
    public void onConfirmDelete(ConfirmDeleteDialogFragment dialog) {
        bssidAdapter.remove(dialog.bssid);
        dialog.bssid.delete();
        Log.v(TAG, "DELETED " + dialog.bssid.bssid);
    }

    @Override
    public void onCancelDelete(ConfirmDeleteDialogFragment dialog) {
    }


}
