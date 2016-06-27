package fr.hahka.seriestracker.episodes.episodes;

import java.util.Calendar;

/**
 * Created by thibautvirolle on 26/06/2016.
 */
public class EpisodeUtils {

    public static int getJourFromString(String date) {
        return Integer.parseInt(date.split("-")[2]);
    }

    public static int getMoisFromString(String date) {
        return Integer.parseInt(date.split("-")[1]);
    }

    public static int getAnneeFromString(String date) {
        return Integer.parseInt(date.split("-")[0]);
    }

    public static int getJour(Episode episode) {
        // TODO : deprecated
        return episode.getDate().getDate();
    }

    public static int getMois(Episode episode) {
        // TODO : deprecated
        return episode.getDate().getMonth() + 1;
    }

    public static int getAnnee(Episode episode) {
        // TODO : deprecated
        return episode.getDate().getYear() + 1900;
    }

    public static long getNbJourAvantDiffusion(Episode episode){

        Calendar thatDay = Calendar.getInstance();
        thatDay.set(Calendar.DAY_OF_MONTH,getJour(episode));
        thatDay.set(Calendar.MONTH,getMois(episode) - 1); // 0-11 so 1 less
        thatDay.set(Calendar.YEAR, getAnnee(episode));

        Calendar today = Calendar.getInstance();

        long diff = thatDay.getTimeInMillis() - today.getTimeInMillis();
        return (diff / (24 * 60 * 60 * 1000));
    }

    public static String getDateShortString(Episode episode) {
        return episode.getDate().getDate() + "/" + (episode.getDate().getMonth()+1);
    }

    public static int getCurrentMonth() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.MONTH) + 1;
    }

    public static int getCurrentYear() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.YEAR);
    }


    public static int getCurrentDayOfWeek() {
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        dayOfWeek -= 1;
        if(dayOfWeek == 0)
            dayOfWeek = 7;

        return dayOfWeek;
    }

}
