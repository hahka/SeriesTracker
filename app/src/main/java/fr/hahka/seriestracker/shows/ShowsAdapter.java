package fr.hahka.seriestracker.shows;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import fr.hahka.seriestracker.R;
import fr.hahka.seriestracker.episodes.episodes.EpisodesActivity;
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.images.BitmapTasks;

import static fr.hahka.seriestracker.utilitaires.images.BitmapTasks.loadBitmap;

/**
 * Created by thibautvirolle on 07/12/14.
 * Adapter pour la liste des shows
 */
public class ShowsAdapter extends BaseAdapter {

    private static String TAG = ShowsAdapter.class.getSimpleName();
    private ArrayList<SimpleShow> showsList = new ArrayList<>();
    private Context context;
    private String token;


    public ShowsAdapter(Context context, ArrayList<SimpleShow> liste, String token) {
        this.context = context;
        this.showsList = liste;
        this.token = token;

        BitmapTasks.setCache();

        Bitmap fond = BitmapFactory.decodeResource(context.getResources(),R.drawable.blackground);
        Log.d(TAG,"fond : "+fond.getWidth()+"/"+fond.getHeight());
        BitmapTasks.addBitmapToMemoryCache("blackground", fond);


    }

    @Override
    public int getCount() {
        return showsList.size();
    }

    @Override
    public SimpleShow getItem(int position) {
        return showsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final SimpleShow show = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_show_row, viewGroup, false);
        }

        ImageView showImageView = (ImageView) view.findViewById(R.id.showImageView);

        if(show.getUrl() != null) {

            //new DownloadImageTask(showImageView).execute(show.getUrl());

            //showImageView.setVisibility(View.VISIBLE);
            loadBitmap(String.valueOf(show.getId()),show.getUrl(),showImageView);

        } else {
            TextView titletv = (TextView) view.findViewById(R.id.showTitleTextView);
            titletv.setText(show.getTitle());
            titletv.setVisibility(View.VISIBLE);

            //showImageView.setVisibility(View.GONE);
            loadBitmap("blackground",null,showImageView);

            //fillViewWithBitmap(showImageView,fond);
        }

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotationEcran = display.getRotation();
        // Et positionner ainsi le nombre de degrés de rotation
        if(rotationEcran == Surface.ROTATION_90 || rotationEcran == Surface.ROTATION_270) {

            ProgressBar statusProgressBar = (ProgressBar) view.findViewById(R.id.statusProgressBar);
            statusProgressBar.setProgress((int)show.getStatus());

            TextView remainingTextView = (TextView) view.findViewById(R.id.remainingTextView);
            int remaining = show.getRemaining();
            float status = show.getStatus();
            if(remaining == 0) {
                if(status == 100)
                    remainingTextView.setText("Série terminée");
                else
                    remainingTextView.setText("Diffusion prochaine");
            }
            else if (remaining == 1) {
                remainingTextView.setText("1 épisode restant");
            }
            else {
                remainingTextView.setText(show.getRemaining() + " épisodes restants");
            }


        }



        TextView idtv = (TextView) view.findViewById(R.id.showIdTextView);
        idtv.setText(String.valueOf(show.getId()));

        view.setOnClickListener(new View.OnClickListener() {
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


        return view;
    }


}

