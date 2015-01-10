package fr.hahka.seriestracker.shows;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import fr.hahka.seriestracker.R;
import fr.hahka.seriestracker.user.User;
import fr.hahka.seriestracker.utilitaires.UserInterface;


public class ShowsFragment extends Fragment {

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.shows_fragment, container, false);

        String token = getArguments().getString("token");

        View mContentView = rootView.findViewById(R.id.showsListContainer);
        View mProgressView = rootView.findViewById(R.id.progressBar);

        User user = getArguments().getParcelable("user");
        ArrayList<Show> userShowsList = user.getShowsList();

        ListView showsListView = (ListView) rootView.findViewById(R.id.showsListView);
        showsListView.setAdapter(new ShowsAdapter(getActivity().getApplicationContext(), userShowsList, token));
        UserInterface.showProgress(false, mContentView, mProgressView);

        return rootView;
    }


}
