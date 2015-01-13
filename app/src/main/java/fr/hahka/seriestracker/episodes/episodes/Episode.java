package fr.hahka.seriestracker.episodes.episodes;

import android.os.Parcel;
import android.os.Parcelable;

import fr.hahka.seriestracker.episodes.AbstractEpisode;

/**
 * Created by thibautvirolle on 06/12/14.
 * Objet Episode
 */
public class Episode extends AbstractEpisode implements Parcelable {

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
    private static String TAG = Episode.class.getSimpleName();
    private int season = 0;
    private int episode = 0;
    private boolean seen = false;
    private String show = "";
    private String date = "";


    public Episode(int id, String title, int season, int episode, boolean seen, String show, String date) {
        setId(id);
        setTitle(title);
        setSeason(season);
        setEpisode(episode);
        setSeen(seen);
        setShow(show);
        setDate(date);
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
        this.seen = (in.readByte() != 0);
        this.show = in.readString();
        this.date = in.readString();
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
        parcel.writeByte((byte) (seen ? 1 : 0));
        parcel.writeString(show);
        parcel.writeString(date);
    }

}
