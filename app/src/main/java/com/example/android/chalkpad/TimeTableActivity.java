package com.example.android.chalkpad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class TimeTableActivity extends AppCompatActivity {



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
                startActivity(new Intent(this,MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        String key = getIntent().getStringExtra("key");
        new NetworkAdapter().execute("http://hp.chitkara.edu.in/MobileApi/api.php?fn=timetable&authkey=" + key);
    }

    private class NetworkAdapter extends AsyncTask<String, Void, JSONObject> {
        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(TimeTableActivity.this);
            pd.setMessage("Loading");
            pd.show();
        }

        @Override
        protected JSONObject doInBackground(String... address) {
            JSONObject obj = null;
            if (!isNetworkAvailable(TimeTableActivity.this)) {
                Toast.makeText(TimeTableActivity.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
                return obj;
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
                obj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return obj;
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
        protected void onPostExecute(JSONObject obj) {
            pd.dismiss();
            if (obj.toString().contains("Enter valid authorisation key")) {
                Toast.makeText(TimeTableActivity.this, "Please Login Again.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(TimeTableActivity.this, MainActivity.class));
            }
            Log.v("Object",obj.toString());
            JSONObject monday=obj.optJSONObject("1");
            JSONObject tuesday=obj.optJSONObject("2");
            JSONObject wednesday=obj.optJSONObject("3");
            JSONObject thursday=obj.optJSONObject("4");
            JSONObject friday=obj.optJSONObject("5");
            ArrayList<TimeTable> tables=new ArrayList<>();
            ListView tableListView=(ListView) findViewById(R.id.list_time_table);
            tables.add(new TimeTable("Mon", getLecture(monday.optJSONObject("1")), getLecture(monday.optJSONObject("2")),getLecture(monday.optJSONObject("3")),getLecture(monday.optJSONObject("4")),getLecture(monday.optJSONObject("5")),getLecture(monday.optJSONObject("6")),getLecture(monday.optJSONObject("7")),getLecture(monday.optJSONObject("8"))));
            tables.add(new TimeTable("Tue", getLecture(tuesday.optJSONObject("1")), getLecture(tuesday.optJSONObject("2")),getLecture(tuesday.optJSONObject("3")),getLecture(tuesday.optJSONObject("4")),getLecture(tuesday.optJSONObject("5")),getLecture(tuesday.optJSONObject("6")),getLecture(tuesday.optJSONObject("7")),getLecture(tuesday.optJSONObject("8"))));
            tables.add(new TimeTable("Wed", getLecture(wednesday.optJSONObject("1")), getLecture(wednesday.optJSONObject("2")),getLecture(wednesday.optJSONObject("3")),getLecture(wednesday.optJSONObject("4")),getLecture(wednesday.optJSONObject("5")),getLecture(wednesday.optJSONObject("6")),getLecture(wednesday.optJSONObject("7")),getLecture(wednesday.optJSONObject("8"))));
            tables.add(new TimeTable("Thu", getLecture(thursday.optJSONObject("1")), getLecture(thursday.optJSONObject("2")),getLecture(thursday.optJSONObject("3")),getLecture(thursday.optJSONObject("4")),getLecture(thursday.optJSONObject("5")),getLecture(thursday.optJSONObject("6")),getLecture(thursday.optJSONObject("7")),getLecture(thursday.optJSONObject("8"))));
            tables.add(new TimeTable("Fri", getLecture(friday.optJSONObject("1")), getLecture(friday.optJSONObject("2")),getLecture(friday.optJSONObject("3")),getLecture(friday.optJSONObject("4")),getLecture(friday.optJSONObject("5")),getLecture(friday.optJSONObject("6")),getLecture(friday.optJSONObject("7")),getLecture(friday.optJSONObject("8"))));
            try
            {
                JSONObject saturday=obj.optJSONObject("6");
                tables.add(new TimeTable("Sat", getLecture(saturday.optJSONObject("1")), getLecture(saturday.optJSONObject("2")),getLecture(saturday.optJSONObject("3")),getLecture(saturday.optJSONObject("4")),getLecture(saturday.optJSONObject("5")),getLecture(saturday.optJSONObject("6")),getLecture(saturday.optJSONObject("7")),getLecture(saturday.optJSONObject("8"))));
            }
            catch (Exception e)
            {
                Log.v("Time Table","Error Adding Saturday");
            }
            TimeTableAdapter adapter=new TimeTableAdapter(TimeTableActivity.this,tables);
            tableListView.setAdapter(adapter);
//            AttendanceAdapter adapter = new AttendanceAdapter(TimeTableActivity.this, attendances);
//            attendanceListView.setAdapter(adapter);
        }

        public String getLecture(JSONObject obj)
        {
            String str=null;
            try
            {
                str=obj.getString("coursecode");
                str+="\n";
                str+=obj.getString("roomname");
                str+="\n";
                str+=obj.getString("teachername");
                str+="\n";
                str+=obj.getString("sectionname");
            }
            catch (Exception e)
            {
                return "";
            }
            return str;
        }
    }
}
