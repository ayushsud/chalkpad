package com.example.android.chalkpad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashboardActivity extends AppCompatActivity {
    private String auth = null;

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
        setContentView(R.layout.activity_dashboard);
        JSONArray array = null;
        try {
            array = new JSONArray(getIntent().getStringExtra("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(DashboardActivity.this, "Some error occured. Please try later.", Toast.LENGTH_LONG).show();
            return;
        }
        JSONObject obj = array.optJSONObject(0);
        String name = obj.optString("firstname");
        auth = obj.optString("authkey");
        TextView name_view = (TextView) findViewById(R.id.profile);
        name_view.setText(name);
        Toast.makeText(DashboardActivity.this, "Welcome! " + name, Toast.LENGTH_LONG).show();
        Button button;
        button = (Button) findViewById(R.id.att);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, AttendanceActivity.class).putExtra("key", auth));
            }
        });
        button = (Button) findViewById(R.id.timetable);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, TimeTableActivity.class).putExtra("key", auth));
            }
        });
        button = (Button) findViewById(R.id.courses);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, CoursesActivity.class).putExtra("key", auth));
            }
        });
        name_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, ProfileActivity.class).putExtra("key", auth));
            }
        });
        button = (Button) findViewById(R.id.marks);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, MarksActivity.class).putExtra("key", auth));
            }
        });
    }
}
