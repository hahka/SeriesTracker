package fr.hahka.seriestracker.shows;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.ArrayList;

import fr.hahka.seriestracker.user.User;
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.XmlParser;

import static fr.hahka.seriestracker.user.UserJsonParser.readUserJsonStream;

/**
 * Created by thibautvirolle on 14/01/15.
 * Service pour récupérer le planning
 */
public class ShowsService extends IntentService {

    private static final String TAG = ShowsService.class.getSimpleName();

    public ShowsService() {
        super("User Service");
    }

    public ShowsService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {

        User user;
        ArrayList<SimpleShow> showsList;
        Bundle bundle = new Bundle();
        final ResultReceiver receiver = workIntent.getParcelableExtra("receiver");


        String userId = workIntent.getStringExtra(Config.USER_ID);
        String token = workIntent.getStringExtra(Config.TOKEN);

        Log.d(TAG,userId + " / " + token);

        // Récupération du planning de l'utilisateur connecté
        HttpGet httpget = new HttpGet("https://api.betaseries.com/members/infos?id=" + userId + "&only=shows" + "&token=" + token + "&key=" + Config.BETASERIES_API_KEY);

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();

            user = readUserJsonStream(is);

            showsList = user.getShowsList();

            for(SimpleShow show : showsList){

                HttpGet httpgetImage = new HttpGet("http://thetvdb.com/api/"+Config.THETVDB_API_KEY+"/series/" + show.getThetvdbId());

                HttpResponse showsPicturesService = httpclient.execute(httpgetImage);

                InputStream is2 = showsPicturesService.getEntity().getContent();

                /*BufferedReader r = new BufferedReader(new InputStreamReader(is2));
                String line;
                while ((line = r.readLine()) != null) {
                    Log.d(TAG,line);
                }*/

                String url = XmlParser.readBannerUrlFromXml(is2);
                if(!url.equals(""))
                    show.setUrl("http://thetvdb.com/banners/"+url);

                is2.close();

            }

            is.close();

            bundle.putParcelableArrayList(Config.SHOWS_LIST, showsList);
            receiver.send(Config.STATUS_FINISHED, bundle);

        } catch (Exception e) {
            e.printStackTrace();
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(Config.STATUS_ERROR, bundle);
        }

    }

}