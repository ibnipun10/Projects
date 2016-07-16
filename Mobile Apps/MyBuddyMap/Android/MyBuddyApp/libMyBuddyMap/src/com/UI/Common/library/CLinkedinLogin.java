package com.UI.Common.library;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.R;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageView;

public class CLinkedinLogin {
	
	private String AUTHORIZATION_URL = "https://www.linkedin.com/uas/oauth2/authorization";
	private String ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken";
	private final String QUESTION_MARK = "?";
	private final String AMPERSAND = "&";
	private final String EQUALS = "=";
	private final String STATE_PARAM = "state";
	private final String SECRET_KEY_PARAM = "client_secret";
	private final String RESPONSE_TYPE_PARAM = "response_type";
	private final String GRANT_TYPE_PARAM = "grant_type";
	private final String GRANT_TYPE = "authorization_code";
	private final String RESPONSE_TYPE_VALUE ="code";
	private final String CLIENT_ID_PARAM = "client_id";	
	private final String REDIRECT_URI_PARAM = "redirect_uri";
	
	//This is any string we want to use. This will be used for avoid CSRF attacks. You can generate one here: http://strongpasswordgenerator.com/
	private final String STATE = "9fI3 }I@?=K884c";
	
	//This is the url that LinkedIn Auth process will redirect to. We can put whatever we want that starts with http:// or https:// .
	//We use a made up url that we will intercept when redirecting. Avoid Uppercases. 
	private static final String REDIRECT_URI = "http://www.google.com";
	
	private Context m_context;
	private Dialog m_dialog;
	private String m_customerKey;
	private String m_customerSecret;
	private ProgressDialog m_pd;
	private ImageView m_closeImageView;
	
	
	public CLinkedinLogin(Context context, String key, String secret)
	{
		m_context = context;
		m_customerKey = key;
		m_customerSecret = secret;
		
		m_pd = Utilities.ShowProgressDialog((Activity)m_context);
		InitializeComponents();
	}
	
	public void InitializeComponents()
	{
		
		int customDialogStyle = Utilities.GetIdentifier(m_context, "CustomDialog", "style");
		if(customDialogStyle != 0)
			m_dialog = new Dialog(m_context, customDialogStyle);
		else
			m_dialog = new Dialog(m_context);
		
		int webViewLayoutID = Utilities.GetIdentifier(m_context, "layout_webview", "layout");
		
		if(webViewLayoutID != 0)
		{
			
			m_dialog.setContentView(webViewLayoutID);
			
			int webViewId = Utilities.GetIdentifier(m_context, "webview", "id");
			if(webViewId != 0)
			{
				WebView wb = (WebView) m_dialog.findViewById(webViewId);
				wb.getSettings().setJavaScriptEnabled(true);
	            wb.loadUrl(getAuthorizationUrl());
				//wb.loadUrl("http://www.facebook.com");
	            wb.setBackgroundColor(Color.TRANSPARENT);
	            wb.setWebViewClient(new MyWebViewClient());
	   
	            m_dialog.setCancelable(true);
	       
			}
			else
				Utilities.PrintMessageInConsole("Unable to get webview id");
			
			int closeButtonViewId = Utilities.GetIdentifier(m_context, "btn_linkedin_close", "id");
			if(closeButtonViewId != 0)
			{
				m_closeImageView =  (ImageView)m_dialog.findViewById(closeButtonViewId);
				m_closeImageView.setOnClickListener(new OnLinkedInButtonCLoseClick());
			}
			else
			{
				Utilities.PrintMessageInConsole("Linked button close if is 0");
			}
		}
		else
		{
			Utilities.PrintMessageInConsole("Unable to get the webviw layout id");
		}
	}
	
