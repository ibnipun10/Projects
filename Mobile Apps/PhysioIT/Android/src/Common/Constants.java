package Common;

public class Constants {
	public static final  String MOBILE_SERVICE_URL = "https://physioitsservice.azure-mobile.net/";
	public static final  String MOBILE_SERVICE_KEY = "qUtgocMuHJkYLvOhXwFUQhuhSOimKf69";
	public static final  String PhysioITdatabase = "physioITdb";
    public static final  int DATABASE_VERSION_ID = 1;
    public static final  String BODYPARTHUBTILEGRP = "BodyPartsHubTileGrp";
    public static final  String POSTURE_WRONG_TEXT = "Wrong Posture";
    public static final  String POSTURE_RIGHT_TEXT = "Right Posture";
    public static final  String POSTURE_DESCRIPTION = "Description";
    public static final  String POSTURE_DOSAGE = "Dosage";
    public static final  String POSTURE_PRECAUTIONS = "Precautions";
    public static final  String APPLICATION_TILE = "MY PHYSIO";
    public static final  String APPLICATION_NAME = "PhysioIT";
    public static final  String REASONS_NAME = "Reasons";
    public static final  String REMEDY_NAME = "Remedy";
    public static final  String COMMON_PROBLEMS_NAME = "Problems";
    public static final  String COMMON_PRECAUTIONS_NAME = "Precautions";
    public static final  String PHYSIOITXMLPATH = "PhysioData.xml";
    public static final  String CRASH_LOG_FILE = "CrashLog.txt";
    public static final  String IMAGES_PATH = "Images";
    public static final  String IMAGE_BODYPART = "BodyParts";
    public static final  String IMAGE_APP_BAR_ICONS = "appBarIcons";
    public static final  String APP_MANIFEST_FILE = "WMAppManifest.xml";
    public static final  String APP_VERSION = "Version";
    public static final  String APP_AUTHOR = "Author";
    public static final  String APP_TITILE = "Title";
    public static final  String EXCERCISE_TITLE = "Exercises";
    public static final  String POSTURE_TITLE = "Posture";
    public static final  String APP_ROOT_NODE = "App";
    public static final  String ABOUT_RATE_BUTTON = "Rate it";
    public static final  String XMLPARTS = "Part";
    public static final  String XML_DATABASE = "Database";
    public static final  String XML_DATABASE_VERSION = "VersionId";
    public static final  String ABOUT_TEXT = "I often meet people who tend to complain about pain due to several reasons like prolong working hours, poor posture and many more. This app will help them in preventing and alleviating various body pains by identifying reasons and then providing light exercises.\n\nI would also like to thank my wife for providing me content for the app who is a physiotherapist";
    public static final  String DISCLAIMER_TEXT = "This app contains information about physiotherapy common body pains and exercises. It is an informational service and for reference only. It is not tailored to individual circumstances and is not intended to replace the advice of a healthcare professional. Only your doctor can determine whether a specific exercise is appropriate for your circumstances.\n\nTo the full extent permitted by law I disclaims all liability and does not accept or assume responsibility to you or anyone else arising from any reliance placed on this information service by any user of the app, or by anyone who may be informed of any of its contents. If you have any questions regarding any information contained on this app or regarding the suitability of this app for you, you should consult your doctor.\n\nAlso all images contained in this app are freely available on internet found using image search engines";
    public static final  int MAX_ATTEMPTS = 10;
    public static final  int MAX_ROWS = 100;
    public static final  String COLUMN_PARTID = "PartID";
    public static final  String EXERCISE_TITLE = "ExerciseTitle";
    public static final  String COLUMN_NAME = "Name";
    public static final String COLUMN_REMEDYID = "RemedyId";
    public static final String COLUMN_REASONSID = "ReasonsId";
    public static final String COLUMN_EXERCISEID = "ExcerciseId";
    public static final String COLUMN_POSTUREID = "PostureId";
    public static final String IMAGES_ABOUT = "About.png";
    public static final String IMAGES_FEEDBACK = "Feedback.png";
    public static final String IMAGES_CLINIC = "Clinic.png";
    public static final String IMAGES_CLINIC_DISABLED = "DisableMap.png";
    public static final String ABOUT_PAGE_TITLE = "About";
    public static final String FEEDBACK_PAGE_TITLE = "Feedback";
    public static final String DISCLAIMER_PAGE_TITLE = "Disclaimer";
    public static final String SUBMIT_BUTTON = "Submit";
    public static final String TAP_ON_TITLE_MESSAGE = "Tap on the title to know more";
    public static final String CHARACTER_SEPERATOR = ";";
    public static final int REASON_PRECAUTION_ID = 1001;

