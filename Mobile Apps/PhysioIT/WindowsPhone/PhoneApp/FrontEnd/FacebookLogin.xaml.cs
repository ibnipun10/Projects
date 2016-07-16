using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using Facebook.Client;
using Facebook;
using System.Threading.Tasks;
using PhoneApp.Common;
using System.Reflection;
using System.Collections.ObjectModel;
using System.Globalization;
using System.Windows.Media;
using Microsoft.Phone.Tasks;


namespace PhoneApp.FrontEnd
{
    public partial class FacebookLogin : PhoneApplicationPage
    {
        public static FaceBookMessageList lmsg;
        private Facebook.Client.Controls.LoginButton fbLoginButton;
        private bool bLoginButton = false;

        public FacebookLogin()
        {
            try
            {

                InitializeComponent();
                applicationTitle.Text = Constants.APPLICATION_TILE;
                
                lmsg = new FaceBookMessageList();
                longlistselector.ItemsSource = lmsg;
                AddFacebookLoginButton();
                EnableAppBarButtons(false);
                textMessage.Text = Constants.FB_LOGING_TEXT;
                usernameId.Text = string.Empty;
            }
            catch (Exception ex)
            {
                CrashLog.WriteCrashLog(ex);
                SetErrorText(ex.Message);
            }
        }

        private void SetErrorText(string error)
        {
            ErrorBlock.Text = error;
            ErrorBlock.Visibility = System.Windows.Visibility.Visible;
        }

        private void AddFacebookLoginButton()
        {
            try
            {
                fbLoginButton = new Facebook.Client.Controls.LoginButton();
               
                FacebookSession session =  fbLoginButton.CurrentSession;
                fbLoginButton.Name = "loginButton";
                
                fbLoginButton.Margin = new Thickness(5);
                fbLoginButton.HorizontalAlignment = System.Windows.HorizontalAlignment.Right;
                fbLoginButton.FetchUserInfo = true;
                fbLoginButton.Permissions = "publish_stream,manage_pages,read_page_mailboxes";
                fbLoginButton.ApplicationId = Constants.FB_PHYSIOIT_APPLICATION_ID;
                fbLoginButton.SessionStateChanged +=OnFacebookSessionStateChanged;
                fbLoginButton.UserInfoChanged += OnFacebookUserInfoChanged;
                fbLoginButton.AuthenticationError += LoginButtonAuthenticationError;
                fbLoginButton.Tap += LoginButtonTap;
                fbLoginButton.SetValue(Grid.RowProperty, 3);
                LayoutRoot.Children.Add(fbLoginButton);
            }
            catch(Exception ex)
            {
                CrashLog.WriteCrashLog(ex);
            }
        }

        
        private void EnableControls(bool bEnable)
        {
            fbLoginButton.IsEnabled = bEnable;
            foreach (var button in ApplicationBar.Buttons)
            {
                ((ApplicationBarIconButton)button).IsEnabled = bEnable;
            }

        }

        private void EnableAppBarButtons(bool bEnable)
        {
            foreach (var button in ApplicationBar.Buttons)
            {
                ((ApplicationBarIconButton)button).IsEnabled = bEnable;
            }
        }

        private ApplicationBarIconButton GetAppBarIconbutton(string buttontext)
        {
            ApplicationBarIconButton Appbutton = null;
            foreach (var button in ApplicationBar.Buttons)
            {
                if (((ApplicationBarIconButton)button).Text.Equals(buttontext))
                    Appbutton = (ApplicationBarIconButton)button;
            }

            if (Appbutton == null)
                throw new Exception("ApplicationBar button not found");

            return Appbutton;    
        }

        

        private async void PopulatePage()
        {
            EnableControls(false);
            SetProgressindicatorText(Constants.FB_RETRIEVE_MESSAGES);

            try
            {
                List<FaceBookMessage> lfbMessage = null;
                while (true)
                {
                    if (lfbMessage == null)
                    {
                        lfbMessage = await CommonFacebookClass.GetAllMessages(CommonFacebookClass.PhysioITPageId, null);
                    }
                    else
                    {
                        lfbMessage = await CommonFacebookClass.GetAllMessages(CommonFacebookClass.PhysioITPageId,
                                                                              lfbMessage[lfbMessage.Count - 1].id);
                    }

                    if (lfbMessage == null || lfbMessage.Count == 0)
                        break;
                    else
                    {
                        AddToObservableList(lfbMessage);
                    }

                }
            }
            catch (Exception ex)
            {
                CrashLog.WriteCrashLog(ex);
                SetErrorText(ex.Message);
            }

            EnableControls(true);
           // GetAppBarIconbutton("Like").IsEnabled = false;
            this.progressIndicator.IsVisible = false;
        }

        private void SetProgressindicatorText(string text)
        {
            this.progressIndicator.IsVisible = true;
            this.progressIndicator.Text = text;
        }

