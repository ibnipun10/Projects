using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using System.Device.Location; // Provides the GeoCoordinate class.
using Windows.Devices.Geolocation; //Provides the Geocoordinate class.
using Nokia.Phone.HereLaunchers;
using System.Threading.Tasks;

namespace PhoneApp.FrontEnd
{
    public partial class Map : PhoneApplicationPage
    {
        public Map()
        {
            InitializeComponent();
        }

        private void PhysioMap_Loaded(object sender, RoutedEventArgs e)
        {
            Microsoft.Phone.Maps.MapsSettings.ApplicationContext.ApplicationId = "ApplicationID";
            Microsoft.Phone.Maps.MapsSettings.ApplicationContext.AuthenticationToken = "AuthenticationToken";
        }

        private async Task<GeoCoordinate> GetMyCurentLocation()
        {
            // Get my current location.
            Geolocator myGeolocator = new Geolocator();
            Geoposition myGeoposition = await myGeolocator.GetGeopositionAsync();
            Geocoordinate myGeocoordinate = myGeoposition.Coordinate;
            return ConvertGeocoordinate(myGeocoordinate);
        }

        public async void MapNearbyLocations()
        {
            try
            {
                ExploremapsSearchPlacesTask searchMap = new ExploremapsSearchPlacesTask();

                searchMap.Location = await GetMyCurentLocation();
                searchMap.SearchTerm = "Physiotherapy";

                searchMap.Show();
            }
            catch (Exception erno)
            {
                MessageBox.Show("Error message: " + erno.Message);
            }
        }

        private static GeoCoordinate ConvertGeocoordinate(Geocoordinate geocoordinate)
        {
            return new GeoCoordinate
                (
                geocoordinate.Latitude,
                geocoordinate.Longitude,
                geocoordinate.Altitude ?? Double.NaN,
                geocoordinate.Accuracy,
                geocoordinate.AltitudeAccuracy ?? Double.NaN,
                geocoordinate.Speed ?? Double.NaN,
                geocoordinate.Heading ?? Double.NaN
                );
        }

    }
}