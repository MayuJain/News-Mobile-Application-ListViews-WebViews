package com.example.homework05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

public class NewsActivity extends AppCompatActivity {
    ArrayList<News> newsArrayList = new ArrayList<>();
    static final String NEWS_KEY = "News_Key";
    ListView newsListView;
    NewsAdapter newsAdapter;
    ProgressBar progressBarNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        progressBarNews = findViewById(R.id.progressBarNews);
        Source sources = (Source) getIntent().getSerializableExtra(MainActivity.Source_KEy);//getStringExtra(MainActivity.Source_KEy);
        setTitle(sources.name);
        String url = "https://newsapi.org/v2/top-headlines?sources=" + sources.id.trim() + "&apiKey=c9059ac0c4cd4fe0989090f935fa7f2f";
        newsListView = findViewById(R.id.newsListView);
        if (isConnected()) {
            new getNewsAsync().execute(url);
        } else {
            Toast.makeText(NewsActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
        }
        newsAdapter = new NewsAdapter(this, R.layout.news_item, newsArrayList);
        newsListView.setAdapter(newsAdapter);
        newsAdapter.notifyDataSetChanged();
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                WebView myWebView = new WebView(NewsActivity.this);
//                setContentView(myWebView);
//                myWebView.setWebViewClient(new WebViewClient());
//                myWebView.loadUrl(newsArrayList.get(i).url);

                Intent viewIntent = new Intent(NewsActivity.this,
                        ViewActivity.class);
                viewIntent.putExtra(NEWS_KEY, newsArrayList.get(i).url);
                startActivity(viewIntent);
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

    public class getNewsAsync extends AsyncTask<String, Void, ArrayList<News>> {
        @Override
        protected void onPreExecute() {
            progressBarNews.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<News> doInBackground(String... params) {

            HttpURLConnection connection = null;
            ArrayList<News> result = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                    JSONObject root = new JSONObject(json);
                    JSONArray sourcearray = root.getJSONArray("articles");
                    for (int i = 0; i < sourcearray.length(); i++) {
                        JSONObject sourceJson = sourcearray.getJSONObject(i);
                        String publishedAt = sourceJson.getString("publishedAt");
                        String title = sourceJson.getString("title");
                        String author = sourceJson.getString("author");
                        String urlToImage = sourceJson.getString("urlToImage");
                        String urlweb = sourceJson.getString("url");

                        News news = new News(title, publishedAt, author, urlToImage, urlweb);


                        result.add(news);
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
        protected void onPostExecute(ArrayList<News> news) {
            progressBarNews.setVisibility(View.INVISIBLE);
            for (News newsitem : news) {
                newsArrayList.add(newsitem);
            }

            newsAdapter.notifyDataSetChanged();

        }
    }
}

