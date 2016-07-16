using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Linq;
using Microsoft.Advertising.Mobile.UI;

namespace SentiApp.Common
{
    class Utlities
    {
        public static string GetValuesFromAppXMLFile(string attribute)
        {
            return XDocument.Load(Constants.APP_MANIFEST_FILE).Root.Element(Constants.APP_ROOT_NODE).Attribute(attribute).Value;
        }
    }

    public class AdCentre
    {
        public const string adUnitId = "146843";
        public const string pubcentreAppID = "62c5d55f-fca9-47eb-b340-e885bb8ae4ca";
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

    class Constants
    {
        public static string FB_SENTIAPP_APPLICATION_ID = "569533189768276";
        public static string FB_LOGGING_IN = "Logging in";
        public static string FB_LOGGING_OUT = "Logging out";
        public static string FB_RETRIEVE_MESSAGES = "Retrieving Facebook posts";
        public static string SENTIGEM_ANALYSE_MESSAGES = "Analysing Facebook posts";
        public const string APP_MANIFEST_FILE = "WMAppManifest.xml";
        public const string APP_ROOT_NODE = "App";
        public const string APP_VERSION = "Version";
        public const string APP_AUTHOR = "Author";
        public const string MY_EMAIL = "mailto:ibnipun10@gmail.com";
        public const string APPLICATION_NAME = "SentiApp";
        public const string EMAIL_SUBJECT = "SentiApp Feedback";

        //Setiments
        public static string SENTIGEM_API_KEY = "351a91d593f0e15d3831854f8b0af029Y70nfpROtJlmkbxS3_NqHEVhC4sP8Xwy";
        public static string SENTIMENT_HAPPY = "Happy";
        public static string SENTIMENT_SAD = "Sad";
        public static string SENTIMENT_NEUTRAL = "Neutral";
        public static string SUBMIT_BUTTON_SUBMIT = "Submit";
        public static string SUBMIT_BUTTON_CANCEL = "Cancel";

        public static string MAINPAGE_MESSAGE = "This app allows you to measure sentiments by analysing facebook posts for the past number of days entered.";

        public static string ABOUT_TEXT = "This is a fun app which finds out your mood(sad, neutral or happy).\n\nIt extracts past 'x' days user posts from facebook and analyse them using free web service provided by http://sentigem.com";
    }
}
