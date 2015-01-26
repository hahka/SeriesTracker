package fr.hahka.seriestracker.shows;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import fr.hahka.seriestracker.R;
import fr.hahka.seriestracker.utilitaires.images.BitmapTasks;

import static fr.hahka.seriestracker.utilitaires.images.BitmapTasks.loadBitmap;

/**
 * Created by thibautvirolle on 24/01/15.
 * Adapter pour la liste des séries avec bannières (SimpleShow)
 */
public class SimpleShowAdapter extends RecyclerView.Adapter<SimpleShowAdapter.ShowViewHolder>{

    private ArrayList<SimpleShow> simpleShowList;
    private Context context;
    private String token = "";

    public SimpleShowAdapter(Context context, ArrayList<SimpleShow> liste, String token) {
        this.simpleShowList = liste;
        this.context = context;
        this.token = token;

        BitmapTasks.setCache();

        Bitmap fond = BitmapFactory.decodeResource(context.getResources(), R.drawable.blackground);
        BitmapTasks.addBitmapToMemoryCache("blackground", fond);
    }

    @Override
    public int getItemCount() {
        return simpleShowList.size();
    }

    @Override
    public void onBindViewHolder(ShowViewHolder showViewHolder, int i) {
        SimpleShow simpleShow = simpleShowList.get(i);


        if(simpleShow.getUrl() != null) {

            loadBitmap(String.valueOf(simpleShow.getId()),simpleShow.getUrl(),showViewHolder.showImageView);

        } else {
            showViewHolder.titletv.setText(simpleShow.getTitle());
            showViewHolder.titletv.setVisibility(View.VISIBLE);

            loadBitmap("blackground",null,showViewHolder.showImageView);

        }


        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotationEcran = display.getRotation();

        if(rotationEcran == Surface.ROTATION_90 || rotationEcran == Surface.ROTATION_270) {

            showViewHolder.statusProgressBar.setProgress((int)simpleShow.getStatus());

            int remaining = simpleShow.getRemaining();
            float status = simpleShow.getStatus();
            if(remaining == 0) {
                if(status == 100)
                    showViewHolder.remainingTextView.setText("Série terminée");
                else
                    showViewHolder.remainingTextView.setText("Diffusion prochaine");
            }
            else if (remaining == 1) {
                showViewHolder.remainingTextView.setText("1 épisode restant");
            }
            else {
                showViewHolder.remainingTextView.setText(simpleShow.getRemaining() + " épisodes restants");
            }


        }


    }

    @Override
    public ShowViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.simple_show_row, viewGroup, false);


        /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView idtv = (TextView) view.findViewById(R.id.showIdTextView);
                String showId = idtv.getText().toString();

                Intent userShowEpisodesIntent;
                userShowEpisodesIntent = new Intent(context, EpisodesActivity.class);
                userShowEpisodesIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                userShowEpisodesIntent.putExtra(Config.SHOW_ID, showId);
                userShowEpisodesIntent.putExtra(Config.TOKEN, token);
                context.startActivity(userShowEpisodesIntent);

            }
        });*/

        return new ShowViewHolder(itemView, viewGroup.getContext());
    }

    public static class ShowViewHolder extends RecyclerView.ViewHolder {

        protected TextView titletv;
        protected ImageView showImageView;
        protected ProgressBar statusProgressBar;
        protected TextView remainingTextView;

        public ShowViewHolder(View v) {
            super(v);

            titletv = (TextView) v.findViewById(R.id.showTitleTextView);
            showImageView = (ImageView) v.findViewById(R.id.showImageView);


        }
        public ShowViewHolder(View v, final Context c) {
            super(v);

            titletv = (TextView) v.findViewById(R.id.showTitleTextView);
            showImageView = (ImageView) v.findViewById(R.id.showImageView);

            Display display = ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int rotationEcran = display.getRotation();
            // Et positionner ainsi le nombre de degrés de rotation
            if(rotationEcran == Surface.ROTATION_90 || rotationEcran == Surface.ROTATION_270) {

                statusProgressBar = (ProgressBar) v.findViewById(R.id.statusProgressBar);
                remainingTextView = (TextView) v.findViewById(R.id.remainingTextView);

            }

        }
    }

}
