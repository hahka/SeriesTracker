package fr.hahka.seriestracker.user;

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

import static fr.hahka.seriestracker.user.UserJsonParser.readUserJsonStream;

/**
 * Created by thibautvirolle on 14/01/15.
 * Service pour récupérer les informations utilisateur
 */
public class UserService extends IntentService {

    public UserService() {
        super("User Service");
    }

    public UserService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {

        Context c = getApplicationContext();
        User user;
        ArrayList<SimpleShow> simpleShowsList = null;
        Bundle bundle = new Bundle();
        final ResultReceiver receiver = workIntent.getParcelableExtra("receiver");

        String userId = workIntent.getStringExtra(Config.USER_ID);
        String token = workIntent.getStringExtra(Config.TOKEN);

        try {
            URL url = new URL("https://api.betaseries.com/members/infos?id=" + userId + "&only=shows" + "&token=" + token + "&key=" + Config.BETASERIES_API_KEY);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                user = readUserJsonStream(c, in);

                in.close();

                bundle.putInt(Config.USER, user.getId());
                receiver.send(Config.STATUS_FINISHED, bundle);
            } catch (Exception e) {
                e.printStackTrace();
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(Config.STATUS_ERROR, bundle);
            } finally {
                urlConnection.disconnect();

            }


        } catch (Exception e) {
            e.printStackTrace();
            bundle.putString(Intent.EXTRA_TEXT, e.toString());
            receiver.send(Config.STATUS_ERROR, bundle);
        }

    }
}