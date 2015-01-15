package fr.hahka.seriestracker.episodes.planning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fr.hahka.seriestracker.R;

/**
 * Created by thibautvirolle on 07/12/14.
 * Adapter pour afficher la listView du planning
 */
public class PlanningAdapter extends BaseAdapter {

    private static String TAG = PlanningAdapter.class.getSimpleName();
    ArrayList<Planning> episodesList = new ArrayList<>();
    private String[] headerList;

    public PlanningAdapter(Context context,ArrayList<Planning> liste) {
        this.episodesList = liste;

        headerList = context.getResources().getStringArray(R.array.header_items);
        int headerIndice = -1;

        for(Planning p : episodesList) {
            int newHeaderIndice = p.getHeaderIndice();
            if(!(headerIndice == newHeaderIndice)){
                headerIndice = newHeaderIndice;
                p.setHeader(headerList[headerIndice]);
            }
        }

    }

    @Override
    public int getCount() {
        return episodesList.size();
    }

    @Override
    public Planning getItem(int position) {
        return episodesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final Planning episode = getItem(position);


        int headerIndice = episode.getHeaderIndice();

        //Log.d(TAG,episode.getTitle()+ " : " +episode.getCurrentDayOfWeek() + "/" +episode.getIndice()+"/"+headerIndice);

        if(position == 0) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.planning_row_with_header, viewGroup, false);
            //Log.d(TAG,"First row");
            TextView headerTextView = (TextView) view.findViewById(R.id.headerTextView);
            headerTextView.setText(headerList[headerIndice]);

        } else {

            String header = episode.getHeader();
            if(!(header == null)) {
                //Log.d(TAG,"With header");
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.planning_row_with_header, viewGroup, false);

                TextView headerTextView = (TextView) view.findViewById(R.id.headerTextView);
                headerTextView.setText(header);

            } else {
                //Log.d(TAG,"Without header");
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.planning_row, viewGroup, false);
            }

        }

        TextView dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        dateTextView.setText(episode.getDateShortString());

        TextView showTitleTextView = (TextView) view.findViewById(R.id.showTitleTextView);
        showTitleTextView.setText(episode.getShow());

        TextView episodeDetailsTextView = (TextView) view.findViewById(R.id.episodeDetailsTextView);
        episodeDetailsTextView.setText(episode.getCode() + " - " + episode.getTitle());


        return view;
    }
}


