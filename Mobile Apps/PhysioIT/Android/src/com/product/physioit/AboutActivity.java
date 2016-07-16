package com.product.physioit;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import Common.Constants;

/**
 * Created by nipuna on 8/25/13.
 */
public class AboutActivity extends FragmentActivity {

    private AboutDisclaimerAdaptor _aboutDisclaimerAdaptor;
    private ViewPager _viewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_about);

        InitializePageFragments();
        SetAppBar();
    }

    private void InitializePageFragments() {
        List<Fragment> lfragments = new ArrayList<Fragment>();
        lfragments.add(new AboutFragment());
        lfragments.add(new DisclaimerFragment());
        lfragments.add(new VersionFragment());

        _aboutDisclaimerAdaptor  = new AboutDisclaimerAdaptor(super.getSupportFragmentManager(), lfragments);
        _viewPager = (ViewPager)super.findViewById(R.id.aboutViewpager);
        _viewPager.setAdapter(_aboutDisclaimerAdaptor);

        _viewPager.setOnPageChangeListener(new OnSwipePageChanger());
    }

    private class OnSwipePageChanger extends ViewPager.SimpleOnPageChangeListener
    {
        @Override
        public void onPageSelected(int position) {
            // When swiping between pages, select the
            // corresponding tab.
            getActionBar().setSelectedNavigationItem(position);
        }

    }

    private void SetAppBar()
    {
        final ActionBar aBar = getActionBar();
        aBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                _viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
        };

        aBar.addTab(aBar.newTab().setText(Constants.ABOUT_PAGE_TITLE).setTabListener(tabListener));
        aBar.addTab(aBar.newTab().setText(Constants.DISCLAIMER_PAGE_TITLE).setTabListener(tabListener));
        aBar.addTab(aBar.newTab().setText(Constants.APP_VERSION).setTabListener(tabListener));

    }

}