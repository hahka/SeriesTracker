package fr.hahka.seriestracker.episodes.planning;

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
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.UserInterface;


public class PlanningFragment extends Fragment implements DownloadResultReceiver.Receiver{

    private static final String TAG = PlanningFragment.class.getSimpleName();
    View rootView;
    View mContentView;
    View mProgressView;
    ArrayList<Planning> planningList;
    private DownloadResultReceiver mReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.planning_fragment, container, false);

        mContentView = rootView.findViewById(R.id.planningListContainer);
        mProgressView = rootView.findViewById(R.id.loadingContainer);
        UserInterface.showProgress(true, mContentView, mProgressView);

        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity().getApplicationContext(), PlanningService.class);


        String userId = getArguments().getString(Config.USER_ID);
        String token = getArguments().getString(Config.TOKEN);
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
            case PlanningService.STATUS_RUNNING:
                break;
            case PlanningService.STATUS_FINISHED:

                ListView planningListView = (ListView) rootView.findViewById(R.id.planningListView);

                planningList = resultData.getParcelableArrayList(Config.PLANNING_LIST);

                planningListView.setAdapter(new PlanningAdapter(getActivity().getApplicationContext(),planningList));

                UserInterface.showProgress(false, mContentView, mProgressView);

                break;
            case PlanningService.STATUS_ERROR:
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_LONG).show();
                break;
        }



    }
}
