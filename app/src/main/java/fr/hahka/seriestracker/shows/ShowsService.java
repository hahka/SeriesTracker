package fr.hahka.seriestracker.shows;

import android.app.IntentService;
import android.content.Context;
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

import fr.hahka.seriestracker.simpleshow.SimpleShow;
import fr.hahka.seriestracker.user.User;
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.RealmUtils;
import fr.hahka.seriestracker.utilitaires.XmlParser;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

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

        Context c = getApplicationContext();
        User user;
        ArrayList<SimpleShow> showsList;
        Bundle bundle = new Bundle();
        final ResultReceiver receiver = workIntent.getParcelableExtra("receiver");


        String userId = workIntent.getStringExtra(Config.USER_ID);
        String token = workIntent.getStringExtra(Config.TOKEN);

        // Récupération du planning de l'utilisateur connecté
        HttpGet httpget = new HttpGet("https://api.betaseries.com/members/infos?id=" + userId + "&only=shows" + "&token=" + token + "&key=" + Config.BETASERIES_API_KEY);

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();

            user = readUserJsonStream(is);

            showsList = user.getShowsList();



            for(SimpleShow show : showsList){

                show.setUserId(Integer.parseInt(userId));
                HttpGet httpgetImage = new HttpGet("http://thetvdb.com/api/"+Config.THETVDB_API_KEY+"/series/" + show.getThetvdbId());

                HttpResponse showsPicturesService = httpclient.execute(httpgetImage);

                InputStream is2 = showsPicturesService.getEntity().getContent();

                String url = XmlParser.readBannerUrlFromXml(is2);
                if(!url.equals(""))
                    show.setUrl("http://thetvdb.com/banners/"+url);

                is2.close();



                Realm realm = Realm.getInstance(this);

                if(!RealmUtils.exists(c,SimpleShow.class,show.getId())){
                    realm.beginTransaction();
                    SimpleShow simpleShow = realm.createObject(SimpleShow.class); // Create a new object
                    simpleShow.setId(show.getId());
                    simpleShow.setThetvdbId(show.getThetvdbId());
                    simpleShow.setTitle(show.getTitle());
                    simpleShow.setStatus(show.getStatus());
                    simpleShow.setRemaining(show.getRemaining());
                    simpleShow.setUserId(show.getUserId());
                    simpleShow.setUrl(show.getUrl());

                    //SimpleShow realmShow = realm.copyToRealm(show);

                    realm.commitTransaction();
                } else {
                    realm.beginTransaction();
                    //realm.clear(SimpleShow.class);
                    realm.commitTransaction();
                    Log.d(TAG, "nombre occurence de " + show.getId() + ": " + RealmUtils.findById(c, SimpleShow.class, show.getId()));
                }




            }

            Realm realm = Realm.getInstance(this);
            RealmQuery<SimpleShow> query = realm.where(SimpleShow.class);

            RealmResults<SimpleShow> result1 = query.findAll();

            Log.d(TAG,"Objets en base : "+String.valueOf(result1.size()));

            is.close();

            //bundle.putParcelableArrayList(Config.SHOWS_LIST, showsList);
            receiver.send(Config.STATUS_FINISHED, bundle);

        } catch (Exception e) {
            e.printStackTrace();
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(Config.STATUS_ERROR, bundle);
        }

    }

}