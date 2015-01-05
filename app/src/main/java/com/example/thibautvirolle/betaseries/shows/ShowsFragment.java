package com.example.thibautvirolle.betaseries.shows;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.thibautvirolle.betaseries.R;
import com.example.thibautvirolle.betaseries.user.User;
import com.example.thibautvirolle.betaseries.utilitaires.Progress;

import java.util.ArrayList;


public class ShowsFragment extends Fragment {

    private static View mContentView;
    private static View mProgressView;
    View rootView;
    private ArrayList<Show> userShowsList = new ArrayList<>();
    private String token = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.shows_fragment, container, false);

        token = getArguments().getString("token");

        mContentView = rootView.findViewById(R.id.showsListContainer);
        mProgressView = rootView.findViewById(R.id.progressBar);

        User user = getArguments().getParcelable("user");
        userShowsList = user.getShowsList();

        ListView showsListView = (ListView) rootView.findViewById(R.id.showsListView);
        showsListView.setAdapter(new ShowsAdapter(getActivity().getApplicationContext(), userShowsList, token));
        Progress.showProgress(false, mContentView, mProgressView);

        return rootView;
    }


}
