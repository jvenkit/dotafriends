package com.dotafriends.dotafriends.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import com.dotafriends.dotafriends.R;

/**
 * Dialog for changing the player who's matches are being tracked
 */
public class DialogPlayerFragment extends DialogFragment {

    public interface DialogPlayerListener {
        void onDialogAddPlayer(DialogFragment dialog, long playerId);
    }

    DialogPlayerListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setTargetFragment(activity.getFragmentManager().findFragmentById(R.id.fragment_container), 0);
        try {
            mListener = (DialogPlayerListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString()
                    + " must implement DialogPlayerListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText editText = new EditText(getActivity());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        builder.setTitle(R.string.dialog_player_id_title)
                .setMessage(R.string.dialog_player_id_hint)
                .setView(editText)
                .setPositiveButton(R.string.dialog_player_id_positive_button,
                        ((dialog, which) -> mListener.onDialogAddPlayer(this,
                                Long.valueOf(editText.getText().toString()))))
                .setNegativeButton(R.string.dialog_player_id_negative_button, null);

        return builder.create();
    }
}
