package fr.hahka.seriestracker.simpleshow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import fr.hahka.seriestracker.episodes.episodes.EpisodesActivity;
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.images.BitmapTasks;

/**
 * Created by thibautvirolle on 24/01/15.
 * Adapter pour la liste des séries avec bannières (SimpleShow)
 */
public class SimpleShowAdapterBackup extends RecyclerView.Adapter<SimpleShowAdapterBackup.ShowViewHolder>{

    private static final String TAG = SimpleShowAdapterBackup.class.getSimpleName();

    private ArrayList<SimpleShow> simpleShowList;
    private Context context;
    private String token = "";

    private int count = 0;

    public SimpleShowAdapterBackup(Context context, ArrayList<SimpleShow> liste, String token) {
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
    public ShowViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.simple_show_row, viewGroup, false);

        ImageView showImageView = (ImageView) itemView.findViewById(R.id.showImageView);

        /*if(showImageView != null && position != RecyclerView.NO_POSITION)
            loadImage(showImageView, position);*/

        //loadImage(showImageView, count);
        Log.d(TAG, "onCreate " + String.valueOf(count));
        count++;

        itemView.setOnClickListener(new View.OnClickListener() {
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
        });

        return new ShowViewHolder(itemView, viewGroup.getContext());
    }

    @Override
    public void onBindViewHolder(ShowViewHolder showViewHolder, int i) {

        SimpleShow simpleShow = simpleShowList.get(i);

        loadImage(showViewHolder.showImageView, i);

        Log.d(TAG, "onBind " + String.valueOf(i));


        String url = simpleShow.getUrl();

        if(url != null && !url.equals("")) {
            showViewHolder.showIdTextView.setText(String.valueOf(simpleShow.getId()));
        } else {
            showViewHolder.titletv.setText(simpleShow.getTitle());
            showViewHolder.titletv.setVisibility(View.VISIBLE);
            showViewHolder.showIdTextView.setText(String.valueOf(simpleShow.getId()));
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


    private void loadImage(ImageView imageView, int i) {
        SimpleShow simpleShow = simpleShowList.get(i);

        String url = simpleShow.getUrl();

        if(url != null && !url.equals("")) {

            BitmapTasks task = new BitmapTasks(imageView);
            task.execute(String.valueOf(simpleShow.getId()), url);

        } else {

            BitmapTasks task = new BitmapTasks(imageView);
            task.execute(null, null);

        }
    }

    public static class ShowViewHolder extends RecyclerView.ViewHolder {

        protected TextView titletv;
        protected ImageView showImageView;
        protected ProgressBar statusProgressBar;
        protected TextView remainingTextView;
        protected TextView showIdTextView;

        public ShowViewHolder(View v) {
            super(v);

            titletv = (TextView) v.findViewById(R.id.showTitleTextView);
            showImageView = (ImageView) v.findViewById(R.id.showImageView);

            Log.d(TAG, String.valueOf(v.getId()));


        }
        public ShowViewHolder(View v, final Context c) {
            super(v);

            titletv = (TextView) v.findViewById(R.id.showTitleTextView);
            showImageView = (ImageView) v.findViewById(R.id.showImageView);

            showIdTextView = (TextView) v.findViewById(R.id.showIdTextView);

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
