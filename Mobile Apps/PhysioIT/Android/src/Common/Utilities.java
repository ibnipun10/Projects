package Common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import Controller.DBController;

public class Utilities {

    public static DBController getDbController(Context context) {
        return new DBController(new ContextWrapper(context.getApplicationContext()), Constants.PhysioITdatabase, Constants.DATABASE_VERSION_ID);
    }

    public static long DateDiff(Date date1, Date date2) {
        return Math.abs(date1.getTime() - date2.getTime());
    }

    public static PhysioITTables.DeviceDB GetDeviceInformation(Context context, int xmlVersion) throws NameNotFoundException {
        PhysioITTables.DeviceDB device = new PhysioITTables.DeviceDB();
        device.XMLVersion = Integer.toString(xmlVersion);

        device.DeviceID = GetDeviceId(context);
        device.DeviceName = android.os.Build.MODEL;

        device.AppVersion = GetPackageInfo(context).versionName;

        return device;
    }

    public static PackageInfo GetPackageInfo(Context context) throws NameNotFoundException {
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return pInfo;
    }

    public static String GetDeviceId(Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    public static Drawable GetDrawableImage(Context context, String imagePath) throws IOException {
        // get input stream
        InputStream ims = context.getAssets().open(imagePath);
        // load image as Drawable
        Drawable d = Drawable.createFromStream(ims, null);

        return d;
    }

    public static Drawable GetDrawableImage(Context context, int resourceImage) {
        return context.getResources().getDrawable(resourceImage);
    }

    public static int GetImageResourceID(String img) {
        return Resources.getSystem().getIdentifier(img, "drawable", "android");
    }

    public static void SetTitleFont(TextView titleView, Resources rc) {
        titleView.setTextColor(Color.BLUE);
        LinearLayout.LayoutParams textVIewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = GetValueinPixels(10, rc);
        textVIewLayoutParams.setMargins(margin, margin / 2, margin, 0);
        titleView.setLayoutParams(textVIewLayoutParams);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        titleView.setTypeface(null, Typeface.BOLD);
    }

    public static void SetTitleFont(TextView titleView, Resources rc, int gravity) {
        titleView.setTextColor(Color.BLUE);
        LinearLayout.LayoutParams textVIewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = GetValueinPixels(10, rc);
        textVIewLayoutParams.setMargins(margin, margin / 2, margin, 0);
        textVIewLayoutParams.gravity = gravity;
        titleView.setLayoutParams(textVIewLayoutParams);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        titleView.setTypeface(null, Typeface.BOLD);
    }


    public static void SetTitleFont(Switch vSwitch, Resources rc, int gravity) {
        vSwitch.setTextColor(Color.BLUE);
        LinearLayout.LayoutParams textVIewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = GetValueinPixels(10, rc);
        textVIewLayoutParams.setMargins(margin, margin / 2, margin, 0);
        textVIewLayoutParams.gravity = gravity;
        vSwitch.setLayoutParams(textVIewLayoutParams);
        vSwitch.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        vSwitch.setTypeface(null, Typeface.BOLD);
    }


    public static void SetDescriptionFont(TextView descriptionView, Resources rc) {
        descriptionView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        descriptionView.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams textVIewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = GetValueinPixels(10, rc);
        textVIewLayoutParams.setMargins(margin, margin / 2, margin, margin / 2);
        descriptionView.setLayoutParams(textVIewLayoutParams);
    }


    public static ProgressDialog ShowProgressDialog(Activity activity) {
        ProgressDialog mDialog = new ProgressDialog(activity);
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();
        return mDialog;
    }

    public static void CancelProgressDialog(ProgressDialog mDialog) {
        mDialog.cancel();
    }

    public static int GetValueinPixels(int value, Resources rc) {
        return ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, rc.getDisplayMetrics()));
    }

    public static <T> boolean IsNullOrEmpty(List<T> object) {
        if (object == null || object.size() == 0)
            return true;
        else
            return false;
    }

    public static boolean IsNullOrEmpty(String object) {
        if (object == null || object.isEmpty())
            return true;
        else
            return false;
    }

    public static boolean haveInternet(Context ctx) {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null || !info.isConnected())
            return false;
        else
            return true;
    }

    public static AdView AddAdMobControl(Context context) {
        AdView adView = new AdView((Activity) context, AdSize.BANNER, Constants.APP_PUBLISHER_ID);
        AdRequest adRequest = new AdRequest();
        adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
        adRequest.addTestDevice(Constants.TEST_DEVICE_ID);
        adView.loadAd(adRequest);
        return adView;

        //LinearLayout layout = (LinearLayout)findViewById(R.id.adView);

        //layout.addView(adView);


    }

    public static boolean GetBooleanValueFromPreferences(Context context, String key)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean(key, true);
    }

    public static boolean IsLocationServiceEnabled(Context context)
    {
        LocationManager locMan = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}


