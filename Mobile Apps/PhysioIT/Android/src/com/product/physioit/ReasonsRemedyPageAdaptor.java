package com.product.physioit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by nipuna on 7/23/13.
 */
public class ReasonsRemedyPageAdaptor extends FragmentPagerAdapter {

    private List<Fragment> _lfragments;

    public ReasonsRemedyPageAdaptor(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this._lfragments = fragments;
    }
    @Override
    public Fragment getItem(int i) {
        return this._lfragments.get(i);
    }

    @Override
    public int getCount() {
        return this._lfragments.size();
    }
}