package com.epitech.newtong.epiandroid;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Guillaume on 11/01/2016.
 */
public class ConnectionUtilities {

    static String urlEpitech = "https://epitech-api.herokuapp.com/";

    static public JSONObject post(String stringFct, JSONObject toSend) {

        try {
            URL url = new URL(urlEpitech + "login");

            // Set connection
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Content-Type", "charset=UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");

            String  strToSend = "";
            Iterator<?> keys = toSend.keys();
            while (keys.hasNext())
            {
                String key = (String)keys.next();
                strToSend += key + "=" + toSend.getString(key);
                if (keys.hasNext())
                    strToSend += "&";
            }
            // Post
            OutputStream os = connection.getOutputStream();
            os.write(strToSend.getBytes("UTF-8"));
            System.out.println(strToSend);
            os.flush();
            os.close();

            // Get response
            JSONObject jsonObject = null;
            StringBuilder sb = new StringBuilder();
            int HttpResult = connection.getResponseCode();

            if(HttpResult == HttpsURLConnection.HTTP_OK)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                jsonObject = new JSONObject(sb.toString());
            } else {
                System.out.println(connection.getResponseMessage());
            }
            connection.disconnect();

            return (jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
