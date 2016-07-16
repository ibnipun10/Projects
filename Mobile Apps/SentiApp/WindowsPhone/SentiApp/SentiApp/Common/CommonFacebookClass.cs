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

namespace SentiApp.Common
{
    class CommonFacebookClass
    {
        public static GraphUser User;
        public static FacebookClient fbClient;
        public static int LIMIT = 100;
        public static DateTime tillDate = DateTime.Now;
        public static string UntilID;

        public static void SetTillDate(int days)
        {
            tillDate = DateTime.Now;
            tillDate = tillDate.AddDays(-days);
        }

         public static async Task<List<FaceBookMessage>> GetAllMessages()
        {

            string query = "me" + "/posts";
            Dictionary<string, object> parameters = new Dictionary<string, object>();
            parameters.Add("fields", "message,story,id,type");
            parameters.Add("limit", LIMIT.ToString());

            if (UntilID != null)
            {
                parameters.Add("until", UntilID);
            }

            dynamic result = await fbClient.GetTaskAsync(query, parameters);
            var messageArray = (IDictionary<string, object>)result;

            var data = (IEnumerable<object>)messageArray["data"];
            List<FaceBookMessage> lfbMessage = new List<FaceBookMessage>();

            string untill = null;
            if (messageArray.ContainsKey("paging"))
            {
               // JObject paging = JObject.Parse(messageArray["paging"].ToString());
                var paging = (IDictionary<string, object>)messageArray["paging"];
                untill = GetUntilID(paging);
            }

            if (untill != null)
                UntilID = untill;

            foreach (var item in data)
            {
                IDictionary<string, object> message = (IDictionary<string, object>)item;
               
                if (IsValidStory(message))
                {
                    message["story"] = GetMessageFromStory(message["story"].ToString());
                    FaceBookMessage fbMessage = PopulateFaceBookObject<FaceBookMessage>(message);
                    if (fbMessage.created_time.Subtract(tillDate).Seconds > 0)
                        lfbMessage.Add(fbMessage);
                    else
                        return lfbMessage;
                }
            }

            return lfbMessage;
        }

         private static string GetUntilID(IDictionary<string, object> page)
         {
             string untilId = null;

                 if (page.ContainsKey("next"))
                 {
                     string next = page["next"].ToString();
                     string[] token = next.Split('=');
                     untilId = token[token.Count() - 1];
                     
                 }

                 
             

             return untilId;
         }

         private static string GetMessageFromStory(string story)
         {
             //string message = "";
             string[] words = story.Split('\"');
             return words[1];
         }

         private static bool IsValidStory(IDictionary<string, object> message)
         {
             bool bValid = false;

             if (message.ContainsKey("story"))
             {
                 string story = message["story"].ToString();
                 string[] words = story.Split('\"');
                 if (words.Length < 3)
                     return false;
                 bValid = true;
             }

             return bValid;
         }

         private static T PopulateFaceBookObject<T>(IDictionary<string, object> item) where T : new()
         {
             T fbObj = new T();

             PropertyInfo[] property = typeof(T).GetProperties();

             foreach (PropertyInfo pt in property)
             {
                 if (item.ContainsKey(pt.Name))
                 {
                     if (pt.PropertyType.Equals(typeof(DateTime)))
                     {
                         pt.SetValue(fbObj, Convert.ToDateTime(item[pt.Name]));
                     }
                     else
                         pt.SetValue(fbObj, item[pt.Name]);
                 }

             }
             return fbObj;
         }
    }

    

    public class FaceBookMessage
    {
        public String id
        { get; set; }

        public String story
        { get; set; }

        public DateTime created_time
        { get; set; }

        public string GetCreatedDate
        {
            get
            {
                return created_time.Date.ToString("d");
            }
        }
    }
}
