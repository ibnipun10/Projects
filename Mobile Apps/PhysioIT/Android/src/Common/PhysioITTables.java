package Common;

import java.util.Date;



public class PhysioITTables {


    public static class FeedbackDB
    {
        public int id;
        public String Name;
        public String Email;
        public String Comments;
        public String DeviceId;
        public Date TimeStamp;
    }
    public static class DeviceDB
    {
        public int id;
        public String DeviceID;
        public String DeviceName;
        public String AppVersion;
        public String XMLVersion;
        public Date TimeStamp;
    }

    public static class BodyPartDB
    {
        public int id;
        public int PartID;
        public String Name;
        public String Image;
        public String Message;
        public Date TimeStamp;
    }

    
    public static class ReasonsDB
    {
        public int id;
        public int PartID;
        public int ReasonsId;
        public String Title;
        public String Description;
        public String Image;
        public Date TimeStamp;
    }

    public static class RemedyDB
    {

        public int id;
        public int PartID;
        public int RemedyId;
        public String Title;
        public String Description;
        public Date TimeStamp;
    }

    public static class PostureDB
    {
        public int id;
        public int PartID;
        public int RemedyId;
        public int PostureId;
        public String Wrong;
        public String Wrong_Img;
        public String Right;
        public String Right_Img;
        public String Description;
        public Date TimeStamp;
    }

    public static class ExcerciseDB
    {        
        public int id;
        public int PartID;
        public int RemedyId;
        public int ExcerciseId;
        public String Title;
        public String Image;
        public String Description;
        public String Dosage;
        public String Precautions;
        public Date TimeStamp;
    }
}
