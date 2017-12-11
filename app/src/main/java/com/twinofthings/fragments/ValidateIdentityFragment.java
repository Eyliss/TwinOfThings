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

public class ValidateIdentityFragment extends Fragment {

    private RelativeLayout mImagesContainer;
    private TextView mScanTitle;
    private TextView mScanDescription;
    private ProgressBar mProgressBar;
    private ImageView rocketImage;
    private ImageView scanImage;

//    AnimationDrawable rocketAnimation;

    public ValidateIdentityFragment() {

    }

    public static ValidateIdentityFragment newInstance() {
        ValidateIdentityFragment fragment = new ValidateIdentityFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_validate_identity, container, false);

        bindViews(rootView);

        return rootView;
    }

    private void bindViews(View rootView){
        mImagesContainer = (RelativeLayout) rootView.findViewById(R.id.images_container);
        mScanTitle = (TextView) rootView.findViewById(R.id.validate_identity_title);
        mScanDescription = (TextView) rootView.findViewById(R.id.validate_identity_description);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        rocketImage = (ImageView) rootView.findViewById(R.id.scan_clip);
        scanImage = (ImageView) rootView.findViewById(R.id.scan_image);

        ((ReaderActivity) getActivity()).setActionBarTitle(R.string.validate_identity);

//        rocketImage.setImageResource(R.drawable.radar_animation);
//        rocketAnimation = (AnimationDrawable) rocketImage.getDrawable();
//        rocketAnimation.start();
    }

    //Configure UI for scanning
    public void startScan(){
//        rocketAnimation.stop();
        mScanTitle.setText(R.string.scanning_successful);
        mScanTitle.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        mScanDescription.setVisibility(View.VISIBLE);
        rocketImage.setImageResource(R.drawable.ic_success);
        scanImage.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);
    }

    //Configure UI when scan stops
    public void stopScan(){
//        rocketAnimation.start();
        mScanTitle.setText(R.string.validate_identity_title);
        mScanTitle.setTextColor(getResources().getColor(android.R.color.white));
        mScanDescription.setVisibility(View.VISIBLE);
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
