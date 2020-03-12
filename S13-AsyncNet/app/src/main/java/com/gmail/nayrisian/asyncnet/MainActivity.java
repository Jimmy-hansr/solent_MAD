package com.gmail.nayrisian.asyncnet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView artistQueryDesc;
    EditText artistQuery;
    Button submitQuery;
    TextView queryResponse;
    Query queryTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Setup parameters.
        artistQueryDesc = findViewById(R.id.artist_query_desc);
        artistQuery = findViewById(R.id.artist_query);
        submitQuery = findViewById(R.id.submit_query);
        queryResponse = findViewById(R.id.query_response);
        // Setup async task.
        submitQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (queryTask != null)
                    queryTask.cancel(true);
                final Toast requestToast = Toast.makeText(MainActivity.this, "Sending request...", Toast.LENGTH_SHORT);
                requestToast.show();
                queryTask = new Query(new AsyncResponse() {
                    @Override
                    public void OnResponse(String songs) {
                        // Yield back from onPostExecute
                        requestToast.cancel();
                        Toast.makeText(MainActivity.this, "Received response.", Toast.LENGTH_SHORT).show();
                        queryResponse.setText("");
                        queryResponse.append(songs);
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