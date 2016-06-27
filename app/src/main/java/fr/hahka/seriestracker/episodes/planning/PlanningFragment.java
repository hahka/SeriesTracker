package fr.hahka.seriestracker.episodes.planning;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;

import fr.hahka.seriestracker.DownloadResultReceiver;
import fr.hahka.seriestracker.R;
import fr.hahka.seriestracker.api.APIService;
import fr.hahka.seriestracker.api.ApiParamHashMap;
import fr.hahka.seriestracker.episodes.episodes.Episode;
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.ScrollableFragmentWithBottomBar;
import fr.hahka.seriestracker.utilitaires.UserInterface;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class PlanningFragment extends ScrollableFragmentWithBottomBar implements DownloadResultReceiver.Receiver{

    private static final String TAG = PlanningFragment.class.getSimpleName();
    View rootView;
    View mContentView;
    View mProgressView;
    PullRefreshLayout layout;

    private String userId;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        if(savedInstanceState == null){

            rootView = inflater.inflate(R.layout.planning_fragment, container, false);

            mContentView = rootView.findViewById(R.id.planningListContainer);
            mProgressView = rootView.findViewById(R.id.loadingContainer);
            UserInterface.showProgress(true, mContentView, mProgressView);

            userId = getArguments().getString(Config.USER_ID);
            token = getArguments().getString(Config.TOKEN);

            displayPlanning(userId, token);

            layout = (PullRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);

            // listen refresh event
            layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    callPlanningService(userId, token);

                }
            });


        }

        return rootView;
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {
            case Config.STATUS_RUNNING:
                break;
            case Config.STATUS_FINISHED:

                if(isAdded()) {
                    layout.setRefreshing(false);

                    displayPlanning(userId, token);

                }


                break;
            case Config.STATUS_ERROR:

                if(isAdded()) {
                    String error = resultData.getString(Intent.EXTRA_TEXT);
                    Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_LONG).show();
                }
                break;
        }

    }


    private void displayPlanning(String userId, String token) {

        ArrayList<String> headers = new ArrayList<>();
        String[] headerList = getActivity().getResources().getStringArray(R.array.header_items);
        int headerIndice = -1;

        RealmConfiguration config =
                new RealmConfiguration.Builder(getActivity().getApplicationContext()).build();
        Realm realm = Realm.getInstance(config);

        RealmQuery<Planning> query = realm.where(Planning.class)
                .equalTo("user.id", Integer.parseInt(userId));

        RealmResults<Planning> result1 = query.findAll();

        if(result1.size() >= 1) {

            ArrayList<Episode> episodesList = new ArrayList<>();
            for (Planning planning : result1) {

                Episode episode = planning.getEpisode();
                int newHeaderIndice = episode.getHeaderIndice();
                if(!(headerIndice == newHeaderIndice)){
                    headerIndice = newHeaderIndice;
                    headers.add(headerList[headerIndice]);
                } else {
                    headers.add("");
                }

                episodesList.add(episode);
            }


            RecyclerView planningRecyclerView = (RecyclerView) rootView.findViewById(R.id.planningRecyclerView);
            planningRecyclerView.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            planningRecyclerView.setLayoutManager(llm);
            planningRecyclerView.setAdapter(new PlanningAdapter(episodesList, headers));

            super.setScrollBehavior(planningRecyclerView);

            UserInterface.showProgress(false, mContentView, mProgressView);

        } else if(result1.size() == 0){

            callPlanningService(userId, token);

        }




    }


    private void callPlanningService(String userId, String token) {
        if(userId != null && token != null) {

            UserInterface.showProgress(true, mContentView, mProgressView);

            DownloadResultReceiver mReceiver = new DownloadResultReceiver(new Handler());
            mReceiver.setReceiver(this);
            /*Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity().getApplicationContext(), PlanningService.class);

            // Send optional extras to Download IntentService
            intent.putExtra("receiver", mReceiver);
            intent.putExtra(Config.USER_ID, userId);
            intent.putExtra(Config.TOKEN, token);
            intent.putExtra("requestId", 101);

            getActivity().startService(intent);*/

            Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity().getApplicationContext(), APIService.class);

            // Send optional extras to Download IntentService
            intent.putExtra("rest", "GET");
            intent.putExtra("resource", "planning");
            intent.putExtra("action", "member");

            ApiParamHashMap params = new ApiParamHashMap();
            params.put("id", userId);
            params.put("token", token);

            intent.putExtra("params", params);

            intent.putExtra("receiver", mReceiver);
            intent.putExtra(Config.TOKEN, token);

            getActivity().startService(intent);
        }
    }

}
