package com.product.physioit;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import Common.*;
import Controller.DBController;
import Controller.NotificationHelper;
import Controller.XMLController;

public class MainActivity extends Activity {

    public DBController dbController;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            //       PopulateDatabase();
            //       PopulatePage();
            new ReadDatabaseClass().execute();
            if (Utilities.GetBooleanValueFromPreferences(this, Constants.SETTINGS_AZURESERVICE_KEY))
                CreateAzureUpdateTablesTask();

            getOverflowMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        try {
            EnableMapMenuItem();
        } catch (IOException e) {
            Log.i("MainActivity", e.getMessage());
        }
        super.onResume();
    }

    private void EnableMapMenuItem() throws IOException {
        if (Utilities.IsLocationServiceEnabled(this) && Utilities.GetBooleanValueFromPreferences(this, Constants.SETTINGS_LOCATIONSERVICE_KEY))
            EnableMenuItem(R.id.menu_Map, true);
        else
            EnableMenuItem(R.id.menu_Map, false);
    }

    private class ReadDatabaseClass extends AsyncTask<Void, Void, List<PhysioITTables.BodyPartDB>> {
        ProgressDialog mprogressDialog;

        @Override
        protected void onPreExecute() {
                /*
                 * This is executed on UI thread before doInBackground(). It is
                 * the perfect place to show the progress dialog.
                 */
            mprogressDialog = Utilities.ShowProgressDialog(MainActivity.this);
        }

        @Override
        protected List<PhysioITTables.BodyPartDB> doInBackground(Void... voids) {
            List<PhysioITTables.BodyPartDB> pt = null;
            try {
                PopulateDatabase();
                pt = dbController.ReadRowsInTable(new PhysioITTables.BodyPartDB(), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return pt;
        }

        @Override
        protected void onPostExecute(List<PhysioITTables.BodyPartDB> lbdb) {
            Utilities.CancelProgressDialog(mprogressDialog);
            try {
                PopulatePage(lbdb);
            } catch (Exception ex) {
              ex.printStackTrace();
            }
        }
    }

    private void CreateAzureUpdateTablesTask() {

        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper.createNotification();
        TablesUpgradeThread tablesUpgradeThread = new TablesUpgradeThread(notificationHelper, dbController, this);
        tablesUpgradeThread.start();

    }

    private void PopulatePage(List<PhysioITTables.BodyPartDB> pt) throws Exception {

        // List<PhysioITTables.BodyPartDB> pt = dbController.ReadRowsInTable(new PhysioITTables.BodyPartDB(), null);
        if(!Utilities.IsNullOrEmpty(pt))
        {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.idTableLayout);
        int row = 0, col = 0;
        TableRow ImagetableRow = CreateAndSetTableRowParam();
        TableRow TextTableRow = CreateAndSetTableRowParam();


        for (PhysioITTables.BodyPartDB bdb : pt) {


            ImageButton imageButton = new ImageButton(this);
            // set image to ImageView
            String imagePath = Constants.IMAGES_PATH + "/" + Constants.IMAGE_BODYPART + "/" + bdb.Image;
            imageButton.setImageDrawable(Utilities.GetDrawableImage(this, imagePath));
            imageButton.setId(bdb.PartID);
            imageButton.setContentDescription(bdb.Name);
            TableRow.LayoutParams layoutparam = new TableRow.LayoutParams();
            layoutparam.height = Utilities.GetValueinPixels(100, getResources());
            layoutparam.width = 0;
            //layoutparam.weight = 1;
            imageButton.setBackgroundColor(Color.BLUE);
            //Set Margins
            int margin = Utilities.GetValueinPixels(20, getResources());
            layoutparam.setMargins(margin, margin, margin, 0);
            imageButton.setLayoutParams(layoutparam);
            imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageButton.setAdjustViewBounds(true);
            imageButton.setOnClickListener(TapOnImageButton);
            ImagetableRow.addView(imageButton);


            //Set text in the textView
            TextView textview = new TextView(this);
            textview.setText(bdb.Name);
            TableRow.LayoutParams textLayoutParams = new TableRow.LayoutParams();
            textLayoutParams.width = TableRow.LayoutParams.MATCH_PARENT;
            textLayoutParams.height = TableRow.LayoutParams.MATCH_PARENT;
            textLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            textview.setLayoutParams(textLayoutParams);
            TextTableRow.addView(textview);

            col++;
            if (col % 2 == 0 || (pt.indexOf(bdb) == pt.size() - 1)) {
                tableLayout.addView(ImagetableRow);
                tableLayout.addView(TextTableRow);
                ImagetableRow = CreateAndSetTableRowParam();
                TextTableRow = CreateAndSetTableRowParam();

                col = 0;
            }

        }
        }
        else
        {
            throw new Exception("the input list is empty or null");
        }
    }

    private TableRow CreateAndSetTableRowParam() {
        TableRow tableRow = new TableRow(this);
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        tableLayoutParams.width = TableLayout.LayoutParams.WRAP_CONTENT;
        //int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        tableLayoutParams.height = TableLayout.LayoutParams.WRAP_CONTENT;
        tableLayoutParams.weight = 1;
        tableRow.setLayoutParams(tableLayoutParams);

        return tableRow;
    }

    private View.OnClickListener TapOnImageButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, ReasonsActivity.class);
            intent.putExtra(Constants.COLUMN_PARTID, view.getId());
            intent.putExtra(Constants.COLUMN_NAME, view.getContentDescription());
            startActivity(intent);
        }
    };

    public void PopulateDatabase() throws Exception {
        dbController = Utilities.getDbController(this);

        if (dbController.bFirstTime) {            //populate database
            //Get values from XML and send it to database
            XMLController xmlController = new XMLController();

            dbController.InsertValueInTable(xmlController.GetXMLValues(this, new PhysioITTables.BodyPartDB()));
            dbController.InsertValueInTable(xmlController.GetXMLValues(this, new PhysioITTables.ExcerciseDB()));
            dbController.InsertValueInTable(xmlController.GetXMLValues(this, new PhysioITTables.PostureDB()));
            dbController.InsertValueInTable(xmlController.GetXMLValues(this, new PhysioITTables.ReasonsDB()));
            dbController.InsertValueInTable(xmlController.GetXMLValues(this, new PhysioITTables.RemedyDB()));
            dbController.InsertValueInTable(Utilities.GetDeviceInformation(this.getApplicationContext(), xmlController.ReturnXMLVersion()));

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        try {
            mMenu = menu;
            PopulateMenu(menu);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void PopulateMenu(Menu menu) throws IOException {

        //Feedback menu item
        android.view.MenuItem feedbackMenu = menu.findItem(R.id.menu_feedback);
        SetMenuIcon(feedbackMenu, Constants.IMAGES_FEEDBACK);

        //Clinic menu item
        android.view.MenuItem clinicMenu = menu.findItem(R.id.menu_Map);
        SetMenuIcon(clinicMenu, Constants.IMAGES_CLINIC);
        EnableMapMenuItem();

    }

    private void SetMenuIcon(MenuItem menuItem, String imagePath) throws IOException {
        String strImagePath = Constants.IMAGES_PATH + "/" + Constants.IMAGE_APP_BAR_ICONS + "/" + imagePath;
        menuItem.setIcon(Utilities.GetDrawableImage(this, strImagePath));
    }

    private void EnableMenuItem(int menuId, boolean bEnable) throws IOException {
        if (mMenu != null) {
            android.view.MenuItem menuItem = mMenu.findItem(menuId);
            menuItem.setEnabled(bEnable);
            if(bEnable)
                SetMenuIcon(menuItem, Constants.IMAGES_CLINIC);
            else
                SetMenuIcon(menuItem, Constants.IMAGES_CLINIC_DISABLED);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_about:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_feedback:
                intent = new Intent(MainActivity.this, FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_Map:
                intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void getOverflowMenu() {

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
