package com.example.thibautvirolle.betaseries.utilitaires;

import android.util.JsonReader;

import com.example.thibautvirolle.betaseries.episodes.Episode;
import com.example.thibautvirolle.betaseries.shows.Show;
import com.example.thibautvirolle.betaseries.user.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thibautvirolle on 10/11/14.
 * Classe pour parser les json récupérés grâce à l'API BetaSeries
 */
public class JsonParser {

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
        while (reader.hasNext()) {
            episodes.add(readEpisode(reader));
        }
        reader.endArray();

        return episodes;
    }

    private static Episode readEpisode(JsonReader reader) throws IOException {
        int id = 0, season = 0, episode = 0;
        String title = "", show = "", code = "";
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
                case "code":
                    code = reader.nextString();
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

        Episode newEpisode = new Episode(id, title, season, episode, code, seen, show);

        return newEpisode;
    }

    private static Show readShow(JsonReader reader) throws IOException {

        int id = 0, seasons = 0, episodes = 0;
        String title = "";
        boolean favorited = false, archived = false;

        reader.beginObject();

        while (reader.hasNext()) {

            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextInt();
            } else if (name.equals("title")) {
                title = reader.nextString();
            } else if (name.equals("seasons")) {
                seasons = Integer.parseInt(reader.nextString());
            } else if (name.equals("episodes")) {
                episodes = Integer.parseInt(reader.nextString());
            } else if (name.equals("user")) {

                reader.beginObject();
                while (reader.hasNext()) {

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


    public static User readUserJsonStream(InputStream in) throws IOException {

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        String login = "", avatar = "";
        int id = 0, friends = 0, badges = 0, xp = 0,
                seasons = 0, episodes = 0, comments = 0,
                episodesToWatch = 0, timeOnTv = 0, timeToSpend = 0;

        float progress = 0;

        ArrayList<Show> showsList = new ArrayList<>();

        try {
            reader.beginObject();
            while (reader.hasNext() && (reader.peek().toString().equals("NAME"))) {
                String value = reader.nextName();
                if (value.equals("member")) {

                    reader.beginObject();

                    while (reader.hasNext()) {

                        String nameBis = reader.nextName();
                        if (nameBis.equals("id")) {
                            id = reader.nextInt();
                        } else if (nameBis.equals("login")) {
                            login = reader.nextString();
                        } else if (nameBis.equals("xp")) {
                            xp = reader.nextInt();
                        } else if (nameBis.equals("avatar")) {
                            avatar = reader.nextString();
                        } else if (nameBis.equals("stats")) {

                            reader.beginObject();

                            while (reader.hasNext()) {
                                String stats = reader.nextName();
                                if (stats.equals("badges")) {
                                    badges = reader.nextInt();
                                } else if (stats.equals("seasons")) {
                                    seasons = reader.nextInt();
                                } else if (stats.equals("episodes")) {
                                    episodes = reader.nextInt();
                                } else if (stats.equals("comments")) {
                                    comments = reader.nextInt();
                                } else if (stats.equals("progress")) {
                                    progress = (float) reader.nextDouble();
                                } else if (stats.equals("episodes_to_watch")) {
                                    episodesToWatch = reader.nextInt();
                                } else if (stats.equals("time_on_tv")) {
                                    timeOnTv = reader.nextInt();
                                } else if (stats.equals("time_to_spend")) {
                                    timeToSpend = reader.nextInt();
                                } else {
                                    reader.skipValue();
                                }
                            }

                            reader.endObject();

                        } else if (nameBis.equals("shows")) {
                            // On arrive dans le trajet
                            reader.beginArray();
                            while (reader.hasNext()) {
                                showsList.add(readShow(reader));
                            }
                            reader.endArray();

                        } else {
                            reader.skipValue();
                        }
                    }

                    reader.endObject();

                } else if (value.equals("error")) {
                    reader.skipValue();
                } else
                    reader.skipValue();

            }
            reader.endObject();
        } finally {
            reader.close();
        }

        User user = new User(login, id, friends, badges, seasons, episodes, comments, progress,
                episodesToWatch, timeOnTv, timeToSpend, xp, avatar);
        user.setShowsList(showsList);

        return user;
    }


}
