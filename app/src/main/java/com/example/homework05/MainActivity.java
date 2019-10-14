package com.example.homework05;

/*
* MainActivity.java
* Homework 05
* Group 29
* Mayuri Jain, Narendra Pahuja
*/
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listViewsource;
    ArrayAdapter<Source> adapter;
    ArrayList<Source> SourcesList = new ArrayList<>();
    static final int getNews_REQUEST = 0001;
    static final String Source_KEy = "Source_KEy";
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBarMain);
        progressBar.setVisibility(View.VISIBLE);
        String url = "https://newsapi.org/v2/sources?apiKey=2869596734ee4c339aa346b09e9a6dc1";
        if (isConnected()) {
            new getSource().execute(url);
        } else {
            Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
        }
        listViewsource = (ListView) findViewById(R.id.listviewsource);
        adapter = new ArrayAdapter<Source>(this, android.R.layout.simple_list_item_1, android.R.id.text1, SourcesList);
        listViewsource.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listViewsource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle b = new Bundle();
                b.putSerializable(Source_KEy, SourcesList.get(i));
                Intent newsIntent = new Intent(MainActivity.this,
                        NewsActivity.class);
                //newsIntent.putExtra(Source_KEy, SourcesList.get(i).id.trim());
                newsIntent.putExtras(b);
                startActivity(newsIntent);
            }
        });

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    public class getSource extends AsyncTask<String, Void, ArrayList<Source>> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Source> doInBackground(String... params) {

            HttpURLConnection connection = null;
            ArrayList<Source> result = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONObject root = new JSONObject(json);
                    JSONArray sourcearray = root.getJSONArray("sources");
                    for (int i = 0; i < sourcearray.length(); i++) {
                        JSONObject sourceJson = sourcearray.getJSONObject(i);
                        String name = sourceJson.getString("name");
                        String id = sourceJson.getString("id");
                        Source source = new Source(id, name);


                        result.add(source);
                    }
                }
            } catch (Exception e) {
                //Handle Exceptions
            } finally {
                //Close the connections
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Source> sources) {
            progressBar.setVisibility(View.INVISIBLE);
            for (Source source :
                    sources) {
                SourcesList.add(source);
            }

            adapter.notifyDataSetChanged();

        }
    }
}
