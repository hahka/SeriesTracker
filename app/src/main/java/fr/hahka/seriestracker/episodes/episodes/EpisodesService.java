package fr.hahka.seriestracker.episodes.episodes;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.ArrayList;

import fr.hahka.seriestracker.utilitaires.Config;

/**
 * Created by thibautvirolle on 14/01/15.
 * Service pour récupérer le planning
 */
public class EpisodesService extends IntentService {

    public EpisodesService() {
        super("Episode Service");
    }

    public EpisodesService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {

        ArrayList<Episode> episodesList;
        Bundle bundle = new Bundle();
        final ResultReceiver receiver = workIntent.getParcelableExtra("receiver");

        String showId = workIntent.getStringExtra(Config.SHOW_ID);
        String token = workIntent.getStringExtra(Config.TOKEN);
        // Récupération du planning de l'utilisateur connecté
        HttpGet httpget = new HttpGet("https://api.betaseries.com/shows/episodes?id=" + showId + "&token=" + token + "&key=" + Config.BETASERIES_API_KEY);

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();

            episodesList = EpisodesJsonParser.readShowEpisodesJsonStream(is);

            is.close();

            bundle.putParcelableArrayList(Config.EPISODES_LIST, episodesList);
            receiver.send(Config.STATUS_FINISHED, bundle);

        } catch (Exception e) {
            e.printStackTrace();
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(Config.STATUS_ERROR, bundle);
        }

    }
}