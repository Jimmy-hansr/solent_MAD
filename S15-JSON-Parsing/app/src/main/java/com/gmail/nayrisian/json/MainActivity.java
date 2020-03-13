package com.gmail.nayrisian.json;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

public class MainActivity extends AppCompatActivity {
    TextView artistQueryDesc;
    EditText artistQuery;
    Button submitQuery;
    //TextView queryResponse;
    Query queryTask;
    LinearLayout songListQuery;
    Drawable songQueryBorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Setup parameters.
        artistQueryDesc = findViewById(R.id.artist_query_desc);
        artistQuery = findViewById(R.id.artist_query);
        submitQuery = findViewById(R.id.submit_query);
        songListQuery = findViewById(R.id.song_list_query);
        songQueryBorder = getDrawable(R.drawable.song_border);
        //queryResponse = findViewById(R.id.query_response);
        // Setup async task.
        submitQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (queryTask != null)
                    queryTask.cancel(true);
                final Toast requestToast = Toast.makeText(MainActivity.this, "Sending request...", Toast.LENGTH_SHORT);
                requestToast.show();
                queryTask = new Query(new Query.AsyncProcess() {
                    @Override
                    public String DoBackground(String... strings) {
                        HttpURLConnection connection = null;
                        try {
                            URL url = new URL("https://hikar.org/course/ws/hits.php?format=json&artist=" + strings[0]);
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setDoInput(true);
                            connection.setDoOutput(false);
                            connection.setRequestMethod("GET");
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
                }, new Query.AsyncResponse() {
                    @Override
                    public void OnResponse(String songs) {
                        // Yield back from onPostExecute
                        requestToast.cancel();
                        Toast.makeText(MainActivity.this, "Received response.", Toast.LENGTH_SHORT).show();
                        // JSON Parse response
                        try {
                            Log.i("SOL", "Response returned " + songs);
                            JSONArray jArr = new JSONArray(songs);
                            for (int i = 0; i < jArr.length(); i++) {
                                JSONObject jObj = jArr.getJSONObject(i);
                                int id = jObj.getInt("ID");
                                String title = jObj.getString("title");
                                String artist = jObj.getString("artist");
                                // Generate object
                                TextView tView = new TextView(MainActivity.this);
                                String temp = "[" + id + "] " + title + " by " + artist;
                                tView.setText(temp);
                                tView.setBackground(songQueryBorder);
                                songListQuery.addView(tView);
                            }
                        } catch (JSONException jE) {
                            Log.i("SOL", jE.getMessage());
                            jE.printStackTrace();
                        }
                    }
                });
                queryTask.execute(artistQuery.getText().toString());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.select_query_artist:
                Log.i("SOL", "Hello.");
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.select_add_song:
                Log.i("SOL", "Bye.");
                intent = new Intent(this, AddSongActivity.class);
                startActivity(intent);
                return true;
            default:
                Log.i("SOL", "Test.");
                return false;
        }
    }
}