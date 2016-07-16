package com.product.physioit;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import Common.*;

/**
 * Created by nipuna on 8/25/13.
 */
public class AboutFragment extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflat and return the layout
        View root = inflater.inflate(R.layout.about_disclaimer_fragment, container, false);

        try {
            PopulateAboutPage(root);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return root;
    }

    private void PopulateAboutPage(View root) throws PackageManager.NameNotFoundException {

        LinearLayout linearLayout = (LinearLayout)root.findViewById((R.id.aboutLinearLayout));

        //Add version
        TextView versiontext = new TextView(root.getContext());
        String version = Constants.APP_VERSION + " : " + Utilities.GetPackageInfo(root.getContext()).versionName;
        versiontext.setText(version);
        Utilities.SetTitleFont(versiontext, getResources(), Gravity.RIGHT);
        linearLayout.addView(versiontext);

        //Add Content
        TextView contentText = new TextView(root.getContext());
        contentText.setText(Constants.ABOUT_TEXT);
        Utilities.SetTitleFont(contentText, getResources(), Gravity.LEFT);
        linearLayout.addView(contentText);

        //Add Author
        TextView authorText = new TextView(root.getContext());
        String author = Constants.APP_AUTHOR + " : " + " Nipun";
        authorText.setText(author);
        Utilities.SetTitleFont(authorText, getResources(), Gravity.RIGHT);
        linearLayout.addView(authorText);

        //Rate button
        Button rateButton = new Button(root.getContext());
        rateButton.setText(Constants.ABOUT_RATE_BUTTON);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int Margin = Utilities.GetValueinPixels(10, getResources());
        params.setMargins(Margin, Margin, Margin, Margin);
        rateButton.setLayoutParams(params);
        rateButton.setTypeface(null, Typeface.BOLD);
        rateButton.setTextColor(Color.BLUE);
        rateButton.setOnClickListener(new RateButtonListener(root));
        linearLayout.addView(rateButton);

    }

    private class RateButtonListener implements View.OnClickListener
    {
        private View _root;
        RateButtonListener(View root)
        {
            _root = root;
        }

        @Override
        public void onClick(View view) {
            Uri uri = Uri.parse("market://details?id=" + _root.getContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try
            {
            startActivity(goToMarket);
            }catch (ActivityNotFoundException e) {

            }
        }
    }


}