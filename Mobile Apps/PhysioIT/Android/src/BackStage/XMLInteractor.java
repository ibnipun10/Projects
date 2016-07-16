package BackStage;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.*;

import Common.Constants;
import android.util.Xml;
import android.content.res.AssetManager;
import android.app.Activity;

public class XMLInteractor {
	
	public static int XMLVersion;

	public static  <T> List<T> PopulateFromXML(Activity activityclass, T objtable) throws Exception
	{
		XmlPullParser parser = Xml.newPullParser();
		List<T> ltableValues = new ArrayList<T>();
		
		AssetManager am = activityclass.getAssets();
		parser.setInput(am.open(Constants.PHYSIOITXMLPATH), null);
	
		int eventType = parser.getEventType();
		String className = objtable.getClass().getSimpleName();
		boolean done = false;
		
		while(eventType != XmlPullParser.END_DOCUMENT && !done)
		{
			String name = null;
			switch(eventType)
			{
			case XmlPullParser.START_DOCUMENT : 
				break;
			case XmlPullParser.START_TAG :
				name = parser.getName();
				if(name.equalsIgnoreCase(className))
				{
					parser.next();
					ltableValues = PopulatePartsFromXML(parser, className, objtable);
					done = true;
				}
				if(name.equalsIgnoreCase(Constants.XML_DATABASE))
				{
					XMLVersion = Integer.parseInt(parser.getAttributeValue(null, Constants.XML_DATABASE_VERSION));
				}
				break;
			
			}
			
			eventType = parser.next();
		
		}
		return ltableValues;
		
	}
	
	private static <T> List<T> PopulatePartsFromXML(XmlPullParser parser, String className, T objTable) throws Exception
	{
		int eventtype = parser.getEventType();
		List<T> partsDB = new ArrayList<T>();
		
		while(eventtype != XmlPullParser.END_DOCUMENT)
		{
			String name = null;
			switch(eventtype)
			{
			case XmlPullParser.START_TAG :
				name = parser.getName();
				if(name.equalsIgnoreCase(Constants.XMLPARTS))
				{
					parser.next();					
					T part = PopulatePartFromXML(parser, className, objTable);
					partsDB.add(part);
				}
				break;
			case XmlPullParser.END_TAG :
				name = parser.getName();
				if(name.equalsIgnoreCase(className))
					return partsDB;
				break;
			}
			
			eventtype = parser.next();
		}
		
		throw new Exception(className + " xml element not found");
	}
	
	private static <T> T PopulatePartFromXML(XmlPullParser parser, String className, T objTable) throws XmlPullParserException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException
	{
		int eventtype = parser.getEventType();
		T part =  (T) objTable.getClass().newInstance();
		Field[] fields = part.getClass().getFields();
		
		while(eventtype != XmlPullParser.END_DOCUMENT)
		{
			String name = null;
			switch(eventtype)
			{
			case XmlPullParser.START_TAG :
				name = parser.getName();
				for(int i=0; i< fields.length; i++)
				{
					if(name.equalsIgnoreCase(fields[i].getName()))
					{
						String value = parser.nextText();
						if(value.isEmpty() || value == null)
							value = "";
						if(fields[i].getType().equals(int.class))
							fields[i].set(part, Integer.parseInt(value));
						else
							fields[i].set(part, value);
						break;
					}				
				}
				break;
			case XmlPullParser.END_TAG :
				name = parser.getName();
				if(name.equalsIgnoreCase(Constants.XMLPARTS))
				{
					return part;
				}
				break;
			}
			eventtype = parser.next();
		}
		
		return null;
	}
	
	
}
