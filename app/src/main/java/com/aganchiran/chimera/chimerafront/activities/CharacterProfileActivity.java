package com.aganchiran.chimera.chimerafront.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.aganchiran.chimera.R;
import com.aganchiran.chimera.chimeracore.character.CharacterModel;
import com.aganchiran.chimera.chimerafront.fragments.CharacterDetailsFragment;
import com.aganchiran.chimera.chimerafront.fragments.CombatProfileFragment;
import com.aganchiran.chimera.chimerafront.fragments.ConsumableListFragment;

public class CharacterProfileActivity extends ActivityWithUpperBar {

    public static final int DETAILS_TAB = 0;
    public static final int CONSUMABLES_TAB = 1;
    public static final int COMBAT_TAB = 2;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_character_profile);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
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
            CharacterModel characterModel =
                    (CharacterModel) getIntent().getSerializableExtra("CHARACTER");
            switch (position) {
                case DETAILS_TAB:
                    return CharacterDetailsFragment.newInstance(characterModel);
                case CONSUMABLES_TAB:
                    return ConsumableListFragment.newInstance(characterModel);
                case COMBAT_TAB:
                    return CombatProfileFragment.newInstance(characterModel);
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
