package com.gmail.nayrisian.asyncnet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddSongActivity extends AppCompatActivity {
    TextView songTitleDesc;
    EditText songTitle;
    TextView songArtistDesc;
    EditText songArtist;
    TextView songYearDesc;
    EditText songYear;
    Button songSubmit;
    PostSong postTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);
        // Setup parameters.
        songTitleDesc = findViewById(R.id.add_song_title_desc);
        songTitle = findViewById(R.id.add_song_title);
        songArtistDesc = findViewById(R.id.add_song_artist_desc);
        songArtist = findViewById(R.id.add_song_artist);
        songYearDesc = findViewById(R.id.add_song_year_desc);
        songYear = findViewById(R.id.add_song_year);
        songSubmit = findViewById(R.id.add_song_submit);
        // Setup async task.
        songSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postTask != null)
                    postTask.cancel(true);
                final Toast requestToast = Toast.makeText(AddSongActivity.this, "Sending request...", Toast.LENGTH_SHORT);
                requestToast.show();
                postTask = new PostSong(new AsyncResponse() {
                    @Override
                    public void OnResponse(String response) {
                        // Yield back from onPostExecute
                        Log.i("SOL", "Response received: " + response);
                        requestToast.cancel();
                        if (response.equals("OK")) {
                            Toast.makeText(AddSongActivity.this, "Song added.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddSongActivity.this, "Song request failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                postTask.execute(
                        songTitle.getText().toString(),
                        songArtist.getText().toString(),
                        songYear.getText().toString());
            }
        });
    }
}
