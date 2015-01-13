package fr.hahka.seriestracker.episodes;

/**
 * Created by thibautvirolle on 13/01/15.
 */
public abstract class AbstractEpisode {


    protected int id = 0;
    protected String title = "";

    int getId(){ return id; }
    String getTitle(){ return title; }

    void setId(int id){ this.id = id; }
    void setTitle(String title){ this.title = title; }

}
