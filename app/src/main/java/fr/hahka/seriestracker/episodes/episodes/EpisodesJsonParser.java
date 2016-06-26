package fr.hahka.seriestracker.episodes.episodes;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thibautvirolle on 15/01/15.
 * Classe pour parser les json des épisodes
 */
public class EpisodesJsonParser {

    public static ArrayList<Episode> readShowEpisodesJsonStream(InputStream in) throws IOException {

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        ArrayList<Episode> values = new ArrayList<>();

        reader.beginObject();
        while (reader.hasNext() && (reader.peek().toString().equals("NAME"))) {
            String value = reader.nextName();
            switch (value) {
                case "episodes":

                    List<Episode> episodes = readEpisodesArray(reader);

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


        return values;
    }

    private static List<Episode> readEpisodesArray(JsonReader reader) throws IOException {
        List<Episode> episodes = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            episodes.add(readEpisode(reader));
        }
        reader.endArray();

        return episodes;
    }

    private static Episode readEpisode(JsonReader reader) throws IOException {
        int id = 0, season = 0, episode = 0;
        String title = "", show = "", date = "";
        boolean seen = false;

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
                case "season":
                    season = Integer.parseInt(reader.nextString());
                    break;
                case "episode":
                    episode = Integer.parseInt(reader.nextString());
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
                case "date":
                    date = reader.nextString();
                    break;
                case "user":

                    reader.beginObject();
                    while (reader.hasNext()) {

                        String nameBis = reader.nextName();
                        if (nameBis.equals("seen")) {
                            seen = reader.nextBoolean();
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();

                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        return new Episode(id, title, season, episode, seen, /*show,*/ 0, date);
    }

}
