package com.example.android.chalkpad;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    public static boolean isNetworkAvailable(Context context) {
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public String md5(String in) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 240) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 15, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button= (Button) findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(MainActivity.this)) {
                    EditText c_id = (EditText) findViewById(R.id.ID);
                    String id = c_id.getText().toString().trim();
                    EditText password = (EditText) findViewById(R.id.password);
                    password.clearFocus();
                    String pass = password.getText().toString().trim();
                    if (!validateID()||!validatePassword())
                        return;
                    new NetworkAdapter().execute("http://hp.chitkara.edu.in/MobileApi/api.php?fn=login&uname=" + id + "&pw=" + md5(pass));
                } else
                    Toast.makeText(MainActivity.this, "Please connect to the internet first.", Toast.LENGTH_LONG).show();
            }
        });
    }


    public boolean validateID() {
        EditText c_id = (EditText) findViewById(R.id.ID);
        String id = c_id.getText().toString().trim();
        if (id == null || id.length() != 10) {
            c_id.setError("ID Incorrect");
            return false;
        }
        return true;
    }
    public boolean validatePassword()
    {
        EditText password=(EditText)findViewById(R.id.password);
        String pass=password.getText().toString().trim();
        if(pass.length()==0) {
            password.setError("Please Enter a password.");
            return false;
        }
        return true;
    }


    private class NetworkAdapter extends AsyncTask<String, Void, String> {
        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Authenticating");
            pd.show();
        }

        @Override
        protected String doInBackground(String... address) {
            URL url = createUrl(address[0]);
            String result = "";
            try {
                result = makeHttpRequest(url);
                Log.v("Main", "" + result);
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.contains("null")) {
                EditText temp;
                temp = (EditText) findViewById(R.id.ID);
                temp.setError("Username or Password Incorrect");
                temp = (EditText) findViewById(R.id.password);
                temp.setError("Username or Password Incorrect");
                temp.setText("");
                pd.dismiss();
                return;
            }
            if(result==null)
            {
                Toast.makeText(MainActivity.this,"Some error occured. Please try later.", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            intent.putExtra("data", result);
            pd.dismiss();
            startActivity(intent);
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
    }

}