package Controller;

import android.content.Context;
import android.content.ContextWrapper;

import java.net.MalformedURLException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import BackStage.AzureInteractor;
import Common.Constants;
import Common.*;

/**
 * Created by nipuna on 8/25/13.
 */
public class AzureController {

    private DBController _dbController;
    private Context _context;

    public AzureController(DBController dbController, Context context)
    {
       this._context = context;
        this._dbController =  dbController;
    }

    public void UpdateLocalBodyPartTable() throws Exception {

        List<PhysioITTables.BodyPartDB> lAzurebodyPartdb = GetRecordsFromAzureTable(new PhysioITTables.BodyPartDB());
        List<PhysioITTables.BodyPartDB> lsqliteBodyPartdb = _dbController.ReadRowsInTable(new PhysioITTables.BodyPartDB(), null);

        for(PhysioITTables.BodyPartDB azuretable : lAzurebodyPartdb)
        {
            boolean bPresent = false;
            int index = 0;
            for(PhysioITTables.BodyPartDB sqlitetable : lsqliteBodyPartdb)
            {
                if(azuretable.PartID == sqlitetable.PartID)
                {
                    bPresent = true;
                    index = lsqliteBodyPartdb.indexOf(sqlitetable);
                    break;
                }
            }


            if(bPresent)
            {
                PhysioITTables.BodyPartDB row = lsqliteBodyPartdb.get(index);

                if(Utilities.DateDiff(row.TimeStamp, azuretable.TimeStamp) > 1)
                    _dbController.UpdateRowsInTable(azuretable, _dbController.GetBodyPartDictionary(row.PartID));
                lsqliteBodyPartdb.remove(index);

            }
            else
             _dbController.InsertValueInTable(azuretable);

        }

        for(PhysioITTables.BodyPartDB sqliteRow : lsqliteBodyPartdb)
        {
            _dbController.DeleteRowsInTable(sqliteRow, _dbController.GetBodyPartDictionary(sqliteRow.PartID));
        }
    }

    public void UpdateLocalReasonsTable() throws Exception {

        List<PhysioITTables.ReasonsDB> lAzurebodyPartdb = GetRecordsFromAzureTable(new PhysioITTables.ReasonsDB());
        List<PhysioITTables.ReasonsDB> lsqliteBodyPartdb = _dbController.ReadRowsInTable(new PhysioITTables.ReasonsDB(), null);

        for(PhysioITTables.ReasonsDB azuretable : lAzurebodyPartdb)
        {
            boolean bPresent = false;
            int index = 0;
            for(PhysioITTables.ReasonsDB sqlitetable : lsqliteBodyPartdb)
            {
                if(azuretable.PartID == sqlitetable.PartID && azuretable.ReasonsId == sqlitetable.ReasonsId)
                {
                    bPresent = true;
                    index = lsqliteBodyPartdb.indexOf(sqlitetable);
                    break;
                }
            }


            if(bPresent)
            {
                PhysioITTables.ReasonsDB row = lsqliteBodyPartdb.get(index);
                if(Utilities.DateDiff(row.TimeStamp, azuretable.TimeStamp) > 1)
                    _dbController.UpdateRowsInTable(azuretable, _dbController.GetReasonDictionary(row.PartID, row.ReasonsId));
                lsqliteBodyPartdb.remove(index);

            }
            else
                _dbController.InsertValueInTable(azuretable);

        }

        for(PhysioITTables.ReasonsDB sqliteRow : lsqliteBodyPartdb)
        {
            _dbController.DeleteRowsInTable(sqliteRow, _dbController.GetReasonDictionary(sqliteRow.PartID, sqliteRow.ReasonsId));
        }
    }

    public void UpdateLocalRemedyTable() throws Exception {

        List<PhysioITTables.RemedyDB> lAzurebodyPartdb = GetRecordsFromAzureTable(new PhysioITTables.RemedyDB());
        List<PhysioITTables.RemedyDB> lsqliteBodyPartdb = _dbController.ReadRowsInTable(new PhysioITTables.RemedyDB(), null);

        for(PhysioITTables.RemedyDB azuretable : lAzurebodyPartdb)
        {
            boolean bPresent = false;
            int index = 0;
            for(PhysioITTables.RemedyDB sqlitetable : lsqliteBodyPartdb)
            {
                if(azuretable.PartID == sqlitetable.PartID && azuretable.RemedyId == sqlitetable.RemedyId)
                {
                    bPresent = true;
                    index = lsqliteBodyPartdb.indexOf(sqlitetable);
                    break;
                }
            }


            if(bPresent)
            {
                PhysioITTables.RemedyDB row = lsqliteBodyPartdb.get(index);
                if(Utilities.DateDiff(row.TimeStamp, azuretable.TimeStamp) > 1)
                    _dbController.UpdateRowsInTable(azuretable, _dbController.GetRemedyDictionary(row.PartID, row.RemedyId));
                lsqliteBodyPartdb.remove(index);

            }
            else
                _dbController.InsertValueInTable(azuretable);

        }

        for(PhysioITTables.RemedyDB sqliteRow : lsqliteBodyPartdb)
        {
            _dbController.DeleteRowsInTable(sqliteRow, _dbController.GetRemedyDictionary(sqliteRow.PartID, sqliteRow.RemedyId));
        }
    }

