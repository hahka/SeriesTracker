package fr.hahka.seriestracker.episodes.episodes;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.RealmUtils;
import io.realm.Realm;
import io.realm.RealmConfiguration;

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


        Context c = getApplicationContext();
        ArrayList<Episode> episodesList;
        Bundle bundle = new Bundle();
        final ResultReceiver receiver = workIntent.getParcelableExtra("receiver");

        String showId = workIntent.getStringExtra(Config.SHOW_ID);
        String token = workIntent.getStringExtra(Config.TOKEN);

        try {

            URL url = new URL("https://api.betaseries.com/shows/episodes?id=" + showId + "&token=" + token + "&key=" + Config.BETASERIES_API_KEY);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            episodesList = EpisodesJsonParser.readShowEpisodesJsonStream(in);

            RealmConfiguration config = new RealmConfiguration.Builder(this).build();
            Realm realm = Realm.getInstance(config);

            for(Episode episode : episodesList) {
                if (!RealmUtils.exists(c, Episode.class, episode.getId())) {
                    realm.beginTransaction();
                    Episode newEpisode = realm.createObject(Episode.class); // Create a new object
                    newEpisode.setId(episode.getId());
                    newEpisode.setDate(episode.getDate());
                    newEpisode.setEpisode(episode.getEpisode());
                    newEpisode.setSeason(episode.getSeason());
                    newEpisode.setSeen(episode.isSeen());
                    newEpisode.setShowId(Integer.parseInt(showId));
                    newEpisode.setTitle(episode.getTitle());

                    realm.commitTransaction();
                } else {
                    realm.beginTransaction();
                    //realm.clear(SimpleShow.class);
                    realm.commitTransaction();
                }
            }
            urlConnection.disconnect();

            receiver.send(Config.STATUS_FINISHED, bundle);

        } catch (Exception e) {
            e.printStackTrace();
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(Config.STATUS_ERROR, bundle);
        }

    }
}