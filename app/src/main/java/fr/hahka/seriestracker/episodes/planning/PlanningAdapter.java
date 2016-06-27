package fr.hahka.seriestracker.episodes.planning;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fr.hahka.seriestracker.R;
import fr.hahka.seriestracker.episodes.episodes.Episode;
import fr.hahka.seriestracker.episodes.episodes.EpisodeUtils;

/**
 * Created by thibautvirolle on 07/12/14.
 * Adapter pour afficher la listView du planning
 */
public class PlanningAdapter extends RecyclerView.Adapter<PlanningAdapter.PlanningViewHolder> {

    ArrayList<Episode> episodesList = new ArrayList<>();
    ArrayList<String> headersList = new ArrayList<>();

    private int count = 0;


    public PlanningAdapter(ArrayList<Episode> liste) {
        this.episodesList = liste;
    }

    public PlanningAdapter(ArrayList<Episode> episodes, ArrayList<String> headers) {
        this.episodesList = episodes;
        this.headersList = headers;
    }



    @Override
    public int getItemCount() {
        return episodesList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public PlanningViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.planning_row_with_header, viewGroup, false);

        final Episode episode = episodesList.get(count);
        String header = headersList.get(count);

        TextView headerTextView = (TextView) itemView.findViewById(R.id.headerTextView);
        View headerSeparator = itemView.findViewById(R.id.headerSeparatorView);
        View episodeSeparator = itemView.findViewById(R.id.episodeSeparatorView);

        if(count != 0 && ((header == null) || (header.equals("")))) {

            headerTextView.setVisibility(View.GONE);
            headerSeparator.setVisibility(View.GONE);
            episodeSeparator.setVisibility(View.VISIBLE);

        } else {

            headerTextView.setVisibility(View.VISIBLE);
            headerSeparator.setVisibility(View.VISIBLE);
            episodeSeparator.setVisibility(View.GONE);
        }

        headerTextView.setText(header);

        TextView dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
        dateTextView.setText(EpisodeUtils.getDateShortString(episode));

        TextView showTitleTextView = (TextView) itemView.findViewById(R.id.showTitleTextView);
        showTitleTextView.setText(episode.getShow());

        TextView episodeDetailsTextView = (TextView) itemView.findViewById(R.id.episodeDetailsTextView);
        episodeDetailsTextView.setText(String.format("%s - %s", episode.getCode(), episode.getTitle()));


        count++;

        return new PlanningViewHolder(itemView, viewGroup.getContext());
    }

    @Override
    public void onBindViewHolder(PlanningViewHolder holder, int position) {


        Episode episode = episodesList.get(position);
        String header = headersList.get(position);

        if(position != 0 && ((header == null) || (header.equals("")))) {

            holder.headerTextView.setVisibility(View.GONE);
            holder.headerSeparator.setVisibility(View.GONE);
            holder.episodeSeparator.setVisibility(View.VISIBLE);

        } else {

            holder.headerTextView.setVisibility(View.VISIBLE);
            holder.headerTextView.setText(episode.getHeader());
            holder.headerSeparator.setVisibility(View.VISIBLE);
            holder.episodeSeparator.setVisibility(View.GONE);
        }

        holder.headerTextView.setText(header);
        holder.dateTextView.setText(EpisodeUtils.getDateShortString(episode));
        holder.showTitleTextView.setText(episode.getShow());
        holder.episodeDetailsTextView.setText(String.format("%s - %s", episode.getCode(), episode.getTitle()));


    }


    public class PlanningViewHolder extends RecyclerView.ViewHolder {

        protected TextView headerTextView;
        protected TextView dateTextView;
        protected TextView showTitleTextView;
        protected TextView episodeDetailsTextView;
        protected View headerSeparator;
        protected View episodeSeparator;

        public PlanningViewHolder(View itemView) {
            super(itemView);
        }

        public PlanningViewHolder(View v, final Context c) {
            super(v);

            headerTextView = (TextView) v.findViewById(R.id.headerTextView);
            dateTextView = (TextView) v.findViewById(R.id.dateTextView);
            showTitleTextView = (TextView) v.findViewById(R.id.showTitleTextView);
            episodeDetailsTextView = (TextView) v.findViewById(R.id.episodeDetailsTextView);
            headerSeparator = v.findViewById(R.id.headerSeparatorView);
            episodeSeparator = v.findViewById(R.id.episodeSeparatorView);

        }
    }
}


