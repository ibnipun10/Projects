using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using SentiApp.Resources;
using Facebook.Client;
using Facebook;
using SentiApp.Common;
using System.ComponentModel;
using System.Windows.Media;
using Microsoft.Phone.Tasks;
using System.Threading;

namespace SentiApp
{
    public partial class MainPage : PhoneApplicationPage
    {

        private bool bLoginButton = false;
        public static List<FaceBookMessage> lmsg;
        public BindingClass _objBindClass;
        private int iSadX1 = 20;
        private int iNeutralX = 220;
        private int iHappyX2 = 420;
        private Thread background;
        private bool bCancel;
        

        // Constructor
        public MainPage()
        {
            InitializeComponent();
            AddFacebookLoginButton();
            _objBindClass = new BindingClass();
            messageText.Text = Constants.MAINPAGE_MESSAGE;
            InitializeNumberLine();
            InitializeAppName();
            InitializeAdControl();
            InitializeOtherControls();

            ShowContentPanel(false);
            txttillNow.Visibility = System.Windows.Visibility.Collapsed;
            setDataContext();
            InitializeAboutivotItem();    

        }

        private void InitializeOtherControls()
        {
            Submitbtn.Content = Constants.SUBMIT_BUTTON_SUBMIT;
        }

        private void ratebutton_click(object sender, EventArgs e)
        {
            MarketplaceReviewTask marketplaceReviewTask = new MarketplaceReviewTask();
            marketplaceReviewTask.Show();
        }

