package fr.hahka.seriestracker.planning;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fr.hahka.seriestracker.R;
import fr.hahka.seriestracker.episodes.Episode;

/**
 * Created by thibautvirolle on 07/12/14.
 */
public class PlanningAdapter extends BaseAdapter {

    private static String TAG = PlanningAdapter.class.getSimpleName();
    ArrayList<Episode> episodesList = new ArrayList<>();

    private String header;
    private boolean inflate = true;

    public PlanningAdapter(ArrayList<Episode> liste) {
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

        if(header != episode.getHeader()) {
            header = episode.getHeader();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.planning_row_with_header, viewGroup, false);
            inflate = true;

            TextView headerTextView = (TextView) view.findViewById(R.id.headerTextView);
            headerTextView.setText(episode.getHeader());

        } else {
            if(inflate) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.planning_row, viewGroup, false);
                inflate = false;
            } else if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.planning_row, viewGroup, false);
            }
        }


        TextView dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        dateTextView.setText(episode.getDateShortString());

        TextView showTitleTextView = (TextView) view.findViewById(R.id.showTitleTextView);
        showTitleTextView.setText(episode.getShow());

        TextView episodeDetailsTextView = (TextView) view.findViewById(R.id.episodeDetailsTextView);
        episodeDetailsTextView.setText(episode.getCode() + " - " + episode.getTitle());

        /*TextView detailstv = (TextView) view.findViewById(R.id.episodeDetailsTextView);
        detailstv.setText(episode.getCode() + " - " + episode.getTitle());

        TextView titletv = (TextView) view.findViewById(R.id.showTitleTextView);
        titletv.setText(episode.getShow());

        TextView idtv = (TextView) view.findViewById(R.id.episodeIdTextView);
        idtv.setText(String.valueOf(episode.getId()));


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView idtv = (TextView) view.findViewById(R.id.episodeIdTextView);
                int showId = Integer.parseInt(idtv.getText().toString());
                Log.d(TAG, String.valueOf(showId));
            }
        });

        */

        return view;
    }
}


