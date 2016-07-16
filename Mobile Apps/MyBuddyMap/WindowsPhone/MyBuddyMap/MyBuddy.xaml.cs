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
    public partial class MyBuddy : PhoneApplicationPage
    {
        public MyBuddy()
        {
            InitializeComponent();
            InitializeAdControl();
            applicationTitle.Text = Constants.APPLICATION_NAME;           
            List<FaceBookFriend> fbfriend = CommonFacebookClass.lShowFriends;
            CommonFacebookClass.lShowFriends = null;
            applicationName.Text = String.Format(Constants.MYBUDDY_PAGE_TITLE, fbfriend.Count);
            locationTitle.Text = fbfriend[0].GetLocationName;
            longlistselector.ItemsSource = fbfriend;
        }

        private void InitializeAdControl()
        {
            AdCentre.SetAddControl(adcontrol);
            adcontrol.ErrorOccurred += adcontrol_ErrorOccurred;
        }

        void adcontrol_ErrorOccurred(object sender, Microsoft.Advertising.AdErrorEventArgs e)
        {

        }
    }
}