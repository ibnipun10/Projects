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
using Coding4Fun.Toolkit.Controls;
using System.Threading.Tasks;
using System.Windows.Media;

namespace PhoneApp.FrontEnd
{
    public partial class FacebookComments : PhoneApplicationPage
    {
        FaceBookMessage _fbmessage;
        string _sender;
        bool _bRight;
        Color messageColor = Colors.Gray;
        Color commentColor = Colors.Green;
        Color ownerColor = Colors.Orange;
        Color AdminColor = Colors.Red;

        public FacebookComments()
        {
            InitializeComponent();
            applicationTitle.Text = Constants.APPLICATION_TILE;
            _bRight = true;
            _sender = null;

            try
            {
                //Get the object
                _fbmessage = PhoneApplicationService.Current.State["facebookMessageObject"] as FaceBookMessage;

                if (_fbmessage != null)
                    PopulatePage(_fbmessage);
            }
            catch (Exception ex)
            {
                WriteToErrorText(ex.Message);
                CrashLog.WriteCrashLog(ex);

            }

        }

        private void SetProgressindicatorText(string text)
        {
            this.progressIndicator.IsVisible = true;
            this.progressIndicator.Text = text;
        }

        private async void PopulatePage(FaceBookMessage fbmessage)
        {
            SetProgressindicatorText(Constants.FB_RETRIEVE_COMMENTS);
            try
            {

                AddCreatorChat(fbmessage, messageColor);
                FaceBookMessageList lfbMessage = null;

                while (true)
                {
                    if (lfbMessage == null)
                        lfbMessage = await CommonFacebookClass.GetAllComments(fbmessage.id, null);
                    else
                        lfbMessage = await CommonFacebookClass.GetAllComments(fbmessage.id, lfbMessage[lfbMessage.Count - 1].id);

                    if (lfbMessage == null || lfbMessage.Count == 0)
                        break;

                    PopulateChatPage(lfbMessage);
                }
            }
            catch (Exception ex)
            {
                WriteToErrorText(ex.Message);
                CrashLog.WriteCrashLog(ex);
            }

            this.progressIndicator.IsVisible = false;

        }

        private void WriteToErrorText(string p)
        {
            ErrorText.Text = p;
            ErrorText.Visibility = System.Windows.Visibility.Visible;
        }

        private void AddCreatorChat(FaceBookMessage fbmessage, Color color)
        {
            ChatBubble chatbuble = new ChatBubble();

            chatbuble.Background = new SolidColorBrush(color);
            chatbuble.BorderBrush = new SolidColorBrush(color);

            chatbuble.ChatBubbleDirection = ChatBubbleDirection.UpperLeft;
            chatbuble.HorizontalAlignment = System.Windows.HorizontalAlignment.Stretch;
            chatbuble.HorizontalContentAlignment = System.Windows.HorizontalAlignment.Stretch;
            chatbuble.Margin = new Thickness(0, 5, 10, 5);

            StackPanel stkpanel = FillChatBubble(fbmessage);
            chatbuble.Content = stkpanel;


            CreatorChatHeader.Children.Add(chatbuble);
        }

        private void PopulateChatPage(FaceBookMessage fbMessage, Color color)
        {
            if (!string.IsNullOrEmpty(_sender))
            {
                if (!_sender.Equals(fbMessage.from.id))
                {
                    _sender = fbMessage.from.id;
                    _bRight = !_bRight;
                }
            }
            else
                _sender = fbMessage.from.id;

            

            ChatBubble chatbubble = new ChatBubble(); 
            chatbubble.Width = 300;
         
            chatbubble.Background = new SolidColorBrush(color);
            chatbubble.BorderBrush = new SolidColorBrush(color);

            if (_bRight)
            {
                chatbubble.ChatBubbleDirection = ChatBubbleDirection.UpperRight;
                chatbubble.HorizontalAlignment = System.Windows.HorizontalAlignment.Right;
               
            }
            else
            {
                chatbubble.ChatBubbleDirection = ChatBubbleDirection.UpperLeft;
                chatbubble.HorizontalAlignment = System.Windows.HorizontalAlignment.Left;
            }

            chatbubble.Margin = new Thickness(0, 5, 10, 5);

            StackPanel stkpanel = FillChatBubble(fbMessage);
            stkpanel.Width = chatbubble.Width - 20;
            chatbubble.Content = stkpanel;

            parentStackPanel.Children.Add(chatbubble);
            scrollViewerId.Measure(scrollViewerId.RenderSize);
            scrollViewerId.ScrollToVerticalOffset(scrollViewerId.ScrollableHeight);
            

        }

