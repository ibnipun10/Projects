package com.product.physioit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import Common.Constants;
import Common.PhysioITTables;
import Common.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nipuna on 8/18/13.
 */
public class PostureExerciseAdaptor  extends BaseExpandableListAdapter{

    private Context context;
    private List<PhysioITTables.PostureDB> postureDBList;
    private List<PhysioITTables.ExcerciseDB> exerciseDBList;
    private List<String> parentList;


    public PostureExerciseAdaptor(Context context, List<PhysioITTables.PostureDB> postureDBList, List<PhysioITTables.ExcerciseDB> exerciseDBList)
    {
        this.context = context;
        this.postureDBList = postureDBList;
        this.exerciseDBList = exerciseDBList;
        SetParentList();
    }

    public void SetParentList()
    {
        parentList = new ArrayList<String>();
        if(!Utilities.IsNullOrEmpty(postureDBList))
            parentList.add(Constants.POSTURE_TITLE);
        if(!Utilities.IsNullOrEmpty(exerciseDBList))
            parentList.add(Constants.EXCERCISE_TITLE);
    }

    @Override
    public int getGroupCount() {
        return parentList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        int nCount = 0;
        if (parentList.get(i).equals(Constants.POSTURE_TITLE))
            nCount = postureDBList.size();
        else if(parentList.get(i).equals(Constants.EXCERCISE_TITLE))
            nCount = exerciseDBList.size();

        return nCount;
    }

    @Override
    public Object getGroup(int i) {
        return parentList.get(i);
    }

    @Override
    public Object getChild(int i, int i2) {
        Object childobj = null;
        if (parentList.get(i).equals(Constants.POSTURE_TITLE))
            childobj = postureDBList.size();
        else if(parentList.get(i).equals(Constants.EXCERCISE_TITLE))
            childobj = exerciseDBList.size();

        return childobj;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i2) {
        int id = -1;
        if (parentList.get(i).equals(Constants.POSTURE_TITLE))
        {
            PhysioITTables.PostureDB pdb =  postureDBList.get(i2);
            id = pdb.PostureId;
        }
        else if(parentList.get(i).equals(Constants.EXCERCISE_TITLE))
        {
            PhysioITTables.ExcerciseDB edb = exerciseDBList.get(i2);
            id = edb.ExcerciseId;
        }

        return id;

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.postureexercise_expandablelist_parent, null);
        }

        TextView lblListHeader = (TextView) view
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(parentList.get(i));

        return view;

    }

    @Override
    public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup) {
        String text = "";
        String img = "";
        MyClickListener myClickListener = new MyClickListener();

        if (parentList.get(i).equals(Constants.POSTURE_TITLE))
        {
            PhysioITTables.PostureDB pdb =  postureDBList.get(i2);
            text = pdb.Right;
            img = pdb.Right_Img;
            myClickListener.Initialize(pdb.PartID, pdb.RemedyId, pdb.PostureId, 0);
        }
        else if(parentList.get(i).equals(Constants.EXCERCISE_TITLE))
        {
            PhysioITTables.ExcerciseDB edb = exerciseDBList.get(i2);
            text = edb.Title;
            img = edb.Image;
            myClickListener.Initialize(edb.PartID, edb.RemedyId, 0, edb.ExcerciseId);
        }

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.postureexercise_expandablelist_child, null);
        }

        RelativeLayout childRelativeLayout = (RelativeLayout) view.findViewById(R.id.childRelativeLayout);
        childRelativeLayout.setOnClickListener(myClickListener);

        TextView childTextView = (TextView) view.findViewById(R.id.childTextView);
        childTextView.setText(text);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

    private class  MyClickListener implements View.OnClickListener
    {
        private int _postureID;
        private int _bodyPartID;
        private int _remedyID;
        private int _exerciseID;

        public MyClickListener() {

        }

        public void Initialize(int bodyPartID, int remedyID, int postureID, int exerciseID)
        {
            this._bodyPartID = bodyPartID;
            this._remedyID = remedyID;
            this._postureID = postureID;
            this._exerciseID = exerciseID;
        }

        public void onClick(View view) {

            Intent intent;
            if(_postureID != 0)
            {
                intent = new Intent(context, PostureActivity.class);
                intent.putExtra(Constants.COLUMN_POSTUREID, _postureID);
            }
            else
            {
                intent = new Intent(context, ExerciseActivity.class);
                intent.putExtra(Constants.COLUMN_EXERCISEID, _exerciseID);
            }
            intent.putExtra(Constants.COLUMN_PARTID, _bodyPartID);
            intent.putExtra(Constants.COLUMN_REMEDYID, _remedyID);
            context.startActivity(intent);
        }
  }


}
