package jnaranj0.uw.edu.accessible;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class ConfirmDeleteDialogFragment extends DialogFragment {
    public static final String BUNDLE_BSSID_PK = "BUNDLE_BSSID_PK";
    OnConfirmDeleteListener mListener;
    BSSID bssid;

    public interface OnConfirmDeleteListener{
        void onConfirmDelete(ConfirmDeleteDialogFragment dialog);
        void onCancelDelete(ConfirmDeleteDialogFragment dialog);
    }

    public ConfirmDeleteDialogFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (OnConfirmDeleteListener) getTargetFragment();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement OnConfirmDeleteListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        long pk = bundle.getLong(DetailSSIDFragment.BUNDLE_ARG_SSID_PK);
        List<BSSID> results = BSSID.find(BSSID.class, "id = ?", "" + pk);
        if (results.size() > 0) {
            bssid = results.get(0);
            Log.v("**A", "Delete " + bssid.bssid);
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete BSSID?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onConfirmDelete(ConfirmDeleteDialogFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        mListener.onCancelDelete(ConfirmDeleteDialogFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}