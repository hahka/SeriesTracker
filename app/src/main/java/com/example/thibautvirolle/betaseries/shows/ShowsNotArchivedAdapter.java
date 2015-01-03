package com.example.thibautvirolle.betaseries.shows;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.thibautvirolle.betaseries.R;
import com.example.thibautvirolle.betaseries.episodes.EpisodesActivity;

import java.util.ArrayList;

/**
 * Created by thibautvirolle on 07/12/14.
 */
public class ShowsNotArchivedAdapter extends BaseAdapter {

    private static String TAG = ShowsNotArchivedAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<Show> showsList = new ArrayList<Show>();
    private String token;


    public ShowsNotArchivedAdapter(Context context, ArrayList<Show> liste, String token)
    {
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

        if(view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_row, viewGroup, false);
        } else{
            // On a déjà une vue correspondant, on veut juste la modifier, et pas l'inflater
        }

        if(show.isArchived()) {
            view.setVisibility(View.GONE);
        } else {

            TextView titletv = (TextView) view.findViewById(R.id.showsTitleTextView);
            titletv.setText(show.getTitle());

            TextView detailstv = (TextView) view.findViewById(R.id.showsDetailsTextView);
            detailstv.setText(show.getSeasonsEpisodesToString());

            TextView idtv = (TextView) view.findViewById(R.id.showsIdTextView);
            idtv.setText(String.valueOf(show.getId()));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView idtv = (TextView) view.findViewById(R.id.showsIdTextView);
                    String showId = idtv.getText().toString();
                    Log.d(TAG, "Show ID : " + showId);


                    Intent userShowEpisodesIntent;
                    userShowEpisodesIntent = new Intent(context, EpisodesActivity.class);
                    userShowEpisodesIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    userShowEpisodesIntent.putExtra("showId", showId);
                    userShowEpisodesIntent.putExtra("token", token);
                    context.startActivity(userShowEpisodesIntent);


                }
            });

        }

        return view;
    }
}
