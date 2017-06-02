package com.twinofthings.fragments;

import com.twinofthings.R;
import com.twinofthings.activities.ReaderActivity;
import com.twinofthings.utils.Constants;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Eyliss on 5/31/17.
 */

public class AlertDialogFragment extends DialogFragment {

    public static AlertDialogFragment newInstance(int title, int text, boolean dataFound) {
        AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.JSON_DIALOG_TITLE, title);
        args.putInt(Constants.JSON_DIALOG_TEXT, text);
        args.putBoolean(Constants.JSON_DATA_FOUND, dataFound);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt(Constants.JSON_DIALOG_TITLE);
        int text = getArguments().getInt(Constants.JSON_DIALOG_TEXT);
        final boolean dataFound = getArguments().getBoolean(Constants.JSON_DATA_FOUND,true);

        return new AlertDialog.Builder(getActivity())
              .setTitle(title)
              .setMessage(text)
              .setPositiveButton(android.R.string.yes,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(dataFound){
                                ((ReaderActivity)getActivity()).goToCreateDigitalTwin();
                            }else{
                                ((ReaderActivity)getActivity()).getCredentials();
                            }
                        }
                    }
              )
              .setNegativeButton(android.R.string.no,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            (getActivity()).finish();
                        }
                    }
              )
              .create();
    }
}