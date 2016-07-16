package BackStage;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import Common.PhysioITTables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;


public class DataStorage extends SQLiteOpenHelper 
{

	public DataStorage(Context context, String DATABASE_NAME, int DATABASE_VERSION) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		CreateTable(new PhysioITTables.BodyPartDB(), db);
		CreateTable(new PhysioITTables.DeviceDB(), db);
		CreateTable(new PhysioITTables.ExcerciseDB(), db);
		CreateTable(new PhysioITTables.FeedbackDB(), db);
		CreateTable(new PhysioITTables.PostureDB(), db);
		CreateTable(new PhysioITTables.ReasonsDB(), db);
		CreateTable(new PhysioITTables.RemedyDB(), db);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	private <T> void CreateTable(T tableName, SQLiteDatabase db)
	{
		Field[] fields = tableName.getClass().getFields();
		
		String createTableQuery = "CREATE TABLE " + tableName.getClass().getSimpleName() + " (";
		String strComma = "";
	    for(int i=0; i<fields.length; i++)
	    {    	
	    	if(i == fields.length - 1)
	    		strComma = "";
	    	else
	    		strComma = ", ";
	    	
	    	Field field = fields[i];
	    	if(field.getName().equals("id"))
	    		createTableQuery += field.getName() + " INTEGER PRIMARY KEY";
	    	else if(field.getType().equals(int.class))
	    		createTableQuery += field.getName() + " INTEGER";
	    	else if(field.getType().equals(String.class))
	    	{
	    		String fieldName = field.getName();
	    		if(fieldName.equalsIgnoreCase("Right"))
	    			fieldName = "'" + fieldName + "'";
	    		createTableQuery += fieldName + " TEXT";	    	
	    	}
	    	else if(field.getType().equals(Date.class))
	    	{
	    		createTableQuery +=  field.getName() + " INTEGER";
	    	}
	    	
	    	createTableQuery += strComma;
	    }
	    
	    createTableQuery += ")";
	    
	    db.execSQL(createTableQuery);
	}

	public <T> void Insert(T tableName) throws Exception {
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = GetValueToInsertInDB(tableName);
	    
	    // Inserting Row
	    long rowId = db.insert(tableName.getClass().getSimpleName(), null, values);
	    if(rowId == -1)
	    {
	    	//Error occurred in inserting row
            throw new Exception("Error Occured in inserting row in table : " + tableName.getClass().getSimpleName());
	    }
	   // db.close(); // Closing database connection
	}	
	
	private String GenerateWhereQuery(Dictionary<String, Integer> whereArgs, AtomicReference<String[]> selectionArgs)
	{
		List<String> strwhere = new ArrayList<String>();
		String whereQuery = "";
		
		Enumeration<String> keys =  whereArgs.keys();
		String strAnd = "AND";
		while(keys.hasMoreElements())
		{
			String column = keys.nextElement();
			if(!keys.hasMoreElements())
                strAnd = "";

			strwhere.add(String.valueOf(whereArgs.get(column).intValue()));
			whereQuery += " " + column + "=? " + strAnd;

		}		
		
		selectionArgs.set(strwhere.toArray(new String[strwhere.size()]));
		
		return whereQuery;
		
	}
	
	public <T> List<T> Read(T tableName, Dictionary<String, Integer> whereArgs) throws InstantiationException, IllegalAccessException
	{
		SQLiteDatabase db = this.getReadableDatabase();
		String whereQuery = null;
		AtomicReference<String[]> selectionArgs  = new AtomicReference<String[]>(null);
		List<T> allRows = new ArrayList<T>();
		
		if(whereArgs != null)
			whereQuery = GenerateWhereQuery(whereArgs, selectionArgs);
		
		Cursor cursor = db.query(tableName.getClass().getSimpleName(), null, whereQuery, selectionArgs.get(), null, null, null);
		
		cursor.moveToFirst();
		Field[] fields = tableName.getClass().getFields();
		
		while(!cursor.isAfterLast())
		{
			T tableRow = (T) tableName.getClass().newInstance();
			
			for(int i=0; i<fields.length; i++)
			{
				Field field = fields[i];
				int iColumnIndex = cursor.getColumnIndex(field.getName());
				
				if(field.getType().equals(int.class))
                {
                    if(field.getName().equalsIgnoreCase("id"))
                        field.setInt(tableRow, 0);
                    else
					    field.setInt(tableRow, cursor.getInt(iColumnIndex));
                }
				else if(field.getType().equals(String.class))
					field.set(tableRow, cursor.getString(iColumnIndex));
				else if(field.getType().equals(Date.class))
					field.set(tableRow, new Date(cursor.getLong(iColumnIndex)));
				
			}
			cursor.moveToNext();
			allRows.add(tableRow);
		}
		
		cursor.close();
		//db.close(); // Closing database connection
		return allRows;
	}
	
	public <T> void Delete(T tableName, Dictionary<String, Integer> whereArgs)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String whereQuery = null;
        AtomicReference<String[]> selectionArgs = new AtomicReference<String[]>(null);
		
		if(whereArgs != null)
			whereQuery = GenerateWhereQuery(whereArgs, selectionArgs);

		int nRowsAffected= db.delete(tableName.getClass().getSimpleName(), whereQuery, selectionArgs.get());
		
		//db.close(); // Closing database connection
	}
	
	private <T> ContentValues GetValueToInsertInDB(T tableName) throws IllegalArgumentException, IllegalAccessException
	{
		ContentValues values = new ContentValues();
		Field[] fields = tableName.getClass().getFields();
		
		for(int i=0; i<fields.length; i++)
		{
			Field field = fields[i];
			if(field.getType().equals(int.class))
	    	{	    		
	    		if(!field.getName().equalsIgnoreCase("id"))
	    			values.put(field.getName(), field.getInt(tableName));
	    	}
	    		else if(field.getType().equals(String.class))
	    			values.put(field.getName(), (String)(field.get(tableName)));
	    		else if(field.getType().equals(Date.class))
	    		{
	    			Object objTimeStamp = field.get(tableName);
	    			long ltime;
	    			if(objTimeStamp == null)
	    				ltime = (new Date()).getTime();
	    			else
                    {
                            Date date = (Date)objTimeStamp;
	    					ltime = date.getTime();
                    }
	    			values.put(field.getName(), ltime);
	    		}
	    }
		
		return values;
	}
	
	public <T> void Update(T tableName, Dictionary<String, Integer> whereArgs) throws IllegalArgumentException, IllegalAccessException
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String whereQuery = null;
        AtomicReference<String[]> selectionArgs = new AtomicReference<String[]>(null);
		
		if(whereArgs != null)
			whereQuery = GenerateWhereQuery(whereArgs, selectionArgs);

		ContentValues values = GetValueToInsertInDB(tableName);
		
		int nRowsAffected = db.update(tableName.getClass().getSimpleName(), values, whereQuery, selectionArgs.get());
		//db.close(); // Closing database connection
	}
}


	