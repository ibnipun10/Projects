package Controller;

import BackStage.XMLInteractor;
import android.app.Activity;

import java.util.List;

/**
 * Created by nipuna on 7/21/13.
 */
public class XMLController {

    public <T> List<T> GetXMLValues(Activity activity, T tableName) throws Exception {
        return XMLInteractor.PopulateFromXML(activity, tableName);
    }

    public int ReturnXMLVersion()
    {
        return XMLInteractor.XMLVersion;
    }
}
