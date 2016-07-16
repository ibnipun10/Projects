package com.product.physioit;

import android.app.Activity;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class ExerciseActivity extends Activity {

    private int _bodyPartID;
    private int _remedyID;
    private int _exerciseID;
    private DBController _dbController;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Bundle values = getIntent().getExtras();
        _bodyPartID = values.getInt(Constants.COLUMN_PARTID);
        _remedyID = values.getInt(Constants.COLUMN_REMEDYID);
        _exerciseID = values.getInt(Constants.COLUMN_EXERCISEID);
        _dbController = new DBController(new ContextWrapper(this.getApplicationContext()), Constants.PhysioITdatabase, Constants.DATABASE_VERSION_ID);

        try {
            PopulateExercisePage();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void PopulateExercisePage() throws InstantiationException, IllegalAccessException, IOException {
        Dictionary<String, Integer> dictionary = new Hashtable<String, Integer>();
        dictionary.put(Constants.COLUMN_PARTID, _bodyPartID);
        dictionary.put(Constants.COLUMN_REMEDYID, _remedyID);
        dictionary.put(Constants.COLUMN_EXERCISEID, _exerciseID);

        List<PhysioITTables.ExcerciseDB> ledb = _dbController.ReadRowsInTable(new PhysioITTables.ExcerciseDB(), dictionary);
        LinearLayout parentLinearLayout = (LinearLayout)findViewById(R.id.parentLinearLayout);

        if(!Utilities.IsNullOrEmpty(ledb))
        {
            PhysioITTables.ExcerciseDB edb = ledb.get(0);

            if(!Utilities.IsNullOrEmpty(edb.Title))
            {
                TextView exerciseTitle = new TextView(this);
                exerciseTitle.setText(edb.Title);
                Utilities.SetTitleFont(exerciseTitle, this.getResources(), Gravity.CENTER_HORIZONTAL);
                parentLinearLayout.addView(exerciseTitle);
            }


            if(!Utilities.IsNullOrEmpty(edb.Image))
            {
                //Exercise image
                ImageView exerciseImage = new ImageView(this);
                String imagePath = Constants.IMAGES_PATH + "/" + edb.PartID + "/" + edb.Image;
                exerciseImage.setImageDrawable(Utilities.GetDrawableImage(this, imagePath));
                int width = Utilities.GetValueinPixels(200, getResources());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
                params.gravity = Gravity.CENTER_HORIZONTAL;
                int margin = Utilities.GetValueinPixels(10, getResources());
                params.setMargins(margin, margin/2, margin, margin/2);
                exerciseImage.setLayoutParams(params);
                parentLinearLayout.addView(exerciseImage);

            }


            if(!Utilities.IsNullOrEmpty(edb.Description))
            {
                //Exercise Description
                TextView descriptionTitle = new TextView(this);
                descriptionTitle.setText(Constants.POSTURE_DESCRIPTION);
                Utilities.SetTitleFont(descriptionTitle, this.getResources(), Gravity.LEFT);

                TextView description = new TextView(this);
                description.setText(edb.Description);
                Utilities.SetDescriptionFont(description, this.getResources());

                parentLinearLayout.addView(descriptionTitle);
                parentLinearLayout.addView(description);
            }


            if(!Utilities.IsNullOrEmpty(edb.Dosage))
            {
                //Exercise Dosage
                TextView DosageTitle = new TextView(this);
                DosageTitle.setText(Constants.POSTURE_DOSAGE);
                Utilities.SetTitleFont(DosageTitle, this.getResources(), Gravity.LEFT);

                TextView dosage = new TextView(this);
                dosage.setText(edb.Dosage);
                Utilities.SetDescriptionFont(dosage, this.getResources());

                parentLinearLayout.addView(DosageTitle);
                parentLinearLayout.addView(dosage);
            }

            if(!Utilities.IsNullOrEmpty(edb.Precautions))
            {
                //Exercise Precautions
                TextView PrecautionTitle = new TextView(this);
                PrecautionTitle.setText(Constants.POSTURE_PRECAUTIONS);
                Utilities.SetTitleFont(PrecautionTitle, this.getResources(), Gravity.LEFT);

                TextView precaution = new TextView(this);
                precaution.setText(edb.Precautions);
                Utilities.SetDescriptionFont(precaution, this.getResources());

                parentLinearLayout.addView(PrecautionTitle);
                parentLinearLayout.addView(precaution);
            }

        }
    }
}