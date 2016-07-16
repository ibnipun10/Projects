package com.product.physioit;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import Common.Constants;
import Common.Utilities;
import Controller.DBController;

/**
 * Created by nipuna on 7/23/13.
 */
public class ReasonsActivity extends FragmentActivity {

    private int _partID;
    private String _name;
    private ViewPager _viewPager;
    private ReasonsRemedyPageAdaptor _reasonsRemedyAdaptor;
    private DBController _dbController;
    private ProgressDialog mprogressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reasons);

        mprogressDialog = Utilities.ShowProgressDialog(this);

            Bundle values = getIntent().getExtras();
        _partID = values.getInt(Constants.COLUMN_PARTID);
        _name = values.getString(Constants.COLUMN_NAME);
        _dbController = Utilities.getDbController(this);


        InitializePageFragments();
        SetAppBar();

        Utilities.CancelProgressDialog(mprogressDialog);

    }



    public void InitializePageFragments()
    {
        List<Fragment> lfragments = new ArrayList<Fragment>();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.COLUMN_PARTID, _partID);

        ReasonsFragment reasonsFragment = new ReasonsFragment();
        reasonsFragment.setArguments(bundle);

        RemedyFragment remedyFragment = new RemedyFragment();
        remedyFragment.setArguments(bundle);

        lfragments.add(reasonsFragment);
        lfragments.add(remedyFragment);

        _reasonsRemedyAdaptor  = new ReasonsRemedyPageAdaptor(super.getSupportFragmentManager(), lfragments);
        _viewPager = (ViewPager)super.findViewById(R.id.viewpager);
        _viewPager.setAdapter(_reasonsRemedyAdaptor);

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

        String reasonsPageTitle;
        String remedyPageTitle;

        if(_partID == Constants.REASON_PRECAUTION_ID)
        {
            reasonsPageTitle = Constants.COMMON_PROBLEMS_NAME;
            remedyPageTitle = Constants.COMMON_PRECAUTIONS_NAME;
        }
        else
        {
            reasonsPageTitle = Constants.REASONS_NAME;
            remedyPageTitle = Constants.REMEDY_NAME;
        }

        aBar.addTab(aBar.newTab().setText(reasonsPageTitle).setTabListener(tabListener));
        aBar.addTab(aBar.newTab().setText(remedyPageTitle).setTabListener(tabListener));

    }


}