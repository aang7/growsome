package xyz.growsome.growsome.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cheko on 28/03/2017.
 */

public class JSONHelper {

    private JSONObject jsonObject;

    public JSONHelper (String string){

        try
        {
            JSONArray jsonArray = new JSONArray(string);

            // looping through All Contacts
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);

            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            try
            {
                jsonObject = new JSONObject(string);
            }
            catch (JSONException e1)
            {
                e1.printStackTrace();
            }
        }
    }

    public JSONObject getJsonObject(){
        return this.jsonObject;
    }
}
