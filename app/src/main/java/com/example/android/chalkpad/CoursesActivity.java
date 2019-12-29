package com.example.android.chalkpad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import static android.R.attr.key;
import static com.example.android.chalkpad.MainActivity.isNetworkAvailable;

public class CoursesActivity extends AppCompatActivity {
    private String key;

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
        setContentView(R.layout.activity_courses);
        key = getIntent().getStringExtra("key");
        new NetworkActivity().execute("http://hp.chitkara.edu.in/MobileApi/api.php?fn=subjects&authkey=" + key);
    }

    private class NetworkActivity extends AsyncTask<String, Void, JSONArray> {
        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CoursesActivity.this);
            pd.setMessage("Loading");
            pd.show();
        }

        @Override
        protected JSONArray doInBackground(String... address) {
            JSONArray array = null;
            if (!isNetworkAvailable(CoursesActivity.this)) {
                Toast.makeText(CoursesActivity.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
                return array;
            }
            URL url = createUrl(address[0]);
            String result = "";
            try {
                result = makeHttpRequest(url);
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
                Toast.makeText(CoursesActivity.this, "Please Login Again.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CoursesActivity.this, MainActivity.class));
            }
            JSONObject temp = null;
            final ArrayList<Course> courses = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                temp = array.optJSONObject(i);
                courses.add(new Course(temp.optString("subjectname"), temp.optString("subjectcode"), temp.optString("type"), temp.optString("teachername")));
            }
            ListView courseListView = (ListView) findViewById(R.id.list_courses);
            CoursesAdapter adapter = new CoursesAdapter(CoursesActivity.this, courses);
            courseListView.setAdapter(adapter);
            courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    // Get the {@link Word} object at the given position the user clicked on
                    Course course = courses.get(position);
                    startActivity(new Intent(CoursesActivity.this, ResourceActivity.class).putExtra("key", key).putExtra("code",course.getCode()).putExtra("title",course.getName()));
                }
            });
        }
    }
}
