package jnaranj0.uw.edu.accessible;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;

import java.util.List;

public class EditNicknameDialog extends DialogFragment {
    public static final String BUNDLE_BSSID_PK = "BSSID_PK";
    public static final int DIALOG_FRAGMENT = 2;
    public BSSID bssid;
    private OnChangeNicknameListener mListener;

    public interface OnChangeNicknameListener {
        void onChangeNickname(BSSID bssid, String nickname);
    }

    public EditNicknameDialog() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnChangeNicknameListener) getTargetFragment();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement OnChangeNicknameListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        long bssid_pk = bundle.getLong(BUNDLE_BSSID_PK, 0);
        List<BSSID> results = BSSID.find(BSSID.class, "id = ?", "" + bssid_pk);
        if (results.size() > 0) {
            bssid = results.get(0);
        }

        final EditText editText = new EditText(getActivity());

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.alert_edit_nickname_title);
        builder.setMessage(getActivity().getString(R.string.alert_edit_nickname_message) + bssid.bssid);

        builder.setView(editText)
                .setPositiveButton(R.string.alert_positive_save_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String nickname = editText.getText().toString();
                        mListener.onChangeNickname(bssid, nickname);
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
