package com.example.android.chalkpad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
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
import static com.example.android.chalkpad.R.string.resources;

public class ResourceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);
        String key=getIntent().getStringExtra("key");
        String code=getIntent().getStringExtra("code");
        String title=getIntent().getStringExtra("title");
        this.setTitle(title);
        new NetworkActivity().execute("http://hp.chitkara.edu.in/MobileApi/api.php?fn=resourcedetails&authkey="+key+"&subjectcode="+code);
    }

    private class NetworkActivity extends AsyncTask<String, Void, JSONArray> {
        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ResourceActivity.this);
            pd.setMessage("Loading");
            pd.show();
        }

        @Override
        protected JSONArray doInBackground(String... address) {
            JSONArray array = null;
            if (!isNetworkAvailable(ResourceActivity.this)) {
                Toast.makeText(ResourceActivity.this, "Network Unavailable", Toast.LENGTH_SHORT).show();
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
            if(array==null)
            {
                Toast.makeText(ResourceActivity.this,"No data available!", Toast.LENGTH_LONG).show();
                return;
            }
            if (array.toString().contains("Enter valid authorisation key")) {
                Toast.makeText(ResourceActivity.this, "Please Login Again.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ResourceActivity.this, MainActivity.class));
            }
            JSONObject temp = null;
            final ArrayList<Resource> resources = new ArrayList<>();
            ListView ListView = (ListView) findViewById(R.id.list_resources);
            for(int i=0;i<array.length();i++)
            {
                temp=array.optJSONObject(i);
                resources.add(new Resource(temp.optString("attachment"),temp.optString("employee"),temp.optString("postdate"),temp.optString("resourceattachment")));
            }
            ResourceAdapter adapter = new ResourceAdapter(ResourceActivity.this, resources);
            ListView.setAdapter(adapter);
            ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Resource resource = resources.get(position);
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(resource.getUrl())));
                }
            });
        }
    }
}
