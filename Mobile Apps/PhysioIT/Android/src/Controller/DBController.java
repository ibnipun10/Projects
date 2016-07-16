package Controller;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import java.io.File;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import BackStage.DataStorage;
import Common.Constants;

/**
 * Created by nipuna on 7/21/13.
 */
public class DBController {
    private DataStorage dataStorage;
    public boolean bFirstTime;

    public DBController(ContextWrapper context, String dbName, int dbVersion)
    {
        bFirstTime = !IsDatabaseExists(context, dbName);
        CreateDatabaseAndTables(context.getApplicationContext(), dbName, dbVersion);
    }

    public boolean IsDatabaseExists(ContextWrapper context, String dbName)
    {
        File dbFile=context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public void CreateDatabaseAndTables(Context context, String dbName, int dbVersion)
    {

        dataStorage = new DataStorage(context, dbName, dbVersion);
    }
    public <T> void InsertValueInTable(T tableName) throws Exception {
        dataStorage.Insert(tableName);
    }

    public <T> void InsertValueInTable(List<T> tableValues) throws Exception {
        for(T table : tableValues)
            InsertValueInTable(table);
    }

    public <T> List<T> ReadRowsInTable(T tableName, Dictionary<String, Integer> where) throws IllegalAccessException, InstantiationException {
        return dataStorage.Read(tableName, where);
    }

    public <T> void DeleteRowsInTable(T tableName, Dictionary<String, Integer> where) throws IllegalAccessException, InstantiationException {
        dataStorage.Delete(tableName, where);
    }

    public <T> void UpdateRowsInTable(T tableName, Dictionary<String, Integer> where) throws IllegalAccessException, InstantiationException {
        dataStorage.Update(tableName, where);
    }

    public Dictionary<String, Integer> GetBodyPartDictionary(int PartId)
    {
        Dictionary<String, Integer> dictionary = new Hashtable<String, Integer>();
        dictionary.put(Constants.COLUMN_PARTID, PartId);
        return dictionary;
    }


    public Dictionary<String, Integer> GetReasonDictionary(int PartId, int ReasonId)
    {
        Dictionary<String, Integer> dictionary = new Hashtable<String, Integer>();
        dictionary.put(Constants.COLUMN_PARTID, PartId);
        dictionary.put(Constants.COLUMN_REASONSID, ReasonId);
        return dictionary;
    }


    public Dictionary<String, Integer> GetRemedyDictionary(int PartId, int RemedyId)
    {
        Dictionary<String, Integer> dictionary = new Hashtable<String, Integer>();
        dictionary.put(Constants.COLUMN_PARTID, PartId);
        dictionary.put(Constants.COLUMN_REMEDYID, RemedyId);
        return dictionary;
    }


    public Dictionary<String, Integer> GetPostureDictionary(int PartId, int RemedyId, int PostureId)
    {
        Dictionary<String, Integer> dictionary = new Hashtable<String, Integer>();
        dictionary.put(Constants.COLUMN_PARTID, PartId);
        dictionary.put(Constants.COLUMN_REMEDYID, RemedyId);
        dictionary.put(Constants.COLUMN_POSTUREID, PostureId);
        return dictionary;
    }


    public Dictionary<String, Integer> GetExerciseDictionary(int PartId, int RemedyId, int ExerciseId)
    {
        Dictionary<String, Integer> dictionary = new Hashtable<String, Integer>();
        dictionary.put(Constants.COLUMN_PARTID, PartId);
        dictionary.put(Constants.COLUMN_REMEDYID, RemedyId);
        dictionary.put(Constants.COLUMN_EXERCISEID, ExerciseId);
        return dictionary;
    }
}
