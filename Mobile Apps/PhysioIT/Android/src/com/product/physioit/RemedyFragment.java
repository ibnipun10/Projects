package com.product.physioit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
public class RemedyFragment extends Fragment {

    private int _PartId;
    private DBController _dbController;
    private String ErgonomicsTitle;
    private Context mContext;

    public RemedyFragment()
    {
        mContext = this.getActivity();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void PopulateRemedyPage(View root) throws java.lang.InstantiationException, IllegalAccessException {
        Dictionary<String, Integer> dictionary = new Hashtable<String, Integer>();
        dictionary.put(Constants.COLUMN_PARTID, _PartId);
        List<PhysioITTables.RemedyDB> lrdb = _dbController.ReadRowsInTable(new PhysioITTables.RemedyDB(), dictionary);
        LinearLayout linearLayout = (LinearLayout)root.findViewById((R.id.reasonsLinearLayout));

        for(PhysioITTables.RemedyDB rdb : lrdb)
        {
            //populate Title
            TextView titleView = new TextView(root.getContext());
            String[] title = rdb.Title.split("\\" + Constants.CHARACTER_SEPERATOR);
            titleView.setText(title[0]);
            if(title.length > 1)
             titleView.setOnClickListener(new OnErgonomicsTitleClicked(rdb, title[1]));
            else
                titleView.setOnClickListener(new OnTitleClicked(rdb.RemedyId));
            Utilities.SetTitleFont(titleView, getResources());

            linearLayout.addView(titleView);

            //populate description
            TextView descriptionView = new TextView(root.getContext());
            descriptionView.setText(rdb.Description);
            Utilities.SetDescriptionFont(descriptionView, getResources());
            linearLayout.addView(descriptionView);

        }

        if(_PartId !=Constants.REASON_PRECAUTION_ID)
            AddTapOnTitle(root);

    }

    public void AddTapOnTitle(View root)
    {
        RelativeLayout linearLayout = (RelativeLayout) root.findViewById(R.id.parentRelativeLayout);

        TextView tapontitle = new TextView(root.getContext());
        tapontitle.setTextColor(Color.RED);
        int margin = Utilities.GetValueinPixels(10, getResources());
        tapontitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        tapontitle.setTypeface(null, Typeface.BOLD_ITALIC);
        tapontitle.setText(Constants.TAP_ON_TITLE_MESSAGE);
        RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, tapontitle.getId());
        params.setMargins(margin, margin / 2, margin, 0);
        tapontitle.setPaddingRelative(0, margin, 0, 0);
        tapontitle.setId(R.id.TapOnTitleID);
        tapontitle.setLayoutParams(params);

        linearLayout.addView(tapontitle);

        ScrollView scrollView = (ScrollView)root.findViewById(R.id.reasonRemedyScrollViewId);
        RelativeLayout.LayoutParams scrollparams = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
        scrollparams.addRule(RelativeLayout.ABOVE, tapontitle.getId());
        scrollView.setLayoutParams(scrollparams);

    }

    private class OnTitleClicked implements View.OnClickListener
    {
        private int _remedyId;

        public OnTitleClicked(int remedyID)
        {
            this._remedyId = remedyID;
        }
        @Override
        public void onClick(View view) {

                Intent intent = new Intent(getActivity(),PostureExcerciseActivity.class);
                intent.putExtra(Constants.COLUMN_PARTID, _PartId);
                intent.putExtra(Constants.COLUMN_REMEDYID, _remedyId);
                startActivity(intent);

        }
    }

    private class OnErgonomicsTitleClicked implements  View.OnClickListener
    {
        private PhysioITTables.RemedyDB mRdb;
        private String mErgonomicsTitle;
        private DBController dbController;

        public OnErgonomicsTitleClicked(PhysioITTables.RemedyDB rdb, String ergonomicsTitle)
        {
            mRdb = rdb;
            mErgonomicsTitle = ergonomicsTitle;
            dbController = Utilities.getDbController(getActivity());

        }
        @Override
        public void onClick(View view) {
            try {

                List<PhysioITTables.ExcerciseDB> edb = dbController.ReadRowsInTable(new PhysioITTables.ExcerciseDB(), dbController.GetRemedyDictionary(mRdb.PartID, mRdb.RemedyId));
                if(!Utilities.IsNullOrEmpty(edb))
                {
                    PhysioITTables.ExcerciseDB ergoExercise = edb.get(0);
                    Intent intent = new Intent(getActivity(), ExerciseActivity.class);
                    intent.putExtra(Constants.COLUMN_EXERCISEID, ergoExercise.ExcerciseId);
                    intent.putExtra(Constants.COLUMN_PARTID, ergoExercise.PartID);
                    intent.putExtra(Constants.COLUMN_REMEDYID, ergoExercise.RemedyId);
                    intent.putExtra(Constants.EXERCISE_TITLE, mErgonomicsTitle);
                    startActivity(intent);
                }
            }
            catch (Exception ex)
            {

            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.reason_remedy_fragment, container, false);
        _PartId = getArguments().getInt(Constants.COLUMN_PARTID);
        _dbController = Utilities.getDbController(this.getActivity());
        try {
            PopulateRemedyPage(root);
        } catch (Exception ex)
        {

        }

        return root;
    }
}