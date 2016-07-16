using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Reflection;
using System.Net;
using RestSharp;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace SentiApp.Common
{
    class CommonSentiClass
    {
        private static T PopulateSEntiMessageObject<T>(IDictionary<string, object> item) where T : new()
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

        private static string GetSentigemUri(string apiKey, string text)
        {
            string Uri = "https://api.sentigem.com/external/get-sentiment?api-key={0}&text={1}";
            return String.Format(Uri, apiKey, text);
        }

        private static string GetCoallatedString(List<FaceBookMessage> lfbMessage)
        {
            string retValue = null;
            foreach (FaceBookMessage fbmsg in lfbMessage)
            {
                retValue = retValue + "." + fbmsg.story;
            }

            return retValue;
        }

        public async static Task<SentiMessage> GetSentiMessage(List<FaceBookMessage> lfbMessage)
        {
            string combineText = GetCoallatedString(lfbMessage);
            return await GetSentiMessage(combineText);
        }

        public async static Task<SentiMessage> GetSentiMessage(string message)
        {                       
            // Create a request for the URL. 
            string Url = GetSentigemUri(Constants.SENTIGEM_API_KEY, message);
            string Uri = "https://api.sentigem.com";
            var client = new RestClient(Uri);
            var request = new RestRequest("/external/get-sentiment", Method.GET);
            request.AddParameter("api-key", Constants.SENTIGEM_API_KEY);
            request.AddParameter("text", message);
            

            IRestResponse sentimsg = await ExecuteTaskAsync(client, request);
            JObject json = (JObject)JsonConvert.DeserializeObject(sentimsg.Content);

            SentiMessage msg = GetSentiMessage(json);

            return msg;
        }

        public static Task<IRestResponse> ExecuteTaskAsync(RestClient client, RestRequest request) 
        {
            if (client == null)
                throw new NullReferenceException();

            var tcs = new TaskCompletionSource<IRestResponse>();

            client.ExecuteAsync(request, (response) =>
            {
                if (response.ErrorException != null)
                    tcs.TrySetException(response.ErrorException);
                else
                    tcs.TrySetResult(response);
            });

            return tcs.Task;
        }

        private static SentiMessage GetSentiMessage(JObject jObject)
        {
            SentiMessage sentimessage = new SentiMessage();
            PropertyInfo[] property = typeof(SentiMessage).GetProperties();
            

            foreach (var x in jObject)
            {
                string name = x.Key;
                JToken value = x.Value;

                foreach (PropertyInfo prop in property)
                {
                    if (prop.Name.Equals(name))
                        prop.SetValue(sentimessage, value.ToString());
                }

            }

            return sentimessage;
        }
    }

    class SentiMessage
    {
        [JsonProperty("data")]
        public string data
        { get; set; }

         [JsonProperty("status")]
        public string status
        { get; set; }

         [JsonProperty("message")]
        public string message
        { get; set; }

         [JsonProperty("errors")]
        public string errors
        { get; set; }

         [JsonProperty("polarity")]
        public string polarity
        { get; set; }

         [JsonProperty("char_count")]
        public string char_count
        { get; set; }

         [JsonProperty("comment")]
        public string comment
        { get; set; }
       
    }
}
