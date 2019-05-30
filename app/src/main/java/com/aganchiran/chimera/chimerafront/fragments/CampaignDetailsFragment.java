package com.aganchiran.chimera.chimerafront.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.campaign.CampaignModel;
import com.aganchiran.chimera.viewmodels.CampaignDetailsVM;

public class CampaignDetailsFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_CAMPAIGN_MODEL = "campaign_model";


    public CampaignDetailsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CampaignDetailsFragment newInstance(CampaignModel campaignModel) {
        CampaignDetailsFragment fragment = new CampaignDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CAMPAIGN_MODEL, campaignModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater
                .inflate(R.layout.fragment_campaign_details, container, false);
        CampaignDetailsVM campaignDetailsVM =
                ViewModelProviders.of(this).get(CampaignDetailsVM.class);

        if (getArguments() != null) {
            CampaignModel campaignModel = (CampaignModel) getArguments()
                    .getSerializable(ARG_CAMPAIGN_MODEL);

            if (campaignModel != null) {
                LiveData<CampaignModel> campaignLiveData =
                        campaignDetailsVM.getCampaignById(campaignModel.getId());
                campaignLiveData.observe(this, new Observer<CampaignModel>() {
                    @Override
                    public void onChanged(@Nullable CampaignModel campaignModel) {
                        assert campaignModel != null;
                        ((TextView) rootView.findViewById(R.id.campaign_image))
                                .setText(campaignModel.getName());
                        ((TextView) rootView.findViewById(R.id.description_text_view))
                                .setText(campaignModel.getDescription());
                    }
                });
            }
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_general, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
