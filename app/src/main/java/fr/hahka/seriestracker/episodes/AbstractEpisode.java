package fr.hahka.seriestracker.episodes;

/**
 * Created by thibautvirolle on 13/01/15.
 * Classe abstraites pour créer les épisodes et le planning
 */
public abstract class AbstractEpisode {


    protected int id = 0;
    protected String title = "";

    public int getId(){ return id; }

    public void setId(int id){ this.id = id; }

    public String getTitle(){ return title; }

    public void setTitle(String title){ this.title = title; }

}
