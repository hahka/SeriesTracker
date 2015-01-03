package com.example.thibautvirolle.betaseries.episodes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thibautvirolle.betaseries.R;

import java.util.ArrayList;

/**
 * Created by thibautvirolle on 07/12/14.
 */
public class EpisodesAdapter extends BaseAdapter {

    private static String TAG = EpisodesAdapter.class.getSimpleName();
    ArrayList<Episode> episodesList = new ArrayList<Episode>();
    private Context context;

    public EpisodesAdapter(Context context, ArrayList<Episode> liste) {
        this.context = context;
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

        if(view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.episode_row, viewGroup, false);
        } else{
            // On a déjà une vue correspondant, on veut juste la modifier, et pas l'inflater
        }

        TextView codetv = (TextView) view.findViewById(R.id.episodeCodeTextView);
        codetv.setText(episode.getCode());

        TextView titletv = (TextView) view.findViewById(R.id.episodeTitleTextView);
        titletv.setText(episode.getTitle());

        TextView idtv = (TextView) view.findViewById(R.id.episodeIdTextView);
        idtv.setText(String.valueOf(episode.getId()));

        if(!episode.isSeen())
        {
            ImageButton isSeenImageButton = (ImageButton) view.findViewById(R.id.isSeenImageButton);
            isSeenImageButton.setBackgroundResource(R.drawable.ic_check_box_outline_blank_black);
        } else {
            ImageButton isSeenImageButton = (ImageButton) view.findViewById(R.id.isSeenImageButton);
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


