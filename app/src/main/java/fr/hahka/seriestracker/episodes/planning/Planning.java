package fr.hahka.seriestracker.episodes.planning;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

import fr.hahka.seriestracker.episodes.AbstractEpisode;

/**
 * Created by thibautvirolle on 13/01/15.
 * Classe Planning : Episode plus header pour affichage
 */
public class Planning extends AbstractEpisode implements Parcelable{

    public static final Parcelable.Creator<Planning> CREATOR = new Parcelable.Creator<Planning>() {
        @Override
        public Planning createFromParcel(Parcel source) {
            return new Planning(source);
        }

        @Override
        public Planning[] newArray(int size) {
            return new Planning[size];
        }
    };
    private String header = null;
    private String show = "";
    private String date = "";
    private String code = "";

    public Planning(int id, String title, String show, String code, String date) {

        setId(id);
        setTitle(title);
        setShow(show);
        setCode(code);
        setDate(date);

    }


    public Planning(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.show = in.readString();
        this.date = in.readString();
        this.code = in.readString();
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(show);
        dest.writeString(date);
        dest.writeString(code);
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

        long diff = thatDay.getTimeInMillis() - today.getTimeInMillis();
        return (diff / (24 * 60 * 60 * 1000));
    }


    public String getDateShortString() {
        return getDate().split("-")[2] + "/" + getDate().split("-")[1];
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


    public long getIndice(){
        return getNbJourAvantDiffusion() + getCurrentDayOfWeek();
    }

    public int getHeaderIndice() {

        long indice = getIndice();

        if(indice < 1) {
            return 0;
        } else if(indice <7) {
            return 1;
        } else if(indice < 14) {
            return 2;
        } else if(indice < 21) {
            return 3;
        } else if(indice < 28) {
            return 4;
        } else {
            int currentMonth = getCurrentMonth();
            int episodeMonth = getMois();

            if(currentMonth == episodeMonth) {
                return 5;
            } else if(currentMonth == episodeMonth -1) {
                return 6;
            } else if(currentMonth == episodeMonth -2) {
                return 7;
            } else if(currentMonth == episodeMonth -3) {
                return 8;
            } else {
                return 9;
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
