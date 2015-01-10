package fr.hahka.seriestracker.episodes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by thibautvirolle on 06/12/14.
 * Objet Episode
 */
public class Episode implements Parcelable {

    private static String TAG = Episode.class.getSimpleName();

    public static final Parcelable.Creator<Episode> CREATOR = new Parcelable.Creator<Episode>() {
        @Override
        public Episode createFromParcel(Parcel source) {
            return new Episode(source);
        }

        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };
    private int id = 0;
    private String title = "";
    private int season = 0;
    private int episode = 0;
    private String code = "";
    private boolean seen = false;
    private String show = "";
    private String date = "";

    public Episode(int id, String title, int season, int episode,
                   String code, boolean seen, String show) {
        setId(id);
        setTitle(title);
        setSeason(season);
        setEpisode(episode);
        setCode(code);
        setSeen(seen);
        setShow(show);
    }

    public Episode(int id, String title, int season, int episode,
                   String code, boolean seen, String show, String date) {
        setId(id);
        setTitle(title);
        setSeason(season);
        setEpisode(episode);
        setCode(code);
        setSeen(seen);
        setShow(show);
        setDate(date);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getJour() {
        return Integer.parseInt(getDate().split("-")[2]);
    }

    public int getMois() {
        return Integer.parseInt(getDate().split("-")[1]);
    }

    public int getAnnee() {
        return Integer.parseInt(getDate().split("-")[0]);
    }

    public long getNbJourAvantDiffusion(){

        Calendar thatDay = Calendar.getInstance();
        thatDay.set(Calendar.DAY_OF_MONTH,getJour());
        thatDay.set(Calendar.MONTH,getMois() - 1); // 0-11 so 1 less
        thatDay.set(Calendar.YEAR, getAnnee());

        Calendar today = Calendar.getInstance();

        System.out.println("ThatDayOfWeekInMonth : "+thatDay.get(Calendar.DAY_OF_WEEK_IN_MONTH));


        long diff = thatDay.getTimeInMillis() - today.getTimeInMillis();
        return (diff / (24 * 60 * 60 * 1000));
    }

    public int getDayOfWeek() {
        Calendar thatDay = Calendar.getInstance();
        thatDay.set(Calendar.DAY_OF_MONTH,getJour());
        thatDay.set(Calendar.MONTH,getMois() - 1); // 0-11 so 1 less
        thatDay.set(Calendar.YEAR, getAnnee());
        int dayOfWeek = thatDay.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        dayOfWeek -= 1;
        if(dayOfWeek == 0)
            dayOfWeek = 7;

        return dayOfWeek;
    }


    public int getCurrentDayOfWeek() {
        Calendar today = Calendar.getInstance();
        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        dayOfWeek -= 1;
        if(dayOfWeek == 0)
            dayOfWeek = 7;

        return dayOfWeek;
    }

    public int getCurrentMonth() {
        Calendar today = Calendar.getInstance();
        int month = today.get(Calendar.MONTH) + 1;

        return month;
    }


    public String getDateShortString() {
        return getDate().split("-")[2] + "/" + getDate().split("-")[1];
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
    public String getHeader() {
        String header = "CETTE SEMAINE";

        int dayToday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH);

        long diff = getNbJourAvantDiffusion();
        long indice = diff + getCurrentDayOfWeek();

        if(indice < 1) {
            header = "LA SEMAINE DERNIÈRE";
        } else if(indice <=7) {
            header = "CETTE SEMAINE";
        } else if(indice <= 14) {
            header = "LA SEMAINE PROCHAINE";
        } else if(indice <= 21) {
            header = "DANS 2 SEMAINES";
        } else if(indice <= 28) {
            header = "DANS 3 SEMAINES";
        } else {
            int currentMonth = getCurrentMonth();
            int episodeMonth = getMois();

            if(currentMonth == episodeMonth) {
                header = "DANS 4 SEMAINES";
            } else if(currentMonth == episodeMonth -1) {
                header = "LE MOIS PROCHAIN";
            } else if(currentMonth == episodeMonth -2) {
                header = "DANS 2 MOIS";
            } else if(currentMonth == episodeMonth -3) {
                header = "DANS 3 MOIS";
            } else {
                header = "PLUS TARD...";
            }

        }
        /*if(indice <= 60) {
            header = "LE MOIS PROCHAIN";
        } else {
            header = "PLUS TARD...";
        }*/


        return header;
    }

    /**
     * Ce qui suit sert à passer des objets Episode entre activité par les intent.
     */

    // Permet de recréer l'épisode
    public Episode(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.season = in.readInt();
        this.episode = in.readInt();
        this.code = in.readString();
        this.seen = (in.readByte() != 0);
        this.show = in.readString();
        this.date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Sauvegarde l'épisode dans la parcelle
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeInt(season);
        parcel.writeInt(episode);
        parcel.writeString(code);
        parcel.writeByte((byte) (seen ? 1 : 0));
        parcel.writeString(show);
        parcel.writeString(date);
    }

}