    public void UpdateLocalPostureTable() throws Exception {

        List<PhysioITTables.PostureDB> lAzurebodyPartdb = GetRecordsFromAzureTable(new PhysioITTables.PostureDB());
        List<PhysioITTables.PostureDB> lsqliteBodyPartdb = _dbController.ReadRowsInTable(new PhysioITTables.PostureDB(), null);

        for(PhysioITTables.PostureDB azuretable : lAzurebodyPartdb)
        {
            boolean bPresent = false;
            int index = 0;
            for(PhysioITTables.PostureDB sqlitetable : lsqliteBodyPartdb)
            {
                if(azuretable.PartID == sqlitetable.PartID && azuretable.RemedyId == sqlitetable.RemedyId && azuretable.PostureId == sqlitetable.PostureId)
                {
                    bPresent = true;
                    index = lsqliteBodyPartdb.indexOf(sqlitetable);
                    break;
                }
            }


            if(bPresent)
            {
                PhysioITTables.PostureDB row = lsqliteBodyPartdb.get(index);
                if(Utilities.DateDiff(row.TimeStamp, azuretable.TimeStamp) > 1)
                    _dbController.UpdateRowsInTable(azuretable, _dbController.GetPostureDictionary(row.PartID, row.RemedyId, row.PostureId));
                lsqliteBodyPartdb.remove(index);

            }
            else
                _dbController.InsertValueInTable(azuretable);

        }

        for(PhysioITTables.PostureDB sqliteRow : lsqliteBodyPartdb)
        {
            _dbController.DeleteRowsInTable(sqliteRow, _dbController.GetPostureDictionary(sqliteRow.PartID, sqliteRow.RemedyId, sqliteRow.PostureId));
        }
    }

    public void UpdateLocalExerciseTable() throws Exception {

        List<PhysioITTables.ExcerciseDB> lAzurebodyPartdb = GetRecordsFromAzureTable(new PhysioITTables.ExcerciseDB());
        List<PhysioITTables.ExcerciseDB> lsqliteBodyPartdb = _dbController.ReadRowsInTable(new PhysioITTables.ExcerciseDB(), null);

        for(PhysioITTables.ExcerciseDB azuretable : lAzurebodyPartdb)
        {
            boolean bPresent = false;
            int index = 0;
            for(PhysioITTables.ExcerciseDB sqlitetable : lsqliteBodyPartdb)
            {
                if(azuretable.PartID == sqlitetable.PartID && azuretable.RemedyId == sqlitetable.RemedyId && azuretable.ExcerciseId == sqlitetable.ExcerciseId)
                {
                    bPresent = true;
                    index = lsqliteBodyPartdb.indexOf(sqlitetable);
                    break;
                }
            }


            if(bPresent)
            {
                PhysioITTables.ExcerciseDB row = lsqliteBodyPartdb.get(index);
                if(Utilities.DateDiff(row.TimeStamp, azuretable.TimeStamp) > 1)
                    _dbController.UpdateRowsInTable(azuretable, _dbController.GetExerciseDictionary(row.PartID, row.RemedyId, row.ExcerciseId));
                lsqliteBodyPartdb.remove(index);

            }
            else
                _dbController.InsertValueInTable(azuretable);

        }

        for(PhysioITTables.ExcerciseDB sqliteRow : lsqliteBodyPartdb)
        {
            _dbController.DeleteRowsInTable(sqliteRow, _dbController.GetExerciseDictionary(sqliteRow.PartID, sqliteRow.RemedyId, sqliteRow.ExcerciseId));
        }
    }


    public <T> List<T> GetRecordsFromAzureTable(T tableName) throws Exception {

        AzureInteractor azureInteractor = new AzureInteractor(_context);
        AzureResultClass<T> azureResultClass = azureInteractor.GetRecordsFromAzureTable(tableName);

        if(azureResultClass.exception != null)
            throw azureResultClass.exception;

        return azureResultClass.result;
    }

    public <T> void InsertIntoAzureTable(T tableName) throws Exception {

        AzureInteractor azureInteractor = new AzureInteractor(_context);
        AzureResultClass<T> azureResultClass = azureInteractor.InsertRecordToAzureTable(tableName);

        if(azureResultClass.exception != null)
            throw azureResultClass.exception;
    }

    public <T> void UpdateIntoAzureTable(T tableName) throws Exception {

        AzureInteractor azureInteractor = new AzureInteractor(_context);
        AzureResultClass<T> azureResultClass = azureInteractor.UpdateRecordToAzureTable(tableName);

        if(azureResultClass.exception != null)
                throw azureResultClass.exception;
    }


    public void UpdateAzureDeviceDB() throws Exception
    {
        List<PhysioITTables.DeviceDB> ldeviceDB = _dbController.ReadRowsInTable(new PhysioITTables.DeviceDB(), null);
        if(!Utilities.IsNullOrEmpty(ldeviceDB))
        {
            PhysioITTables.DeviceDB deviceinfo = ldeviceDB.get(0);
            AzureInteractor<PhysioITTables.DeviceDB> azureInteractor = new AzureInteractor(_context);
            AzureResultClass<PhysioITTables.DeviceDB> azureResultClass = azureInteractor.GetRecordFromAzureTableInDeviceDB(new PhysioITTables.DeviceDB(), deviceinfo);

            if(azureResultClass.exception != null)
                throw new Exception(azureResultClass.exception);
            else
            {
                if(!Utilities.IsNullOrEmpty(azureResultClass.result))
                {
                    PhysioITTables.DeviceDB azureDeviceInfo = azureResultClass.result.get(0);

                    if(!azureDeviceInfo.XMLVersion.equalsIgnoreCase(deviceinfo.XMLVersion))
                    {
                        azureDeviceInfo.XMLVersion = deviceinfo.XMLVersion;
                        UpdateIntoAzureTable(azureDeviceInfo);
                    }

                }
                else
                {
                   //Insert in Azure table
                    InsertIntoAzureTable(deviceinfo);
                }
            }

        }
        else
            throw new Exception(Constants.ERROR_DEVICE_INFORMATION_NOT_FOUND_LOCAL);
    }

}
