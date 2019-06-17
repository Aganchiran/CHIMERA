package com.aganchiran.chimera.chimerafront.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.event.EventModel;
import com.aganchiran.chimera.chimerafront.fragments.EventCharactersFragment;
import com.aganchiran.chimera.chimerafront.fragments.EventCombatsFragment;
import com.aganchiran.chimera.chimerafront.fragments.EventDetailsFragment;
import com.aganchiran.chimera.viewmodels.EventDetailsVM;
import com.aganchiran.chimera.viewmodels.EventProfileVM;

public class EventProfileActivity extends ActivityWithUpperBar {

    public static final int DETAILS_TAB = 0;
    public static final int CHARACTER_TAB = 1;
    public static final int COMBAT_TAB = 2;

    private EventModel event;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private EventProfileVM eventProfileVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_event_profile);
        eventProfileVM = ViewModelProviders.of(this).get(EventProfileVM.class);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        event = (EventModel) getIntent().getSerializableExtra("EVENT");
        if (getIntent().getBooleanExtra("FROMCOMBAT", false)) {
            tabLayout.getTabAt(COMBAT_TAB).select();
        }

        super.onCreate(savedInstanceState);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        final TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(event.getName());

        eventProfileVM.getEventById(event.getId()).observe(this, new Observer<EventModel>() {
            @Override
            public void onChanged(@Nullable EventModel eventModel) {
                if (eventModel != null){
                    toolbarTitle.setText(eventModel.getName());
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("EVENT", event);
        setResult(RESULT_OK, intent);
        super.onBackPressed();

    }

    public EventModel getEvent() {
        return event;
    }

    public void setEvent(EventModel event) {
        this.event = event;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            EventModel eventModel = (EventModel) getIntent().getSerializableExtra("EVENT");
            switch (position) {
                case DETAILS_TAB:
                    return EventDetailsFragment.newInstance(eventModel);
                case CHARACTER_TAB:
                    return EventCharactersFragment.newInstance(eventModel);
                case COMBAT_TAB:
                    return EventCombatsFragment.newInstance(eventModel);
                default:
                    throw new RuntimeException("This tab does not exist");
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
