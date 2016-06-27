package fr.hahka.seriestracker.shows;

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

import fr.hahka.seriestracker.simpleshow.SimpleShow;
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.RealmUtils;
import fr.hahka.seriestracker.utilitaires.XmlParser;
import io.realm.Realm;
import io.realm.RealmConfiguration;

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

        Context c = getApplicationContext();
        ArrayList<SimpleShow> showsList;
        Bundle bundle = new Bundle();
        final ResultReceiver receiver = workIntent.getParcelableExtra("receiver");


        String userId = workIntent.getStringExtra(Config.USER_ID);
        String token = workIntent.getStringExtra(Config.TOKEN);

        try {

            URL url = new URL("https://api.betaseries.com/members/infos?id=" + userId + "&only=shows" + "&token=" + token + "&key=" + Config.BETASERIES_API_KEY);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            showsList = ShowsJsonParser.readUserShowsListJsonStream(in);

            for(SimpleShow show : showsList){

                show.setUserId(Integer.parseInt(userId));
                URL urlImage = new URL("http://thetvdb.com/api/"+Config.THETVDB_API_KEY+"/series/" + show.getThetvdbId());
                HttpURLConnection urlImageConnection = (HttpURLConnection) urlImage.openConnection();

                InputStream is2 = new BufferedInputStream(urlImageConnection.getInputStream());

                String urlBanner = XmlParser.readBannerUrlFromXml(is2);
                if(!urlBanner.equals(""))
                    show.setUrl("http://thetvdb.com/banners/"+urlBanner);
                else
                    show.setUrl("");

                is2.close();



                RealmConfiguration config = new RealmConfiguration.Builder(this).build();
                Realm realm = Realm.getInstance(config);

                if(!RealmUtils.exists(c,SimpleShow.class,show.getId())){
                    realm.beginTransaction();

                    realm.copyToRealm(show);

                    realm.commitTransaction();
                } else {
                    realm.beginTransaction();
                    //realm.clear(SimpleShow.class);
                    realm.commitTransaction();
                }

            }

            in.close();

            receiver.send(Config.STATUS_FINISHED, bundle);

        } catch (Exception e) {
            e.printStackTrace();
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(Config.STATUS_ERROR, bundle);
        }

    }

}