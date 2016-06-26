package fr.hahka.seriestracker.episodes.episodes;

import java.util.Calendar;

/**
 * Created by thibautvirolle on 26/06/2016.
 */
public class EpisodeUtils {

    public static int getJour(Episode episode) {
        return Integer.parseInt(episode.getDate().split("-")[2]);
    }

    public static int getMois(Episode episode) {
        return Integer.parseInt(episode.getDate().split("-")[1]);
    }

    public static int getAnnee(Episode episode) {
        return Integer.parseInt(episode.getDate().split("-")[0]);
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
        return episode.getDate().split("-")[2] + "/" + episode.getDate().split("-")[1];
    }

    public static int getCurrentMonth() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.MONTH) + 1;
    }


}
