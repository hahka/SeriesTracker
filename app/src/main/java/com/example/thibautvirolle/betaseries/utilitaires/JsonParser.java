package com.example.thibautvirolle.betaseries.utilitaires;

import android.util.JsonReader;
import android.util.Log;

import com.example.thibautvirolle.betaseries.episodes.Episode;
import com.example.thibautvirolle.betaseries.shows.Show;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thibautvirolle on 10/11/14.
 */
public class JsonParser {

    private static final String TAG = JsonParser.class.getSimpleName();

    public static ArrayList readUserShowsJsonStream(InputStream in) throws IOException {

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        ArrayList values = new ArrayList();
        try {
            reader.beginObject();
            while (reader.hasNext() && (reader.peek().toString().equals("NAME"))) {
                String value = reader.nextName();
                if (value.equals("member")) {
                    List<Show> shows = readShowsArray(reader);
                    for (Show show : shows) {
                        values.add(show);
                    }
                } else if (value.equals("error")) {
                    reader.skipValue();
                } else
                    reader.skipValue();

            }
            reader.endObject();
        } finally {
            reader.close();
        }

        return values;
    }


    public static ArrayList readShowEpisodesJsonStream(InputStream in) throws IOException {

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        ArrayList values = new ArrayList();
        try {
            reader.beginObject();
            while (reader.hasNext() && (reader.peek().toString().equals("NAME"))) {
                String value = reader.nextName();
                if (value.equals("episodes")) {

                    List<Episode> episodes = readEpisodesArray(reader);

                    for (Episode episode : episodes) {
                        values.add(episode);
                    }
                } else if (value.equals("error")) {
                    reader.skipValue();
                } else
                    reader.skipValue();

            }
            reader.endObject();
        } finally {
            reader.close();
        }

        return values;
    }

    private static List<Episode> readEpisodesArray(JsonReader reader) throws IOException {
        List<Episode> episodes = new ArrayList<Episode>();

        reader.beginArray();
        while(reader.hasNext()) {
            episodes.add(readEpisode(reader));
        }
        reader.endArray();

        return episodes;
    }

    private static Episode readEpisode(JsonReader reader) throws IOException {
        int id = 0;
        String title = "";
        int season = 0;
        int episode = 0;
        String code = "";
        boolean seen = false;

        reader.beginObject();

        while (reader.hasNext()) {

            //Log.d(TAG, reader.peek().toString());

            String name = reader.nextName();
            switch (name) {
                case "id":
                    //Log.d(TAG,name);
                    id = reader.nextInt();
                    break;
                case "title":
                    //Log.d(TAG,name);
                    title = reader.nextString();
                    break;
                case "season":
                    //Log.d(TAG,name);
                    season = Integer.parseInt(reader.nextString());
                    break;
                case "episode":
                    //Log.d(TAG,name);
                    episode = Integer.parseInt(reader.nextString());
                    break;
                case "code":
                    //Log.d(TAG,name);
                    code = reader.nextString();
                    break;
                case "user":
                    //Log.d(TAG,name);

                    reader.beginObject();
                    while (reader.hasNext()) {

                        Log.d(TAG, reader.peek().toString());

                        String nameBis = reader.nextName();
                        if (nameBis.equals("seen")) {
                            Log.d(TAG, nameBis);
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

        Episode newEpisode = new Episode(id, title, season, episode, code, seen);

        return newEpisode;
    }


    private static List<Show> readShowsArray(JsonReader reader) throws IOException {

        List<Show> shows = new ArrayList<Show>();

        //while (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {

                String name = reader.nextName();

                if (name.equals("shows")) {
                    // On arrive dans le trajet
                    reader.beginArray();
                    while(reader.hasNext()) {
                        shows.add(readShow(reader));
                    }
                    reader.endArray();

                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        //}
        return shows;
    }

    private static Show readShow(JsonReader reader) throws IOException {

        int id = 0, seasons = 0, episodes = 0;
        String title = "";
        boolean favorited = false, archived = false;

        reader.beginObject();

        while (reader.hasNext()) {

            //Log.d(TAG, reader.peek().toString());

            String name = reader.nextName();
            if (name.equals("id")) {
                //Log.d(TAG,name);
                id = reader.nextInt();
            } else if (name.equals("title")) {
                //Log.d(TAG,name);
                title = reader.nextString();
            } else if (name.equals("seasons")) {
                //Log.d(TAG,name);
                seasons = Integer.parseInt(reader.nextString());
            }else if (name.equals("episodes")) {
                //Log.d(TAG,name);
                episodes = Integer.parseInt(reader.nextString());
            } else if (name.equals("user")) {
                //Log.d(TAG,name);

                reader.beginObject();
                while (reader.hasNext()) {

                    //Log.d(TAG, reader.peek().toString());

                    String nameBis = reader.nextName();
                    if (nameBis.equals("archived")) {
                        //Log.d(TAG,nameBis);
                        archived = reader.nextBoolean();
                    } else if (nameBis.equals("favorited")) {
                        //Log.d(TAG,nameBis);
                        favorited = reader.nextBoolean();
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();


            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        Show show = new Show(id, title, seasons, episodes, archived, favorited);

        return show;
    }





}
