package fr.hahka.seriestracker.api;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import fr.hahka.seriestracker.episodes.episodes.Episode;
import fr.hahka.seriestracker.episodes.episodes.EpisodesJsonParser;
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.RealmUtils;
import io.realm.Realm;

/**
 * Created by thibautvirolle on 23/06/2016.
 */
public class APIService extends IntentService {

    private final static String TAG = APIService.class.getSimpleName();

    public APIService() {
        super("API Service");
    }

    public APIService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {


        Context c = getApplicationContext();
        ArrayList<Episode> episodesList;
        Bundle bundle = new Bundle();

        final String resource = workIntent.getStringExtra("resource");
        final String action = workIntent.getStringExtra("action");
        final String rest = workIntent.getStringExtra("rest");

        String errorMessage = "";

        final ResultReceiver receiver = workIntent.getParcelableExtra("receiver");

        /*String showId = workIntent.getStringExtra(Config.SHOW_ID);
        String token = workIntent.getStringExtra(Config.TOKEN);*/

        Log.d(TAG, "1");

        try {

            ApiParamHashMap params = (ApiParamHashMap)workIntent.getParcelableExtra("params");
            Log.d(TAG, "2");

            String showId = params.get("id");
            Log.d(TAG, "3");

            /*URL url = new URL(Config.BETASERIES_API
                    + resource + "/" + action
                    + "?key=" + Config.BETASERIES_API_KEY
                    + "&id=" + showId + "&token=" + token);*/

            URL url = new URL(Config.BETASERIES_API
                    + resource + "/" + action
                    + "?key=" + Config.BETASERIES_API_KEY
                    + "&" + params.toString());


            Log.d(TAG, "4");

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            Log.d(TAG, "5");
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());


            Log.d(TAG, "6");

            if(rest.equals("GET")) {

                Log.d(TAG, "7");

                episodesList = EpisodesJsonParser.readShowEpisodesJsonStream(in);
                Realm realm = Realm.getInstance(this);

                Log.d(TAG, "8");

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

                Log.d(TAG, "9");

            } else {
                errorMessage = "404 Not Found";
            }

            if(!errorMessage.equals("")) {
                bundle.putString(Intent.EXTRA_TEXT, errorMessage);
                receiver.send(Config.STATUS_ERROR, bundle);
            }

            Log.d(TAG, "10");

            urlConnection.disconnect();

            Log.d(TAG, "11");

            receiver.send(Config.STATUS_FINISHED, bundle);

            Log.d(TAG, "12");

        } catch (Exception e) {
            e.printStackTrace();
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(Config.STATUS_ERROR, bundle);
        }

    }
}