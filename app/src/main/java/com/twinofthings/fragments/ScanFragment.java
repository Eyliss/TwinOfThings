package com.twinofthings.fragments;

import com.twinofthings.R;
import com.twinofthings.activities.ReaderActivity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ScanFragment extends Fragment {

    private OnScanTagListener mListener;

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
        ((ReaderActivity) getActivity()).setActionBarTitle(R.string.scan_activity_title);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnScanTagListener) {
            mListener = (OnScanTagListener) context;
        } else {
            throw new RuntimeException(context.toString()
                  + " must implement OnScanTagListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnScanTagListener {
        void onFragmentInteraction();
    }
}
