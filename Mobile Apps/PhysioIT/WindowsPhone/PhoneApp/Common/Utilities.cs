using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using PhoneApp.Presentation;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows;
using System.Xml.Linq;
using System.Reflection;
using PhoneApp.BackStage;
using Microsoft.Phone.Info;
using System.IO.IsolatedStorage;
using CommonBackstage;
using Windows.Storage;
using System.IO;
using System.Threading;
using Microsoft.Advertising.Mobile.UI;
using Microsoft.Phone.Shell;
using Microsoft.Phone.Net.NetworkInformation;

namespace PhoneApp.Common
{

    public static class Font
    {
        public static void TitleFont(TextBlock txtblock)
        {
            txtblock.FontWeight = FontWeights.ExtraBold;
            txtblock.FontSize = 35;
            txtblock.Foreground = new SolidColorBrush(Colors.Green);
            txtblock.TextWrapping = TextWrapping.Wrap;
        }

        public static void DescriptionFont(TextBlock txtblock)
        {
            txtblock.FontWeight = FontWeights.SemiBold;
            txtblock.FontSize = 25;
            txtblock.TextWrapping = TextWrapping.Wrap;
        }

        public static void ImageLook(Image img)
        {
            img.Margin = new Thickness(20);
            img.Height = 300;
        }

        public static void SetFontColor(TextBlock control, string foregroundcolor)
        {
                control.Foreground = GetColorFromHexa(foregroundcolor);
        }

        public static SolidColorBrush GetColorFromHexa(string hexaColor)
        {
            return new SolidColorBrush(
            Color.FromArgb(
                Convert.ToByte(hexaColor.Substring(1, 2), 16),
                Convert.ToByte(hexaColor.Substring(3, 2), 16),
                Convert.ToByte(hexaColor.Substring(5, 2), 16),
                Convert.ToByte(hexaColor.Substring(7, 2), 16)));
        }

        public static void SetFont(TextBlock control, string text, string foregroundcolor, string fontfamily, FontWeight weight, int size)
        {
            control.Text = text;
            Font.SetFontColor(control, foregroundcolor);
            control.FontWeight = weight;
            control.FontSize = size;
            control.FontFamily = new FontFamily(fontfamily);
        }


    }

    public class AdCentre
    {
        public const string adUnitId = "136593";
        public const string pubcentreAppID = "ccd568de-634f-408b-9a2b-273d5308365d";
        //public const string testadunitID = "Image480_80";
        //public const string testapplicationid = "test_client";

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
    public static class Constants
    {
        public const string PhysioITdatabase = "physioITdb";
        public const string BODYPARTHUBTILEGRP = "BodyPartsHubTileGrp";
        public const string POSTURE_WRONG_TEXT = "Wrong Posture";
        public const string POSTURE_RIGHT_TEXT = "Right Posture";
        public const string POSTURE_DESCRIPTION = "Description";
        public const string POSTURE_DOSAGE = "Dosage";
        public const string POSTURE_PRECAUTIONS = "Precautions";
        public const string APPLICATION_TILE = "MY PHYSIO";
        public const string REASONS_NAME = "Reasons";
        public const string REMEDY_NAME = "Remedy";
        public const string COMMON_PROBLEMS_NAME = "Problems";
        public const string COMMON_PRECAUTIONS_NAME = "Precautions";
        public const string PHYSIOITXMLPATH = "PhysioData.xml";
        public const string CRASH_LOG_FILE = "CrashLog.txt";
        public const string IMAGES_PATH = "Images";
        public const string IMAGE_BODYPART = "BodyParts";
        public const string APP_MANIFEST_FILE = "WMAppManifest.xml";
        public const string APP_VERSION = "Version";
        public const string APP_AUTHOR = "Author";
        public const string APP_TITILE = "Title";
        public const string EXCERCISE_TITLE = "Exercises";
        public const string APP_ROOT_NODE = "App";
        public const string ABOUT_DISCLAIMER_BUTTON = "Disclaimer";
        public const string ABOUT_TEXT = "I often meet people who tend to complain about pain due to several reasons like prolong working hours, poor posture and many more. This app will help them in preventing and alleviating various body pains by identifying reasons and then providing light exercises.\n\nI would also like to thank my wife for providing me content for the app who is a physiotherapist";
        public const string DISCLAIMER_TEXT = "This app contains information about physiotherapy common body pains and exercises. It is an informational service and for reference only. It is not tailored to individual circumstances and is not intended to replace the advice of a healthcare professional. Only your doctor can determine whether a specific exercise is appropriate for your circumstances.\n\n To the full extent permitted by law I disclaims all liability and does not accept or assume responsibility to you or anyone else arising from any reliance placed on this information service by any user of the app, or by anyone who may be informed of any of its contents. If you have any questions regarding any information contained on this app or regarding the suitability of this app for you, you should consult your doctor.\n\n Also all images contained in this app are freely available on internet found using image search engines";
        public const int MAX_ATTEMPTS = 10;
        public const int MAX_ROWS = 100;
        public const char CHARACTER_SEPERATOR = ';';

