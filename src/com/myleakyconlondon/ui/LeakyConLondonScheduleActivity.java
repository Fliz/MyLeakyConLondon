package com.myleakyconlondon.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import com.myleakyconlondon.dao.DataContract;
import com.myleakyconlondon.dao.DaysProvider;
import com.myleakyconlondon.dao.EventProvider;
import com.myleakyconlondon.model.DayHelper;
import com.myleakyconlondon.model.Event;
import com.myleakyconlondon.adapter.PagerAdapter;
import com.myleakyconlondon.model.TabInfo;
import com.myleakyconlondon.util.TabFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * User: Elizabeth Hamlet
 */
public class LeakyConLondonScheduleActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, EventsFragment.OnEventSelectedListener {
            //TODO FIX TAB
    private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo;
    private PagerAdapter mPagerAdapter;
    private List<Integer> days;

    public LeakyConLondonScheduleActivity() {
        mapTabInfo = new HashMap<String, TabInfo>();
    }

    @Override
        public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs_viewpager_layout);

        DayHelper dayHelper = new DayHelper();
        days = dayHelper.getUniqueDays(this);

        if(!days.isEmpty()) {

            this.initialiseTabHost(savedInstanceState, days);

            if (savedInstanceState != null) {
                mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
            }

            this.intialiseViewPager(days);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                    startConfig();
                break;
            case R.id.add_event:
                   onAddSelected();
            default:
                break;
        }

        return true;
    }

    protected void onSaveInstanceState(Bundle outState) {

        if(days != null && !days.isEmpty()) {

            outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
            super.onSaveInstanceState(outState);
        }
    }

    private void intialiseViewPager(List<Integer> days) {

        //todo change to array not list of ints
        List<Fragment> fragments = new Vector<Fragment>();

        for(Integer day : days) {

            Bundle args = new Bundle();
            args.putInt("dayId", day);

            Fragment fragment = Fragment.instantiate(this, EventsFragment.class.getName());
            fragment.setArguments(args);
            fragments.add(fragment);
        }

        this.mPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);

        this.mViewPager = (ViewPager)super.findViewById(R.id.viewpager);
        this.mViewPager.setOffscreenPageLimit(4);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setOnPageChangeListener(this);
    }

    private void initialiseTabHost(Bundle args, List<Integer> days) {

        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo = null;

        for(int day : days) {
            LeakyConLondonScheduleActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("tab" + day).setIndicator("Day " + day), (tabInfo = new TabInfo("tab" + day, EventsFragment.class, args)));

            this.mapTabInfo.put(tabInfo.tag, tabInfo);
        }

        // Default to first tab
        //this.onTabChanged("Tab1");
        //
        mTabHost.setOnTabChangedListener(this);
    }

    private static void AddTab(LeakyConLondonScheduleActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {

        tabSpec.setContent(new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    public void onTabChanged(String tag) {

        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //nothing here
    }

    @Override
    public void onPageSelected(int position) {
        this.mTabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
             //nothing here
    }

    @Override
    public void onEventSelected(Event selectedEvent) {

        Intent intent = new Intent(LeakyConLondonScheduleActivity.this, EventDetailActivity.class);
        intent.putExtra("selectedEvent", selectedEvent);
        startActivity(intent);
    }

    public void onAddSelected() {

        Intent intent = new Intent();
        intent.setClass(LeakyConLondonScheduleActivity.this, EventDetailActivity.class);
        startActivity(intent);
    }

    private void startConfig() {

        Intent intent = new Intent();
        intent.setClass(LeakyConLondonScheduleActivity.this, ConfigActivity.class);
        startActivity(intent);
    }

}
