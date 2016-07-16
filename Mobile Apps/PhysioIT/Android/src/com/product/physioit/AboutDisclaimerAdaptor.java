package com.product.physioit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by nipuna on 8/25/13.
 */
public class AboutDisclaimerAdaptor extends FragmentPagerAdapter {

    private List<Fragment> _lfragments;
    public AboutDisclaimerAdaptor(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        _lfragments = fragments;
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
