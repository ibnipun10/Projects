package com.UI.Common.library;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

public class DBController {

	private DataStorage m_dataStorage;
	private boolean m_bFirstTime;
	private String m_myId;
	private boolean m_bHomeTown;

	public DBController(ContextWrapper context, String dbName, int dbVersion, boolean bhometown, String myId)
	{
		m_myId = myId;
		m_bHomeTown = bhometown;
		m_bFirstTime = !IsDatabaseExists(context, dbName);
		CreateDatabaseAndTables(context.getApplicationContext(), dbName, dbVersion);     

	}	    

	public boolean IsDatabaseExists(ContextWrapper context, String dbName)
	{
		File dbFile=context.getDatabasePath(dbName);
		return dbFile.exists();
	}

	public void CreateDatabaseAndTables(Context context, String dbName, int dbVersion)
	{

		m_dataStorage = new DataStorage(context, dbName, dbVersion, m_bHomeTown);
	}

	public List<FacebookFriend> ReadRowsInTable(String fbFriendId) throws IllegalAccessException, InstantiationException {

		return ReadRows(fbFriendId, null);
	}
	
	private List<FacebookFriend> ReadRows(String fbFriendId, String where) throws IllegalAccessException, IllegalArgumentException, InstantiationException
	{
		if(m_dataStorage != null)
			return m_dataStorage.Read(new FacebookFriend(), fbFriendId, m_myId, where);
		else
		{
			Utilities.PrintMessageInConsole("Data Storage object is till not initialized");
			return null;
		}
	}

	public List<FacebookFriend> ReadRowswithNameQuery(String fbFriendId, String name) throws IllegalAccessException, InstantiationException {

		String where = null;
		
		if(name != null)
			where = "name like '%" + name + "%'";
		
		if(m_dataStorage != null)
			return ReadRows(fbFriendId, where);
		else
		{
			Utilities.PrintMessageInConsole("Data Storage object is till not initialized");
			return null;
		}
	}


	public  void InsertRowInTable(FacebookFriend tableName) throws Exception
	{
		tableName.myId = m_myId;
		try
		{
			m_dataStorage.Insert(tableName);
		}
		catch(Exception ex)
		{
			Utilities.PrintMessageInConsole(Log.getStackTraceString(ex));
		}
	}

	public void UpdateRowInTable(FacebookFriend tableName) throws Exception
	{
		tableName.myId = m_myId;
		try
		{
			m_dataStorage.Update(tableName, tableName.id, tableName.myId);
		}
		catch(Exception ex)
		{
			Utilities.PrintMessageInConsole(Log.getStackTraceString(ex));
		}
	}
}
