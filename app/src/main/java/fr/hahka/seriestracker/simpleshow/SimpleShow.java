package fr.hahka.seriestracker.simpleshow;

import io.realm.RealmObject;

/**
 * Created by thibautvirolle on 02/12/14.
 * Classe pour les Shows affichés dans la liste
 */
public class SimpleShow extends RealmObject{

    private int id;
    private int userId;
    private int thetvdbId;
    private String title = "";
    private String url = "";
    private float status = 0;
    private int remaining = 0;

    public SimpleShow(){

    }
    /*public SimpleShow(int id, String title) {
        setId(id);
        setTitle(title);
    }

    public SimpleShow(int id, int thetvdbId, String title) {
        setId(id);
        setThetvdbId(thetvdbId);
        setTitle(title);
    }*/

    public SimpleShow(int id, int thetvdbId, String title, float status, int remaining) {
        setId(id);
        setThetvdbId(thetvdbId);
        setTitle(title);
        setStatus(status);
        setRemaining(remaining);
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getThetvdbId() {
        return thetvdbId;
    }

    public void setThetvdbId(int id) {
        this.thetvdbId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public float getStatus() {
        return status;
    }

    public void setStatus(float status) {
        this.status = status;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


}
