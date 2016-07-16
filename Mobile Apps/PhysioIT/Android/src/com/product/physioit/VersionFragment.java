package com.product.physioit;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class VersionFragment extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflat and return the layout
        View root = inflater.inflate(R.layout.about_disclaimer_fragment, container, false);
        try {
            PopulateVersionPage(root);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return root;
    }

    private void PopulateVersionPage(View root) throws PackageManager.NameNotFoundException {

        int row = Constants.VERSION_DESC.length;

        for(int i=0; i<row; i++)
        {
            String version = Constants.VERSION_DESC[i][0];
            String versionDesc = Constants.VERSION_DESC[i][1];

            PopulateEachVersion(version, root, versionDesc);
        }

    }

    private void PopulateEachVersion(String version, View root, String versionDesc)  {
        LinearLayout linearLayout = (LinearLayout)root.findViewById((R.id.aboutLinearLayout));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //Add version
        TextView versiontext = new TextView(root.getContext());
        String versionString = Constants.APP_VERSION + " : " + version;
        versiontext.setText(versionString);
        Utilities.SetTitleFont(versiontext, getResources(), Gravity.LEFT);
        linearLayout.addView(versiontext);

        //Add Version Description
        TextView versionInfo = new TextView(root.getContext());
        versionInfo.setText(versionDesc);
        Utilities.SetDescriptionFont(versionInfo, getResources());
        linearLayout.addView(versionInfo);

    }

}