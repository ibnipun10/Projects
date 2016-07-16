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
using Microsoft.Phone.Tasks;

namespace MyBuddyMap
{
    public partial class About : PhoneApplicationPage
    {
        public About()
        {
            InitializeComponent();
            InitializeAboutivotItem();
        }

        private void InitializeAboutivotItem()
        {
            applicationName.Text = "about";
            VersionBox.Text = "Version : " + Utlities.GetValuesFromAppXMLFile(Constants.APP_VERSION);
            AuthorBox.Text = "Author : " + Utlities.GetValuesFromAppXMLFile(Constants.APP_AUTHOR);
            AboutBox.Text = Constants.ABOUT_TEXT;
            applicationTitle.Text = Constants.APPLICATION_NAME;
           
        }

        private void EmailLink_Click(object sender, RoutedEventArgs e)
        {
            EmailComposeTask emailComposeTask = new EmailComposeTask();
            emailComposeTask.Subject = Constants.EMAIL_SUBJECT;
            emailComposeTask.Body = "";
            emailComposeTask.To = emailLink.Content.ToString();
            emailComposeTask.Show();
        }

        private void ratebutton_click(object sender, RoutedEventArgs e)
        {
            MarketplaceReviewTask marketplaceReviewTask = new MarketplaceReviewTask();
            marketplaceReviewTask.Show();
        }
    }
    
}