package com.twinofthings.fragments;

import com.twinofthings.R;
import com.twinofthings.activities.ReaderActivity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ScanFragment extends Fragment {

    private ImageButton mCloseButton;
    private ImageView mScanIcon;
    private TextView mScanTitle;
    private TextView mScanDescription;

    public ScanFragment() {

    }

    public static ScanFragment newInstance() {
        ScanFragment fragment = new ScanFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_scan, container, false);

        //Bind views
        mCloseButton = (ImageButton)rootView.findViewById(R.id.close_button);
        mScanIcon = (ImageView) rootView.findViewById(R.id.scanning_icon);
        mScanTitle = (TextView) rootView.findViewById(R.id.scanning_title);
        mScanDescription = (TextView) rootView.findViewById(R.id.scanning_description);

        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReaderActivity) getActivity()).finishProcess();
            }
        });

        ((ReaderActivity) getActivity()).setActionBarTitle(R.string.scan_activity_title);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
