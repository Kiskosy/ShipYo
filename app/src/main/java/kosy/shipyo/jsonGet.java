package kosy.shipyo;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

/** *
 * Created by Ákos on 2017. 04. 29.. *
 * **/

public class jsonGet extends AsyncTask<String, Void, String> {

    private TextView textView;
    private String json = "UNDEFINED";

    public jsonGet(TextView textView) {
        ;
    }

    @Override
    protected String doInBackground(String... params) {

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

            System.out.println(builder.toString());
            JSONObject jsonOutput = new JSONObject(builder.toString());
            json = String.valueOf(jsonOutput);

            /*Iterator x = jsonOutput.keys();
            JSONArray jsonArray = new JSONArray();

            while (x.hasNext()){
                String key = (String) x.next();
                jsonArray.put(jsonOutput.get(key));
            }*/

            urlConnection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    @Override
    protected void onPostExecute(String temp) {
        ;
    }
}
