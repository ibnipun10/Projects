using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using PhoneApp.Presentation;
using PhoneApp.Common;
using System.Windows.Media.Imaging;
using PhoneApp.BackStage;
using CommonBackstage;

namespace PhoneApp
{
    public partial class Posture : PhoneApplicationPage
    {

        private int _id;
        private int _remedyId;
        private Interactor _worker;
        private bool bNavigated;
        private string _bodyPartName;
        private string _excerciseTitle;

        public Posture()
        {
            InitializeComponent();
            InitializeAdControl();
            _worker = new Interactor();
            PivotControl.Title = Constants.APPLICATION_TILE;
            bNavigated = false;
        }

        private void InitializeAdControl()
        {
            AdCentre.SetAddControl(adcontrol);
            adcontrol.ErrorOccurred += adcontrol_ErrorOccurred;
            adcontrol.VerticalAlignment = System.Windows.VerticalAlignment.Bottom;
        }

        void adcontrol_ErrorOccurred(object sender, Microsoft.Advertising.AdErrorEventArgs e)
        {

        }


        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            base.OnNavigatedTo(e);
            if (!bNavigated)
            {
                string strID;
                string strRemedyId;
                NavigationContext.QueryString.TryGetValue("ID", out strID);
                NavigationContext.QueryString.TryGetValue("Name", out _bodyPartName);
                NavigationContext.QueryString.TryGetValue("RemedyId", out strRemedyId);
                NavigationContext.QueryString.TryGetValue("PageTitle", out _excerciseTitle);

                _id = Convert.ToInt32(strID);
                _remedyId = Convert.ToInt32(strRemedyId);

                PopulatePosturePage();
                PopulateExcercisePage();
                bNavigated = true;
            }

        }

        public void PopulatePosturePage()
        {
            List<PostureDB> posturedb = _worker.GetRows<PostureDB>(_id, _remedyId);

            foreach (PostureDB pdb in posturedb)
            {
                StackPanel stpanel = AddPivotitem("Posture");

                //Wrong posture title
                
                TextBlock wrongtitle = new TextBlock();
                Common.Font.TitleFont(wrongtitle);
                wrongtitle.Text = Common.Constants.POSTURE_WRONG_TEXT;
                wrongtitle.TextAlignment = TextAlignment.Center;
                stpanel.Children.Add(wrongtitle);

                //Wrong posture Image
                Image wrongImg = new Image();
                Font.ImageLook(wrongImg);
                Uri imageUri = new Uri("/" + Constants.IMAGES_PATH + "/" + pdb.PartID + "/" + pdb.Wrong_Img, UriKind.Relative);                
                wrongImg.Source = new BitmapImage(imageUri);
                stpanel.Children.Add(wrongImg);

                //Wrong posture desciption
                TextBlock wrongdesc = new TextBlock();
                Common.Font.DescriptionFont(wrongdesc);
                wrongdesc.Text = pdb.Wrong;
                wrongdesc.TextAlignment = TextAlignment.Center;
                stpanel.Children.Add(wrongdesc);

                //Right Posture title
                TextBlock righttitle = new TextBlock();
                Common.Font.TitleFont(righttitle);
                righttitle.Text = Common.Constants.POSTURE_RIGHT_TEXT;
                righttitle.TextAlignment = TextAlignment.Center;
                stpanel.Children.Add(righttitle);

                //Right posture Image
                Image rightImg = new Image();
                Font.ImageLook(rightImg);
                imageUri = new Uri("/" + Constants.IMAGES_PATH + "/" + pdb.PartID + "/" + pdb.Right_Img, UriKind.Relative);
                rightImg.Source = new BitmapImage(imageUri);
                stpanel.Children.Add(rightImg);

                //Right posture description
                TextBlock rightdesc = new TextBlock();
                Common.Font.DescriptionFont(rightdesc);
                rightdesc.Text = pdb.Right;
                rightdesc.TextAlignment = TextAlignment.Center;
                stpanel.Children.Add(rightdesc);
            }
        }

        public void PopulateExcercisePage()
        {
            List<ExcerciseDB> excercisedb = _worker.GetRows<ExcerciseDB>(_id, _remedyId);
            int nCount = 1;

            foreach (ExcerciseDB edb in excercisedb)
            {

                StackPanel stpanel = AddPivotitem(_excerciseTitle);
       

                //Add excercise title 
                if (!string.IsNullOrEmpty(edb.Title))
                {
                    TextBlock title = new TextBlock();
                    Common.Font.TitleFont(title);
                    title.Text = nCount.ToString() + ". " + edb.Title;
                    stpanel.Children.Add(title);
                }


                //Add image
                if (!string.IsNullOrEmpty(edb.Image))
                {
                    Image img = new Image();
                    Font.ImageLook(img);
                    Uri imageUri = new Uri("/" + Constants.IMAGES_PATH + "/" + edb.PartID + "/" + edb.Image, UriKind.Relative);
                    img.Source = new BitmapImage(imageUri);
                    stpanel.Children.Add(img);
                }

                //Add desciprion
                if (!string.IsNullOrEmpty(edb.Description))
                {
                    TextBlock desc = new TextBlock();
                    Common.Font.TitleFont(desc);
                    desc.Text = Constants.POSTURE_DESCRIPTION;
                    stpanel.Children.Add(desc);


                    //add desciption test
                    TextBlock description = new TextBlock();
                    Common.Font.DescriptionFont(description);
                    description.Text = edb.Description;
                    stpanel.Children.Add(description);
                }

                if (!string.IsNullOrEmpty(edb.Dosage))
                {
                    //Add dosage
                    TextBlock dos = new TextBlock();
                    Common.Font.TitleFont(dos);
                    dos.Text = Constants.POSTURE_DOSAGE;
                    stpanel.Children.Add(dos);

                    //add dosage text
                    TextBlock dosage = new TextBlock();
                    Common.Font.DescriptionFont(dosage);
                    dosage.Text = edb.Dosage;
                    stpanel.Children.Add(dosage);
                }

                //add Precautions
                if (!string.IsNullOrEmpty(edb.Precautions))
                {
                    TextBlock prec = new TextBlock();
                    Common.Font.TitleFont(prec);
                    prec.Text = Constants.POSTURE_PRECAUTIONS;
                    stpanel.Children.Add(prec);

                    //add Precautions text
                    TextBlock precuations = new TextBlock();
                    Common.Font.DescriptionFont(precuations);
                    precuations.Text = edb.Precautions;
                    stpanel.Children.Add(precuations);
                }

                nCount++;

            }
        }

        public StackPanel AddPivotitem(string pivotHeader)
        {
            PivotItem pivotItem = new PivotItem();
            
            pivotItem.Header = pivotHeader;

            Grid grid = new Grid();
            ScrollViewer scrollviewer = new ScrollViewer();
            StackPanel stackpanel = new StackPanel();
            scrollviewer.Content = stackpanel;
            grid.Children.Add(scrollviewer);
            pivotItem.Content = grid;
            PivotControl.Items.Add(pivotItem);

            return stackpanel;

        }
    }
}