package fr.hahka.seriestracker.episodes.planning;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import fr.hahka.seriestracker.episodes.episodes.Episode;
import fr.hahka.seriestracker.utilitaires.Config;

/**
 * Created by thibautvirolle on 14/01/15.
 * Service pour récupérer le planning
 */
public class PlanningService extends IntentService {

    public PlanningService() {
        super("PlanningBackup Service");
    }

    public PlanningService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {

        ArrayList<PlanningBackup> planningBackupList;
        ArrayList<Episode> episodesList;
        Bundle bundle = new Bundle();
        final ResultReceiver receiver = workIntent.getParcelableExtra("receiver");

        String userId = workIntent.getStringExtra(Config.USER_ID);
        String token = workIntent.getStringExtra(Config.TOKEN);

        try {
            URL url = new URL("https://api.betaseries.com/planning/member?id=" + userId + "&token=" + token + "&key=" + Config.BETASERIES_API_KEY);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //planningBackupList = PlanningJsonParser.readUserPlanningJsonStream(in);
            episodesList = PlanningJsonParser.readUserPlanningJsonStream(getApplicationContext(), in);

            //bundle.putParcelableArrayList(Config.PLANNING_LIST, planningBackupList);
            receiver.send(Config.STATUS_FINISHED, bundle);

            urlConnection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(Config.STATUS_ERROR, bundle);
        }

    }
}