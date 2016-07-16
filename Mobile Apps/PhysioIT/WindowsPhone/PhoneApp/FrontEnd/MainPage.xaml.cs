using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using PhoneApp.Resources;
using PhoneApp.Common;
using PhoneApp.Presentation;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using PhoneApp.BackStage;
using Microsoft.WindowsAzure.MobileServices;
using CommonBackstage;
using Microsoft.Phone.Net.NetworkInformation;
using System.ComponentModel;
using Microsoft.Phone.Tasks;
using System.Device.Location;

namespace PhoneApp
{
    public partial class MainPage : PhoneApplicationPage
    {
        Interactor _worker;
        private GeoCoordinateWatcher watcher;

        // Constructor
        public MainPage()
        {
            InitializeComponent();
            _worker = new Interactor();
            applicationName.Text = ApplicationSetting.GetValuesFromAppXMLFile(Constants.APP_TITILE);
            applicationTitle.Text = Constants.APPLICATION_TILE;

            InitializeAdControl();           
            PopulatePage();

            watcher = Map.StartLocationWatcherService();
            // Sample code to localize the ApplicationBar
            //BuildLocalizedApplicationBar();
            EnableChatIcon();
        }

        private void InitializeAdControl()
        {
            AdCentre.SetAddControl(adcontrol);
            adcontrol.ErrorOccurred += adcontrol_ErrorOccurred;
        }

        void adcontrol_ErrorOccurred(object sender, Microsoft.Advertising.AdErrorEventArgs e)
        {
           
        }

        private void AdControl_ErrorOccurred(object sender, Microsoft.Advertising.AdErrorEventArgs e)
        {
            string errorcode = e.ErrorCode.ToString();
            Exception error = e.Error;
        }  

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            string strlocation = ApplicationSetting.ReadApplicationSetting(Constants.SETTINGS_LOCATION_SERVICE);

            if (!string.IsNullOrEmpty(strlocation) && strlocation.CompareTo(Constants.SETTINGS_YES) == 0 && Map.IsLocationServiceEnabledOnPhone(watcher))
                EnableAppBarIconButton(Constants.APPLICATIONBAR_ICON_CLINIC, true);
            else
                EnableAppBarIconButton(Constants.APPLICATIONBAR_ICON_CLINIC, false);

            EnableChatIcon();
            
        }

        private void EnableChatIcon()
        {
            if (PhoneSettings.IsDeviceNetwork())
                EnableAppBarIconButton(Constants.APPLICATIONBAR_ICON_CHAT, true);
            else
                EnableAppBarIconButton(Constants.APPLICATIONBAR_ICON_CHAT, false);
        }



       
        public void PopulatePage()
        {
            
            //create database and insert initial values
            _worker.InsertInitialValues();

            //Create thread to updte data
            CreateThreadfunction();

            //Get the number of body parts and their values from database
            List<BodyPartDB> bp = _worker.GetRows<BodyPartDB>();

            //ContentPanel.ShowGridLines = true;
            //Create the hubtiles according to count
            RowDefinition rowdef = new RowDefinition();
            rowdef.Height = GridLength.Auto;
            ContentPanel.RowDefinitions.Add(rowdef);
            int nRow = 0, nCol = 0, nCount = 0;
            foreach (BodyPartDB _bp in bp)
            {
                nCount++;
                HubTile ht = new HubTile();
                ht.Name = _bp.PartID.ToString();
                ht.Title = _bp.Name;
                ht.Message = _bp.Message;

                Uri imageUri = new Uri("/" + Constants.IMAGES_PATH +"/" + Constants.IMAGE_BODYPART + "/" + _bp.Image, UriKind.Relative);
                ImageSource img = new BitmapImage(imageUri);
                ht.Source = img;
                ht.GroupTag = Constants.BODYPARTHUBTILEGRP;
                ht.SetValue(Grid.RowProperty, nRow);
                ht.SetValue(Grid.ColumnProperty, nCol);
                ht.HorizontalAlignment = System.Windows.HorizontalAlignment.Center;
                ht.VerticalAlignment = System.Windows.VerticalAlignment.Center;
                ht.Tap += ht_Tap;
                ht.Margin = new Thickness(10);

                ContentPanel.Children.Add(ht);
                nCol++;
                if (nCol % 2 == 0 && nCount < bp.Count())
                {
                    nRow ++;
                    nCol = 0;
                    RowDefinition df2 = new RowDefinition();
                    df2.Height = GridLength.Auto;
                    ContentPanel.RowDefinitions.Add(df2);
                }
            }

           
             
        }

