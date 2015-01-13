package fr.hahka.seriestracker.shows;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thibautvirolle on 02/12/14.
 */
public class Show implements Parcelable {

    public static final Parcelable.Creator<Show> CREATOR = new Parcelable.Creator<Show>() {
        @Override
        public Show createFromParcel(Parcel source) {
            return new Show(source);
        }

        @Override
        public Show[] newArray(int size) {
            return new Show[size];
        }

    };
    private int id;
    private String title;
    private int seasons;
    private int episodes;
    private boolean archived;
    private boolean favorited;

    public Show(int id, String title, int seasons, int episodes, boolean archived, boolean favorited) {
        this.id = id;
        this.title = title;
        this.seasons = seasons;
        this.episodes = episodes;
        this.archived = archived;
        this.favorited = favorited;
    }

    // Permet de recréer le show
    public Show(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.seasons = in.readInt();
        this.episodes = in.readInt();
        this.archived = (in.readByte() != 0);
        this.favorited = (in.readByte() != 0);
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

    public int getSeasons() {
        return seasons;
    }

    public void setSeasons(int seasons) {
        this.seasons = seasons;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public String getSeasonsEpisodesToString() {

        return getSeasons() + (getSeasons() > 1 ? " saisons, " : " saison, ") + getEpisodes() +
                (getEpisodes() > 1 ? " épisodes" : " épisode");

    }

    /**
     * Ce qui suit sert à passer des objets Show entre activité par les intent.
     */

    @Override
    public int describeContents() {
        return 0;
    }

    // Sauvegarde le show dans la parcelle
    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeInt(seasons);
        parcel.writeInt(episodes);
        parcel.writeByte((byte) (archived ? 1 : 0));
        parcel.writeByte((byte) (favorited ? 1 : 0));
    }

}
