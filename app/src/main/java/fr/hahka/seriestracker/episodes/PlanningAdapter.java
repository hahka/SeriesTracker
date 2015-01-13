package fr.hahka.seriestracker.episodes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fr.hahka.seriestracker.R;

/**
 * Created by thibautvirolle on 07/12/14.
 */
public class PlanningAdapter extends BaseAdapter {

    private static String TAG = PlanningAdapter.class.getSimpleName();
    ArrayList<Planning> episodesList = new ArrayList<>();
    //ArrayList<Integer> headerList = new ArrayList<>();

    private String[] headerList;

    private Context context;

    private static boolean inflated = false;

    public PlanningAdapter(Context context,ArrayList<Planning> liste) {
        this.episodesList = liste;
        this.context = context;

        headerList = context.getResources().getStringArray(R.array.header_items);
        /*String header = "";
        int i = 0;
        for(Episode ep : episodesList) {
            String newHeader = ep.getHeader();
            if(!header.equals(newHeader)){
                headerList.add(i);
                header = newHeader;
            }
            i++;
        }

        for(int h : headerList){
            Log.d(TAG,String.valueOf(h));
        }*/
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

        //String header = episode.getHeader();

        //long diff = episode.getNbJourAvantDiffusion() + episode.getCurrentDayOfWeek();

        /*Log.d(TAG, episode.getTitle()+" : "+episode.getDateShortString());
        Log.d(TAG,header + " : " + diff);*/

        /*if(headerList.contains(position)) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.planning_row_with_header, viewGroup, false);
            TextView headerTextView = (TextView) view.findViewById(R.id.headerTextView);
            headerTextView.setText(header);
            Log.d(TAG,episode.getTitle()+ " : "+episode.getIndice());
            inflated = false;

        } else {
            Log.d(TAG,"Without header");
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.planning_row, viewGroup, false);

        }*/

        int headerIndice = episode.getHeaderIndice();

        String header = headerList[headerIndice];

        if(position == 0) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.planning_row_with_header, viewGroup, false);
            Log.d(TAG,"First row");
            TextView headerTextView = (TextView) view.findViewById(R.id.headerTextView);
            headerTextView.setText(header);

        } else {

            final Planning previousEpisode = getItem(position-1);
            int prevHeader = previousEpisode.getHeaderIndice();
            if(!(headerIndice == prevHeader)) {
                Log.d(TAG,"With header");
                Log.d(TAG,episode.getTitle() + " : " + headerIndice + " / " + prevHeader);
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.planning_row_with_header, viewGroup, false);

                TextView headerTextView = (TextView) view.findViewById(R.id.headerTextView);
                headerTextView.setText(header);

            } else {
                Log.d(TAG,"Without header");
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


