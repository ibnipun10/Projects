using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using MyBuddyMap.Common;

namespace MyBuddyMap
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

        private void InitializeControls()
        {

            applicationName.Text = Constants.APPLICATION_NAME;
            applicationTitle.Text = Constants.SETTINGS_TEXT;

            privacy.Text = "Privacy Control";
            locationprivacytext.Text = Constants.SETTINGS_LOCATION_PRIVACY_TEXT;

            String value = ApplicationSetting.ReadApplicationSetting(Constants.SETTINGS_LOCATION_SERVICE);
            if(value == null)
                toggleswitchlocation.IsChecked = true;
            else
                toggleswitchlocation.IsChecked = ((value == Constants.SETTINGS_YES) ? true : false);
          
        }       
    }
}