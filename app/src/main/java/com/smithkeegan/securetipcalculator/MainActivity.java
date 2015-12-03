package com.smithkeegan.securetipcalculator;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Parent activity that contains viewpager for the secure tipping app. View pager contains two
 * fragments, the calculator and a history fragment.
 * @author Keegan Smith
 * @since 12/1/2015
 */
public class MainActivity extends AppCompatActivity {

    private CalculatorPagerAdapter mSectionsPagerAdapter;
    SharedPreferences mSPref;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private final int HISTORY_PAGE_INDEX = 1;
    private final int CALCULATOR_PAGE_INDEX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new CalculatorPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            /**
             * Refreshes the listView in the history fragment whenever the user moves to it.
             * @param position page index moving to
             */
            @Override
            public void onPageSelected(int position) {
                CalculatorPagerAdapter fragmentPagerAdapter = (CalculatorPagerAdapter) mViewPager.getAdapter();
                if (position == HISTORY_PAGE_INDEX)
                    ((HistoryFragment) fragmentPagerAdapter.getRegisteredFragment(position)).refreshHistory();

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //Show welcome demo if this is the first launch
        mSPref = PreferenceManager.getDefaultSharedPreferences(this);
        //if(mSPref.getBoolean(getString(R.string.pref_is_first_launch_key),true))
            showWelcomeDemo();

        AdView adView = (AdView) findViewById(R.id.main_ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("90B00D7879E8259B432B66C66559001B")
                .build();
        adView.loadAd(adRequest);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if( id == R.id.action_about){
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Displays the welcome demo if this is the first time running the app.
     */
    public void showWelcomeDemo(){
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.demo_layout);
        RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.transparent_demo);

        //Only show the demo on first launch
        SharedPreferences.Editor editor = mSPref.edit();
        editor.putBoolean(getString(R.string.pref_is_first_launch_key),false);
        editor.apply();

        layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * FragmentStatePagerAdapter that returns a fragment corresponding to
     * one of the tabs.
     */
    public class CalculatorPagerAdapter extends FragmentStatePagerAdapter {

        private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>(); //SparseArray holding fragments

        public CalculatorPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position ==0) return new CalculatorFragment();
            else return new HistoryFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Calculator";
                case 1:
                    return "History";
            }
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position){
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position,fragment);
            return fragment;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object){
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        /**
         * For use in viewpager to get a fragment from the adapter.
         * @param position position to return
         * @return the requested fragment
         */
        public Fragment getRegisteredFragment(int position){
            return registeredFragments.get(position);
        }
    }
}
