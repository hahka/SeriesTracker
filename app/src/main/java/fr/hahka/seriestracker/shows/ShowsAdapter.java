package fr.hahka.seriestracker.shows;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import fr.hahka.seriestracker.R;
import fr.hahka.seriestracker.episodes.EpisodesActivity;

/**
 * Created by thibautvirolle on 07/12/14.
 */
public class ShowsAdapter extends BaseAdapter {

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

