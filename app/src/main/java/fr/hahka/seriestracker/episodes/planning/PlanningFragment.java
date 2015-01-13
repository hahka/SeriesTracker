package fr.hahka.seriestracker.episodes.planning;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import fr.hahka.seriestracker.R;
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.UserInterface;


public class PlanningFragment extends Fragment {

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.planning_fragment, container, false);

        ArrayList<Planning> planningList = getArguments().getParcelableArrayList(Config.PLANNING_LIST);


        View mContentView = rootView.findViewById(R.id.planningListContainer);
        View mProgressView = rootView.findViewById(R.id.loadingContainer);


        ListView planningListView = (ListView) rootView.findViewById(R.id.planningListView);

        planningListView.setAdapter(new PlanningAdapter(getActivity().getApplicationContext(),planningList));

        UserInterface.showProgress(false, mContentView, mProgressView);


        return rootView;
    }


}
