using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Facebook;
using Facebook.Client;
using System.Reflection;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.Net;

namespace MyBuddyMap.Common
{
    class CommonFacebookClass
    {
        public static GraphUser User;
        public static FacebookClient fbClient;
        public static bool bHomeTown;
        public static List<FaceBookFriend> lShowFriends;

        public static async Task<List<string>> GetAllFriends()
        {
            List<string> lfriends = new List<string>();
            string query = "me";
            Dictionary<string, object> parameters = new Dictionary<string, object>();
            parameters.Add("fields", "friends");

            dynamic result = await fbClient.GetTaskAsync(query, parameters);
            var messageArray = (IDictionary<string, object>)result;

            var data = (IDictionary<string, object>)messageArray["friends"];
            var friendsList = (IEnumerable<object>)data["data"];


            foreach (var item in friendsList)
            {
                IDictionary<string, object> friend = (IDictionary<string, object>)item;
                lfriends.Add(friend["id"].ToString());               
            }
            
            return lfriends;
        }

        public static async Task<List<FaceBookFriend>> GetFriendsDetails(List<string> lfriendId)
        {
            List<FaceBookFriend> lfriendsDetails = new List<FaceBookFriend>();
            int i = 0;
            foreach (string id in lfriendId)
            {
                FaceBookFriend fbfriend = await GetFriendsDetails(id);
                lfriendsDetails.Add(fbfriend);
                if (i > 10)
                    break;
                i++;
            }

            return lfriendsDetails;
        }

        public static async Task<FaceBookFriend> GetFriendsDetails(string friendId)
        {
            FaceBookFriend fbFriend = new FaceBookFriend();

            string query =  friendId;           

            dynamic result = await fbClient.GetTaskAsync(query);
            var messageArray = (IDictionary<string, object>)result;


            fbFriend = PopulateFaceBookObject<FaceBookFriend>(messageArray);

            return fbFriend;
        }

        public static async Task<LocationClass> GetLocationDetails(LocationClass location)
        {
            if (location == null)
                return null;

            string query = location.id;

            dynamic result = await fbClient.GetTaskAsync(query);
            var messageArray = (IDictionary<string, object>)result;
            IDictionary<string, object> locationArray = null;

            if(messageArray.ContainsKey("location"))
                locationArray = (IDictionary<string, object>)messageArray["location"];

            if (locationArray != null)
            {
                location.lattitude = float.Parse(locationArray["latitude"].ToString());
                location.longitude = float.Parse(locationArray["longitude"].ToString());
            }
            return location;
        }

     
        private static T PopulateFaceBookObject<T>(IDictionary<string, object> item) where T : new()
         {
             T fbObj = new T();

             PropertyInfo[] property = typeof(T).GetProperties();

             foreach (PropertyInfo pt in property)
             {

                 if (pt.PropertyType.Equals(typeof(LocationClass)))
                 {
                     if (bHomeTown)
                     {
                         if (item.ContainsKey("hometown"))
                         {
                             var from = (IDictionary<string, object>)item["hometown"];
                             pt.SetValue(fbObj, PopulateFaceBookObject<LocationClass>(from));
                         }
                     }
                     else
                     {
                         if (item.ContainsKey("location"))
                         {
                             var from = (IDictionary<string, object>)item["location"];
                             pt.SetValue(fbObj, PopulateFaceBookObject<LocationClass>(from));
                         }
                     }
                 }

                 else if (item.ContainsKey(pt.Name))
                     pt.SetValue(fbObj, item[pt.Name]);
                 

             }
             return fbObj;
         }
    }

    

    public class FaceBookFriend
    {
        public String id
        { get; set; }

        public String name
        { get; set; }

        public String birthday
        { get; set; }

        public String gender
        { get; set; }

        public String relationship_status
        { get; set; }

        public LocationClass location
        { get; set; }

        public int GetAge
        {
            get
            {
                TimeSpan tsAge = DateTime.Now.Subtract(Convert.ToDateTime(birthday));
                return new DateTime(tsAge.Ticks).Year - 1;               
            }
        }

        public string GetLocationName
        {
            get
            {
                return location.name;
            }
        }

        public string GetImageSrc
        {
            get
            {
                WebResponse response = null;
                string pictureUrl = string.Empty;
                try
                {
                    /*
                    WebRequest request = WebRequest.Create(string.Format("https://graph.facebook.com/{0}/picture", id));
                    response = request.
                     
                    pictureUrl = response.ResponseUri.ToString();
                    
                     */
                    return string.Format("https://graph.facebook.com/{0}/picture", id);
                }
                catch (Exception ex)
                {
                    //? handle
                }
                finally
                {
                  //  if (response != null) response.Close();
                }
                return pictureUrl;
            }
        }
  
    }

    public class LocationClass
    {
        public string id
        { get; set; }

        public string name
        { get; set; }

        public float lattitude
        { get; set; }

        public float longitude
        { get; set; }
    }
}