        void ht_Tap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            HubTile hubtile = (HubTile)sender;
            NavigationService.Navigate(new Uri("/FrontEnd/Reasons.xaml?ID=" + hubtile.Name + "&Name=" + hubtile.Title, UriKind.Relative));
        }

        void CreateProgressIndicator()
        {
            ProgressIndicator progress = new ProgressIndicator
            {
                IsVisible = false,
                IsIndeterminate = true,
                Text = ""
            };
            SystemTray.SetProgressIndicator(this, progress);
        }

        void CreateThreadfunction()
        {
            string strupdate = ApplicationSetting.ReadApplicationSetting(Constants.SETTINGS_AZURE_UPDATE);
            if (!string.IsNullOrEmpty(strupdate) && strupdate.CompareTo(Constants.SETTINGS_YES) == 0)
            {
                BackgroundWorker bw = new BackgroundWorker();

                this.progressIndicator.IsVisible = true;
                bw.DoWork += SyncWithAzureService;
                bw.RunWorkerAsync();
            }
        }

        public void UpdateProgressIndicatorText(string message)
        {
            Dispatcher.BeginInvoke(new Action(() => this.progressIndicator.Text = message));
        }

        public void SetProgressIndicatorVisibility(bool bVisible)
        {
            Dispatcher.BeginInvoke(new Action(() => this.progressIndicator.IsVisible = bVisible));
        }

        public void EnableAppBarIconButton(int index, bool bEnable)
        {
            Dispatcher.BeginInvoke(new Action(() => ((ApplicationBarIconButton)(this.ApplicationBar.Buttons[index])).IsEnabled = bEnable));
        
        }

        public void AzureDownloadProgress(AzureThreadProgressEnum eprogress)
        {

            switch (eprogress)
            {
                case AzureThreadProgressEnum.Started :
                    UpdateProgressIndicatorText("Updating...");
                    break;

                case AzureThreadProgressEnum.UpdatedAzureDeviceDB :
                    UpdateProgressIndicatorText("Sending device info...");
                    break;

                case AzureThreadProgressEnum.UpdatedAzureBodyPartDB :
                case AzureThreadProgressEnum.UpdatdAzureReasonsDB:
                case AzureThreadProgressEnum.UpdatedAzureRemedyDB:
                case AzureThreadProgressEnum.UpdatedAzurePostureDB:
                case AzureThreadProgressEnum.UpdatedAzureExcerciseDB:
                    UpdateProgressIndicatorText("Updating tables");
                    break;
                case AzureThreadProgressEnum.Completed :
                    SetProgressIndicatorVisibility(false);
                    break;
                default: break;
            }
        }

        public async void  SyncWithAzureService(object sender, DoWorkEventArgs e)
        {
            //DO the syncronization 
            BackgroundWorker bw = (BackgroundWorker)(sender);
            AzureThreadProgressEnum ecompletion = AzureThreadProgressEnum.Started;

            int nAttempts = 0;
           
            while (nAttempts < Constants.MAX_ATTEMPTS)
            {
                
                try
                {
                    if (DeviceNetworkInformation.IsCellularDataEnabled || DeviceNetworkInformation.IsWiFiEnabled)
                    {
                        Azureinteractor azureInteractor = new Azureinteractor();
                        switch (ecompletion)
                        {
                            case AzureThreadProgressEnum.Started:
                                AzureDownloadProgress(AzureThreadProgressEnum.UpdatedAzureDeviceDB);
                                await azureInteractor.InsertDeviceDescription();
                                ecompletion = AzureThreadProgressEnum.UpdatedAzureDeviceDB;
                                goto case AzureThreadProgressEnum.UpdatedAzureDeviceDB;
                            
                            case AzureThreadProgressEnum.UpdatedAzureDeviceDB:
                                AzureDownloadProgress(AzureThreadProgressEnum.UpdatedAzureBodyPartDB);
                                await azureInteractor.UpdateRowsInTable<BodyPartDB>();
                                ecompletion = AzureThreadProgressEnum.UpdatdAzureReasonsDB;
                                goto case AzureThreadProgressEnum.UpdatdAzureReasonsDB;

                            case AzureThreadProgressEnum.UpdatdAzureReasonsDB:
                                AzureDownloadProgress(AzureThreadProgressEnum.UpdatdAzureReasonsDB);
                                await azureInteractor.UpdateRowsInTable<ReasonsDB>();
                                ecompletion = AzureThreadProgressEnum.UpdatedAzureRemedyDB;
                                goto case AzureThreadProgressEnum.UpdatedAzureRemedyDB;

                            case AzureThreadProgressEnum.UpdatedAzureRemedyDB:
                                AzureDownloadProgress(AzureThreadProgressEnum.UpdatedAzureRemedyDB);
                                await azureInteractor.UpdateRowsInTable<RemedyDB>();
                                ecompletion = AzureThreadProgressEnum.UpdatedAzurePostureDB;
                                goto case AzureThreadProgressEnum.UpdatedAzurePostureDB;

                            case AzureThreadProgressEnum.UpdatedAzurePostureDB:
                                AzureDownloadProgress(AzureThreadProgressEnum.UpdatedAzurePostureDB);
                                await azureInteractor.UpdateRowsInTable<PostureDB>();
                                ecompletion = AzureThreadProgressEnum.UpdatedAzureExcerciseDB;
                                goto case AzureThreadProgressEnum.UpdatedAzureExcerciseDB;

                            case AzureThreadProgressEnum.UpdatedAzureExcerciseDB:
                                AzureDownloadProgress(AzureThreadProgressEnum.UpdatedAzureExcerciseDB);
                                await azureInteractor.UpdateRowsInTable<ExcerciseDB>();
                                ecompletion = AzureThreadProgressEnum.Completed;
                                goto default;

                            default :
                                AzureDownloadProgress(AzureThreadProgressEnum.Completed);
                                return;
                        }
                        
                    }
                    else
                    {
                        //No data network available, sleep the thread and attempt after some time
                        System.Threading.Thread.Sleep(10000);

                    }
                }
                catch (Exception ex)
                {
                    //Exception occured try again after sleeping for some time

                    CrashLog.WriteCrashLog(ex);
                    UpdateProgressIndicatorText("Error updating retrying after 10 sec ...");

                    System.Threading.Thread.Sleep(10000);
                }

                nAttempts++;
            }

            SetProgressIndicatorVisibility(false);
                                
        }

       
        public enum AzureThreadProgressEnum
        {
            Started,
            UpdatedAzureDeviceDB,
            UpdatedAzureBodyPartDB,
            UpdatdAzureReasonsDB,
            UpdatedAzureRemedyDB,
            UpdatedAzurePostureDB,
            UpdatedAzureExcerciseDB,
            Completed
        }

        private void feedbackbutton_click(object sender, EventArgs e)
        {
            NavigationService.Navigate(new Uri("/FrontEnd/FeedBack.xaml", UriKind.Relative));
        }

        private void aboutbutton_click(object sender, EventArgs e)
        {
            NavigationService.Navigate(new Uri("/FrontEnd/AboutDisclaimer.xaml", UriKind.Relative));
        }

        private void settingsbutton_click(object sender, EventArgs e)
        {
            NavigationService.Navigate(new Uri("/FrontEnd/Settings.xaml", UriKind.Relative));
        }
        

        private void clinicbutton_click(object sender, EventArgs e)
        {
            try
            {
                EnableAppBarIconButton(Constants.APPLICATIONBAR_ICON_CLINIC, false);
                Map map = new Map();
                map.MapNearbyLocations(this);             
            }
            catch (Exception ex)
            {
                CrashLog.WriteCrashLog(ex);
            }
        }

        private void ratebutton_click(object sender, EventArgs e)
        {
            MarketplaceReviewTask marketplaceReviewTask = new MarketplaceReviewTask();
            marketplaceReviewTask.Show();        
        }

        private void chatbutton_click(object sender, EventArgs e)
        {
            NavigationService.Navigate(new Uri("/FrontEnd/FacebookLogin.xaml", UriKind.Relative));
        }
        
 
        

        // Sample code for building a localized ApplicationBar
        //private void BuildLocalizedApplicationBar()
        //{
        //    // Set the page's ApplicationBar to a new instance of ApplicationBar.
        //    ApplicationBar = new ApplicationBar();

        //    // Create a new button and set the text value to the localized string from AppResources.
        //    ApplicationBarIconButton appBarButton = new ApplicationBarIconButton(new Uri("/Assets/AppBar/appbar.add.rest.png", UriKind.Relative));
        //    appBarButton.Text = AppResources.AppBarButtonText;
        //    ApplicationBar.Buttons.Add(appBarButton);

        //    // Create a new menu item with the localized string from AppResources.
        //    ApplicationBarMenuItem appBarMenuItem = new ApplicationBarMenuItem(AppResources.AppBarMenuItemText);
        //    ApplicationBar.MenuItems.Add(appBarMenuItem);
        //}
    }

}