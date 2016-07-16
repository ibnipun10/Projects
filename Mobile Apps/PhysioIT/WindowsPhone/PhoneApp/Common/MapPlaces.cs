using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using System.Device.Location;

namespace PhoneApp.Common
{
    [JsonObject(MemberSerialization.OptIn)]
    public class MapPlaces
    {

        [JsonProperty]
        public int distance { get; set; }

        [JsonProperty]
        public string title { get; set; }

        [JsonProperty]
        public double averageRating { get; set; }

        [JsonProperty]
        public string icon { get; set; }

        [JsonProperty]
        public string vicinity { get; set; }

        [JsonProperty]
        public string type { get; set; }

        [JsonProperty]
        public string href { get; set; }

        [JsonProperty]
        public string id { get; set; }

        public GeoCoordinate position { get; set; }

        [JsonProperty]
        public Category category { get; set; }

    }

    [JsonObject(MemberSerialization.OptIn)]
    public class Category
    {
        [JsonProperty]
        public string id { get; set; }

        [JsonProperty]
        public string title { get; set; }

        [JsonProperty]
        public string href { get; set; }

        [JsonProperty]
        public string type { get; set; }
    }


}
