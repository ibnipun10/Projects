using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SQLite;
using System.Runtime.Serialization;

namespace CommonBackstage
{
    public class FeedbackDB
    {
        [PrimaryKey, AutoIncrement]
        public int? id
        { get; set; }

        public string Name
        { get; set; }

        public string Email
        { get; set; }

        public string Comments
        { get; set; }

        public string DeviceId
        { get; set; }

        public DateTime TimeStamp
        { get; set; }
    }
    public class DeviceDB
    {
        [PrimaryKey, AutoIncrement]
        public int? id
        { get; set; }

        public string DeviceID
        { get; set; }       

        public string DeviceName
        { get; set; }

        public string AppVersion
        { get; set; }

        public string XMLVersion
        { get; set; }

        public DateTime TimeStamp
        { get; set; }
    }

    public class BodyPartDB
    {
        [PrimaryKey, AutoIncrement]
        public int? id
        { get; set; }

        public int PartID
        { get; set; }

        public string Name
        { get; set; }

        public string Image
        { get; set; }

        public string Message
        { get; set; }

        public DateTime TimeStamp
        { get; set; }
    }

    public class ReasonsDB
    {

        [PrimaryKey, AutoIncrement]
        public int? id
        { get; set; }

        public int PartID
        { get; set; }

        public int ReasonsId
        { get; set; }

        public string Title
        { get; set; }

        public string Description
        { get; set; }

        public string Image
        { get; set; }


        public DateTime TimeStamp
        { get; set; }
    }

    public class RemedyDB
    {

        [PrimaryKey, AutoIncrement]
        public int? id
        { get; set; }

        public int PartID
        { get; set; }

        public int RemedyId
        { get; set; }

        public string Title
        { get; set; }

        public string Description
        { get; set; }

        public DateTime TimeStamp
        { get; set; }
    }

    public class PostureDB
    {

        [PrimaryKey, AutoIncrement]
        public int? id
        { get; set; }

        public int PartID
        { get; set; }

        public int RemedyId
        { get; set; }

        public int PostureId
        { get; set; }

        public string Wrong
        { get; set; }

        public string Wrong_Img
        { get; set; }

        public string Right
        { get; set; }

        public string Right_Img
        { get; set; }

        public string Description
        { get; set; }

        public DateTime TimeStamp
        { get; set; }

    }

    public class ExcerciseDB
    {

        [PrimaryKey, AutoIncrement]
        public int? id
        { get; set; }

        public int PartID
        { get; set; }

        public int RemedyId
        { get; set; }

        public int ExcerciseId
        { get; set; }

        public string Title
        { get; set; }

        public string Image
        { get; set; }

        public string Description
        { get; set; }

        public string Dosage
        { get; set; }

        public string Precautions
        { get; set; }

        public DateTime TimeStamp
        { get; set; }
    }

}
