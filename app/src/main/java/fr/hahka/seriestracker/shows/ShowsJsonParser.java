package fr.hahka.seriestracker.shows;

import android.util.JsonReader;

import java.io.IOException;

/**
 * Created by thibautvirolle on 16/01/15.
 * Parser Json pour les Shows
 */
public class ShowsJsonParser {

    /*public static Show readShow(JsonReader reader) throws IOException {

        int id = 0, seasons = 0, episodes = 0;
        String title = "";
        boolean favorited = false, archived = false;

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
                case "seasons":
                    seasons = Integer.parseInt(reader.nextString());
                    break;
                case "episodes":
                    episodes = Integer.parseInt(reader.nextString());
                    break;
                case "user":

                    reader.beginObject();
                    while (reader.hasNext()) {

                        String nameBis = reader.nextName();
                        switch (nameBis) {
                            case "archived":
                                archived = reader.nextBoolean();
                                break;
                            case "favorited":
                                favorited = reader.nextBoolean();
                                break;
                            default:
                                reader.skipValue();
                                break;
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

        return new Show(id, title, seasons, episodes, archived, favorited);
    }*/



    public static SimpleShow readSimpleShow(JsonReader reader) throws IOException {

        int id = 0, thetvdb_id = 0, remaining = 0;
        float status = 0;
        String title = "";

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
                case "thetvdb_id":
                    thetvdb_id = reader.nextInt();
                    break;
                case "user":

                    reader.beginObject();
                    while (reader.hasNext()) {

                        String nameBis = reader.nextName();
                        switch (nameBis) {
                            case "status":
                                status = Float.parseFloat(reader.nextString());
                                break;
                            case "remaining":
                                remaining = Integer.parseInt(reader.nextString());
                                break;
                            default:
                                reader.skipValue();
                                break;
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

        //return new SimpleShow(id, thetvdb_id, title);
        return new SimpleShow(id, thetvdb_id, title, status, remaining);
    }

    /*public static String readShowsPicturesJsonStream(InputStream in) throws IOException {

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        String url = "";

        reader.beginObject();

        while (reader.hasNext()) {

            String name = reader.nextName();
            switch (name) {
                case "pictures":

                    reader.beginArray();
                    while (reader.hasNext()) {
                        reader.beginObject();

                        String urlTemp = "", picked = "";

                        while (reader.hasNext()) {

                            String nameBis = reader.nextName();
                            switch (nameBis) {
                                case "picked":
                                    picked = reader.nextString();
                                    break;
                                case "url":
                                    urlTemp = reader.nextString();
                                    break;
                                default:
                                    reader.skipValue();
                                    break;
                            }

                        }

                        if(picked.equals("show"))
                            url = urlTemp;

                        reader.endObject();
                    }
                    reader.endArray();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        reader.close();

        return url;
    }*/

    /*public static String readShowsPicturesXMLStream(InputStream in) throws IOException {

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        String url = "";

        reader.beginObject();

        while (reader.hasNext()) {

            String name = reader.nextName();
            switch (name) {
                case "pictures":

                    reader.beginArray();
                    while (reader.hasNext()) {
                        reader.beginObject();

                        String urlTemp = "", picked = "";

                        while (reader.hasNext()) {

                            String nameBis = reader.nextName();
                            switch (nameBis) {
                                case "picked":
                                    picked = reader.nextString();
                                    break;
                                case "url":
                                    urlTemp = reader.nextString();
                                    break;
                                default:
                                    reader.skipValue();
                                    break;
                            }

                        }

                        if(picked.equals("show"))
                            url = urlTemp;

                        reader.endObject();
                    }
                    reader.endArray();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        reader.close();

        return url;
    }*/


}
