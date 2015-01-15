package fr.hahka.seriestracker.shows;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thibautvirolle on 02/12/14.
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
    private String title;
    private String url;

    public SimpleShow(int id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }

    // Permet de recréer le show
    public SimpleShow(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.url = in.readString();
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        parcel.writeString(url);
    }

}