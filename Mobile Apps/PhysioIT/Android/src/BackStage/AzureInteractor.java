package BackStage;

import Common.AzureResultClass;
import Common.Constants;
import Common.PhysioITTables;
import Controller.DBController;

import android.app.Activity;
import android.content.Context;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import java.util.concurrent.CountDownLatch;


import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AzureInteractor<T> {

	private MobileServiceClient mClient;
    private Context mContext;

	
	public  AzureInteractor(Context context) throws MalformedURLException {
		
			// Create the Mobile Service Client instance, using the provided
			// Mobile Service URL and key
			mClient = new MobileServiceClient(Constants.MOBILE_SERVICE_URL, Constants.MOBILE_SERVICE_KEY, context);
            mContext = context;
			// Get the Mobile Service Table instance to use
			//mToDoTable = mClient.getTable(ToDoItem.class);

	}
	
	public AzureResultClass GetRecordsFromAzureTable(T tableName) throws Exception
	{
		MobileServiceTable<T> azureTable = (MobileServiceTable<T>)mClient.getTable(tableName.getClass());
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AzureResultClass<T> azureResultClass = new AzureResultClass<T>();

        azureTable.execute(new TableQueryCallback<T>() {

            @Override
            public void onCompleted(List<T> result, int count,
                                    Exception exception, ServiceFilterResponse response) {

                azureResultClass.exception = exception;
                azureResultClass.result = result;
                countDownLatch.countDown();
            }
        });
        CountDownAwait(countDownLatch);
        return azureResultClass;
    }


    public  AzureResultClass GetRecordFromAzureTableInDeviceDB(T tablename, PhysioITTables.DeviceDB deviceDB) throws Exception
    {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AzureResultClass<T> azureResultClass = new AzureResultClass<T>();

        MobileServiceTable<T> azureTable = (MobileServiceTable<T>)mClient.getTable(tablename.getClass());

        azureTable.where().field("DeviceID").eq(deviceDB.DeviceID).execute(new TableQueryCallback<T>()
           {
               @Override
               public void onCompleted(List<T> result, int count, Exception exception, ServiceFilterResponse response)
               {
                    azureResultClass.exception = exception;
                    azureResultClass.result = result;
                    countDownLatch.countDown();
               }
           });

        CountDownAwait(countDownLatch);
        return azureResultClass;
    }
	
	public <T> AzureResultClass InsertRecordToAzureTable(T tableName) throws Exception
	{
		MobileServiceTable<T> azureTable = (MobileServiceTable<T>)mClient.getTable(tableName.getClass());
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AzureResultClass<T> azureResultClass = new AzureResultClass<T>();


        azureTable.insert(tableName,
                new TableOperationCallback<T>()
                {
                    public void onCompleted(T entity, Exception exception, ServiceFilterResponse response)
                    {
                        azureResultClass.exception = exception;
                        countDownLatch.countDown();
                    }
                }
        );

        CountDownAwait(countDownLatch);
        return  azureResultClass;
	}

    private void CountDownAwait(CountDownLatch countDownLatch) throws InterruptedException {
        countDownLatch.await(Constants.SECONDS_TO_WAIT, TimeUnit.SECONDS);
    }

    public <T> AzureResultClass UpdateRecordToAzureTable(T tableName) throws Exception
    {
        MobileServiceTable<T> azureTable = (MobileServiceTable<T>)mClient.getTable(tableName.getClass());
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AzureResultClass<T> azureResultClass = new AzureResultClass<T>();

        azureTable.update(tableName,
                new TableOperationCallback<T>() {
                    public void onCompleted(T entity, Exception exception, ServiceFilterResponse response) {

                        azureResultClass.exception = exception;
                        countDownLatch.countDown();

                    }
                }
        );

        CountDownAwait(countDownLatch);
        return  azureResultClass;
    }
}
