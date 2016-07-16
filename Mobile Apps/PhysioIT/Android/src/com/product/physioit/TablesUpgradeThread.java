package com.product.physioit;

import android.content.Context;

import Common.Constants;
import Common.Utilities;
import Controller.AzureController;
import Controller.DBController;
import Controller.NotificationHelper;

/**
 * Created by nipuna on 8/30/13.
 */
public class TablesUpgradeThread extends Thread
{
    private NotificationHelper mNotificationHelper;
    private DBController mDBController;
    private Context mcontext;

    public TablesUpgradeThread(NotificationHelper notificationHelper, DBController dbController, Context context)
    {
        mNotificationHelper = notificationHelper;
        mDBController = dbController;
        mcontext = context;
    }

    private enum TableComplete
    {
        StartedUpdate,
        DeviceDBUpdateCompelete,
        BodyPartDBUpdateCompelte,
        ReasonsDBUpdateComplete,
        RemedyDBUpdateComplete,
        PostureDBUpdateComplete,
        ExcerciseDBUpdateComplete
    }
    public void run()
    {

        AzureController azureController = new AzureController(mDBController, mcontext);

        int nAttempts = 0;

        TableComplete tableComplete = TableComplete.StartedUpdate;

        while(nAttempts < Constants.MAX_ATTEMPTS)
        {
            try
            {
                if(Utilities.haveInternet(mcontext))
                {

                    switch (tableComplete)
                    {
                        case StartedUpdate: azureController.UpdateAzureDeviceDB();
                            tableComplete = TableComplete.DeviceDBUpdateCompelete;
                            mNotificationHelper.progressUpdate(Constants.STATUS_BAR_DEVICEINFO_UPDATE);

                        case DeviceDBUpdateCompelete: azureController.UpdateLocalBodyPartTable();
                            tableComplete = TableComplete.BodyPartDBUpdateCompelte;
                            mNotificationHelper.progressUpdate(Constants.STATUS_BAR_UPDATE_TABLES);

                        case BodyPartDBUpdateCompelte: azureController.UpdateLocalReasonsTable();
                            tableComplete = TableComplete.ReasonsDBUpdateComplete;

                        case ReasonsDBUpdateComplete:  azureController.UpdateLocalRemedyTable();
                            tableComplete = TableComplete.RemedyDBUpdateComplete;

                        case RemedyDBUpdateComplete: azureController.UpdateLocalPostureTable();
                            tableComplete = TableComplete.PostureDBUpdateComplete;

                        case PostureDBUpdateComplete:  azureController.UpdateLocalExerciseTable();
                            tableComplete = TableComplete.ExcerciseDBUpdateComplete;

                        default:
                            mNotificationHelper.completed();
                            return;
                    }
                }
                else
                {
                    mNotificationHelper.progressUpdate(Constants.STATUS_BAR_NO_INTERNET_CONNECTION);
                    Thread.sleep(10000);
                }
            }
            catch (Exception e) {

                try {
                    mNotificationHelper.progressUpdate(Constants.STATUS_BAR_ERROR_DESC);
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

            nAttempts++;
        }

        mNotificationHelper.completed();
    }
}
