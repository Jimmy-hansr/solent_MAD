package com.gmail.nayrisian.json;

import android.os.AsyncTask;

/**
 * AsyncTask<t1, t2, t3>
 * t1 - Input parameter into doInBackground
 * t2 - Variable used to indicate state within the task.
 * t3 - Return value from the task.
 */
public class Query extends AsyncTask<String, Void, String> {
    interface AsyncProcess {
        String DoBackground(String... strings);
    }

    interface AsyncResponse {
        void OnResponse(String songs);
    }

    private final AsyncProcess Process;
    private final AsyncResponse Response;

    Query(AsyncProcess process, AsyncResponse response) {
        Process = process;
        Response = response;
    }

    @Override
    protected String doInBackground(String... strings) {
        return Process.DoBackground(strings);
    }

    @Override
    protected void onPostExecute(String s) {
        Response.OnResponse(s);
    }
}
