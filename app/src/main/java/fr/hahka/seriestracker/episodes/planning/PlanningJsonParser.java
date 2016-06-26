package fr.hahka.seriestracker.episodes.planning;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fr.hahka.seriestracker.R;
import fr.hahka.seriestracker.episodes.episodes.Episode;
import fr.hahka.seriestracker.utilitaires.RealmUtils;
import io.realm.Realm;

/**
 * Created by thibautvirolle on 15/01/15.
 */
public class PlanningJsonParser {

    public static ArrayList<Episode> readUserPlanningJsonStream(Context c, InputStream in) throws IOException {

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

        Realm realm = Realm.getInstance(c);

        String[] headerList = c.getResources().getStringArray(R.array.header_items);
        int headerIndice = -1;

        for(Episode episode : values){

            int newHeaderIndice = episode.getHeaderIndice();
            if(!(headerIndice == newHeaderIndice)){
                headerIndice = newHeaderIndice;
                episode.setHeader(headerList[headerIndice]);
            }

            Log.d("toto", episode.getHeader());

            if(!RealmUtils.exists(c,Episode.class,episode.getId())){
                realm.beginTransaction();

                realm.copyToRealm(episode);

                realm.commitTransaction();
            } else {
                realm.beginTransaction();
                //realm.clear(SimpleShow.class);
                realm.commitTransaction();
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
        String title = "", show = "", code = "", date = "";

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
                    date = reader.nextString();
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
