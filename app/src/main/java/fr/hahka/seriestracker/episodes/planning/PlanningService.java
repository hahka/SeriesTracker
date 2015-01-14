package fr.hahka.seriestracker.episodes.planning;

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
import fr.hahka.seriestracker.utilitaires.JsonParser;

/**
 * Created by thibautvirolle on 14/01/15.
 * Service pour récupérer le planning
 */
public class PlanningService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    public PlanningService() {
        super("Planning Service");
    }

    public PlanningService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {

        ArrayList<Planning> planningList;
        Bundle bundle = new Bundle();
        final ResultReceiver receiver = workIntent.getParcelableExtra("receiver");

        String userId = workIntent.getStringExtra(Config.USER_ID);
        String token = workIntent.getStringExtra(Config.TOKEN);
        // Récupération du planning de l'utilisateur connecté
        HttpGet httpget = new HttpGet("https://api.betaseries.com/planning/member?id=" + userId + "&token=" + token + "&key=" + Config.API_KEY);

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();

            planningList = JsonParser.readUserPlanningJsonStream(is);

            is.close();

            bundle.putParcelableArrayList(Config.PLANNING_LIST, planningList);
            receiver.send(STATUS_FINISHED, bundle);

        } catch (Exception e) {
            e.printStackTrace();
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(STATUS_ERROR, bundle);
        }

    }
}