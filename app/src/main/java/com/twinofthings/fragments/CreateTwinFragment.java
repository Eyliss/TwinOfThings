package com.twinofthings.fragments;

import android.content.Context;
import android.graphics.PorterDuff;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.twinofthings.R;
import com.twinofthings.activities.ReaderActivity;

public class CreateTwinFragment extends Fragment {

    private ImageButton mCloseButton;
    private ImageView mCreateTwinIcon;
    private TextView mCreateTwinTitle;
    private TextView mCreateTwinDescription;
    private Button mEnterData;

    public CreateTwinFragment() {
        // Required empty public constructor
    }

    public static CreateTwinFragment newInstance() {
        CreateTwinFragment fragment = new CreateTwinFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_create_twin, container, false);
        bindViews(rootView);

        return rootView;
    }

    private void bindViews(View rootView){
        mCreateTwinIcon = (ImageView) rootView.findViewById(R.id.creating_icon);
        mCreateTwinTitle = (TextView) rootView.findViewById(R.id.creating_title);
        mCreateTwinDescription = (TextView) rootView.findViewById(R.id.creating_description);
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
        mCreateTwinIcon.setImageResource(R.drawable.ic_scan_success);
        mCreateTwinTitle.setText(R.string.scanning_successful);
        mCreateTwinTitle.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
        mCreateTwinTitle.setVisibility(View.GONE);
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
