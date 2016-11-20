package com.example.anh.anhnguyen_pset3;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.start;
import static android.net.wifi.WifiConfiguration.Status.strings;
import static android.provider.UserDictionary.Words.APP_ID;
import static com.example.anh.anhnguyen_pset3.R.id.clickedView;
import static com.example.anh.anhnguyen_pset3.R.id.progressionBar;
import static com.example.anh.anhnguyen_pset3.R.id.searchText;
import static com.example.anh.anhnguyen_pset3.R.id.title;

public class MainActivity extends AppCompatActivity {

    TextView search_View;
    Button search_Button;
    EditText search_Tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search_View = (TextView) findViewById(R.id.clickedView);
        search_Button = (Button) findViewById(R.id.searchButton);
        search_Tag = (EditText) findViewById(R.id.searchText);
        search_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new startSearch().execute();
            }
        });

    }
        private class startSearch extends AsyncTask<Void, Void, String> {
            String movieTitle = ((EditText) findViewById(R.id.searchText)).getText().toString();
            ProgressBar progressionBar = (ProgressBar) findViewById(R.id.progressionBar);
            EditText searchText = (EditText) findViewById(R.id.searchText);

            protected void onPreExecute() {
                progressionBar.setVisibility(View.VISIBLE);
                searchText.setText("");
            }

            @Override

            protected String doInBackground(Void... params) {


                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                String movieJsonString = null;

                try {


                    String httpUrl = "http://www.omdbapi.com/?t=";

                    URL url = new URL(httpUrl + movieTitle);

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    // Read the input stream into string
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {

                        return null;
                    }

                    movieJsonString = buffer.toString();

                    JSONObject parentObject = new JSONObject(movieJsonString);


                    String movieName = parentObject.getString("Title");
                    String movieYear = parentObject.getString("Year");

                    return movieName+"  -  "+movieYear;

                } catch (IOException e) {
                    return null;
                }
                  catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }

                finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {

                        }
                    }
                }
            }

            @Override
            protected void onPostExecute(String s) {

                super.onPostExecute(s);


                search_View.setText(s);
                progressionBar.setVisibility(View.GONE);

            }



            }





        }









