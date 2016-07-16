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
using PhoneApp.Presentation;
using PhoneApp.BackStage;
using CommonBackstage;


namespace PhoneApp
{
    public partial class CommonProblems : PhoneApplicationPage
    {
        private string _id;
        private Interactor _worker;
        private bool bNavigated;
        private string _bodyPartName;
        private string commonProblemsID;
            
        public CommonProblems()
        {
            InitializeComponent();
            InitializeAdControl();
            _worker = new Interactor();
            Pivot_Causes.Title = Constants.APPLICATION_TILE;
            commonProblemsID = "1001";
            bNavigated = false;
        }

        private void InitializeAdControl()
        {
            AdCentre.SetAddControl(adcontrol);
            adcontrol.VerticalAlignment = System.Windows.VerticalAlignment.Bottom;
            adcontrol.ErrorOccurred += adcontrol_ErrorOccurred;
            
        }

        void adcontrol_ErrorOccurred(object sender, Microsoft.Advertising.AdErrorEventArgs e)
        {
            //throw new NotImplementedException();
        }

        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            base.OnNavigatedTo(e);
            if (!bNavigated)
            {
                NavigationContext.QueryString.TryGetValue("ID", out _id);
                NavigationContext.QueryString.TryGetValue("Name", out _bodyPartName);

                if (_id.CompareTo("1001") == 0)
                {
                    Reasons.Header = Constants.COMMON_PROBLEMS_NAME;
                    Remedy.Header = Constants.COMMON_PRECAUTIONS_NAME;
                    
            
                }
                else
                {
                    Reasons.Header = Constants.REASONS_NAME;
                    Remedy.Header = Constants.REMEDY_NAME;
                    
                }
                PopulateReasonsPage();
                PopulateRemedyPage();
                bNavigated = true;
            }

           
        }

       

        private void PopulateRemedyPage()
        {
            //Get all the objects with ID
            List<RemedyDB> remedydb = _worker.GetRows<RemedyDB>(Convert.ToInt32(_id));
            tapOnTitle.Visibility = System.Windows.Visibility.Visible;

            foreach (RemedyDB rdb in remedydb)
            {
                //StackPanel_Problems.RowDefinitions.Add(new RowDefinition());
                TextBlock titleBlock = new TextBlock();
                TextBlock desciptionBlock = new TextBlock();
                

                //Title TextBlock
                if (!string.IsNullOrEmpty(rdb.Title))
                {
                    Common.Font.TitleFont(titleBlock);

                    String[] strTitle = rdb.Title.Split(Constants.CHARACTER_SEPERATOR);

                    titleBlock.Text = strTitle[0];

                    string pageTitle = Constants.EXCERCISE_TITLE;
                    if (strTitle.Count() > 1)
                        pageTitle = strTitle[1];

                    titleBlock.Name = rdb.PartID.ToString() + "_" + rdb.RemedyId.ToString() + "_" + pageTitle;
                    titleBlock.Tap += titleBlock_Tap;
                    if (!string.IsNullOrEmpty(titleBlock.Text))
                        StackPanel_Remedy.Children.Add(titleBlock);
                }

                //Description TextBlock
                if (!string.IsNullOrEmpty(rdb.Description))
                {
                    Common.Font.DescriptionFont(desciptionBlock);
                    desciptionBlock.Text = rdb.Description;
                    StackPanel_Remedy.Children.Add(desciptionBlock);
                }
                StackPanel_Remedy.Children.Add(new TextBlock());
            }
            
        }

        void titleBlock_Tap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            TextBlock remedy = (TextBlock)sender;
            string textBlockName = remedy.Name;

            string[] strIDs = textBlockName.Split('_');
            NavigationService.Navigate(new Uri("/FrontEnd/Posture.xaml?ID="+strIDs[0]+"&Name="+_bodyPartName+"&RemedyId="+strIDs[1]+"&PageTitle="+strIDs[2], UriKind.Relative));
        }

        public void PopulateReasonsPage()
        {
            //Get all the objects with ID
            List<ReasonsDB> problemsdb = _worker.GetRows<ReasonsDB>(Convert.ToInt32(_id));
            int nRow = 0;            

            foreach (ReasonsDB pdb in problemsdb)
            {
                //StackPanel_Problems.RowDefinitions.Add(new RowDefinition());
                TextBlock titleBlock = new TextBlock();
                TextBlock desciptionBlock = new TextBlock();

                //Title TextBlock
                if (!string.IsNullOrEmpty(pdb.Title))
                {
                    Common.Font.TitleFont(titleBlock);
                    titleBlock.Text = pdb.Title;
                    //titleBlock.SetValue(Grid.RowProperty, nRow);
                    StackPanel_Problems.Children.Add(titleBlock);
                    nRow++;
                }

                //Description TextBlock
                if (!string.IsNullOrEmpty(pdb.Description))
                {
                    
                    Common.Font.DescriptionFont(desciptionBlock);
                    desciptionBlock.Text = pdb.Description;
                    //desciptionBlock.SetValue(Grid.RowProperty, nRow);
                    StackPanel_Problems.Children.Add(desciptionBlock);
                }
                StackPanel_Problems.Children.Add(new TextBlock());
                nRow++;
            }
            
        }

        private void Pivot_Causes_SelectionChanged_1(object sender, SelectionChangedEventArgs e)
        {
            if (Pivot_Causes.SelectedIndex == 1)
            {
                if (_id.CompareTo(commonProblemsID) != 0)
                    tapOnTitle.Visibility = System.Windows.Visibility.Visible;
            }
            else
                tapOnTitle.Visibility = System.Windows.Visibility.Collapsed;            
            
        }

     
    }
}