	private class OnLinkedInButtonCLoseClick implements View.OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			m_dialog.dismiss();
			
		}
		
	}
	
	private class MyWebViewClient extends WebViewClient { 

	        @Override 
	        //show the web page in webview but not in web browser
	        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
	        	
	        	Uri uri = Uri.parse(url);
	        	Utilities.PrintMessageInConsole(uri.toString());
	        	
	        	String stateToken = uri.getQueryParameter(STATE_PARAM);
	        	if(stateToken==null || !stateToken.equals(STATE)){
                    Utilities.PrintMessageInConsole("State Token does not match");
                    return true;
                }
	        	
	        	//If the user doesn't allow authorization to our application, the authorizationToken Will be null.
                String authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE);
                if(authorizationToken==null){
                    Utilities.PrintMessageInConsole("The user doesn't allow authorization.");
                    return true;
                }
                
                Utilities.PrintMessageInConsole("Authorization token received : " + authorizationToken);

                //Generate URL for requesting Access Token
                String accessTokenUrl = getAccessTokenUrl(authorizationToken);
                //We make the request in a AsyncTask
                new PostRequestAsyncTask().execute(accessTokenUrl);
	            return true;
	     }
	        
	        @Override
	          public void onPageFinished(WebView view, String url) {
	                //This method will be executed each time a page finished loading.
	                //The only we do is dismiss the progressDialog, in case we are showing any.
	              if(m_pd!=null && m_pd.isShowing()){
	                  Utilities.CancelProgressDialog(m_pd);
	                  m_closeImageView.setVisibility(View.VISIBLE);
	                  
	              }
	          }
	};
	
	public void ShowDialog()
	{
		m_dialog.show();
	}
	
	/**
	 * Method that generates the url for get the authorization token from the Service
	 * @return Url
	 */
	private String getAuthorizationUrl(){
	    return AUTHORIZATION_URL
	            +QUESTION_MARK+RESPONSE_TYPE_PARAM+EQUALS+RESPONSE_TYPE_VALUE
	            +AMPERSAND+CLIENT_ID_PARAM+EQUALS+m_customerKey
	            +AMPERSAND+STATE_PARAM+EQUALS+STATE
	            +AMPERSAND+REDIRECT_URI_PARAM+EQUALS+REDIRECT_URI;
	}
	
	/**
	 * Method that generates the url for get the access token from the Service
	 * @return Url
	 */
	private String getAccessTokenUrl(String authorizationToken){
	    return ACCESS_TOKEN_URL
	            +QUESTION_MARK
	            +GRANT_TYPE_PARAM+EQUALS+GRANT_TYPE
	            +AMPERSAND
	            +RESPONSE_TYPE_VALUE+EQUALS+authorizationToken
	            +AMPERSAND
	            +CLIENT_ID_PARAM+EQUALS+m_customerKey
	            +AMPERSAND
	            +REDIRECT_URI_PARAM+EQUALS+REDIRECT_URI
	            +AMPERSAND
	            +SECRET_KEY_PARAM+EQUALS+m_customerSecret;
	}
	
	
	private class PostRequestAsyncTask extends AsyncTask<String, Void, Boolean>{

	    @Override
	    protected void onPreExecute(){
	        m_pd = Utilities.ShowProgressDialog((Activity)m_context);
	    }

	    @Override
	    protected Boolean doInBackground(String... urls) {
	        if(urls.length>0){
	            String url = urls[0];
	            HttpClient httpClient = new DefaultHttpClient();
	            HttpPost httpost = new HttpPost(url);
	            try{
	                HttpResponse response = httpClient.execute(httpost);
	                if(response!=null){
	                    //If status is OK 200
	                    if(response.getStatusLine().getStatusCode()==200){
	                        String result = EntityUtils.toString(response.getEntity());
	                        //Convert the string result to a JSON Object
	                        JSONObject resultJson = new JSONObject(result);
	                        //Extract data from JSON Response
	                        int expiresIn = resultJson.has("expires_in") ? resultJson.getInt("expires_in") : 0;

	                        String accessToken = resultJson.has("access_token") ? resultJson.getString("access_token") : null;
	                        Utilities.PrintMessageInConsole("Token : " +accessToken);
	                        
	                        if(expiresIn>0 && accessToken!=null){
	                            Utilities.PrintMessageInConsole("This is the access Token: "+accessToken+". It will expires in "+expiresIn+" secs");

	                            //Calculate date of expiration
	                            Calendar calendar = Calendar.getInstance();
	                            calendar.add(Calendar.SECOND, expiresIn);
	                            long expireDate = calendar.getTimeInMillis();

	                            ////Store both expires in and access token in shared preferences
	                            SharedPreferences preferences = m_context.getSharedPreferences("user_info", 0);
	                            SharedPreferences.Editor editor = preferences.edit();
	                            editor.putLong("expires", expireDate);
	                            editor.putString("accessToken", accessToken);
	                            editor.commit();

	                            return true;
	                        }
	                    }
	                }
	            }catch(IOException e){
	                Log.e("Authorize","Error Http response "+e.getLocalizedMessage());  
	            }
	            catch (ParseException e) {
	                Log.e("Authorize","Error Parsing Http response "+e.getLocalizedMessage());
	            } catch (JSONException e) {
	                Log.e("Authorize","Error Parsing Http response "+e.getLocalizedMessage());
	            }
	        }
	        return false;
	    }

	    @Override
	    protected void onPostExecute(Boolean status){
	        
	    	if(m_pd!=null && m_pd.isShowing()){
	            m_pd.dismiss();
	        }
	        if(status){
	            //If everything went Ok, change to another activity.
	            Intent startProfileActivity = new Intent(MainActivity.this, ProfileActivity.class);
	            MainActivity.this.startActivity(startProfileActivity);
	        }
	    }

	};
	
}