        private void PopulateChatPage(FaceBookMessageList lmessage)
        {

            
            foreach (FaceBookMessage fbMessage in lmessage)
            {
               
                PopulateChatPage(fbMessage, GetColorofBubble(fbMessage));
            }
        }

        private Color GetColorofBubble(FaceBookMessage fbMessage)
        {
            Color color = commentColor;

            if (fbMessage.from.id.Equals(CommonFacebookClass.PhysioITPageId))  /* admin color */
                color = AdminColor;
            else if (fbMessage.from.id.Equals(CommonFacebookClass.User.Id))   /* owner color */
                color = ownerColor;
            else
                color = commentColor;

            return color;
        }

        private StackPanel FillChatBubble(FaceBookMessage fbMessage)
        {
            StackPanel topStackPanel = new StackPanel();

            // Message
            TextBlock comment = new TextBlock();
            comment.TextWrapping = TextWrapping.Wrap;
            comment.Text = fbMessage.message;
            topStackPanel.Children.Add(comment);

            // Sender
            Grid childGrid = new Grid();

            TextBlock sender = new TextBlock();
            sender.Text = fbMessage.from.name;
            childGrid.Children.Add(sender);

            //Time
            TextBlock time = new TextBlock();
            time.Text = fbMessage.GetCreatedDate;
            time.HorizontalAlignment = System.Windows.HorizontalAlignment.Right;
            childGrid.Children.Add(time);

            topStackPanel.Children.Add(childGrid);
            return topStackPanel;
        }

        private async void OnSendButtonClick(object sender, System.Windows.Input.GestureEventArgs e)
        {
            EnableControls(false);
            ErrorText.Visibility = System.Windows.Visibility.Collapsed;
            if (!string.IsNullOrEmpty(chatbubbleText.Text))
            {
                try
                {
                    if (_fbmessage == null)
                    {
                        SetProgressindicatorText(Constants.FB_POST_MESSAGE);
                        _fbmessage = await CommonFacebookClass.SendMessage(CommonFacebookClass.PhysioITPageId, chatbubbleText.Text);
                        AddCreatorChat(_fbmessage, messageColor);
                        chatbubbleText.Text = string.Empty;
                    }
                    else
                    {
                        SetProgressindicatorText(Constants.FB_POST_COMMENT);
                        FaceBookMessage fbMessage = await CommonFacebookClass.SendComment(_fbmessage.id, chatbubbleText.Text);
                        PopulateChatPage(fbMessage, ownerColor);
                        chatbubbleText.Text = string.Empty;
                    }
                }
                catch (Exception ex)
                {
                    WriteToErrorText(ex.Message);
                    CrashLog.WriteCrashLog(ex);
                }
            }
            else
            {
                //please enter something
                ErrorText.Text = Constants.FB_EMPTY_TEXT_ERROR;
                ErrorText.Visibility = System.Windows.Visibility.Visible;
            }

            EnableControls(true);
            this.progressIndicator.IsVisible = false;
        }

        private void EnableControls(bool bEnable)
        {
            chatbubbleText.IsEnabled = bEnable;
            SendButton.IsEnabled = bEnable;
        }
    }
}