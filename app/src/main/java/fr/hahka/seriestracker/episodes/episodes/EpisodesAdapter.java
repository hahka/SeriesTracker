package fr.hahka.seriestracker.episodes.episodes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import fr.hahka.seriestracker.R;

/**
 * Created by thibautvirolle on 07/12/14.
 * Adapter pour afficher la listView des épisodes
 */
public class EpisodesAdapter extends BaseAdapter {

    private static String TAG = EpisodesAdapter.class.getSimpleName();
    ArrayList<Episode> episodesList = new ArrayList<>();

    public EpisodesAdapter(ArrayList<Episode> liste) {
        this.episodesList = liste;
    }

    @Override
    public int getCount() {
        return episodesList.size();
    }

    @Override
    public Episode getItem(int position) {
        return episodesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Episode episode = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.episode_row, viewGroup, false);
        }

        TextView codetv = (TextView) view.findViewById(R.id.episodeCodeTextView);
        codetv.setText(String.valueOf(episode.getEpisode()));

        TextView titletv = (TextView) view.findViewById(R.id.episodeTitleTextView);
        titletv.setText(episode.getTitle());

        TextView idtv = (TextView) view.findViewById(R.id.episodeIdTextView);
        idtv.setText(String.valueOf(episode.getId()));


        ImageButton isSeenImageButton = (ImageButton) view.findViewById(R.id.isSeenImageButton);

        if (!episode.isSeen()) {
            isSeenImageButton.setBackgroundResource(R.drawable.ic_check_box_outline_blank_black);
        } else {
            isSeenImageButton.setBackgroundResource(R.drawable.ic_check_box_black);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView idtv = (TextView) view.findViewById(R.id.episodeIdTextView);
                int showId = Integer.parseInt(idtv.getText().toString());
                Log.d(TAG, String.valueOf(showId));
            }
        });

        return view;
    }
}


