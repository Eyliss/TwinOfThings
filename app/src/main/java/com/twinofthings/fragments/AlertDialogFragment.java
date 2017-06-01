package com.twinofthings.fragments;

import com.twinofthings.R;
import com.twinofthings.activities.ReaderActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Eyliss on 5/31/17.
 */

public class AlertDialogFragment extends DialogFragment {

    public static AlertDialogFragment newInstance(int title) {
        AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        return new AlertDialog.Builder(getActivity())
              .setTitle(title)
              .setMessage(R.string.dialog_text)
              .setPositiveButton(android.R.string.yes,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ((ReaderActivity)getActivity()).goToCreateDigitalTwin();
                        }
                    }
              )
              .setNegativeButton(android.R.string.no,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ((ReaderActivity)getActivity()).finish();
                        }
                    }
              )
              .create();
    }
}