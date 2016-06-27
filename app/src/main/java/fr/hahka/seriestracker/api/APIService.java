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
import fr.hahka.seriestracker.episodes.planning.PlanningJsonParser;
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.RealmUtils;
import io.realm.Realm;
import io.realm.RealmConfiguration;

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

        try {
            /*URL url = new URL(Config.BETASERIES_API
                    + resource + "/" + action
                    + "?key=" + Config.BETASERIES_API_KEY
                    + "&id=" + showId + "&token=" + token);*/
            ApiParamHashMap params = (ApiParamHashMap) workIntent.getParcelableExtra("params");

            URL url = new URL(Config.BETASERIES_API
                    + resource + "/" + action
                    + "?key=" + Config.BETASERIES_API_KEY
                    + "&" + params.toString());

            if(resource.equals("planning") && action.equals("member")) {

                String userId = params.get("id");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                PlanningJsonParser.readUserPlanningJsonStream(getApplicationContext(), in, userId);

                receiver.send(Config.STATUS_FINISHED, bundle);

                urlConnection.disconnect();

            } else {
                String showId = params.get("id");


                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                if (rest.equals("GET")) {

                    episodesList = EpisodesJsonParser.readShowEpisodesJsonStream(in);

                    RealmConfiguration config = new RealmConfiguration.Builder(this).build();
                    Realm realm = Realm.getInstance(config);

                    for (Episode episode : episodesList) {
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

                            Log.d(TAG, episode.getTitle());

                            realm.commitTransaction();
                        } else {
                            realm.beginTransaction();
                            //realm.clear(SimpleShow.class);
                            realm.commitTransaction();
                        }
                    }

                } else {
                    errorMessage = "404 Not Found";
                }

                urlConnection.disconnect();

                if (!errorMessage.equals("")) {
                    bundle.putString(Intent.EXTRA_TEXT, errorMessage);
                    receiver.send(Config.STATUS_ERROR, bundle);
                }
            }

            receiver.send(Config.STATUS_FINISHED, bundle);

        } catch (Exception e) {
            e.printStackTrace();
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(Config.STATUS_ERROR, bundle);
        }

    }
}