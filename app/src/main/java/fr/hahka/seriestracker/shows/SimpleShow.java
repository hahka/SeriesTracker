package fr.hahka.seriestracker.shows;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thibautvirolle on 02/12/14.
 * Classe pour les Shows affichés dans la liste
 */
public class SimpleShow implements Parcelable {

    public static final Parcelable.Creator<SimpleShow> CREATOR = new Parcelable.Creator<SimpleShow>() {
        @Override
        public SimpleShow createFromParcel(Parcel source) {
            return new SimpleShow(source);
        }

        @Override
        public SimpleShow[] newArray(int size) {
            return new SimpleShow[size];
        }

    };
    private int id;
    private int thetvdbId;
    private String title;
    private String url;
    private float status = 0;
    private int remaining = 0;

    public SimpleShow(int id, String title) {
        setId(id);
        setTitle(title);
    }

    public SimpleShow(int id, int thetvdbId, String title) {
        setId(id);
        setThetvdbId(thetvdbId);
        setTitle(title);
    }

    public SimpleShow(int id, int thetvdbId, String title, float status, int remaining) {
        setId(id);
        setThetvdbId(thetvdbId);
        setTitle(title);
        setStatus(status);
        setRemaining(remaining);
    }

    // Permet de recréer le show
    public SimpleShow(Parcel in) {
        this.id = in.readInt();
        this.thetvdbId = in.readInt();
        this.title = in.readString();
        this.url = in.readString();
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
        parcel.writeInt(thetvdbId);
        parcel.writeString(title);
        parcel.writeString(url);
    }
}