        //Settings Application Data
        public const string SETTINGS_TEXT = "settings";
        public const string SETTINGS_LOCATION_SERVICE = "LocationService";
        public const string SETTINGS_AZURE_UPDATE = "UpdateService";
        public const string SETTINGS_YES = "1";
        public const string SETTINGS_NO = "0";

        //Map
        public const string MAP_PHYSIOTHERAPY_CLINIC = "Physiotherapy clinic";
        public const string PHYSIOIT_MAP_SERVICE_APP_ID = "c7a76a80-c2cf-4287-adf6-fb54d7856e09";
        public const string PHYSIOIT_MAP_SERVICE_AUTH_TOKEN_ID = "Z7bl63mRQWJQc4AX-JkMzQ";

        //Application bar
        public const int APPLICATIONBAR_ICON_CLINIC = 1;
        public const int APPLICATIONBAR_ICON_CHAT = 3;

        public const string SETTINGS_LOCATION_PRIVACY_TEXT = @"This application uses your current location for mapping services. You may disable this service any time";
        public const string SETTINGS_DATA_PRIVACY_TEXT = @"This application uses your data network to update its content. You may disable this service any time";
    
        //About, Disclaimer, Version
         public static readonly string[,] VERSION_DESC = new string[5,2]
            { {"1.0.0.0", "Problems and Precautions.\nFive body parts Reasons and Remedies.\nPosture correction and exercises.\nContent update using Azure cloud."},
              {"2.0.0.0", "Enhanced UI.\nMap showing Physiotherapy clinics nearby.\nSettings to enable/disable services"} ,
              {"2.0.1.0", "Enhanced UI.\nFacebook integration.\nNew icons"},
              {"2.0.1.1", "Minor facebook Login button sign in fix"},
              {"2.0.1.2", "Removed Like button"}};
         public const string ABOUT_VERSION_TITLE = "version";

        //Facebook Integration
        public static string FB_LOGING_TEXT = "In order to start or participate in a discussion, please login to your facebook account";
        public static string FB_LIKE_PAGE = "Ask, discuss and share your thoughts on any physiotherapy related matter. Please click the like button at the bottom to like PhysioIT facebook page";
        public static string FB_LOGGING_IN = "Logging in";
        public static string FB_LOGGING_OUT = "Logging out";
        public static string FB_RETRIEVE_MESSAGES = "Geting messages from facebook page";
        public static string FB_RETRIEVE_COMMENTS = "Geting comments from facebook page";
        public static string FB_POST_MESSAGE = "Posting message on facebook page";
        public static string FB_POST_COMMENT = "Posting comment on facebook page";
        public static string FB_PAGE_LIKE = "Checking page like";
        public static string FB_EMPTY_TEXT_ERROR = "Cannot send blank text";
        public static string FB_PHYSIOIT_APPLICATION_ID = "200620433448270";

    }

