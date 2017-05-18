package com.twinofthings.fragments;

import android.app.Fragment;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
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

        //Bind views
        mCloseButton = (ImageButton)rootView.findViewById(R.id.close_button);
        mCreateTwinIcon = (ImageView) rootView.findViewById(R.id.creating_icon);
        mCreateTwinTitle = (TextView) rootView.findViewById(R.id.creating_title);
        mCreateTwinDescription = (TextView) rootView.findViewById(R.id.creating_description);

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
