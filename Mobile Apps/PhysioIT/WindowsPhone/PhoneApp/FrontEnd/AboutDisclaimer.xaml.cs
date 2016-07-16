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
using System.Xml.Linq;

namespace PhoneApp
{
    public partial class AboutDisclaimer : PhoneApplicationPage
    {
        public AboutDisclaimer()
        {
            try
            {
                InitializeComponent();
                pivotControl.Title = Constants.APPLICATION_TILE;

                InitializeAboutivotItem();
                InitializeDisclaimerItem();
                InitializeVersionItem();
            }
            catch (Exception ex)
            {
                CrashLog.WriteCrashLog(ex);
            }
        }

        private void InitializeDisclaimerItem()
        {
            disclaimerPivot.Header = "disclaimer";
            disclaimerBox.Text = Constants.DISCLAIMER_TEXT;
        }

        private void InitializeVersionItem()
        {
            VersionPivot.Header = Constants.ABOUT_VERSION_TITLE;

            int row = Constants.VERSION_DESC.Length / 2;

            for(int i=0; i<row; i++)
                PopulateEachVersionItem(Constants.VERSION_DESC[i,0], Constants.VERSION_DESC[i,1]);
            
        }

        private void PopulateEachVersionItem(string version, string versionDesc)
        {
            TextBlock versionControl = new TextBlock();
            TextBlock versionDescControl = new TextBlock();


            //Version
            versionControl.Text = version;
            Common.Font.TitleFont(versionControl);
            versionStackPanel.Children.Add(versionControl);

            //Verson Desc
            versionDescControl.Text = versionDesc;
            Common.Font.DescriptionFont(versionDescControl);
            versionDescControl.Margin = new Thickness(0, 0, 0, 20);
            versionStackPanel.Children.Add(versionDescControl);
        }

        private void InitializeAboutivotItem()
        {
            aboutPivot.Header = "about";
            VersionBox.Text = "Version : " + ApplicationSetting.GetValuesFromAppXMLFile(Constants.APP_VERSION);
            AuthorBox.Text = "Author : " + ApplicationSetting.GetValuesFromAppXMLFile(Constants.APP_AUTHOR);
            AboutBox.Text = Constants.ABOUT_TEXT;

           
        }

       
        
    }
}