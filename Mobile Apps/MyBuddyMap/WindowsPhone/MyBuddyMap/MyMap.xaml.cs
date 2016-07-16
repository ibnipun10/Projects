using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Microsoft.Phone.Maps.Controls;
using MyBuddyMap.Common;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Shapes;
using System.Windows.Media;
using Windows.Devices.Geolocation;
using System.Device.Location;

namespace MyBuddyMap
{
    public partial class MyMap : PhoneApplicationPage
    {
        private List<string> friendsIds;
        private Queue<FaceBookFriend> lFriends;
        private bool bDone;
        private MapLayer locationLayer;
        private Dictionary<string, List<FaceBookFriend>> dictLocation;
        private Semaphore semaphore;

        public MyMap()
        {
            try
            {
                InitializeComponent();
                InitializeVariables();
                InitializeAdControl();

                InsertIntoFriendList();
                Thread thread = new Thread(FriendsThread);
                thread.Start();
            }
            catch (Exception ex)
            {
                //Set Error Text
            }

        }

        private void myMapControl_Loaded(object sender, RoutedEventArgs e)
        {
            Microsoft.Phone.Maps.MapsSettings.ApplicationContext.ApplicationId = Constants.MAP_APPLICATION_ID;
            Microsoft.Phone.Maps.MapsSettings.ApplicationContext.AuthenticationToken = Constants.MAP_AUTHENTICATION_ID;
        }

        private void InitializeAdControl()
        {
            AdCentre.SetAddControl(adcontrol);
            adcontrol.ErrorOccurred += adcontrol_ErrorOccurred;
        }

        void adcontrol_ErrorOccurred(object sender, Microsoft.Advertising.AdErrorEventArgs e)
        {

        }

        private async void InsertIntoFriendList()
        {
            try
            {
                SetProgressindicatorText("Retrieve facebook friends");

                await GetFacebookFriendsCount();

                foreach (string id in friendsIds)
                {
                    FaceBookFriend friend = await CommonFacebookClass.GetFriendsDetails(id);
                    AddToQueue(friend);
                }

                bDone = true;
            }
            catch (Exception ex)
            {
                //Set Error Text
            }
        }

        private void InitializeVariables()
        {
            lFriends = new Queue<FaceBookFriend>();
            dictLocation = new Dictionary<string, List<FaceBookFriend>>();
            bDone = false;
            semaphore = new Semaphore(1, 1);
            applicationTitle.Text = Constants.APPLICATION_NAME;

            locationLayer = new MapLayer();
            mapName.Layers.Add(locationLayer);


            ShowMyLocationOnTheMap();
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

        public static GeoCoordinateWatcher StartLocationWatcherService()
        {
            GeoCoordinateWatcher watcher = new GeoCoordinateWatcher();
            watcher.Start();

            return watcher;
        }

        private async void ShowMyLocationOnTheMap()
        {
            string strlocation = ApplicationSetting.ReadApplicationSetting(Constants.SETTINGS_LOCATION_SERVICE);

            try
            {

                if (!string.IsNullOrEmpty(strlocation) && strlocation.CompareTo(Constants.SETTINGS_YES) == 0 && IsLocationServiceEnabledOnPhone(StartLocationWatcherService()))
                {

                    // Get my current location.
                    Geolocator myGeolocator = new Geolocator();
                    Geoposition myGeoposition = await myGeolocator.GetGeopositionAsync();
                    Geocoordinate myGeocoordinate = myGeoposition.Coordinate;
                    GeoCoordinate myGeoCoordinate =
                    Utlities.ConvertGeocoordinate(myGeocoordinate);

                    // Make my current location the center of the Map.
                    this.mapName.ZoomLevel = 5;
                    this.mapName.Center = myGeoCoordinate;

                    // Create a small circle to mark the current location.
                    Ellipse myCircle = new Ellipse();
                    myCircle.Fill = new SolidColorBrush(Colors.Orange);
                    myCircle.Height = 20;
                    myCircle.Width = 20;
                    myCircle.Opacity = 50;

                    // Create a MapOverlay to contain the circle.
                    MapOverlay myLocationOverlay = new MapOverlay();
                    myLocationOverlay.Content = myCircle;
                    myLocationOverlay.PositionOrigin = new Point(0.5, 0.5);
                    myLocationOverlay.GeoCoordinate = myGeoCoordinate;

                    // Create a MapLayer to contain the MapOverlay.
                    //MapLayer locationLayer = new MapLayer();
                    locationLayer.Add(myLocationOverlay);

                    // Add the MapLayer to the Map.
                    //  mapName.Layers.Add(locationLayer);
                }

            }
            catch
            {
                SetProgressindicatorText("Unable to show your location");
            }
        }

        private GeoCoordinate GetLocation(double lattitude, double longitude)
        {
            return new GeoCoordinate(lattitude, longitude);
        }

        private string GetKey(double lattitude, double longitude)
        {
            return lattitude + "." + longitude;
        }


        private void ShowLocation(double lattitude, double longitude)
        {
            // Create a small circle to mark the current location.
            Ellipse myCircle = new Ellipse();
            myCircle.Fill = new SolidColorBrush(Colors.Blue);
            myCircle.Height = 20;
            myCircle.Width = 20;
            myCircle.Opacity = 50;

            if (dictLocation.ContainsKey(GetKey(lattitude, longitude)))
                myCircle.Tag = dictLocation[GetKey(lattitude, longitude)];
            else
                myCircle.Tag = null;
            myCircle.Tap += myCircle_Tap;

            // Create a MapOverlay to contain the circle.
            MapOverlay myLocationOverlay = new MapOverlay();
            myLocationOverlay.Content = myCircle;
            myLocationOverlay.PositionOrigin = new Point(0.5, 0.5);
            myLocationOverlay.GeoCoordinate = GetLocation(lattitude, longitude);


            // Create a MapLayer to contain the MapOverlay.
            //MapLayer locationLayer = new MapLayer();
            locationLayer.Add(myLocationOverlay);

            // Add the MapLayer to the Map.
            //  mapName.Layers.Add(locationLayer);

        }

        void myCircle_Tap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            List<FaceBookFriend> lfacebookFriend = (List<FaceBookFriend>)((Ellipse)(sender)).Tag;
            CommonFacebookClass.lShowFriends = lfacebookFriend;
            this.NavigationService.Navigate(new Uri("/MyBuddy.xaml", UriKind.Relative));
        }

