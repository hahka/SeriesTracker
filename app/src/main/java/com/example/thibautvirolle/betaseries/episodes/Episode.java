package com.example.thibautvirolle.betaseries.episodes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thibautvirolle on 06/12/14.
 */
public class Episode implements Parcelable{

    private int id = 0;
    private String title = "";
    private int season = 0;
    private int episode = 0;
    private String code = "";
    private boolean seen = false;

    private String show = "";

    public Episode(int id, String title, int season, int episode,
                   String code, boolean seen)
    {
        setId(id);
        setTitle(title);
        setSeason(season);
        setEpisode(episode);
        setCode(code);
        setSeen(seen);
    }

    public Episode(int id, String title, int season, int episode,
                   String code, boolean seen, String show)
    {
        setId(id);
        setTitle(title);
        setSeason(season);
        setEpisode(episode);
        setCode(code);
        setSeen(seen);
        setShow(show);
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeInt(season);
        parcel.writeInt(episode);
        parcel.writeString(code);
        parcel.writeByte((byte) (seen ? 1 : 0));
        parcel.writeString(show);
    }


    public static final Parcelable.Creator<Episode> CREATOR = new Parcelable.Creator<Episode>()
    {
        @Override
        public Episode createFromParcel(Parcel source)
        {
            return new Episode(source);
        }

        @Override
        public Episode[] newArray(int size)
        {
            return new Episode[size];
        }
    };

    public Episode(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.season = in.readInt();
        this.episode = in.readInt();
        this.code = in.readString();
        this.seen = (in.readByte() != 0);
        this.show = in.readString();
    }

}
