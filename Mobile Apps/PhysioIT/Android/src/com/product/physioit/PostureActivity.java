package com.product.physioit;

import android.app.Activity;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import Common.Constants;
import Common.PhysioITTables;
import Common.Utilities;
import Controller.DBController;

/**
 * Created by nipuna on 8/22/13.
 */
public class PostureActivity extends Activity {

    private int _bodyPartID;
    private int _remedyID;
    private int _postureID;
    private DBController _dbController;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posture);

        Bundle values = getIntent().getExtras();
        _bodyPartID = values.getInt(Constants.COLUMN_PARTID);
        _remedyID = values.getInt(Constants.COLUMN_REMEDYID);
        _postureID = values.getInt(Constants.COLUMN_POSTUREID);

        _dbController = new DBController(new ContextWrapper(this.getApplicationContext()), Constants.PhysioITdatabase, Constants.DATABASE_VERSION_ID);

        try {
            PopulatePosturePage();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void PopulatePosturePage() throws InstantiationException, IllegalAccessException, IOException {
        Dictionary<String, Integer> dictionary = new Hashtable<String, Integer>();
        dictionary.put(Constants.COLUMN_PARTID, _bodyPartID);
        dictionary.put(Constants.COLUMN_REMEDYID, _remedyID);
        dictionary.put(Constants.COLUMN_POSTUREID, _postureID);
        List<PhysioITTables.PostureDB> lpdb = _dbController.ReadRowsInTable(new PhysioITTables.PostureDB(), dictionary);

       if(!Utilities.IsNullOrEmpty(lpdb))
       {
            PhysioITTables.PostureDB pdb = lpdb.get(0);
            //wrong title
            TextView wrongtext = (TextView)findViewById(R.id.wrongTitle);
            wrongtext.setText(pdb.Wrong);
           Utilities.SetTitleFont(wrongtext, this.getResources(), Gravity.CENTER_HORIZONTAL);

            //wrong image
            ImageView wrongImg = (ImageView)findViewById(R.id.wrongImage);
            String imagePath = Constants.IMAGES_PATH + "/" + pdb.PartID + "/" + pdb.Wrong_Img;
            wrongImg.setImageDrawable(Utilities.GetDrawableImage(this, imagePath));

            //right title
            TextView righttext = (TextView)findViewById(R.id.rightTitle);
            righttext.setText(pdb.Right);
            Utilities.SetTitleFont(righttext, this.getResources(), Gravity.CENTER_HORIZONTAL);


            //Right Image
            ImageView rightImg = (ImageView)findViewById(R.id.rightImage);
            imagePath = Constants.IMAGES_PATH + "/" + pdb.PartID + "/" + pdb.Right_Img;
            rightImg.setImageDrawable(Utilities.GetDrawableImage(this, imagePath));

            //Description
           TextView descriptionTitle = (TextView)findViewById(R.id.descriptionTitle);
           descriptionTitle.setText(Constants.POSTURE_DESCRIPTION);
           TextView desciptiontext = (TextView)findViewById(R.id.rightDescription);
           desciptiontext.setText(pdb.Description);
           Utilities.SetDescriptionFont(descriptionTitle, this.getResources());

           if(Utilities.IsNullOrEmpty(pdb.Description))
           {
               descriptionTitle.setVisibility(View.GONE);
                desciptiontext.setVisibility(View.GONE);
           }
        }
    }
}