    //Feedback page
    public static final String FEEDBACK_PAGE_LABEL_NAME = "Name";

    public static final String FEEDBACK_PAGE_LABEL_EMAIL = "Email";
    public static final String FEEDBACK_PAGE_LABEL_SUGGESTIONS = "Suggestions(*)";
    public static final String FEEDBACK_PAGE_BLANK_ERROR = "Help me to make PhysioIT better. Plz suggests";
    public static final String FEEDBACK_PAGE_SENDING = "Sending Feedback....";
    public static final String FEEDBACK_PAGE_SENT = "FeedBack Sent :)";

    //Status bar Update
    public static final String STATUS_BAR_UPDATE_TABLES = "Updating tables";
    public static final String STATUS_BAR_PHYSIOIT_UPDATE = "PhysioIT update";
    public static final String STATUS_BAR_PERCENTAGE_COMPLETE = " % Complete";
    public static final String STATUS_BAR_UPDATE_DESCRIPTION = "Updating Content";
    public static final String STATUS_BAR_UPDATE_COMPLETE = "Content Update Complete";
    public static final int STATUS_BAR_ERROR_CODE = 100;
    public static final String STATUS_BAR_ERROR_DESC = "Error updating retrying after 10 sec ...";
    public static final String STATUS_BAR_DEVICEINFO_UPDATE = "Sending device info...";
    public static final String STATUS_BAR_NO_INTERNET_CONNECTION = "Unable to Connect";

    public static final String[][] VERSION_DESC =
            { {"1.0.0.0", "Problems and Precautions.\nFive body parts Reasons and Remedies.\nPosture correction and exercises.\nContent update using Azure cloud."},
              {"2.0.0.0", "Enhanced UI.\nMap showing Physiotherapy clinics nearby.\nSettings to enable/disable services"},
              {"2.0.0.1", "Map updates" }};

    //Error text
    public static final String ERROR_DEVICE_INFORMATION_NOT_FOUND_LOCAL = "Device information not found in local database";
    public static final String ERROR_DEVICE_INFORMATION_NOT_FOUND_AZURE = "Device information not found in Azure database";

    public static final int SECONDS_TO_WAIT = 20;
    public static final String FEEDBACK_MESSAGE_ID = "FBMessage";

    public static final String APP_PUBLISHER_ID = "a15222db950e0c1";
    public static final String TEST_DEVICE_ID = "4afaffe6c1df3eb3";

    //Maps
    public static final String MAP_PHYSIOTHERAPY_CLINIC = "physiotherapist";
    public static final String MAP_PLACES_ACCESS_KEY = "AIzaSyCk3iConJumKu73j-0LHvD22Wrfyl1YrUg";

    //Settings Application Data
    public static final String SETTINGS_TEXT = "Settings";
    public static final String SETTINGS_LOCATION_SERVICE = "LocationService";
    public static final String SETTINGS_AZURE_UPDATE = "UpdateService";
    public static final String SETTINGS_YES = "1";
    public static final String SETTINGS_NO = "0";
    public static final String SETTINGS_PRIVACYCONTROL_TITLE = "Privacy Control";
    public static final String SETTINGS_LOCATION_PRIVACY_TEXT = "This application uses your current location for mapping services. You may disable this service any time";
    public static final String SETTINGS_DATA_PRIVACY_TEXT = "This application uses your data network to update its content. You may disable this service any time";
    public static final String SETTINGS_PRIVACYCONTROL_KEY = "prefCategory";
    public static final String SETTINGS_AZURESERVICE_KEY = "prefSwitchAzureService" ;
    public static final String SETTINGS_LOCATIONSERVICE_KEY = "prefSwitchLocationService";

}
