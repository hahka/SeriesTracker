package fr.hahka.seriestracker.episodes.planning;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thibautvirolle on 15/01/15.
 */
public class PlanningJsonParser {



    public static ArrayList<Planning> readUserPlanningJsonStream(InputStream in) throws IOException {

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        ArrayList<Planning> values = new ArrayList<>();

        reader.beginObject();
        while (reader.hasNext() && (reader.peek().toString().equals("NAME"))) {
            String value = reader.nextName();
            switch (value) {
                case "episodes":

                    List<Planning> episodes = readPlanningArray(reader);

                    for (Planning episode : episodes) {
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


    private static List<Planning> readPlanningArray(JsonReader reader) throws IOException {
        List<Planning> episodes = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            episodes.add(readPlanning(reader));
        }
        reader.endArray();

        return episodes;
    }


    private static Planning readPlanning(JsonReader reader) throws IOException {
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

        return new Planning(id, title, show, code, date);
    }

}
