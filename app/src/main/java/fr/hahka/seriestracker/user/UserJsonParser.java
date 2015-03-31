package fr.hahka.seriestracker.user;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import fr.hahka.seriestracker.simpleshow.SimpleShow;

import static fr.hahka.seriestracker.shows.ShowsJsonParser.readSimpleShow;

/**
 * Created by thibautvirolle on 16/01/15.
 * Parser Json pour l'objet User
 */
public class UserJsonParser {

    public static User readUserJsonStream(InputStream in) throws IOException {

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        String login = "", avatar = "";
        int id = 0, friends = 0, badges = 0, xp = 0,
                seasons = 0, episodes = 0, comments = 0,
                episodesToWatch = 0, timeOnTv = 0, timeToSpend = 0;

        float progress = 0;

        ArrayList<SimpleShow> showsList = new ArrayList<>();

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
                                            case "shows":
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
                                        showsList.add(readSimpleShow(reader));
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
