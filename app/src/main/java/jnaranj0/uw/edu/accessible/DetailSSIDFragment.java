package jnaranj0.uw.edu.accessible;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailSSIDFragment extends Fragment implements ConfirmDeleteDialogFragment.OnConfirmDeleteListener, EditNicknameDialog.OnChangeNicknameListener {
    public static final String TAG = "**ACC_DETAIL";
    public static final String BUNDLE_ARG_SSID_PK = "SSID_PK";
    public static final int DIALOG_FRAGMENT = 1;
    public static final String STATE_BSSIDS = "bssids";

    public ArrayList<BSSID> bssids; // stay
    public BSSIDAdapter bssidAdapter; // stay
    public ListView listView; // stay
    public SSID ssid;

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
            ssid = results.get(0);

            TextView title = (TextView) rootView.findViewById(R.id.textViewSSIDDetailName);
            title.setText(ssid.ssid);
        }
        listView = (ListView) rootView.findViewById(R.id.listViewBSSID);

        return rootView;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // restoring state
            ArrayList<Long> serialized = (ArrayList<Long>) savedInstanceState.getSerializable(STATE_BSSIDS);
            bssids = BSSID.unserialize(serialized);
        } else {
            // creating state
            bssids = new ArrayList<>();
            // converting from List to ArrayList
            for (BSSID bssid : ssid.getBSSIDs()) {
                bssids.add(bssid);
            }
        }
        bssidAdapter = new BSSIDAdapter(getActivity(), bssids);
        listView.setAdapter(bssidAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BSSID bssid = (BSSID) parent.getItemAtPosition(position);
                // open edit nickname dialog
                EditNicknameDialog editNicknameDialog = new EditNicknameDialog();
                Bundle bundle = new Bundle();
                bundle.putLong(EditNicknameDialog.BUNDLE_BSSID_PK, bssid.getId());
                editNicknameDialog.setArguments(bundle);
                editNicknameDialog.setTargetFragment(DetailSSIDFragment.this,
                        EditNicknameDialog.DIALOG_FRAGMENT);
                editNicknameDialog.show(getChildFragmentManager(), null);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                BSSID bssid = (BSSID) parent.getItemAtPosition(position);
                ConfirmDeleteDialogFragment confirmDeleteFragment = new ConfirmDeleteDialogFragment();

                Bundle bundle = new Bundle();
                bundle.putLong(ConfirmDeleteDialogFragment.BUNDLE_BSSID_PK, bssid.getId());

                confirmDeleteFragment.setArguments(bundle);
                confirmDeleteFragment.setTargetFragment(DetailSSIDFragment.this,
                        ConfirmDeleteDialogFragment.DIALOG_FRAGMENT);
                confirmDeleteFragment.show(getChildFragmentManager(), null);
                return true;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_BSSIDS, BSSID.serialize(bssids));
    }

    @Override
    public void onConfirmDelete(ConfirmDeleteDialogFragment dialog) {
        bssidAdapter.remove(dialog.bssid);
        if (bssidAdapter.isEmpty()) {
            dialog.bssid.ssid.delete();
            getActivity().getSupportFragmentManager().popBackStack();
        }
        dialog.bssid.delete();
    }

    @Override
    public void onChangeNickname(BSSID updatedBSSID, String nickname) {
        for (BSSID bssid : bssids) {
            if (bssid.equals(updatedBSSID)) {
                bssid.nickname = nickname;
                bssid.save();
                bssidAdapter.notifyDataSetChanged();
            }
        }
    }
}
