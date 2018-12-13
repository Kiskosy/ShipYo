package kosy.shipyo;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Ákos on 2017. 04. 30..
 */

public class jsonPost extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String url = "http://kosysite.hol.es/webservices.php";

        return sendHTTPData(url,params[0],params[1]);
    }

    @Override
    protected void onPostExecute(String temp) {
        System.out.println("meghívtalak..........");
        ;
    }

    public String sendHTTPData(String url_path, String... params) {
        HttpURLConnection connection = null;
        String result = "0";
        try {

            String username = (String) params[0];
            String password = (String) params[1];
            String link = "http://kosysite.hol.es/webservices.php?username="+username+"&password="+password;
            //"http://myphpmysqlweb.hostei.com/login.php?username="+username
            URL object=new URL(link);

            connection = (HttpURLConnection) object.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches (false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            OutputStreamWriter streamWriter = new OutputStreamWriter(connection.getOutputStream());
            //streamWriter.write(data);
            //streamWriter.flush();

            StringBuilder stringBuilder = new StringBuilder();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){

                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(streamReader);

                String response = null;
                while ((response = bufferedReader.readLine()) != null) {
                    stringBuilder.append(response + "\n");
                }
                bufferedReader.close();

                //JSONObject jsonOutput = new JSONObject(stringBuilder.toString());
                System.out.println(stringBuilder.toString());
                result = stringBuilder.toString();
                //json = String.valueOf(jsonOutput);
            } else {
                System.out.println(connection.getResponseMessage());
            }
        } catch (Exception ex){
            System.out.println(ex);
        } finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return result;
    }
}
