using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using SentiApp.Common;
using Microsoft.Phone.Tasks;

namespace SentiApp
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
            aboutPivot.Text = "about";
            VersionBox.Text = "Version : " + Utlities.GetValuesFromAppXMLFile(Constants.APP_VERSION);
            AuthorBox.Text = "Author : " + Utlities.GetValuesFromAppXMLFile(Constants.APP_AUTHOR);
            AboutBox.Text = Constants.ABOUT_TEXT;
            appName.Text = Constants.APPLICATION_NAME;
           
        }

        private void EmailLink_Click(object sender, RoutedEventArgs e)
        {
            EmailComposeTask emailComposeTask = new EmailComposeTask();
            emailComposeTask.Subject = Constants.EMAIL_SUBJECT;
            emailComposeTask.Body = "";
            emailComposeTask.To = emailLink.Content.ToString();
            emailComposeTask.Show();
        }
    }
}