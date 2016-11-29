package com.example.anh.anhnguyen_pset3;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.anh.anhnguyen_pset3.models.MovieModel;

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
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.start;
import static android.net.wifi.WifiConfiguration.Status.strings;
import static android.os.Build.VERSION_CODES.M;
import static android.provider.UserDictionary.Words.APP_ID;

//import static com.example.anh.anhnguyen_pset3.R.id.lvMovies;
import static com.example.anh.anhnguyen_pset3.R.id.progressionBar;
import static com.example.anh.anhnguyen_pset3.R.id.searchText;
import static com.example.anh.anhnguyen_pset3.R.id.title;

public class MainActivity extends AppCompatActivity {

    TextView search_View;
    Button search_Button;
    EditText search_Tag;
    ListAdapter setAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // ListView lvMovies=(ListView) findViewById(R.id.lvMovies);

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
        private class startSearch extends AsyncTask<Void, Void,  String> {
            String movieTitle = ((EditText) findViewById(R.id.searchText)).getText().toString();
            ProgressBar progressionBar = (ProgressBar) findViewById(R.id.progressionBar);
            EditText searchText = (EditText) findViewById(R.id.searchText);


            protected void onPreExecute() {
                progressionBar.setVisibility(View.VISIBLE);
                searchText.setText("");
            }

            @Override

            protected  String doInBackground(Void... params) {


                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String movieJsonString = null;

                try {


                    String httpUrl = "http://www.omdbapi.com/?s=";

                    URL url = new URL(httpUrl + movieTitle);

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    // Read the input stream into string
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {

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
                    JSONArray parentArray = parentObject.getJSONArray("Search");

                    StringBuffer finalBufferedData = new StringBuffer();

                    List<MovieModel> movieModelList = new ArrayList<>();
                    for (int i=0; i<parentArray.length(); i++){
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    MovieModel movieModel = new MovieModel();
                    movieModel.setTitle(finalObject.getString("Title"));
                    //movieModel.setYear(finalObject.getInt("Year"));
                    //movieModel.setImdbID(finalObject.getString("imdbID"));
                    //movieModel.setType(finalObject.getString("Type"));
                    //movieModel.setPoster(finalObject.getString("Poster"));
                    String movieName = finalObject.getString("Title");
                    String movieYear = finalObject.getString("Year");





                    //add final object
                    movieModelList.add(movieModel);
                    finalBufferedData.append(movieName + "  -  "+movieYear + "\n");
                    }

                    //return movieModelList;
                    return finalBufferedData.toString();


                    /*JSONObject parentObject = new JSONObject(movieJsonString);


                    String movieName = parentObject.getString("Title");
                    String movieYear = parentObject.getString("Year");

                    return movieName+"  -  "+movieYear; */

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
            protected void onPostExecute( String s) {
            //ListView lvMovies = (ListView) findViewById(R.id.lvMovies);

                super.onPostExecute(s);
                //MovieAdapter adapter = new MovieAdapter(getApplicationContext(), R.layout.row, s);
                //lvMovies.setAdapter(adapter);

                search_View.setText(s);
                progressionBar.setVisibility(View.GONE);

            }



            }

    public class MovieAdapter extends ArrayAdapter {

        public List<MovieModel> movieModelList;
        public int resource;
        private LayoutInflater inflater;
        public MovieAdapter(Context context, int resource, List<MovieModel> objects){
            super(context, resource, objects);
            movieModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            if (convertView == null){
                convertView = inflater.inflate(resource, null);
            return super.getView(position, convertView, parent);
        }

            ImageView ivMovieIcon;
            TextView tvMovie;
            TextView tvYear;
            TextView tvID;
            TextView tvType;

            ivMovieIcon = (ImageView) findViewById(R.id.ivIcon);
            tvMovie = (TextView) findViewById(R.id.tvMovie);
            tvYear = (TextView) findViewById(R.id.tvYear);
            tvID = (TextView) findViewById(R.id.tvID);
            tvType = (TextView) findViewById(R.id.tvType);

            tvMovie.setText(movieModelList.get(position).getTitle());
            tvYear.setText("Year: " + movieModelList.get(position).getYear());
            tvID.setText(movieModelList.get(position).getImdbID());
            tvType.setText(movieModelList.get(position).getType());

            return convertView;




    }






        }

    //makes menu bigger
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}









