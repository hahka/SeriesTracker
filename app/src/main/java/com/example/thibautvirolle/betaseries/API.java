package com.example.thibautvirolle.betaseries;

import android.os.AsyncTask;

import com.example.thibautvirolle.betaseries.shows.Show;
import com.example.thibautvirolle.betaseries.utilitaires.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by thibautvirolle on 07/12/14.
 */
public class API extends AsyncTask<Void, Void, Boolean> {

    private String target = "";

    public API(String pTarget)
    {
        this.target = pTarget;
    }


    @Override
    protected Boolean doInBackground(Void... voids) {

        ArrayList<Show> showsList = new ArrayList<Show>();

        HttpGet httpget = new HttpGet(target);

        try {
            HttpClient httpclient = new DefaultHttpClient();

            HttpResponse response=httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();

            showsList = JsonParser.readUserShowsJsonStream(is);

            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return (!showsList.isEmpty());

    }


    @Override
    protected void onPostExecute(final Boolean success) {

        // Tache à effectuer après la fin de la requête
    }

    @Override
    protected void onCancelled() {

    }



}
