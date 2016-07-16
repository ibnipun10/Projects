using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using PhoneApp.Common;
using System.Windows.Media;

namespace PhoneApp.FrontEnd
{
    public partial class Settings : PhoneApplicationPage
    {
        public Settings()
        {
            InitializeComponent();
            InitializeControls();
            }

        private void toggleswitchlocation_Checked(object sender, RoutedEventArgs e)
        {
            ApplicationSetting.AddApplicationSetting(Constants.SETTINGS_LOCATION_SERVICE, Constants.SETTINGS_YES);
        }

        private void toggleswitchlocation_UnChecked(object sender, RoutedEventArgs e)
        {
            ApplicationSetting.AddApplicationSetting(Constants.SETTINGS_LOCATION_SERVICE, Constants.SETTINGS_NO);
        }

        private void toggleswitchAzureService_Checked(object sender, RoutedEventArgs e)
        {
            ApplicationSetting.AddApplicationSetting(Constants.SETTINGS_AZURE_UPDATE, Constants.SETTINGS_YES);
        }

        private void toggleswitchAzureService_UnChecked(object sender, RoutedEventArgs e)
        {
            ApplicationSetting.AddApplicationSetting(Constants.SETTINGS_AZURE_UPDATE, Constants.SETTINGS_NO);
        }

        private void InitializeControls()
        {

            applicationName.Text = Constants.APPLICATION_TILE;
            applicationTitle.Text = Constants.SETTINGS_TEXT;
        
           toggleswitchlocation.IsChecked = (ApplicationSetting.ReadApplicationSetting(Constants.SETTINGS_LOCATION_SERVICE) == Constants.SETTINGS_YES ? true : false);
           toggleswitchAzureUpdate.IsChecked = (ApplicationSetting.ReadApplicationSetting(Constants.SETTINGS_AZURE_UPDATE) == Constants.SETTINGS_YES ? true : false);


           privacy.Text = "Privacy Control";
           locationprivacytext.Text =  Constants.SETTINGS_LOCATION_PRIVACY_TEXT;
           dataprivacycontrol.Text = Constants.SETTINGS_DATA_PRIVACY_TEXT;
        
        }       
    }
}