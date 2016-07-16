package com.UI.Common.library;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.helpers.LocatorImpl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataStorage extends SQLiteOpenHelper
{
	boolean m_bHomeTown;
	public DataStorage(Context context, String DATABASE_NAME, int DATABASE_VERSION, boolean bhometown) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		m_bHomeTown = bhometown;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		CreateTable(new FacebookFriend(), db, Constants.HOMETOWN_TABLE);
		CreateTable(new FacebookFriend(), db, Constants.LOCATION_TABLE);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	private <T> void CreateTable(T tableClass, SQLiteDatabase db, String tableName)
	{
		Field[] fields = tableClass.getClass().getFields();

		String createTableQuery = "CREATE TABLE " + tableName + " (";
		String strComma = "";
		for(int i=0; i<fields.length; i++)
		{    	
			if(i == fields.length - 1)
				strComma = "";
			else
				strComma = ", ";

			Field field = fields[i];
			if(field.getName().equals("id"))
				createTableQuery += field.getName() + " INTEGER";
			else if(field.getType().equals(int.class) || field.getType().equals(long.class))
				createTableQuery += field.getName() + " INTEGER";
			else if(field.getType().equals(String.class))
			{
				String fieldName = field.getName();
				if(fieldName.equalsIgnoreCase("Right"))
					fieldName = "'" + fieldName + "'";
				createTableQuery += fieldName + " TEXT";	    	
			}
			else if(field.getType().equals(LocationClass.class))
			{
				Field[] locationFields = field.getType().getFields();
				String locationColumnAppender = field.getName();

				for(int j=0; j<locationFields.length; j++)
				{
					Field locfeild = locationFields[j];
					
					String strColumnName = locationColumnAppender + "_" + locfeild.getName();
					String innnerComma;
					if(j == locationFields.length - 1 )
						innnerComma = "";
					else
						innnerComma = ", ";				
					

					if(locfeild.getType().equals(int.class))
						createTableQuery += strColumnName + " INTEGER";
					if(locfeild.getType().equals(double.class))
						createTableQuery += strColumnName + " REAL";
					if(locfeild.getType().equals(String.class))
						createTableQuery += strColumnName + " TEXT";
					
					createTableQuery += innnerComma;
				}
			}

			createTableQuery += strComma;
		}
		
		createTableQuery += ", PRIMARY KEY (id, myId)";

		createTableQuery += ")";
		
		Utilities.PrintMessageInConsole("Query : " + createTableQuery );

		db.execSQL(createTableQuery);
	}

	public <T> void Insert(T tableName) throws Exception {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = GetValueToInsertInDB(tableName);

		// Inserting Row
		long rowId = db.insert(Utilities.GetTableName(m_bHomeTown), null, values);
		if(rowId == -1)
		{
			//Error occurred in inserting row
			throw new Exception("Error Occured in inserting row in table : " + Utilities.GetTableName(m_bHomeTown));
		}
		db.close(); // Closing database connection
	}	

	private <T> ContentValues GetValueToInsertInDB(T tableName) throws IllegalArgumentException, IllegalAccessException
	{
		ContentValues values = new ContentValues();
		Field[] fields = tableName.getClass().getFields();

		for(int i=0; i<fields.length; i++)
		{
			Field field = fields[i];
			if(field.getType().equals(int.class))
				values.put(field.getName(), field.getInt(tableName));
			else if(field.getType().equals(String.class))
			{
				Object value = field.get(tableName);
				String strValue = null;
				if(value != null)
						strValue = (String)value;
				values.put(field.getName(), strValue);
			}
			else if(field.getType().equals(LocationClass.class))
			{
				
				Field[] locationFields = field.getType().getFields();
				
				
				String locationColumnAppender = field.getName();
				Object locClassValue = field.get(tableName);
				boolean blocClassValueNull = false;
				if(locClassValue == null)
					blocClassValueNull = true;

				for(int j=0; j<locationFields.length; j++)
				{
					Field locfeild = locationFields[j];
					String fieldName = locationColumnAppender + "_" + locfeild.getName();                    

					if(locfeild.getType().equals(int.class))
					{
						int value = 0;
						if(!blocClassValueNull)
							value = locfeild.getInt(locClassValue);
						values.put(fieldName, value);
					}
					if(locfeild.getType().equals(double.class))
					{
						double value = 0;
						if(!blocClassValueNull)
						{
							
							value = locfeild.getDouble(locClassValue);
						}
						
						values.put(fieldName, value);
					}
					if(locfeild.getType().equals(String.class))
					{
						String value = null;
						if(!blocClassValueNull)
						{
							Object objValue = locfeild.get(locClassValue);
							if(objValue != null)
								value = (String)objValue;
							
						}
						values.put(fieldName, value);
					}
				}
			}
		}

		return values;
	}

	public <T> List<T> Read(T tableName, String friendId, String myId, String where) throws IllegalAccessException, IllegalArgumentException, InstantiationException
	{
		SQLiteDatabase db = this.getReadableDatabase();
		
		String whereQuery = null;
		String[] selectionArgs = null;
		List<T> allRows = new ArrayList<T>();
		
		if(friendId != null && myId != null)
		{
			whereQuery = "id = ? and myId = ?";
			selectionArgs = new String[] {friendId, myId};
		}
		else if(myId != null)
		{
			whereQuery = "myId = ?";
			selectionArgs = new String[] {myId};
		}
		else if(friendId != null)
		{
			whereQuery = "id = ?";
			selectionArgs = new String[] {friendId};
		}
		
		if(where != null)
		{
			if(whereQuery != null)
				whereQuery += " and " + where;
			else
				whereQuery = where;
		}
		
		Cursor cursor = db.query(Utilities.GetTableName(m_bHomeTown), null, whereQuery, selectionArgs, null, null, null);

		// 3. if we got results get the first one
		if (cursor != null)
			cursor.moveToFirst();
		else
		{
			Utilities.PrintMessageInConsole("No friend found with id : " + friendId);
			return null;
		}

		
		Field[] fields = tableName.getClass().getFields();
		int iCount = 0;
		
		while(!cursor.isAfterLast())
		{
			
			T tableRow = (T) tableName.getClass().newInstance();
		
			for(int i=0; i<fields.length; i++)
			{
				Field field = fields[i];
				String columnName = field.getName();
				
				int iColumnIndex = cursor.getColumnIndex(columnName);
	
				if(iColumnIndex >= 0)
				{
					if(field.getType().equals(int.class))
						field.setInt(tableRow, cursor.getInt(iColumnIndex));
					if(field.getType().equals(long.class))
						field.setLong(tableRow, cursor.getLong(iColumnIndex));
					else if(field.getType().equals(String.class))
						field.set(tableRow, cursor.getString(iColumnIndex));				
				}
				else
				{
					String locationColumnAppender = field.getName();
					LocationClass objLocClass = new LocationClass();
					Field[] locFields = objLocClass.getClass().getFields();
					
					for(int j =0; j< locFields.length; j++)
					{
						Field locField = locFields[j];
						columnName = locationColumnAppender + "_" + locField.getName();
						iColumnIndex = cursor.getColumnIndex(columnName);
						
						if(locField.getType().equals(int.class))
							locField.setInt(objLocClass, cursor.getInt(iColumnIndex));
						if(locField.getType().equals(double.class))
							locField.setDouble(objLocClass, cursor.getDouble(iColumnIndex));
						else if(locField.getType().equals(String.class))
							locField.set(objLocClass, cursor.getString(iColumnIndex));					
					}
					
					field.set(tableRow, objLocClass);
				}
				
				
			}			
			
			cursor.moveToNext();
			allRows.add(tableRow);
			iCount++;
			
		}
		
		Utilities.PrintMessageInConsole("Total rows returned : " + iCount);
		cursor.close();
		db.close();
		return allRows;

	}
	
	public <T> void Update(T tableName, String friendId, String myId) throws Exception 
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = GetValueToInsertInDB(tableName);
		
		String whereQuery = "id = ? and myId = ?";
		String[] selectionArgs = new String[] {friendId, myId};
		List<T> allRows = new ArrayList<T>();
	

		// Inserting Row
		long rowId = db.update(Utilities.GetTableName(m_bHomeTown), values, whereQuery, selectionArgs);
		if(rowId == -1)
		{
			//Error occurred in inserting row
			throw new Exception("Error Occured in inserting row in table : " + Utilities.GetTableName(m_bHomeTown));
		}
		db.close(); // Closing database connection
	}
}
