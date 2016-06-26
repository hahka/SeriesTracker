package fr.hahka.seriestracker.episodes.episodes;

import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by thibautvirolle on 23/06/2016.
 */
public class EpisodeDialogUtils {


    public static void showEpisodeDialog(final EpisodesActivity activity, int episodeId) {

        CharSequence actions[] = new CharSequence[] {"J'ai vu cet épisode", "Accéder aux options Android", "Redémarrer l'application"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Que désirez-vous faire?");

        final AlertDialog optionsDialog = builder.create();


        builder.setItems(actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]

                switch (which) {

                    case 0:
                        optionsDialog.dismiss();
                        break;

                    case 1:
                        optionsDialog.dismiss();
                        break;

                    case 2:
                        optionsDialog.dismiss();
                        break;

                    default:
                        break;

                }
            }
        });
        builder.show();

    }
}
