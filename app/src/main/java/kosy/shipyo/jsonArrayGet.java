package kosy.shipyo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/** *
 * Created by Ákos on 2017. 04. 29.. *
 * **/

public class jsonArrayGet extends AsyncTask<String, Void, ArrayList<JSONObject>> {

    private TextView textView;
    private String json = "UNDEFINED";
    private ArrayList<JSONObject> jsonObjects = new ArrayList<>();

    ArrayList<JSONObject> str;
    private RealTimeGame activity;

    public jsonArrayGet(RealTimeGame activity) {
        this.activity = activity;
    }


    @Override
    protected ArrayList<JSONObject> doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream,"iso-8859-1"), 8);
            StringBuilder builder = new StringBuilder();

            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString + "\n");
            }

            //System.out.println(builder.toString());
            JSONObject jsonOutput = new JSONObject(builder.toString());
            JSONArray result = jsonOutput.getJSONArray("posts");
            for(int i = 0; i < result.length();i++){
                JSONObject tempO = result.getJSONObject(i);
                jsonObjects.add(tempO);
            }
            //System.out.println(result.toString());
            //json = String.valueOf(jsonOutput);
            //System.out.println(json);

            //jsonObjects.add(jsonOutput);

            urlConnection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        str = jsonObjects;
        return jsonObjects;
    }

    @Override
    protected void onPostExecute(ArrayList<JSONObject> jsonObjects) {
        super.onPostExecute(jsonObjects);
        activity.setList(str);
    }

}

            /*Iterator x = jsonOutput.keys();
            JSONArray jsonArray = new JSONArray();

            while (x.hasNext()){
                String key = (String) x.next();
                jsonArray.put(jsonOutput.get(key));
            }*/