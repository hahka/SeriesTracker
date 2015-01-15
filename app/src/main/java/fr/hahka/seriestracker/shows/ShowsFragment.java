package fr.hahka.seriestracker.shows;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import fr.hahka.seriestracker.DownloadResultReceiver;
import fr.hahka.seriestracker.R;
import fr.hahka.seriestracker.user.User;
import fr.hahka.seriestracker.user.UserService;
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.UserInterface;


public class ShowsFragment extends Fragment implements DownloadResultReceiver.Receiver{

    View rootView;
    View mContentView;
    View mProgressView;
    String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.shows_fragment, container, false);


        mContentView = rootView.findViewById(R.id.showsListContainer);
        mProgressView = rootView.findViewById(R.id.progressBar);


        UserInterface.showProgress(false, mContentView, mProgressView);

        /*String token = getArguments().getString(Config.TOKEN);
        User user = getArguments().getParcelable(Config.USER);
        ArrayList<Show> userShowsList = user.getShowsList();

        ListView showsListView = (ListView) rootView.findViewById(R.id.showsListView);
        showsListView.setAdapter(new ShowsAdapter(getActivity().getApplicationContext(), userShowsList, token));*/




        DownloadResultReceiver mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity().getApplicationContext(), UserService.class);

        String userId = getArguments().getString(Config.USER_ID);
        token = getArguments().getString(Config.TOKEN);

        /* Send optional extras to Download IntentService */
        intent.putExtra("receiver", mReceiver);
        intent.putExtra(Config.USER_ID, userId);
        intent.putExtra(Config.TOKEN, token);
        intent.putExtra("requestId", 101);

        getActivity().startService(intent);


        return rootView;
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case Config.STATUS_RUNNING:
                break;
            case Config.STATUS_FINISHED:

                User user = resultData.getParcelable(Config.USER);

                ArrayList<Show> userShowsList = user.getShowsList();

                ListView showsListView = (ListView) rootView.findViewById(R.id.showsListView);
                showsListView.setAdapter(new ShowsAdapter(getActivity().getApplicationContext(), userShowsList, token));



                break;
            case Config.STATUS_ERROR:

                UserInterface.showProgress(true, mContentView, mProgressView);
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_LONG).show();
                break;
        }

    }
}
