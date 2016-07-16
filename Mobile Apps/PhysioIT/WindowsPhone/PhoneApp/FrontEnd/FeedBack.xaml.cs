using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using CommonBackstage;
using PhoneApp.Presentation;
using Microsoft.Phone.Info;
using PhoneApp.Common;

namespace PhoneApp.FrontEnd
{
    public partial class FeedBack : PhoneApplicationPage
    {
        public FeedBack()
        {
            InitializeComponent();
            Applicationtitle.Text = Constants.APPLICATION_TILE;
        }

        private async void Sendbutton_Click_1(object sender, RoutedEventArgs e)
        {
            try
            {
                if (string.IsNullOrEmpty(commentstxt.Text))
                {
                    
                    throw new Exception("Help me to make PhysioIT better. Plz suggests");
                }
                messagetxt.Text = "Sending Feedback....";
                messagetxt.Visibility = System.Windows.Visibility.Visible;

                FeedbackDB fdb = new FeedbackDB();
                fdb.Name = nametxt.Text;
                fdb.Email = emailtxt.Text;
                fdb.Comments = commentstxt.Text;
                fdb.DeviceId = Convert.ToBase64String((byte[])DeviceExtendedProperties.GetValue("DeviceUniqueId"));

                Azureinteractor az = new Azureinteractor();
                await az.InsertAzureTable<FeedbackDB>(fdb);
                messagetxt.Text = "FeedBack Sent :)";
            }
            catch (Exception ex)
            {
                messagetxt.Visibility = System.Windows.Visibility.Visible;
                messagetxt.Text = ex.Message;
            }
        }

        private void txtBoxTap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            messagetxt.Visibility = System.Windows.Visibility.Collapsed;
        }
    }
}