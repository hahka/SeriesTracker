package fr.hahka.seriestracker.utilitaires;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import fr.hahka.seriestracker.shows.Show;
import fr.hahka.seriestracker.user.User;

/**
 * Created by thibautvirolle on 10/11/14.
 * Classe pour parser les json récupérés grâce à l'API BetaSeries
 */
public class JsonParser {

    private static Show readShow(JsonReader reader) throws IOException {

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
    }


    public static User readUserJsonStream(InputStream in) throws IOException {

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        String login = "", avatar = "";
        int id = 0, friends = 0, badges = 0, xp = 0,
                seasons = 0, episodes = 0, comments = 0,
                episodesToWatch = 0, timeOnTv = 0, timeToSpend = 0;

        float progress = 0;

        ArrayList<Show> showsList = new ArrayList<>();

        reader.beginObject();
        while (reader.hasNext() && (reader.peek().toString().equals("NAME"))) {
            String value = reader.nextName();
            switch (value) {
                case "member":

                    reader.beginObject();

                    while (reader.hasNext()) {

                        String nameBis = reader.nextName();

                        if (reader.peek() == JsonToken.NULL) {
                            reader.nextNull();
                        } else {

                            switch (nameBis) {
                                case "id":
                                    id = reader.nextInt();
                                    break;
                                case "login":
                                    login = reader.nextString();
                                    break;
                                case "xp":
                                    xp = reader.nextInt();
                                    break;
                                case "avatar":
                                    avatar = reader.nextString();
                                    break;
                                case "stats":

                                    reader.beginObject();

                                    while (reader.hasNext()) {
                                        String stats = reader.nextName();
                                        switch (stats) {
                                            case "badges":
                                                badges = reader.nextInt();
                                                break;
                                            case "seasons":
                                                seasons = reader.nextInt();
                                                break;
                                            case "episodes":
                                                episodes = reader.nextInt();
                                                break;
                                            case "comments":
                                                comments = reader.nextInt();
                                                break;
                                            case "progress":
                                                progress = (float) reader.nextDouble();
                                                break;
                                            case "episodes_to_watch":
                                                episodesToWatch = reader.nextInt();
                                                break;
                                            case "time_on_tv":
                                                timeOnTv = reader.nextInt();
                                                break;
                                            case "time_to_spend":
                                                timeToSpend = reader.nextInt();
                                                break;
                                            default:
                                                reader.skipValue();
                                                break;
                                        }
                                    }

                                    reader.endObject();

                                    break;
                                case "shows":
                                    // On arrive dans le trajet
                                    reader.beginArray();
                                    while (reader.hasNext()) {
                                        showsList.add(readShow(reader));
                                    }
                                    reader.endArray();

                                    break;
                                default:
                                    reader.skipValue();
                                    break;
                            }

                        }


                    }

                    reader.endObject();

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

        User user = new User(login, id, friends, badges, seasons, episodes, comments, progress,
                episodesToWatch, timeOnTv, timeToSpend, xp, avatar);
        user.setShowsList(showsList);

        return user;
    }


}
