package fr.hahka.seriestracker.planning;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import fr.hahka.seriestracker.R;
import fr.hahka.seriestracker.episodes.Episode;
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.Progress;

import java.util.ArrayList;


public class PlanningFragment extends Fragment {

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.planning_fragment, container, false);

        ArrayList<Episode> planningList = getArguments().getParcelableArrayList(Config.PLANNING_LIST);


        View mContentView = rootView.findViewById(R.id.planningListContainer);
        View mProgressView = rootView.findViewById(R.id.loadingContainer);


        ListView planningListView = (ListView) rootView.findViewById(R.id.planningListView);
        planningListView.setAdapter(new PlanningAdapter(planningList));

        Progress.showProgress(false, mContentView, mProgressView);


        return rootView;
    }


}