    public class TableInteractor
    {
        private XDocument xmlData;
        private const string XMLPARTS = "Part";
        public const string XML_DATABASE = "Database";
        public const string XML_DATABASE_VERSION = "VersionId";

        public TableInteractor()
        {
            xmlData = XDocument.Load(Constants.PHYSIOITXMLPATH);
        }

        public List<T> PopulateTableFromXML<T>() where T : new()
        {
            List<T> lbdb = new List<T>();
            PropertyInfo[] property = typeof(T).GetProperties();

            string rootNode = typeof(T).Name;
            XElement dbPart = xmlData.Descendants(rootNode).Single();

            foreach (XElement value in dbPart.Descendants(XMLPARTS))
            {
                T bp = new T();

                foreach (PropertyInfo pt in property)
                {

                    if ((string)value.Element(pt.Name) == null)
                        pt.SetValue(bp, null);
                    else
                    {
                        string text = value.Element(pt.Name).Value;
                        if (string.IsNullOrEmpty(text))
                            text = "";
                        if (pt.PropertyType == typeof(int))
                            pt.SetValue(bp, Convert.ToInt32(text));
                        else
                            pt.SetValue(bp, text);
                    }
                }

                lbdb.Add(bp);
            }

            return lbdb;

        }

        public DeviceDB PopulateDeviceDB()
        {
            DeviceDB device = new DeviceDB();
            device.XMLVersion = GetXMLVersionId().ToString();

            device.DeviceID = Convert.ToBase64String((byte[])DeviceExtendedProperties.GetValue("DeviceUniqueId"));
            device.DeviceName = DeviceStatus.DeviceName;

            //Still not implement package so not using it
            //device.AppVersion = Windows.ApplicationModel.Package.Current.Id.Version.ToString();
            device.AppVersion = ApplicationSetting.GetValuesFromAppXMLFile(Constants.APP_VERSION);

            return device;
        }

        public int GetXMLVersionId()
        {
            XElement database = xmlData.Descendants(XML_DATABASE).Single();
            int versionId = Convert.ToInt32(database.Attribute(XML_DATABASE_VERSION).Value);
            return versionId;
        }

        public void SetXMLVersionId(int versionId)
        {

        }
    }

    public class CrashLog
    {
        private static Semaphore semaphore = new Semaphore(1, 1);
        public static void WriteCrashLog(Exception ex)
        {
            /*
            string path = System.IO.Path.Combine(ApplicationData.Current.LocalFolder.Path, Constants.CRASH_LOG_FILE);
            IsolatedStorageFile store = IsolatedStorageFile.GetUserStoreForApplication();            

            semaphore.WaitOne();
            
            IsolatedStorageFileStream fs = store.OpenFile(path, System.IO.FileMode.Append);

            using (StreamWriter sw = new StreamWriter(fs))
            {
                string errorString = DateTime.Now.ToLocalTime().ToString() + " | " + "Message: " + ex.Message + "Stack Trace: " + ex.StackTrace;
                sw.WriteLine(errorString);
                sw.Close(); 
            }

            fs.Close();
            semaphore.Release();
            */
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

        public static string GetValuesFromAppXMLFile(string attribute)
        {
            return XDocument.Load(Constants.APP_MANIFEST_FILE).Root.Element(Constants.APP_ROOT_NODE).Attribute(attribute).Value;
        }

       

    }

    public class PhoneSettings
    {
        public static bool IsPhonethemeDark()
        {
            bool btheme = false;
            Visibility dbgisibility = (Visibility)Application.Current.Resources["PhoneDarkThemeVisibility"];
            if (dbgisibility == Visibility.Visible)
                btheme = true;

            return btheme;
               
        }

        public static bool IsDeviceNetwork()
        {
            if (DeviceNetworkInformation.IsCellularDataEnabled || DeviceNetworkInformation.IsWiFiEnabled)
                return true;
            else
                return false;
        }

    }
}
