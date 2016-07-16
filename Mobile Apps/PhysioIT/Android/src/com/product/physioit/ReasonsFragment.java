package com.product.physioit;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import Common.Constants;
import Common.PhysioITTables;
import Common.Utilities;
import Controller.DBController;

/**
 * Created by nipuna on 7/23/13.
 */
public class ReasonsFragment extends Fragment {

    private int _PartId;

    private DBController _dbController;

    public ReasonsFragment()
    {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _dbController = Utilities.getDbController(this.getActivity());
    }

    private void PopulateReasonsPage(View root) throws java.lang.InstantiationException, IllegalAccessException {
        Dictionary<String, Integer> dictionary = new Hashtable<String, Integer>();
        dictionary.put(Constants.COLUMN_PARTID, _PartId);
        List<PhysioITTables.ReasonsDB> lrdb = _dbController.ReadRowsInTable(new PhysioITTables.ReasonsDB(), dictionary);
        LinearLayout linearLayout = (LinearLayout)root.findViewById((R.id.reasonsLinearLayout));

        for(PhysioITTables.ReasonsDB rdb : lrdb)
        {
            //populate Title
            TextView titleView = new TextView(root.getContext());
            titleView.setText(rdb.Title);
            Utilities.SetTitleFont(titleView, getResources());
            linearLayout.addView(titleView);

            //populate description
            TextView descriptionView = new TextView(root.getContext());
            descriptionView.setText(rdb.Description);
            Utilities.SetDescriptionFont(descriptionView, getResources());
            linearLayout.addView(descriptionView);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflat and return the layout
        View root = inflater.inflate(R.layout.reason_remedy_fragment, container, false);
        _PartId = getArguments().getInt(Constants.COLUMN_PARTID);

        try {
            PopulateReasonsPage(root);
        } catch (Exception ex)
        {

        }

        return root;
    }

}