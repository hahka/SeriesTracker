package com.example.thibautvirolle.betaseries.episodes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.thibautvirolle.betaseries.R;
import com.example.thibautvirolle.betaseries.utilitaires.Config;
import com.example.thibautvirolle.betaseries.utilitaires.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.ArrayList;


public class EpisodesActivity extends Activity {

    private static String TAG = EpisodesActivity.class.getSimpleName();
    private ArrayList<Episode> userShowEpisodesList = new ArrayList<Episode>();
    private View mContentView;
    private View mProgressView;
    private EpisodesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);


        mContentView = findViewById(R.id.episodesListContainer);
        mProgressView = findViewById(R.id.loadingContainer);

        Intent callingIntent = getIntent();
        String showId = callingIntent.getStringExtra("showId");
        String token = callingIntent.getStringExtra("token");

        API request = new API("https://api.betaseries.com/shows/episodes?id="+showId+"&token="+ token + "&key=" + Config.API_KEY);
        request.execute((Void) null);
        showProgress(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_episodes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            int i = 0;

        }

        return super.onOptionsItemSelected(item);
    }



    private class API extends AsyncTask<Void, Void, Boolean> {

        private String target = "";

        public API(String pTarget)
        {
            this.target = pTarget;
        }


        @Override
        protected Boolean doInBackground(Void... voids) {

            Log.d(TAG,"3");
            HttpGet httpget = new HttpGet(target);
            Log.d(TAG,"3.1");

            try {
                HttpClient httpclient = new DefaultHttpClient();
                Log.d(TAG,"3.2");

                HttpResponse response=httpclient.execute(httpget);
                Log.d(TAG,"3.3");
                InputStream is = response.getEntity().getContent();

                Log.d(TAG,"4");

                userShowEpisodesList = JsonParser.readShowEpisodesJsonStream(is);
                for(Episode ep : userShowEpisodesList)
                {
                    if(ep.isSeen())
                        Log.d(TAG,ep.getTitle());
                }
                Log.d(TAG,"5");

                is.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return (!userShowEpisodesList.isEmpty());

        }


        @Override
        protected void onPostExecute(final Boolean success) {

            if(success)
            {
                Log.d(TAG,"success");
                ListView episodesListView = (ListView) findViewById(R.id.episodesListView);
                adapter = new EpisodesAdapter(getApplicationContext(),userShowEpisodesList);
                episodesListView.setAdapter(adapter);
                showProgress(false);
                Log.d(TAG,"post adapter");
            } else {
                // TODO : Erreur à gérer
            }


        }

        @Override
        protected void onCancelled() {
            // TODO : annulation requête
        }


    }




    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mContentView.setVisibility(show ? View.GONE : View.VISIBLE);
            mContentView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mContentView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mContentView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}
