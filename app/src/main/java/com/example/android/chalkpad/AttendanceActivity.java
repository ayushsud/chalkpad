package com.example.android.chalkpad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static com.example.android.chalkpad.MainActivity.isNetworkAvailable;

public class AttendanceActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        String key = getIntent().getStringExtra("key");
        new NetworkAdapter().execute("http://hp.chitkara.edu.in/MobileApi/api.php?fn=attendance&authkey=" + key);
    }

    private class NetworkAdapter extends AsyncTask<String, Void, JSONArray> {
        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(AttendanceActivity.this);
            pd.setMessage("Loading");
            pd.show();
        }

        @Override
        protected JSONArray doInBackground(String... address) {
            JSONArray array = null;
            if (!isNetworkAvailable(AttendanceActivity.this)) {
                Toast.makeText(AttendanceActivity.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
                return array;
            }
            URL url = createUrl(address[0]);
            String result = "";
            try {
                result = makeHttpRequest(url);
                Log.v("Main", "" + result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                array = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return array;
        }

        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e("URL", "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        @Override
        protected void onPostExecute(JSONArray array) {
            pd.dismiss();
            if (array.toString().contains("Enter valid authorisation key")) {
                Toast.makeText(AttendanceActivity.this, "Please Login Again.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AttendanceActivity.this, MainActivity.class));
            }
            JSONObject temp = null;
            ArrayList<Attendance> attendances = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                temp = array.optJSONObject(i);
                attendances.add(new Attendance(temp.optString("sectionname"), temp.optString("subjectname") + "\n(" + temp.optString("subjectcode") + ")", temp.optString("teachername"), temp.optString("lecturedelivered"), temp.optString("lectureattended")));
            }
            ListView attendanceListView = (ListView) findViewById(R.id.list_attendance);
            AttendanceAdapter adapter = new AttendanceAdapter(AttendanceActivity.this, attendances);
            attendanceListView.setAdapter(adapter);
        }
    }
}
