using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Linq;
using Microsoft.Advertising.Mobile.UI;
using System.Device.Location;
using Windows.Devices.Geolocation;
using System.IO.IsolatedStorage;

namespace MyBuddyMap.Common
{
    class Utlities
    {
        public static string GetValuesFromAppXMLFile(string attribute)
        {
            return XDocument.Load(Constants.APP_MANIFEST_FILE).Root.Element(Constants.APP_ROOT_NODE).Attribute(attribute).Value;
        }

        public static GeoCoordinate ConvertGeocoordinate(Geocoordinate geocoordinate)
        {
            return new GeoCoordinate
                (
                geocoordinate.Latitude,
                geocoordinate.Longitude,
                geocoordinate.Altitude ?? Double.NaN,
                geocoordinate.Accuracy,
                geocoordinate.AltitudeAccuracy ?? Double.NaN,
                geocoordinate.Speed ?? Double.NaN,
                geocoordinate.Heading ?? Double.NaN
                );
        }
    }

    public class ApplicationSetting
    {
        public static string ReadApplicationSetting(string key)
        {
            string value = null;
            if (IsolatedStorageSettings.ApplicationSettings.Contains(key))
                value = IsolatedStorageSettings.ApplicationSettings[key] as string;
            
            return value;
        }

        private static void WriteApplicationSetting(string key, string value, IsolatedStorageSettings settings)
        {
            if (settings.Contains(key))
            {
                settings[key] = value;
                settings.Save();
            }
        }

        public static void RemoveApplicationSetting(string key)
        {
            if (IsolatedStorageSettings.ApplicationSettings.Contains(key))
                IsolatedStorageSettings.ApplicationSettings.Remove(key);
        }

        public static void AddApplicationSetting(string key, string value)
        {
            IsolatedStorageSettings settings = IsolatedStorageSettings.ApplicationSettings;
            if (!settings.Contains(key))
            {
                settings.Add(key, value);
                settings.Save();
            }
            else
            {
                WriteApplicationSetting(key, value, settings);
            }
        }   

    }

    public class AdCentre
    {
        public const string adUnitId = "148780";
        public const string pubcentreAppID = "10b05b33-66af-45a5-9f04-2e431d23d195";
       // public const string adUnitId = "Image480_80";
       // public const string pubcentreAppID = "test_client";

        public static void SetAddControl(AdControl adcontrol)
        {
            adcontrol.AdUnitId = adUnitId;
            adcontrol.ApplicationId = pubcentreAppID;
            adcontrol.IsAutoRefreshEnabled = true;
            adcontrol.Height = 80;
            adcontrol.Width = 480;
            adcontrol.IsAutoCollapseEnabled = true;
            
        }

       
    }

    class Constants
    {
        public static string FB_MYBUDDYAPP_APPLICATION_ID = "247086835438412";
        public static string FB_LOGGING_IN = "Logging in";
        public static string FB_LOGGING_OUT = "Logging out";
        public static string FB_RETRIEVE_MESSAGES = "Retrieving Facebook posts";
        public static string SENTIGEM_ANALYSE_MESSAGES = "Analysing Facebook posts";
        public const string APP_MANIFEST_FILE = "WMAppManifest.xml";
        public const string APP_ROOT_NODE = "App";
        public const string APP_VERSION = "Version";
        public const string APP_AUTHOR = "Author";
        public const string MY_EMAIL = "mailto:ibnipun10@gmail.com";
        public const string APPLICATION_NAME = "MyBuddyMap";
        public const string MYBUDDY_PAGE_TITLE = "{0} friend(s)";
        public const string EMAIL_SUBJECT = "SentiApp Feedback";


        public static string MAINPAGE_MESSAGE = "This app will mark your facebook friends on the world map as per their current location or hometown. Tapping on the mark will show you the list of friends in that location";

        public static string ABOUT_TEXT = "This is a utility app which marks your facebook friends on the world map. Based on the current location or hometown as updated by your friends on facebook, it will mark them on a map. You can also tap on the mark to see the list of friends in that location. To see your location on map, please enable location service in your phone settings";
        public static string PROGRESS_TEXT = "{0} out of {1} friends mapped"; 

        //MAP
        public static string MAP_APPLICATION_ID = "ee10d1b0-4e42-40c3-9437-9fe3df2be49b";
        public static string MAP_AUTHENTICATION_ID = "krojl4D-ZOi6KCPiluXF-Q";

        //Settings Application Data
        public const string SETTINGS_TEXT = "settings";
        public const string SETTINGS_LOCATION_SERVICE = "LocationService";
        public const string SETTINGS_AZURE_UPDATE = "UpdateService";
        public const string SETTINGS_YES = "1";
        public const string SETTINGS_NO = "0";
        public const string SETTINGS_LOCATION_PRIVACY_TEXT = @"This application uses your current location for mapping services. You may disable this service any time";
        
    
    }
}
