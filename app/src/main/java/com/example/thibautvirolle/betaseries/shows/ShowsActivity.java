package com.example.thibautvirolle.betaseries.shows;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thibautvirolle.betaseries.R;
import com.example.thibautvirolle.betaseries.utilitaires.Config;
import com.example.thibautvirolle.betaseries.utilitaires.DownloadResultReceiver;
import com.example.thibautvirolle.betaseries.utilitaires.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.ArrayList;


public class ShowsActivity extends Activity implements DownloadResultReceiver.Receiver {

    private static String TAG = ShowsActivity.class.getSimpleName();
    private ArrayList<Show> userShowsList = new ArrayList<>();
    private static View mContentView;
    private static View mProgressView;
    private boolean displayArchived = true;
    private String token = "";
    private String userId;

    private DownloadResultReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shows);

        Intent callingIntent = getIntent();
        userId = callingIntent.getStringExtra("user_id");
        token = callingIntent.getStringExtra("token");

        mContentView = findViewById(R.id.showsListContainer);
        mProgressView = findViewById(R.id.progressBar);

        Log.d(TAG,"onCreate");
        /*API request = new API("https://api.betaseries.com/members/infos?id="+userId+"&only=shows"+"&key="+ Config.API_KEY);
        request.execute((Void) null);
        */

        /* Starting Download Service */
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, ShowsService.class);

        /* Send optional extras to Download IntentService */
        intent.putExtra("url", "https://api.betaseries.com/members/infos?id="+userId+"&only=shows"+"&key="+ Config.API_KEY);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("requestId", 101);

        startService(intent);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shows, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_archived) {

            ListView showsListView = (ListView) findViewById(R.id.showsListView);

            if(displayArchived){
                showsListView.setAdapter(new ShowsNotArchivedAdapter(getApplicationContext(),userShowsList,token));
                displayArchived = false;
                item.setTitle(R.string.menu_show_archived);
            } else {
                showsListView.setAdapter(new ShowsAdapter(getApplicationContext(),userShowsList,token));
                displayArchived = true;
                item.setTitle(R.string.menu_hide_archived);
            }

            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_reload) {

            /* Starting Download Service */
            mReceiver = new DownloadResultReceiver(new Handler());
            mReceiver.setReceiver(this);
            Intent intent = new Intent(Intent.ACTION_SYNC, null, this, ShowsService.class);

        /* Send optional extras to Download IntentService */
            intent.putExtra("url", "https://api.betaseries.com/members/infos?id="+userId+"&only=shows"+"&key="+ Config.API_KEY);
            intent.putExtra("receiver", mReceiver);
            intent.putExtra("requestId", 101);

            startService(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case ShowsService.STATUS_RUNNING:

                //setProgressBarIndeterminateVisibility(true);
                break;
            case ShowsService.STATUS_FINISHED:
                /* Hide progress & extract result from bundle */
                //setProgressBarIndeterminateVisibility(false);

                userShowsList = resultData.getParcelableArrayList("shows");
                //Toast.makeText(this, String.valueOf(userShowsList.size()), Toast.LENGTH_SHORT).show();
                Log.d(TAG,"Data received");

                ListView showsListView = (ListView) findViewById(R.id.showsListView);
                showsListView.setAdapter(new ShowsAdapter(getApplicationContext(),userShowsList,token));
                showProgress(false);

                break;
            case ShowsService.STATUS_ERROR:
                /* Handle the error */
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                break;
        }
    }


    private class API extends AsyncTask<Void, Void, Boolean> {

        private String target = "";

        public API(String pTarget)
        {
            this.target = pTarget;
        }


        @Override
        protected Boolean doInBackground(Void... voids) {

            HttpGet httpget = new HttpGet(target);

            try {
                Log.d(TAG, "1");
                HttpClient httpclient = new DefaultHttpClient();

                Log.d(TAG, "2");
                HttpResponse response=httpclient.execute(httpget);
                InputStream is = response.getEntity().getContent();

                Log.d(TAG, "3");
                userShowsList = JsonParser.readUserShowsJsonStream(is);

                Log.d(TAG, "4");
                is.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return (!userShowsList.isEmpty());

        }


        @Override
        protected void onPostExecute(final Boolean success) {

            if(success)
            {
                ListView showsListView = (ListView) findViewById(R.id.showsListView);
                showsListView.setAdapter(new ShowsAdapter(getApplicationContext(),userShowsList,token));
                showProgress(false);
            } else {
                Log.d(TAG,"ERREUR");
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
