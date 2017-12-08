package com.twinofthings.fragments;

import com.twinofthings.R;
import com.twinofthings.activities.ReaderActivity;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScanFragment extends Fragment {

    private ImageButton mCloseButton;
    private RelativeLayout mImagesContainer;
    private TextView mScanTitle;
    private TextView mScanDescription;
    private ProgressBar mProgressBar;
    private ImageView rocketImage;
    private ImageView scanImage;

    AnimationDrawable rocketAnimation;

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

        bindViews(rootView);

        return rootView;
    }

    private void bindViews(View rootView){
        mCloseButton = (ImageButton)rootView.findViewById(R.id.close_button);
        mImagesContainer = (RelativeLayout) rootView.findViewById(R.id.images_container);
        mScanTitle = (TextView) rootView.findViewById(R.id.scanning_title);
        mScanDescription = (TextView) rootView.findViewById(R.id.scanning_description);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        rocketImage = (ImageView) rootView.findViewById(R.id.scan_clip);
        scanImage = (ImageView) rootView.findViewById(R.id.scan_image);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReaderActivity) getActivity()).finishProcess();
            }
        });

        ((ReaderActivity) getActivity()).setActionBarTitle(R.string.scan_activity_title);

        rocketImage.setImageResource(R.drawable.radar_animation);
        rocketAnimation = (AnimationDrawable) rocketImage.getDrawable();
        rocketAnimation.start();
    }

    //Configure UI for scanning
    public void startScan(){
        rocketAnimation.stop();
        mScanTitle.setText(R.string.scanning_successful);
        mScanTitle.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        mScanDescription.setText(R.string.loading);
        rocketImage.setImageResource(R.drawable.ic_scan_success);
        scanImage.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);
    }

    //Configure UI when scan stops
    public void stopScan(){
        rocketAnimation.start();
        mScanTitle.setText(R.string.scanning_tag_title);
        mScanTitle.setTextColor(getResources().getColor(android.R.color.white));
        mScanDescription.setText(R.string.scanning_tag_description);
        mProgressBar.setVisibility(View.GONE);
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
