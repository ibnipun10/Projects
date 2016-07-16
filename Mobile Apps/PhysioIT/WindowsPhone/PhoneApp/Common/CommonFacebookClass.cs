using Facebook;
using Facebook.Client;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace PhoneApp.Common
{
    class CommonFacebookClass
    {
        public static FacebookClient fbClient;
        public static int LIMIT = 50;
        public static GraphUser User;
        public static string PhysioITPageId = "375812439211272";

        public static async Task<List<FaceBookMessage>> GetAllMessages(string PageId, string lastID)
        {

            string query = PageId + "/feed";
            Dictionary<string, object> parameters = new Dictionary<string, object>();
            parameters.Add("fields", "from,message");
            parameters.Add("limit", LIMIT.ToString());

            if (lastID != null)
            {
                parameters.Add("__after_id", lastID);
            }

            dynamic result = await fbClient.GetTaskAsync(query, parameters);
            var messageArray = (IDictionary<string, object>)result;

            var data = (IEnumerable<object>)messageArray["data"];
            List<FaceBookMessage> lfbMessage = new List<FaceBookMessage>();

            foreach (var item in data)
            {
                IDictionary<string, object> message = (IDictionary<string, object>)item;
                if (message.ContainsKey("message"))
                {
                    FaceBookMessage fbMessage = PopulateFaceBookObject<FaceBookMessage>(message);
                    lfbMessage.Add(fbMessage);

                }
            }

            return lfbMessage;

        }

        private static async Task<FaceBookMessage> GetMessageObjectFromMessageID(string messageId)
        {
            string query = messageId;
            FaceBookMessage fbMessage = null;
            Dictionary<string, object> parameters = new Dictionary<string, object>();
            parameters.Add("fields", "from,message");

            dynamic result = await fbClient.GetTaskAsync(query, parameters);
            IDictionary<string, object> message = (IDictionary<string, object>)result;

            if (message.ContainsKey("message"))
                fbMessage = PopulateFaceBookObject<FaceBookMessage>(message);
            else
                throw new Exception("Unable to get message from messageID");
            
            return fbMessage;

        }

        private static T PopulateFaceBookObject<T>(IDictionary<string, object> item) where T : new()
        {
            T fbObj = new T();

            PropertyInfo[] property = typeof(T).GetProperties();

            foreach (PropertyInfo pt in property)
            {
                if (item.ContainsKey(pt.Name))
                {
                    if (pt.Name == "from")
                    {
                        var from = (IDictionary<string, object>)item[pt.Name];
                        pt.SetValue(fbObj, PopulateFaceBookObject<FacebookFrom>(from));
                    }
                    else if (pt.PropertyType.Equals(typeof(DateTime)))
                    {
                        pt.SetValue(fbObj, Convert.ToDateTime(item[pt.Name]));
                    }
                    else
                        pt.SetValue(fbObj, item[pt.Name]);
                }

            }
            return fbObj;
        }

        public static async Task<FaceBookMessageList> GetAllComments(string messageId, string lastID)
        {
            string query = messageId + "/comments";
            Dictionary<string, object> parameters = new Dictionary<string, object>();
            parameters.Add("fields", "from,message");
            parameters.Add("limit", LIMIT.ToString());

            if (lastID != null)
            {
                parameters.Add("__after_id", lastID);
            }

            dynamic result = await fbClient.GetTaskAsync(query, parameters);
            var commentArray = (IDictionary<string, object>)result;

            var data = (IEnumerable<object>)commentArray["data"];
            FaceBookMessageList lfbComment = new FaceBookMessageList();

            
            foreach (var item in data)
            {
                IDictionary<string, object> message = (IDictionary<string, object>)item;
                if (message.ContainsKey("message"))
                {
                    FaceBookMessage fbMessage = PopulateFaceBookObject<FaceBookMessage>(message);
                    lfbComment.Add(fbMessage);

                }
            }

            return lfbComment;
        }

        public static async Task<FaceBookMessage> SendMessage(string PageId, string message)
        {
            FaceBookMessage fbMessage = null;
            string query = PageId + "/feed";

                Dictionary<string, object> parameters = new Dictionary<string, object>();
                parameters.Add("message", message);

                dynamic result = await fbClient.PostTaskAsync(query, parameters);
                var messageArray = (IDictionary<string, object>)result;

                string data = (string)messageArray["id"];

                if (data != null)
                {
                    fbMessage = await GetMessageObjectFromMessageID(data);
                }

            return fbMessage;
        }

        public static async Task<FaceBookMessage> SendComment(string messageId, string comment)
        {
            FaceBookMessage fbMessage = null;
            string query = messageId + "/comments";

            Dictionary<string, object> parameters = new Dictionary<string, object>();
            parameters.Add("message", comment);

            dynamic result = await fbClient.PostTaskAsync(query, parameters);
            var messageArray = (IDictionary<string, object>)result;

            string data = (string)messageArray["id"];

            if (data != null)
            {
                fbMessage = await GetMessageObjectFromMessageID(data);
            }

            return fbMessage;
        }
        public static async Task<bool> IsPageLiked(string PageId)
        {
            bool blike = false;

            try
            {
                string query = "me/likes/" + PageId;
                dynamic result = await fbClient.GetTaskAsync(query);
                if (result.data.Count > 0)
                    blike = true;
            }
            catch (Exception ex)
            {

            }

            return blike;
        }

    }

    public class FaceBookMessage : INotifyPropertyChanged
    {
        private string _id;
        private FacebookFrom _from;
        private DateTime _created_time;
        private string _message;

        public string id
        {
            get
            {
                return _id;
            }
            set
            {
                _id = value;
                NotifyPropertyChanged("id");
            }
        }

        public FacebookFrom from
        {
            get
            {
                return _from;
            }
            set
            {
                _from = value;
                NotifyPropertyChanged("from");
            }
        }

        public DateTime created_time
        {
            get
            {
                return _created_time;
            }
            set
            {
                _created_time = value;
                NotifyPropertyChanged("created_time");
            }
        }

        public string message
        {
            get
            {
                return _message;
            }
            set
            {
                _message = value;
                NotifyPropertyChanged("message");
            }
        }

        public string GetCreatorName
        {
            get
            {
                return _from.name;
            }
        }

        public string GetCreatedDate
        {
            get
            {
                return _created_time.Date.ToString("d");
            }
        }


        public event PropertyChangedEventHandler PropertyChanged;

        public void NotifyPropertyChanged(string propertyName)
        {
            if (PropertyChanged != null)
                PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
        }
    }

    public class FaceBookMessageList : ObservableCollection<FaceBookMessage>
    {
        public FaceBookMessageList()
            : base()
        {

        }
    }

    public class FacebookFrom
    {
        public string id
        { get; set; }

        public string name
        { get; set; }

    }

}
