package com.product.physioit;

import android.content.Context;
import android.os.AsyncTask;

import Common.Constants;
import Common.Utilities;
import Controller.AzureController;
import Controller.DBController;
import Controller.NotificationHelper;

/**
 * Created by nipuna on 8/27/13.
 */
public class DownLoadUpdateTask extends AsyncTask {

    private NotificationHelper mNotificationHelper;
    private DBController mDBController;
    private Context mContext;
    public DownLoadUpdateTask(Context context, DBController dbController){
        mNotificationHelper = new NotificationHelper(context);
        mDBController = dbController;
        mContext = context;
    }

    protected void onPreExecute(){
        //Create the notification in the statusbar
        mNotificationHelper.createNotification();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        //This is where we would do the actual download stuff
        //for now I'm just going to loop for 10 seconds
        // publishing progress every second
        AzureController azureController = new AzureController(mDBController, mContext);

        int nAttempts = 0;
        int iCompletion = 0;
        while(nAttempts < Constants.MAX_ATTEMPTS)
        {
            try
            {
                if(Utilities.haveInternet(mContext))
                {
                    /*
                    switch (iCompletion)
                    {
                        case 0 : SendDeviceInfo(azureController);
                            iCompletion = 1;
                            publishProgress(15);
                        case 1 : azureController.UpdateLocalBodyPartTable();
                            iCompletion = 2;
                            publishProgress(30);
                        case 2 : azureController.UpdateLocalReasonsTable();
                            iCompletion = 3;
                            publishProgress(45);
                        case 3 : azureController.UpdateLocalRemedyTable();
                            iCompletion = 4;
                            publishProgress(60);
                        case 4 : azureController.UpdateLocalPostureTable();
                            iCompletion = 5;
                            publishProgress(75);
                        case 5 : azureController.UpdateLocalExerciseTable();
                            iCompletion = 6;
                            publishProgress(90);
                    }
*/
                }
                else
                {
                    Thread.sleep(10000);
                }
            }
            catch (Exception e) {
                publishProgress(Constants.STATUS_BAR_ERROR_CODE);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

            nAttempts++;
        }

        return null;
    }

    private void SendDeviceInfo(AzureController azureController) throws Exception {
       // azureController.UpdateAzureDeviceDB();
    }

    protected void onProgressUpdate(Integer... progress) {
        //This method runs on the UI thread, it receives progress updates
        //from the background thread and publishes them to the status bar
        String message = "";
        if(progress[0] == Constants.STATUS_BAR_ERROR_CODE)
            message = Constants.STATUS_BAR_ERROR_DESC;
        else
            message = progress[0] + Constants.STATUS_BAR_PERCENTAGE_COMPLETE;

        mNotificationHelper.progressUpdate(message);
    }
    protected void onPostExecute(Void result)    {
        //The task is complete, tell the status bar about it
        mNotificationHelper.completed();
    }
}
