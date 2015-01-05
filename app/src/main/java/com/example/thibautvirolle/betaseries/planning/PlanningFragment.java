package com.example.thibautvirolle.betaseries.planning;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.thibautvirolle.betaseries.R;
import com.example.thibautvirolle.betaseries.episodes.Episode;
import com.example.thibautvirolle.betaseries.utilitaires.Config;
import com.example.thibautvirolle.betaseries.utilitaires.DownloadResultReceiver;
import com.example.thibautvirolle.betaseries.utilitaires.Progress;

import java.util.ArrayList;


public class PlanningFragment extends Fragment {

    private static String TAG = PlanningFragment.class.getSimpleName();
    private static View mContentView;
    private static View mProgressView;
    View rootView;
    private ArrayList<Episode> planningList = new ArrayList<>();
    private boolean displayArchived = true;
    private String token = "";
    private String userId;
    private DownloadResultReceiver mReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.planning_fragment, container, false);

        planningList = getArguments().getParcelableArrayList(Config.PLANNING_LIST);


        mContentView = rootView.findViewById(R.id.planningListContainer);
        mProgressView = rootView.findViewById(R.id.loadingContainer);


        ListView planningListView = (ListView) rootView.findViewById(R.id.planningListView);
        planningListView.setAdapter(new PlanningAdapter(getActivity().getApplicationContext(), planningList));

        Progress.showProgress(false, mContentView, mProgressView);


        return rootView;
    }


}
