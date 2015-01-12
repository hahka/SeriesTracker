package fr.hahka.seriestracker.shows;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.hahka.seriestracker.R;
import fr.hahka.seriestracker.episodes.EpisodesActivity;
import fr.hahka.seriestracker.utilitaires.DownloadImageTask;

/**
 * Created by thibautvirolle on 07/12/14.
 * Adapter pour la liste des shows
 */
public class ShowsAdapter extends BaseAdapter {

    private static String TAG = ShowsAdapter.class.getSimpleName();
    private ArrayList<Show> showsList = new ArrayList<>();
    private Context context;
    private String token;

    public ShowsAdapter(Context context, ArrayList<Show> liste, String token) {
        this.context = context;
        this.showsList = liste;
        this.token = token;
    }


    @Override
    public int getCount() {
        return showsList.size();
    }

    @Override
    public Show getItem(int position) {
        return showsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Show show = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_row, viewGroup, false);
        }

        TextView titletv = (TextView) view.findViewById(R.id.showTitleTextView);
        titletv.setText(show.getTitle());

        ImageView showImageView = (ImageView) view.findViewById(R.id.showImageView);

        new DownloadImageTask(showImageView)
                .execute("http://cdn.betaseries.com//betaseries//images//fonds//original//1275_1362361611.jpg");

        Log.d(TAG,"Image downloaded");



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
                userShowEpisodesIntent.putExtra("showId", showId);
                userShowEpisodesIntent.putExtra("token", token);
                context.startActivity(userShowEpisodesIntent);

            }
        });


        return view;
    }
}

