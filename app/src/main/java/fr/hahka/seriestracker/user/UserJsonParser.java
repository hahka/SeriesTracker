package fr.hahka.seriestracker.user;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonToken;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import fr.hahka.seriestracker.simpleshow.SimpleShow;
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.RealmUtils;
import fr.hahka.seriestracker.utilitaires.XmlParser;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static fr.hahka.seriestracker.shows.ShowsJsonParser.readSimpleShow;

/**
 * Created by thibautvirolle on 16/01/15.
 * Parser Json pour l'objet User
 */
public class UserJsonParser {

    public static User readUserJsonStream(Context c, InputStream in) throws IOException {

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


        RealmConfiguration config = new RealmConfiguration.Builder(c).build();
        Realm realm = Realm.getInstance(config);

        for(SimpleShow show : showsList){

            show.setUserId(id);
            URL urlImage = new URL("http://thetvdb.com/api/"+ Config.THETVDB_API_KEY+"/series/" + show.getThetvdbId());
            HttpURLConnection urlImageConnection = (HttpURLConnection) urlImage.openConnection();

            InputStream is2 = new BufferedInputStream(urlImageConnection.getInputStream());

            String urlBanner = null;
            try {
                urlBanner = XmlParser.readBannerUrlFromXml(is2);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            if(!urlBanner.equals(""))
                show.setUrl("http://thetvdb.com/banners/"+urlBanner);
            else
                show.setUrl("");

            is2.close();

            if(!RealmUtils.exists(c,SimpleShow.class,show.getId())){
                realm.beginTransaction();
                SimpleShow simpleShow = realm.createObject(SimpleShow.class); // Create a new object
                simpleShow.setId(show.getId());
                simpleShow.setThetvdbId(show.getThetvdbId());
                simpleShow.setTitle(show.getTitle());
                simpleShow.setStatus(show.getStatus());
                simpleShow.setRemaining(show.getRemaining());
                simpleShow.setUserId(show.getUserId());
                simpleShow.setUrl(show.getUrl());

                realm.commitTransaction();
            } else {
                realm.beginTransaction();
                //realm.clear(SimpleShow.class);
                realm.commitTransaction();
            }

        }

        /*User user = realm.createObject(User.class);

        user.setLogin(login);
        user.setId(id);
        user.setFriends(friends);
        user.setBadges(badges);
        user.setSeasons(seasons);
        user.setEpisodes(episodes);
        user.setComments(comments);
        user.setProgress(progress);
        user.setEpisodesToWatch(episodesToWatch);
        user.setTimeOnTv(timeOnTv);
        user.setTimeToSpend(timeToSpend);
        user.setXp(xp);
        user.setAvatar(avatar);*/

        User user = new User(login, id, friends, badges, seasons, episodes, comments, progress,
                episodesToWatch, timeOnTv, timeToSpend, xp, avatar);

        if(!RealmUtils.exists(c,User.class,user.getId())){
            realm.beginTransaction();
            realm.copyToRealm(user);
            realm.commitTransaction();
        } else {
            realm.beginTransaction();
            //realm.clear(SimpleShow.class);
            realm.commitTransaction();
        }

        return new User(login, id, friends, badges, seasons, episodes, comments, progress,
                episodesToWatch, timeOnTv, timeToSpend, xp, avatar);
    }


}
