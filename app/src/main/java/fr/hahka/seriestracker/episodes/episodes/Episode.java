package fr.hahka.seriestracker.episodes.episodes;

import java.util.Calendar;

import io.realm.RealmObject;

/**
 * Created by thibautvirolle on 06/12/14.
 * Objet Episode
 */
public class Episode extends RealmObject {

    private int id = 0;
    private String title = "";
    private int season = 0;
    private int episode = 0;
    private boolean seen = false;
    private int showId = 0;
    private String date = "";

    private String header = "";
    private String show = "";
    private String code = "";

    public Episode() {

    }

    public Episode(int id, String title, int season, int episode, boolean seen, int showId, String date) {
        setId(id);
        setTitle(title);
        setSeason(season);
        setEpisode(episode);
        setSeen(seen);
        setShowId(showId);
        setDate(date);
    }

    public static int getCurrentDayOfWeek() {
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        dayOfWeek -= 1;
        if(dayOfWeek == 0)
            dayOfWeek = 7;

        return dayOfWeek;
    }

    public int getId(){ return id; }

    public void setId(int id){ this.id = id; }

    public String getTitle(){ return title; }

    public void setTitle(String title){ this.title = title; }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }



    public long getIndice(){
        return EpisodeUtils.getNbJourAvantDiffusion(this) + getCurrentDayOfWeek();
    }

    public int getHeaderIndice() {

        long indice = getIndice();


        if(indice < -7) {
            return 0;
        } else if(indice < 0) {
            return 1;
        } else if(indice < 7) {
            return 2;
        } else if(indice < 14) {
            return 3;
        } else if(indice < 21) {
            return 4;
        } else if(indice < 28) {
            return 5;
        } else {
            int currentMonth = EpisodeUtils.getCurrentMonth();
            int episodeMonth = EpisodeUtils.getMois(this);

            if(currentMonth == episodeMonth) {
                return 6;
            } else if(currentMonth == episodeMonth -1) {
                return 7;
            } else if(currentMonth == episodeMonth -2) {
                return 8;
            } else if(currentMonth == episodeMonth -3) {
                return 9;
            } else {
                return 10;
            }

        }

    }


    /*
     *  Dimanche = 1
     *  ...
     *  Samedi = 7
     *
     *  diff + jour
     *  currentDay + diff
     *
     */
    /*public String getHeader() {

        long indice = getIndice();

        if(indice < 1) {
            return "LA SEMAINE DERNIÈRE";
        } else if(indice <=7) {
            return "CETTE SEMAINE";
        } else if(indice <= 14) {
            return "LA SEMAINE PROCHAINE";
        } else if(indice <= 21) {
            return "DANS 2 SEMAINES";
        } else if(indice <= 28) {
            return "DANS 3 SEMAINES";
        } else {
            int currentMonth = getCurrentMonth();
            int episodeMonth = getMois();

            if(currentMonth == episodeMonth) {
                return "DANS 4 SEMAINES";
            } else if(currentMonth == episodeMonth -1) {
                return "LE MOIS PROCHAIN";
            } else if(currentMonth == episodeMonth -2) {
                return "DANS 2 MOIS";
            } else if(currentMonth == episodeMonth -3) {
                return "DANS 3 MOIS";
            } else {
                return "PLUS TARD...";
            }

        }

    }*/





}
