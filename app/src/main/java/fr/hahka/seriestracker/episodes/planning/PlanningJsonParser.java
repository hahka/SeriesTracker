package fr.hahka.seriestracker.episodes.planning;

import android.content.Context;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.hahka.seriestracker.episodes.episodes.Episode;
import fr.hahka.seriestracker.episodes.episodes.EpisodeUtils;
import fr.hahka.seriestracker.user.User;
import fr.hahka.seriestracker.utilitaires.RealmUtils;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;

/**
 * Created by thibautvirolle on 15/01/15.
 */
public class PlanningJsonParser {

    public static ArrayList<Episode> readUserPlanningJsonStream(Context c, InputStream in, String userId) throws IOException {

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        ArrayList<Episode> values = new ArrayList<>();

        reader.beginObject();
        while (reader.hasNext() && (reader.peek().toString().equals("NAME"))) {
            String value = reader.nextName();
            switch (value) {
                case "episodes":

                    List<Episode> episodes = readPlanningArray(reader);

                    for (Episode episode : episodes) {
                        values.add(episode);
                    }
                    break;
                case "error":
                    reader.skipValue();
                    break;
                default:
                    reader.skipValue();
                    break;
            }

        }
        reader.endObject();
        reader.close();


        RealmConfiguration config = new RealmConfiguration.Builder(c).build();
        Realm realm = Realm.getInstance(config);


        RealmQuery<User> userQuery = realm.where(User.class)
                .equalTo("id", Integer.parseInt(userId));

        User user = userQuery.findFirst();

        if(user != null) {
            for (Episode episode : values) {

                if (!RealmUtils.exists(c, Episode.class, episode.getId())) {
                    realm.beginTransaction();
                    realm.copyToRealm(episode);
                    realm.commitTransaction();

                } else {
                    //realm.beginTransaction();
                    //realm.clear(SimpleShow.class);
                    //realm.commitTransaction();
                }

                realm.beginTransaction();

                RealmQuery<Episode> episodeQuery = realm.where(Episode.class)
                        .equalTo("id", episode.getId());

                Episode ep = episodeQuery.findFirst();
                realm.commitTransaction();

                if(ep != null) {
                    realm.beginTransaction();

                    RealmQuery<Planning> planningRealmQuery = realm.where(Planning.class)
                            .equalTo("user.id", user.getId())
                            .equalTo("episode.id", ep.getId());

                    if (planningRealmQuery.findAll().size() <= 0) {
                        Planning planning = new Planning();
                        planning.setId(PlanningUtils.getNextId(c));
                        planning.setEpisode(ep);
                        planning.setUser(user);
                        realm.copyToRealm(planning);
                    }
                    realm.commitTransaction();
                }

            }
        }


        return values;
    }


    private static List<Episode> readPlanningArray(JsonReader reader) throws IOException {
        List<Episode> episodes = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            episodes.add(readPlanning(reader));
        }
        reader.endArray();

        return episodes;
    }



    private static Episode readPlanning(JsonReader reader) throws IOException {
        int id = 0;
        String title = "", show = "", code = "";//, date = "";
        Date date = null;

        reader.beginObject();

        while (reader.hasNext()) {

            String name = reader.nextName();
            switch (name) {
                case "id":
                    id = reader.nextInt();
                    break;
                case "title":
                    title = reader.nextString();
                    break;
                case "show":
                    reader.beginObject();
                    while (reader.hasNext()) {

                        String nameBis = reader.nextName();
                        if (nameBis.equals("title")) {
                            show = reader.nextString();
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();

                    break;
                case "code":
                    code = reader.nextString();
                    break;
                case "date":
                    String dateString = reader.nextString();
                    Calendar instance = Calendar.getInstance();
                    instance.set(EpisodeUtils.getAnneeFromString(dateString),
                            EpisodeUtils.getMoisFromString(dateString) - 1,
                            EpisodeUtils.getJourFromString(dateString));
                    date = instance.getTime();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        Episode episode = new Episode();
        episode.setId(id);
        episode.setTitle(title);
        episode.setShow(show);
        episode.setCode(code);
        episode.setDate(date);

        return episode;
    }

}
