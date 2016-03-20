package com.sdsmdg.harshit.draw.Chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.sdsmdg.harshit.draw.R;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity {

    private MobileServiceClient mClient;
    private MobileServiceTable<Query> mQueryTable;
    EditText question;
    Button submitButton;
    RecyclerView questionsList;
    RecyclerView.LayoutManager mLayoutManager;
    QueriesAdapter mAdapter;
    OnlineDBManager dbManager;
    boolean isConnected = false;
    ProgressDialog progress;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        relativeLayout = (RelativeLayout) findViewById(R.id.rootView);

        if(isConnected()) {
            progress = new ProgressDialog(this);
            progress.setTitle("Loading ...");
            progress.setMessage("Please wait ...");
            progress.show();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    dbManager = new OnlineDBManager("https://iitjee.azurewebsites.net", getApplicationContext());
                    refresh();
                }
            };

            Thread t = new Thread(r);
            t.start();
        }
        else
        {
            Toast.makeText(this, "Check your internet connectivity and try again", Toast.LENGTH_SHORT).show();
            finish();
        }


        question = (EditText) findViewById(R.id.question_input);
        submitButton = (Button) findViewById(R.id.submitButton);
        questionsList = (RecyclerView) findViewById(R.id.questionsList);
        questionsList.addItemDecoration(new DividerItemDecoration(this));

        questionsList.requestFocus();

        //Adding layoutManager and Adapters to the RecyclerView
        List<Query> list = new ArrayList<Query>();
        mAdapter = new QueriesAdapter(this, list);
        mLayoutManager = new LinearLayoutManager(this);//Instantiating LayoutManager object for recyclerView
        questionsList.setAdapter(mAdapter);
        questionsList.setLayoutManager(mLayoutManager);
        questionsList.setHasFixedSize(true);

        getSupportActionBar();

    }

    public boolean isConnected()
    {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            progress = new ProgressDialog(this);
            progress.setTitle("Loading ...");
            progress.setMessage("Please wait ...");
            progress.setCancelable(false);
            progress.show();
            refresh();
        }
        return super.onOptionsItemSelected(item);
    }

    public void refresh()//For refreshing the list of questions in the RecyclerView
    {
        new AsyncTask<Void, Void, List<Query>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected List<Query> doInBackground(Void... params) {

                Log.i("harshit", "Retrieving list");
                List<Query> list = null;
                if (dbManager != null)
                    list = dbManager.refreshItemsFromMobileServiceTable();
                Log.i("harshit", "List retrieved");
                return list;
            }

            @Override
            protected void onPostExecute(List<Query> list) {
                mAdapter.swap(list);
                if(list!=null) {
                    progress.dismiss();
                }
                super.onPostExecute(list);
            }
        }.execute();
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }

    public void submitButtonClicked(View view) {

        String query = question.getText().toString();
        final Query item = new Query(query, "");



        if(isConnected()) {
            progress = new ProgressDialog(this);
            progress.setTitle("Loading ...");
            progress.setMessage("Please wait ...");
            progress.show();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    dbManager.insert(item);
                    try {
                        Thread.sleep(100);
                    }catch (Exception e) { }
                    refresh();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getApplicationContext(), "Question successfully posted", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            };
            Thread t = new Thread(r);
            t.start();
        }
        else {
            Snackbar.make(relativeLayout, "Check internet Connectivity", Toast.LENGTH_SHORT).show();
        }

        question.setText("");


    }


}

