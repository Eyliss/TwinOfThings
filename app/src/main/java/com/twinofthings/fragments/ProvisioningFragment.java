package com.twinofthings.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twinofthings.R;
import com.twinofthings.activities.ReaderActivity;

public class ProvisioningFragment extends Fragment {

    private ImageButton mCloseButton;
    private ImageView mProvisioningIcon;
    private ImageView mProvisioningClip;

    private TextView mProvisioningTitle;
    private TextView mProvisioningDescription;
    private Button mEnterData;

    public ProvisioningFragment() {
        // Required empty public constructor
    }

    public static ProvisioningFragment newInstance() {
        ProvisioningFragment fragment = new ProvisioningFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_provisioning, container, false);
        bindViews(rootView);

        return rootView;
    }

    private void bindViews(View rootView){
        mProvisioningIcon = (ImageView) rootView.findViewById(R.id.provisioning_icon);
        mProvisioningClip = (ImageView) rootView.findViewById(R.id.provisioning_clip);
        mProvisioningTitle = (TextView) rootView.findViewById(R.id.creating_title);
        mProvisioningDescription = (TextView) rootView.findViewById(R.id.creating_description);
        mEnterData = (Button)rootView.findViewById(R.id.btn_enter_product_data);
        mCloseButton = (ImageButton)rootView.findViewById(R.id.close_button);

        mEnterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReaderActivity)getActivity()).goToCreateDigitalTwin();
            }
        });
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReaderActivity) getActivity()).finishProcess();
            }
        });

        ((ReaderActivity) getActivity()).setActionBarTitle(R.string.create_twin_activity_title);
    }

    //If scanning has been success, modify the interface to notify to the user
    public void adaptUItoResult(){
        mProvisioningIcon.setImageResource(R.drawable.provisioning_icon_success);
        mProvisioningClip.getLayoutParams().height = mProvisioningIcon.getLayoutParams().height * 2;
        mProvisioningClip.getLayoutParams().width = mProvisioningIcon.getLayoutParams().width * 2;
        mProvisioningClip.requestLayout();
        mProvisioningClip.setImageResource(R.drawable.provisioning_clip_success);

        mProvisioningTitle.setText(R.string.scanning_successful);
        mProvisioningTitle.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
        mProvisioningTitle.setVisibility(View.GONE);
        mCloseButton.setVisibility(View.GONE);
        mEnterData.setVisibility(View.VISIBLE);
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
