package com.sdsmdg.harshit.draw;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sdsmdg.harshit.draw.Chat.ChatActivity;
import com.sdsmdg.harshit.draw.TeacherNotes.ContactAdapter;
import com.sdsmdg.harshit.draw.TeacherNotes.Contacts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static int count = 1;
    private ProgressBar bar;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ContactAdapter contactAdapter;
    ListView jsonListView;
    String json_string = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        bar = (ProgressBar) this.findViewById(R.id.progressBar);
        checkConnection();

        contactAdapter = new ContactAdapter(this, R.layout.row_layout);
        jsonListView = (ListView) findViewById(R.id.jsonListView);
        jsonListView.setAdapter(contactAdapter);

        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.my_toolbar1);
        setSupportActionBar(myChildToolbar);
        getSupportActionBar().setTitle("Lectures");
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);




//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myChildToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_refresh: {
                checkConnection();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void checkConnection() {
        if (isNetworkAvailable()) {
            new BackgroundTask().execute();
        } else {
            Toast.makeText(this, "Check your Internet Connection !", Toast.LENGTH_LONG).show();
        }
    }


    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String JSON_STRING;
        String json_url;

        @Override
        protected void onPreExecute() {
            bar.setVisibility(View.VISIBLE);
            json_url = "http://rahul16031998.ueuo.com/mPaathshala/json_get_data.php";
        }

        @Override
        protected String doInBackground(Void[] params) {

            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                while ((JSON_STRING = bufferedReader.readLine()) != null) {
                    stringBuilder.append(JSON_STRING + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            json_string = result;
            try {
                jsonObject = new JSONObject(json_string);
                jsonArray = jsonObject.getJSONArray("server_response");
                int count = 0;
                String name, topic, content;
                contactAdapter.clear();
                while (count < jsonArray.length()) {

                    JSONObject JO = jsonArray.getJSONObject(count);
                    name = JO.getString("name");
                    topic = JO.getString("topic");
                    content = JO.getString("content");

                    Contacts contacts = new Contacts(name, topic, content);
                    contactAdapter.add(contacts);
                    String toFileString = "Name : " + name + "\n" + "Topic : " +
                            topic + "\n" + "Content : " + content;

                    String state;
                    state = Environment.getExternalStorageState();
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        File Root = Environment.getExternalStorageDirectory();
                        File Dir = new File(Root.getAbsolutePath() + "/mPaathshala/Teacher Notes/");
                        if (!Dir.exists()) {
                            Dir.mkdir();
                        }
                        File file = new File(Dir, "MyMessage" + count + ".txt");
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(toFileString.getBytes());
                        fos.close();

                    } else {
                        Toast.makeText(getApplicationContext(), "External Storage not Available !", Toast.LENGTH_SHORT).show();
                    }

                    count++;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bar.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Void[] values) {
            super.onProgressUpdate(values);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_takenotes) {
            Intent i = new Intent(this, NoteMakingActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_drawnotes) {
            Intent i = new Intent(this, DrawActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_askusers) {
            Intent i = new Intent(this, ChatActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
