using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using MyBuddyMap.Resources;
using System.Threading;
using MyBuddyMap.Common;
using Microsoft.Phone.Maps;
using Facebook;

namespace MyBuddyMap
{
    public partial class MainPage : PhoneApplicationPage
    {

        // Constructor
        public MainPage()
        {
            InitializeComponent();
            messageText.Text = Constants.MAINPAGE_MESSAGE;
            applicationTitle.Text = Constants.APPLICATION_NAME;
            ShowContentPanel(false);
        }

        private async void OnFacebookSessionStateChanged(object sender, Facebook.Client.Controls.SessionStateChangedEventArgs e)
        {
            
            ErrorBlock.Visibility = System.Windows.Visibility.Collapsed;

            try
            {
                if (e.SessionState == Facebook.Client.Controls.FacebookSessionState.Opened)
                {

                    CommonFacebookClass.fbClient = new FacebookClient(this.fbLoginButton.CurrentSession.AccessToken);
                    ShowContentPanel(true);                   


                }
                else if (e.SessionState == Facebook.Client.Controls.FacebookSessionState.Closed)
                {
                    //logout
                    await new WebBrowser().ClearCookiesAsync();
                    //lmsg.Clear();
                    //EnableAppBarButtons(false);
                    //textMessage.Text = Constants.FB_LOGING_TEXT;
                    usernameId.Text = String.Empty;
                    fbLoginButton.IsEnabled = true;
                    ShowContentPanel(false);
                   
                }
                else if (e.SessionState == Facebook.Client.Controls.FacebookSessionState.ClosedLoginFailed)
                {
                    throw new Exception("Login Failed");
                }
                else if (e.SessionState == Facebook.Client.Controls.FacebookSessionState.Opening)
                {
                    throw new Exception("Trying to Login");
                }
                else
                {
                    throw new Exception("Unable to connect");
                }

            }
            catch (Exception ex)
            {
                SetErrorText(ex.Message);
            }


        }

        private void ShowContentPanel(bool bVisible)
        {
            if (bVisible)
            {
                stackpanel_Content.Visibility = System.Windows.Visibility.Visible;
                // stacksubmitline.Visibility = System.Windows.Visibility.Visible;
                messageText.Visibility = System.Windows.Visibility.Collapsed;
            }
            else
            {
               stackpanel_Content.Visibility = System.Windows.Visibility.Collapsed;
                //  stacksubmitline.Visibility = System.Windows.Visibility.Collapsed;
               messageText.Visibility = System.Windows.Visibility.Visible;
            }
        }

        private void SetErrorText(string error)
        {
            ErrorBlock.Text = error;
            ErrorBlock.Visibility = System.Windows.Visibility.Visible;
        }

        private void OnFacebookUserInfoChanged(object sender, Facebook.Client.Controls.UserInfoChangedEventArgs e)
        {
            usernameId.Text = "Welcome : " + e.User.FirstName;
            CommonFacebookClass.User = e.User;
        }

        private void OnButtonClick(object sender, RoutedEventArgs e)
        {
          
            this.NavigationService.Navigate(new Uri("/MyMap.xaml", UriKind.Relative));
            CommonFacebookClass.bHomeTown = false;
        }

        private void aboutbutton_click(object sender, EventArgs e)
        {
            NavigationService.Navigate(new Uri("/About.xaml", UriKind.Relative));
        }

        private void settingsbutton_click(object sender, EventArgs e)
        {
            NavigationService.Navigate(new Uri("/Settings.xaml", UriKind.Relative));
        }
        

        private void OnHomtownbtnClick(object sender, RoutedEventArgs e)
        {
            CommonFacebookClass.bHomeTown = true;
            NavigateToMap();
        }

        private void NavigateToMap()
        {
            this.NavigationService.Navigate(new Uri("/MyMap.xaml", UriKind.Relative));
        }

        private void OnlocationbtnClick(object sender, RoutedEventArgs e)
        {
            CommonFacebookClass.bHomeTown = false;
            NavigateToMap();
        }
       
    }
}