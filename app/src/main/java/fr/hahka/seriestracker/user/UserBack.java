package fr.hahka.seriestracker.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import fr.hahka.seriestracker.simpleshow.SimpleShow;

/**
 * Created by thibautvirolle on 04/01/15.
 * Objet User (pour le profil)
 */
public class UserBack implements Parcelable {

    public static final Creator<UserBack> CREATOR = new Creator<UserBack>() {
        @Override
        public UserBack createFromParcel(Parcel source) {
            return new UserBack(source);
        }

        @Override
        public UserBack[] newArray(int size) {
            return new UserBack[size];
        }
    };
    private String login;
    private int id;
    private int friends;
    private int badges;
    private int xp;
    private String avatar;
    private int seasons;
    private int episodes;
    private int comments;
    private float progress;
    private int episodesToWatch;
    private int timeOnTv;
    private int timeToSpend;
    private ArrayList<SimpleShow> showsList;

    public UserBack(String login, int id, int friends, int badges,
                    int seasons, int episodes, int comments, float progress,
                    int episodesToWatch, int timeOnTv, int timeToSpend, int xp, String avatar) {
        setLogin(login);
        setId(id);
        setFriends(friends);
        setBadges(badges);
        setSeasons(seasons);
        setEpisodes(episodes);
        setComments(comments);
        setProgress(progress);
        setEpisodesToWatch(episodesToWatch);
        setTimeOnTv(timeOnTv);
        setTimeToSpend(timeToSpend);
        setAvatar(avatar);
        setXp(xp);
    }

    public UserBack(Parcel in) {
        this.login = in.readString();
        this.id = in.readInt();
        this.friends = in.readInt();
        this.badges = in.readInt();
        this.xp = in.readInt();
        this.avatar = in.readString();

        this.seasons = in.readInt();
        this.episodes = in.readInt();
        this.comments = in.readInt();
        this.progress = in.readFloat();

        this.episodesToWatch = in.readInt();
        this.timeOnTv = in.readInt();
        this.timeToSpend = in.readInt();

        //this.showsList = in.readArrayList(Show.class.getClassLoader());
        this.showsList = in.readArrayList(SimpleShow.class.getClassLoader());
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFriends() {
        return friends;
    }

    public void setFriends(int friends) {
        this.friends = friends;
    }

    public int getBadges() {
        return badges;
    }

    public void setBadges(int badges) {
        this.badges = badges;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getSeasons() {
        return seasons;
    }

    public void setSeasons(int seasons) {
        this.seasons = seasons;
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public int getEpisodesToWatch() {
        return episodesToWatch;
    }

    public void setEpisodesToWatch(int episodesToWatch) {
        this.episodesToWatch = episodesToWatch;
    }

    public int getTimeOnTv() {
        return timeOnTv;
    }

    public void setTimeOnTv(int timeOnTv) {
        this.timeOnTv = timeOnTv;
    }

    public int getTimeToSpend() {
        return timeToSpend;
    }

    public void setTimeToSpend(int timeToSpend) {
        this.timeToSpend = timeToSpend;
    }

    public ArrayList<SimpleShow> getShowsList() {
        return showsList;
    }

    public void setShowsList(ArrayList<SimpleShow> showsList) {
        this.showsList = showsList;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeString(login);
        parcel.writeInt(id);
        parcel.writeInt(friends);
        parcel.writeInt(badges);
        parcel.writeInt(xp);
        parcel.writeString(avatar);

        parcel.writeInt(seasons);
        parcel.writeInt(episodes);
        parcel.writeInt(comments);
        parcel.writeFloat(progress);

        parcel.writeInt(episodesToWatch);
        parcel.writeInt(timeOnTv);
        parcel.writeInt(timeToSpend);

        parcel.writeList(showsList);
    }

}
