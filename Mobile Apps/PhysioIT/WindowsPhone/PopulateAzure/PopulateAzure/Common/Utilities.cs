using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows;
using System.Xml.Linq;
using System.Reflection;
using Microsoft.Phone.Info;
using System.IO.IsolatedStorage;
using CommonBackstage;

namespace PopulateAzure.Common
{

    public static class Font
    {
        public static void TitleFont(TextBlock txtblock)
        {
            txtblock.FontWeight = FontWeights.Bold;
            txtblock.FontSize = 35;
            txtblock.Foreground = new SolidColorBrush(Colors.Green);
            txtblock.TextWrapping = TextWrapping.Wrap;
        }

        public static void DescriptionFont(TextBlock txtblock)
        {
            txtblock.FontWeight = FontWeights.SemiBold;
            txtblock.FontSize = 25;
            txtblock.Foreground = new SolidColorBrush(Colors.White);
            txtblock.TextWrapping = TextWrapping.Wrap;
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
        public const string APPLICATION_TILE = "MY PERSONAL PHYSIO";
        public const string APPLICATION_NAME = "PhysioIT";
        public const string REASONS_NAME = "Reasons";
        public const string REMEDY_NAME = "Remedy";
        public const string PHYSIOITXMLPATH = "PhysioData.xml";
        
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
                    string text;
                    
                    if((string)value.Element(pt.Name) == null)
                        continue;
                    else
                        text = value.Element(pt.Name).Value;
                    if (string.IsNullOrEmpty(text))
                        text = "";
                    if (pt.PropertyType == typeof(int))
                        pt.SetValue(bp, Convert.ToInt32(text));
                    else
                        pt.SetValue(bp, text);
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
            device.AppVersion = "";

            return device;
        }

        public int GetXMLVersionId()
        {
            XElement database =  xmlData.Descendants(XML_DATABASE).Single();
            int versionId = Convert.ToInt32(database.Attribute(XML_DATABASE_VERSION).Value);
            return versionId;
        }

        public void SetXMLVersionId(int versionId)
        {

        }
    }

}