        public void UpdateProgressIndicatorText(string message)
        {
            Dispatcher.BeginInvoke(new Action(() => this.SetProgressindicatorText(message)));
        }

        public void SetProgressIndicatorVisibility(bool bVisible)
        {
            Dispatcher.BeginInvoke(new Action(() => this.progressIndicator.IsVisible = bVisible));
        }

        private void SetProgressindicatorText(string text)
        {
            this.progressIndicator.IsVisible = true;
            this.progressIndicator.Text = text;
        }


        private FaceBookFriend RemoveFromQueue()
        {
            FaceBookFriend item = null;
            if (lFriends != null && lFriends.Count != 0)
            {
                item = lFriends.First();
                Dispatcher.BeginInvoke(new Action(() => this.RemoveItemInQueue()));
            }
            return item;
        }

        private FaceBookFriend RemoveItemInQueue()
        {
            semaphore.WaitOne();
            FaceBookFriend item = lFriends.Dequeue();
            semaphore.Release();

            return item;
        }

        private void AddToQueue(FaceBookFriend item)
        {
            semaphore.WaitOne();
            lFriends.Enqueue(item);
            semaphore.Release();
        }

        private async Task<int> GetFacebookFriendsCount()
        {
            friendsIds = await CommonFacebookClass.GetAllFriends();
            return friendsIds.Count;
        }

        private async void FriendsThread()
        {
            try
            {
                int iCount = 0;
                while (!bDone || lFriends.Count != 0)
                {
                    if (lFriends.Count != 0)
                    {
                        FaceBookFriend fbFriend = RemoveItemInQueue();

                        if (fbFriend.location != null)
                        {
                            iCount++;

                            fbFriend.location = await CommonFacebookClass.GetLocationDetails(fbFriend.location);

                            //Add to dictionary and pin the locations.
                            if (InsertIntoLocationDictionary(fbFriend))
                                ShowLocationByThread(fbFriend.location.lattitude, fbFriend.location.longitude);

                            UpdateProgressIndicatorText(String.Format(Constants.PROGRESS_TEXT, iCount, friendsIds.Count));
                        }

                    }
                    else
                    {
                        Thread.Sleep(1000);
                    }
                }

                SetProgressIndicatorVisibility(false);
            }
            catch (Exception ex)
            {
                //Set Error Text
            }
        }

        private void ShowLocationByThread(double lattitude, double longitude)
        {
            Dispatcher.BeginInvoke(new Action(() => this.ShowLocation(lattitude, longitude)));
        }

        private bool InsertIntoLocationDictionary(FaceBookFriend friend)
        {
            bool bNewEntry = false;

            string key = GetKey(friend.location.lattitude, friend.location.longitude);

            List<FaceBookFriend> lfbfriend = null;

            if (!dictLocation.ContainsKey(key))
            {
                lfbfriend = new List<FaceBookFriend>();
                lfbfriend.Add(friend);
                bNewEntry = true;

            }
            else
            {
                lfbfriend = dictLocation[key];
                lfbfriend.Add(friend);
            }
            dictLocation[key] = lfbfriend;

            return bNewEntry;
        }


    }
}