        private async void OnFacebookSessionStateChanged(object sender, Facebook.Client.Controls.SessionStateChangedEventArgs e)
        {
            this.progressIndicator.IsVisible = false;
            ErrorBlock.Visibility = System.Windows.Visibility.Collapsed;
            bLoginButton = true;

            try
            {
                if (e.SessionState == Facebook.Client.Controls.FacebookSessionState.Opened)
                {
                    
                    CommonFacebookClass.fbClient = new FacebookClient(this.fbLoginButton.CurrentSession.AccessToken);
                    SetProgressindicatorText(Constants.FB_PAGE_LIKE);
                    bool bPageLike = true; // await CommonFacebookClass.IsPageLiked(CommonFacebookClass.PhysioITPageId);
                    this.progressIndicator.IsVisible = false;
                    if ( bPageLike )
                    {
                        //Show him the conversations
                        // lmsg = await CommonFacebookClass.GetAllMessages(CommonFacebookClass.PhysioITPageId, null);
                        EnableAppBarButtons(true);

                        /*
                        if(GetAppBarIconbutton("Like") != null)
                            GetAppBarIconbutton("Like").IsEnabled = false;
                         */
                        textMessage.Visibility = System.Windows.Visibility.Collapsed;
                        PopulatePage();
                    }
                    else
                    {
                        GetAppBarIconbutton("Like").IsEnabled = true;
                        longlistselector.Visibility = System.Windows.Visibility.Collapsed;
                        textMessage.Text = Constants.FB_LIKE_PAGE;                        
                    }

                }
                else if (e.SessionState == Facebook.Client.Controls.FacebookSessionState.Closed)
                {
                    //logout
                    await new WebBrowser().ClearCookiesAsync();                   
                    lmsg.Clear();
                    EnableAppBarButtons(false);
                    textMessage.Text = Constants.FB_LOGING_TEXT;
                    usernameId.Text = String.Empty;
                    this.progressIndicator.IsVisible = false;
                    fbLoginButton.IsEnabled = true;
                }
                else if (e.SessionState == Facebook.Client.Controls.FacebookSessionState.ClosedLoginFailed)
                {
                    throw new Exception("Login Failed");
                }
                else
                {
                    throw new Exception("Unable to connect");
                }

            }
            catch (Exception ex)
            {
                CrashLog.WriteCrashLog(ex);
                SetErrorText(ex.Message);
                this.progressIndicator.IsVisible = false;
                bLoginButton = false;
            }


        }

        private void AddToObservableList(List<FaceBookMessage> lfbMessage)
        {
            foreach (FaceBookMessage fbMessage in lfbMessage)
                lmsg.Add(fbMessage);
        }

        private void OnFacebookUserInfoChanged(object sender, Facebook.Client.Controls.UserInfoChangedEventArgs e)
        {
            usernameId.Text = "Welcome : " + e.User.FirstName;
            CommonFacebookClass.User = e.User;
        }

        private void LoginButtonAuthenticationError(object sender, Facebook.Client.Controls.AuthenticationErrorEventArgs e)
        {
            int a = 10;
        }

        private void LoginButtonTap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            if (bLoginButton)
            {
                if (string.IsNullOrEmpty(usernameId.Text))
                {
                    //login is clicked 
                    EnableControls(false);
                    SetProgressindicatorText(Constants.FB_LOGGING_IN);
                }
                else
                {
                    //logout is clicked
                    EnableControls(false);
                    SetProgressindicatorText(Constants.FB_LOGGING_OUT);
                }
            }
            else
            {
                bLoginButton = true;
            }
        }

        private void OnMessageTap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            Grid grid = sender as Grid;
            UIElementCollection children = grid.Children;
            int index = 0;

            foreach (UIElement child in children)
            {
                TextBlock textblockChild = child as TextBlock;
                if (textblockChild.Name.Equals("messageIndex"))
                {
                    index = Convert.ToInt32(textblockChild.Text);
                    break;
                }

            }

            FaceBookMessage objectToPass = lmsg[index];

            PhoneApplicationService.Current.State["facebookMessageObject"] = objectToPass;
            this.NavigationService.Navigate(new Uri("/FrontEnd/FacebookComments.xaml", UriKind.Relative));
        }

        private void AddChatButtonClick(object sender, EventArgs e)
        {
            PhoneApplicationService.Current.State["facebookMessageObject"] = null;
            this.NavigationService.Navigate(new Uri("/FrontEnd/FacebookComments.xaml", UriKind.Relative));
        }

        private void RefreshButtonClick(object sender, EventArgs e)
        {
            ErrorBlock.Visibility = System.Windows.Visibility.Collapsed;
            lmsg.Clear();
            PopulatePage();
        }

        private void LikeButtonClick(object sender, EventArgs e)
        {
            string page = "https://www.facebook.com/pages/Physioit/" + CommonFacebookClass.PhysioITPageId;
            var Uri = new Uri(page);
            WebBrowserTask browserTask = new WebBrowserTask();
            browserTask.Uri = Uri;
            browserTask.Show();
        }

    }

    public class IndexConverter : System.Windows.Data.IValueConverter
    {

        public object Convert(
            object value,
            Type targetType,
            object parameter,
            CultureInfo culture)
        {
            FaceBookMessage item = (FaceBookMessage)value;
            return FacebookLogin.lmsg.IndexOf(item).ToString();
        }

        public object ConvertBack(
            object value,
            Type targetType,
            object parameter,
            CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }

   
}