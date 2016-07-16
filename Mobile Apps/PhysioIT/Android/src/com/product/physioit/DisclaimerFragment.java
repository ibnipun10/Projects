package com.product.physioit;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import Common.Constants;
import Common.Utilities;

/**
 * Created by nipuna on 8/25/13.
 */
public class DisclaimerFragment extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflat and return the layout
        View root = inflater.inflate(R.layout.about_disclaimer_fragment, container, false);

        PopulateDisclaimerPage(root);
        return root;
    }

    private void PopulateDisclaimerPage(View root) {

        LinearLayout linearLayout = (LinearLayout)root.findViewById((R.id.aboutLinearLayout));

        //Add Disclaimer
        TextView disclaimerText = new TextView(root.getContext());
        disclaimerText.setText(Constants.DISCLAIMER_TEXT);
        Utilities.SetTitleFont(disclaimerText, getResources(), Gravity.LEFT);

        linearLayout.addView(disclaimerText);

    }
}