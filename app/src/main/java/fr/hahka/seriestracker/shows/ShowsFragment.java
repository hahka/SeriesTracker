package fr.hahka.seriestracker.shows;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;

import java.util.ArrayList;

import fr.hahka.seriestracker.DownloadResultReceiver;
import fr.hahka.seriestracker.R;
import fr.hahka.seriestracker.simpleshow.SimpleShow;
import fr.hahka.seriestracker.simpleshow.SimpleShowAdapter;
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.ScrollableFragmentWithBottomBar;
import fr.hahka.seriestracker.utilitaires.UserInterface;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class ShowsFragment extends ScrollableFragmentWithBottomBar implements DownloadResultReceiver.Receiver{

    private static final String TAG = ShowsFragment.class.getSimpleName();
    View rootView;
    View mContentView;
    View mProgressView;
    String token;
    String userId;

    PullRefreshLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.simple_shows_list_fragment, container, false);


        mContentView = rootView.findViewById(R.id.showsListContainer);
        mProgressView = rootView.findViewById(R.id.progressBar);


        UserInterface.showProgress(true, mContentView, mProgressView);

        userId = getArguments().getString(Config.USER_ID);
        token = getArguments().getString(Config.TOKEN);

        displayShowsList();

        layout = (PullRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);

        // listen refresh event
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                DownloadResultReceiver mReceiver = new DownloadResultReceiver(new Handler());
                mReceiver.setReceiver(ShowsFragment.this);
                Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity().getApplicationContext(), ShowsService.class);

                // Send optional extras to Download IntentService
                intent.putExtra("receiver", mReceiver);
                intent.putExtra(Config.USER_ID, userId);
                intent.putExtra(Config.TOKEN, token);

                getActivity().startService(intent);
            }
        });

        return rootView;
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case Config.STATUS_RUNNING:
                //Log.d(TAG, "running");
                break;
            case Config.STATUS_FINISHED:

                if(isAdded()) {
                    System.out.println("finished");
                    layout.setRefreshing(false);

                    displayShowsList();

                }

                break;
            case Config.STATUS_ERROR:

                if(isAdded()) {
                    Log.e(TAG, "error");
                    UserInterface.showProgress(true, mContentView, mProgressView);
                    String error = resultData.getString(Intent.EXTRA_TEXT);
                    Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    public void displayShowsList() {
        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.showsRecyclerView);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);


        RealmConfiguration config = new RealmConfiguration.Builder(getActivity().getApplicationContext()).build();
        Realm realm = Realm.getInstance(config);

        RealmQuery<SimpleShow> query = realm.where(SimpleShow.class)
                .equalTo("userId", Integer.parseInt(userId));

        RealmResults<SimpleShow> result1 = query.findAll();

        result1 = result1.sort("title");

        ArrayList<SimpleShow> userShowsList = new ArrayList<>();
        for (SimpleShow show : result1) {
            userShowsList.add(show);
        }

        SimpleShowAdapter simpleShowAdapter = new SimpleShowAdapter(getActivity().getApplicationContext(), userShowsList, token);
        recList.setAdapter(simpleShowAdapter);

        UserInterface.showProgress(false, mContentView, mProgressView);

        super.setScrollBehavior(recList);
    }
}