        private void InitializeAboutivotItem()
        {
            
            VersionBox.Text = "Version : " + Utlities.GetValuesFromAppXMLFile(Constants.APP_VERSION);
            AuthorBox.Text = "Author : " + Utlities.GetValuesFromAppXMLFile(Constants.APP_AUTHOR);
            AboutBox.Text = Constants.ABOUT_TEXT;
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

        private void aboutbutton_click(object sender, EventArgs e)
        {
            NavigationService.Navigate(new Uri("/About.xaml", UriKind.Relative));
        }

        private void InitializeAppName()
        {
            //appName.Text = Constants.APPLICATION_NAME;
        }

        private void EmailLink_Click(object sender, RoutedEventArgs e)
        {
            EmailComposeTask emailComposeTask = new EmailComposeTask();
            emailComposeTask.Subject = Constants.EMAIL_SUBJECT;
            emailComposeTask.Body = "";
            emailComposeTask.To = emailLink.Content.ToString();
            emailComposeTask.Show();
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

        private void setDataContext()
        {
            ContentPanel.DataContext = _objBindClass;
        }

        private void AddFacebookLoginButton()
        {
            try
            {              
                /*
                fbLoginButton.Margin = new Thickness(5);
                fbLoginButton.HorizontalAlignment = System.Windows.HorizontalAlignment.Right;
                fbLoginButton.FetchUserInfo = true;
                fbLoginButton.Permissions = "read_page_mailboxes,read_insights,read_mailbox,read_stream";
                fbLoginButton.ApplicationId = Constants.FB_SENTIAPP_APPLICATION_ID;
                fbLoginButton.SessionStateChanged += OnFacebookSessionStateChanged;
                fbLoginButton.UserInfoChanged += OnFacebookUserInfoChanged;                           
                */
            }
            catch (Exception ex)
            {
                //CrashLog.WriteCrashLog(ex);
            }
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
                    ShowContentPanel(true);
                    this.progressIndicator.IsVisible = false;    
                   
                  
                }
                else if (e.SessionState == Facebook.Client.Controls.FacebookSessionState.Closed)
                {
                    //logout
                    await new WebBrowser().ClearCookiesAsync();
                    //lmsg.Clear();
                    //EnableAppBarButtons(false);
                    //textMessage.Text = Constants.FB_LOGING_TEXT;
                    usernameId.Text = String.Empty;
                    this.progressIndicator.IsVisible = false;
                    fbLoginButton.IsEnabled = true;
                    ShowContentPanel(false);
                    bCancel = true;                        
                    ClearAllCounts();
                    EnterDays.Text = String.Empty;
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
        

        public void UpdateProgressIndicatorText(string message)
        {
            Dispatcher.BeginInvoke(new Action(() => this.SetProgressindicatorText(message)));
        }

        public void UpdateErrorText(string Error)
        {
            Dispatcher.BeginInvoke(new Action(() => this.SetErrorText(Error)));
        }

        public void RunAfterCompletion()
        {
            Dispatcher.BeginInvoke(new Action(() => this.RunAfterThreadControl()));
        }

        public void UpdateBindingObject(BindingClass objBindClass)
        {
            Dispatcher.BeginInvoke(new Action(() => this._objBindClass = objBindClass));
        }

        private async void PopulatePage()
        {
            BindingClass objBindClass = new BindingClass();
            try
            {
                List<FaceBookMessage> lfbMessage = null;
                while (true && !bCancel)
                {
                    if (lfbMessage == null)
                    {
                        UpdateProgressIndicatorText(Constants.FB_RETRIEVE_MESSAGES);
                        lfbMessage = await CommonFacebookClass.GetAllMessages();
                        if (bCancel)
                            break;
                        Dispatcher.BeginInvoke(new Action(() => this._objBindClass.fbMessageCount += lfbMessage.Count));
                       
                    }
                    else
                    {
                        UpdateProgressIndicatorText(Constants.FB_RETRIEVE_MESSAGES);
                        lfbMessage = await CommonFacebookClass.GetAllMessages();
                        if (bCancel)
                            break;
                        Dispatcher.BeginInvoke(new Action(() => this._objBindClass.fbMessageCount += lfbMessage.Count));
                    }

                    if (lfbMessage == null || lfbMessage.Count == 0)
                        break;

                    UpdateProgressIndicatorText(Constants.SENTIGEM_ANALYSE_MESSAGES);

                    try
                    {
                        for (int i = 0; i < lfbMessage.Count; i += 10)
                        {
                            List<FaceBookMessage> tempList = lfbMessage.Skip(i).Take(10).ToList();
                            SentiMessage sentimessage = await CommonSentiClass.GetSentiMessage(tempList);
                            if (bCancel)
                                break;
                            Dispatcher.BeginInvoke(new Action(() => this._objBindClass.SentiMessageCount += tempList.Count));                            
                            ChangeSentiCount(sentimessage, objBindClass);                           
                            Dispatcher.BeginInvoke(new Action(() => this.EnableTextTillNow(true)));
                        }
                    }
                    catch (Exception ex)
                    {
                        int a = 10;
                    }
                }
            }
            catch (Exception ex)
            {
               // CrashLog.WriteCrashLog(ex);
                UpdateErrorText(ex.Message);
            }

            RunAfterCompletion();
        }

        public void EnableControls(bool benable)
        {
            //Submitbtn.IsEnabled = benable;
            EnterDays.IsEnabled = benable;
        }

        public void EnableTextTillNow(bool bEnable)
        {
            if (bEnable)
                txttillNow.Visibility = System.Windows.Visibility.Visible;
            else
                txttillNow.Visibility = System.Windows.Visibility.Collapsed;
        }

        private void ChangeSentiCount(SentiMessage sentimessage, BindingClass objBindClass)
        {
            switch (sentimessage.polarity)
            {
                case "positive":
                    Dispatcher.BeginInvoke(new Action(() => this._objBindClass.Happy++));
                    MoveTriangleByThread(true);
                    break;
                case "neutral": if (_objBindClass.Happy == 0)
                        Dispatcher.BeginInvoke(new Action(() => this._objBindClass.Happy = 0)); 
                      break;
                case "negative":
                      Dispatcher.BeginInvoke(new Action(() => this._objBindClass.Happy--));
                      MoveTriangleByThread(false);
                    break;
                default: break;
            }
        }

        private void AddToObservableList(List<FaceBookMessage> lfbMessage)
        {
            foreach (FaceBookMessage fbMessage in lfbMessage)
                lmsg.Add(fbMessage);
        }

        private void SetProgressindicatorText(string text)
        {
            this.progressIndicator.IsVisible = true;
            this.progressIndicator.Text = text;
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

        private void Submit_Click(object sender, RoutedEventArgs e)
        {
            bCancel = false;
            if (((Button)(sender)).Content.Equals(Constants.SUBMIT_BUTTON_SUBMIT))
            {
                int days;
                ErrorBlock.Visibility = System.Windows.Visibility.Collapsed;
                string strdays = EnterDays.Text;

                ClearAllCounts();



                if (int.TryParse(strdays, out days))
                {
                    ((Button)(sender)).Content = Constants.SUBMIT_BUTTON_CANCEL;

                    //EnableControls(false);
                    SetProgressindicatorText(Constants.FB_RETRIEVE_MESSAGES);
                    CommonFacebookClass.UntilID = null;
                    EnableControls(false);
                    CommonFacebookClass.SetTillDate(days);

                    background = new Thread(PopulatePage);
                    background.Start();                   

                }
                else
                {
                    SetErrorText("Please enter an integer value");
                    EnterDays.Focus();
                }
            }
            else
            {
                if (background != null)
                    background.Abort();
                bCancel = true;

                ((Button)(sender)).Content = Constants.SUBMIT_BUTTON_SUBMIT;
            }
        }

        void RunAfterThreadControl()
        {
            //EnableControls(true);
            //GetAppBarIconbutton("Like").IsEnabled = false;
            this.progressIndicator.IsVisible = false;
            txttillNow.Visibility = System.Windows.Visibility.Collapsed;
            EnableControls(true);
            Submitbtn.Content = Constants.SUBMIT_BUTTON_SUBMIT;
        }

       
        private void ClearAllCounts()
        {
            //Clear all counts
            _objBindClass.fbMessageCount = 0;
            _objBindClass.Happy = 0;
            _objBindClass.SentiMessageCount = 0;
            _objBindClass.Sentiment = "";
            //EnterDays.Text = "";
            InitializeNumberLine();
        }

        public void MoveTriangleByThread(bool bPositive)
        {
            Dispatcher.BeginInvoke(new Action(() => this.MoveTriangle(bPositive)));   
        }

          private void MoveTriangle(bool bPositive)
        {
            PointCollection pc = triangle.Points;
             double iXIncrement = (iHappyX2 - iNeutralX) / 10;

             if (pc[0].X < iHappyX2 && pc[0].X > iSadX1)
             {
                 if (!bPositive)
                     iXIncrement = -iXIncrement;

                 Point top = new Point(pc[0].X + iXIncrement, pc[0].Y);
                 Point right = new Point(pc[1].X + iXIncrement, pc[1].Y);
                 Point left = new Point(pc[2].X + iXIncrement, pc[2].Y);
                 CreateTriangle(top, right, left);
             }
        }

          private void InitializeNumberLine()
          {
              CreateLine();
              Point top = new Point(iNeutralX, 0);
              Point right = new Point(iNeutralX + 20, 30);
              Point left = new Point(iNeutralX - 20, 30);
              CreateTriangle(top, right, left);
          }

          private void CreateLine()
          {
              SadLine.X1 = iSadX1;
              SadLine.X2 = iNeutralX;
              HappyLine.X1 = iNeutralX;
              HappyLine.X2 = iHappyX2;
          }

          private void CreateTriangle(Point top, Point right, Point left)
          {
              PointCollection pc = new PointCollection();
              pc.Add(top);
              pc.Add(right);
              pc.Add(left);
              triangle.Points = pc;
          }

    }



    public class BindingClass : INotifyPropertyChanged
    {
        private int _fbMessageCount;
        private int _SentiMessageCount;
        private string _strSentiment;

        private int _nHappy;  

        public int fbMessageCount
        { get {return _fbMessageCount;} 
          set
          {
              _fbMessageCount = value;
              NotifyPropertyChanged("fbMessageCount");
          }
        }

        public int SentiMessageCount
        {
            get { return _SentiMessageCount; }
            set
            {
                _SentiMessageCount = value;
                NotifyPropertyChanged("SentiMessageCount");
            }
        }

        public int Happy
        {
            get { return _nHappy; }
            set
            {
                _nHappy = value;

                if (_nHappy == 0)
                    Sentiment = Constants.SENTIMENT_NEUTRAL;
                else if (_nHappy > 0)
                    Sentiment = Constants.SENTIMENT_HAPPY;
                else
                    Sentiment = Constants.SENTIMENT_SAD;

                NotifyPropertyChanged("Happy");
            }
        }

        public string Sentiment
        {
            get
            {
                return _strSentiment;
            }
            set
            {
                _strSentiment = value;
                NotifyPropertyChanged("Sentiment");
            }
        }

      

        public event PropertyChangedEventHandler PropertyChanged;

        private void NotifyPropertyChanged(string propertyName)
        {
            if (PropertyChanged != null)
            {
                PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
            }
        }
    }
}