package fr.hahka.seriestracker.episodes.planning;

import fr.hahka.seriestracker.episodes.episodes.Episode;
import fr.hahka.seriestracker.user.User;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by thibautvirolle on 13/01/15.
 * Classe PlanningBackup : Episode plus header pour affichage
 */
public class Planning extends RealmObject {

    @PrimaryKey
    private int id;

    private Episode episode;
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Episode getEpisode() {
        return episode;
    }

    public void setEpisode(Episode episode) {
        this.episode = episode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
