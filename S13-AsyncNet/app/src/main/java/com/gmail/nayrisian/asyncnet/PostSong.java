package com.gmail.nayrisian.asyncnet;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * AsyncTask<t1, t2, t3>
 * t1 - Input parameter into doInBackground
 * t2 - Variable used to indicate state within the task.
 * t3 - Return value from the task.
 */
public class PostSong extends AsyncTask<String, Void, String> {
    private final AsyncResponse Response;

    PostSong(AsyncResponse response) {
        Response = response;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("https://hikar.org/course/ws/addhit.php");
            String postData = "song=" + strings[0] + "&artist=" + strings[1] + "&year=" + strings[2];
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setFixedLengthStreamingMode(postData.length());
            // Write post data.
            OutputStream out = null;
            out = connection.getOutputStream();
            out.write(postData.getBytes());
            // Get result.
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                StringBuilder all = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    all.append(line);
                }
                return all.toString();
            } else {
                String err = "ERROR: " + connection.getResponseCode();
                Log.e("SOL", err);
                return err;
            }
        } catch (IOException e) {
            Log.e("SOL", e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return "Fatal ERROR.";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Response.OnResponse(s);
    }
}
