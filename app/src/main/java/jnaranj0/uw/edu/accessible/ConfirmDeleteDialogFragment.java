package jnaranj0.uw.edu.accessible;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class ConfirmDeleteDialogFragment extends DialogFragment {
    public static final String BUNDLE_BSSID_PK = "BUNDLE_BSSID_PK";
    public static final String BUNDLE_SSID_PK = "BUNDLE_SSID_PK";
    public static final int DIALOG_FRAGMENT = 1;

    OnConfirmDeleteListener mListener;

    BSSID bssid;
    SSID ssid;

    public interface OnConfirmDeleteListener{
        void onConfirmDelete(ConfirmDeleteDialogFragment dialog);
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
        long bssidPk = bundle.getLong(BUNDLE_BSSID_PK, 0);
        long ssidPk = bundle.getLong(BUNDLE_SSID_PK, 0);

        String message = "";
        // if we are confirming whether or not to delete a bssid
        if (bssidPk != 0) {
            // set the dialog title string
            message = getActivity().getString(R.string.alert_confirm_delete_message_bssid);
            // find the bssid, and set it as a field for the callback to refer to
            List<BSSID> results = BSSID.find(BSSID.class, "id = ?", "" + bssidPk);
            if (results.size() > 0) {
                bssid = results.get(0);
                Log.v("**A", "Delete " + bssid.bssid);
            }
            // if we are confirming whether or not to delete a ssid
        } else if (ssidPk != 0) {
            // set the dialog title string
            message = getActivity().getString(R.string.alert_confirm_delete_message_ssid);
            // find the ssid, and set it as a field for the callback to refer to
            List<SSID> results = SSID.find(SSID.class, "id = ?", "" + ssidPk);
            if (results.size() > 0) {
                ssid = results.get(0);
                Log.v("**A", "Delete " + ssid.ssid);
            }

        }



        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton(R.string.alert_positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onConfirmDelete(ConfirmDeleteDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.alert_negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}