package com.product.physioit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import Common.Constants;
import Common.PhysioITTables;
import Common.Utilities;
import Controller.AzureController;
import Controller.DBController;

/**
 * Created by nipuna on 8/26/13.
 */
public class FeedbackActivity extends Activity {

    private DBController _dbController;
    private EditText suggestionText;
    private EditText nameText;
    private EditText emailText;
    private TextView SubmissionText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);

        InitializeComponents();
        _dbController = Utilities.getDbController(this);
    }

    private void InitializeComponents() {

        //Feedback Title
        TextView feedbackTitle = (TextView)findViewById(R.id.feedbackTitle);
        feedbackTitle.setText(Constants.FEEDBACK_PAGE_TITLE);
        Utilities.SetTitleFont(feedbackTitle, getResources(), Gravity.CENTER_HORIZONTAL);

        //LabelName
        TextView labelName = (TextView)findViewById(R.id.labelName);
        labelName.setText(Constants.FEEDBACK_PAGE_LABEL_NAME);
        Utilities.SetTitleFont(labelName, getResources());

        //Name text
        nameText = (EditText)findViewById(R.id.nameText);
        Utilities.SetDescriptionFont(nameText, getResources());

        //Label email
        TextView labelEmail = (TextView)findViewById(R.id.emailLabel);
        labelEmail.setText(Constants.FEEDBACK_PAGE_LABEL_EMAIL);
        Utilities.SetTitleFont(labelEmail, getResources());

        //email text
        emailText = (EditText)findViewById(R.id.emailText);
        Utilities.SetDescriptionFont(emailText, getResources());

        //Suggestion Label
        TextView labelSuggest = (TextView)findViewById(R.id.suggestionLabel);
        labelSuggest.setText(Constants.FEEDBACK_PAGE_LABEL_SUGGESTIONS);
        Utilities.SetTitleFont(labelSuggest, getResources());

        //Suggestion text
        suggestionText = (EditText)findViewById(R.id.suggestionText);
        Utilities.SetDescriptionFont(suggestionText, getResources());

        //Submit button
        Button submitButton = (Button)findViewById(R.id.submissionButton);
        submitButton.setText(Constants.SUBMIT_BUTTON);
        submitButton.setOnClickListener(new SubmitButtonListener(this));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int Margin = Utilities.GetValueinPixels(10, getResources());
        params.setMargins(Margin, Margin, Margin, Margin);
        submitButton.setLayoutParams(params);
        submitButton.setTypeface(null, Typeface.BOLD);
        submitButton.setTextColor(Color.BLUE);

        SubmissionText = (TextView)findViewById(R.id.SubmissionText);
        SubmissionText.setText("");
        Utilities.SetDescriptionFont(SubmissionText, getResources());
        SubmissionText.setVisibility(TextView.INVISIBLE);

    }

    private class SubmitButtonListener implements View.OnClickListener
    {
        private Context _context;
        SubmitButtonListener(Context context)
        {
            _context = context;
        }

        @Override
        public void onClick(View view)
        {
            try
            {
            if(Utilities.IsNullOrEmpty(suggestionText.getText().toString()))
            {
               throw new Exception(Constants.FEEDBACK_PAGE_BLANK_ERROR);
            }

            else
            {

                SubmissionText.setVisibility(TextView.VISIBLE);
                SubmissionText.setText(Constants.FEEDBACK_PAGE_SENDING);

                final AzureController azureController = new AzureController(_dbController, _context);
                final PhysioITTables.FeedbackDB feedbackDB = new PhysioITTables.FeedbackDB();
                feedbackDB.Name = nameText.getText().toString();
                feedbackDB.Email = emailText.getText().toString();
                feedbackDB.Comments = suggestionText.getText().toString();
                feedbackDB.DeviceId = Utilities.GetDeviceId(_context);

                final FeedBackHandler feedBackHandler = new FeedBackHandler();
                new Thread(){
                        public void run()
                        {
                            Bundle bundle = new Bundle();
                            Message message = feedBackHandler.obtainMessage();
                            try {
                                azureController.InsertIntoAzureTable(feedbackDB);
                                bundle.putString(Constants.FEEDBACK_MESSAGE_ID, Constants.FEEDBACK_PAGE_SENT);
                            } catch (Exception e) {
                                bundle.putString(Constants.FEEDBACK_MESSAGE_ID, e.getMessage());
                            }
                            message.setData(bundle);
                            feedBackHandler.sendMessage(message);
                        }
                }.start();

            }
            }
            catch(Exception ex)
            {

                SubmissionText.setVisibility(TextView.VISIBLE);
                SubmissionText.setText(ex.getMessage());
            }
        }
    }

    private class FeedBackHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String message = bundle.getString(Constants.FEEDBACK_MESSAGE_ID);
            SubmissionText.setText(message);
        }
    }

}