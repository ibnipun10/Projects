using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Device.Location; // Provides the GeoCoordinate class.
using Windows.Devices.Geolocation; //Provides the Geocoordinate class.
using Nokia.Phone.HereLaunchers;
using System.Threading.Tasks;
using PhoneApp.Common;
using System.Threading;

namespace PhoneApp.Presentation
{
    class Map
    {

        private async Task<GeoCoordinate> GetMyCurentLocation()
        {
            // Get my current location.
            Geolocator myGeolocator = new Geolocator();
            Geoposition myGeoposition = await myGeolocator.GetGeopositionAsync();
            Geocoordinate myGeocoordinate = myGeoposition.Coordinate;
            return ConvertGeocoordinate(myGeocoordinate);
        }

        public async void MapNearbyLocations(MainPage objmainPage)
        {
            try
            {
                Microsoft.Phone.Maps.MapsSettings.ApplicationContext.ApplicationId = Constants.PHYSIOIT_MAP_SERVICE_APP_ID;
                Microsoft.Phone.Maps.MapsSettings.ApplicationContext.AuthenticationToken = Constants.PHYSIOIT_MAP_SERVICE_AUTH_TOKEN_ID;

                ExploremapsSearchPlacesTask searchMap = new ExploremapsSearchPlacesTask();
                
                searchMap.Location = await GetMyCurentLocation();
                searchMap.SearchTerm = Constants.MAP_PHYSIOTHERAPY_CLINIC;
            
                searchMap.Show();

                objmainPage.EnableAppBarIconButton(Constants.APPLICATIONBAR_ICON_CLINIC, true);
            }
            catch (Exception ex)
            {
                CrashLog.WriteCrashLog(ex);
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

        public static GeoCoordinateWatcher StartLocationWatcherService()
        {
            GeoCoordinateWatcher watcher = new GeoCoordinateWatcher();
            watcher.Start();

            return watcher;
        }

        public static bool IsLocationServiceEnabledOnPhone(GeoCoordinateWatcher watcher)
        {
            bool bLocationService;
            if (watcher.Status == GeoPositionStatus.Disabled)
                bLocationService = false;
            else
                bLocationService = true;

            return bLocationService;
        }
    }
}
