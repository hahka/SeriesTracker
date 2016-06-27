package fr.hahka.seriestracker.episodes.planning;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by thibautvirolle on 27/06/2016.
 */
public class PlanningUtils {

    public static int getNextId(Context c) {

        RealmConfiguration config = new RealmConfiguration.Builder(c).build();
        Realm realm = Realm.getInstance(config);
        Number value = realm.where(Planning.class).max("id");
        if(value != null)
            return value.intValue() + 1;
        else
            return 1;

    }

}
