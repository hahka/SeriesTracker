package fr.hahka.seriestracker.shows;

import android.app.Fragment;
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

import java.util.ArrayList;

import fr.hahka.seriestracker.DownloadResultReceiver;
import fr.hahka.seriestracker.R;
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.UserInterface;


public class ShowsFragment extends Fragment implements DownloadResultReceiver.Receiver{

    private static final String TAG = ShowsFragment.class.getSimpleName();
    View rootView;
    View mContentView;
    View mProgressView;
    String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.simple_shows_list_fragment, container, false);


        mContentView = rootView.findViewById(R.id.showsListContainer);
        mProgressView = rootView.findViewById(R.id.progressBar);


        UserInterface.showProgress(true, mContentView, mProgressView);

        String userId = getArguments().getString(Config.USER_ID);
        token = getArguments().getString(Config.TOKEN);


        System.out.println(userId);
        System.out.println(token);


        DownloadResultReceiver mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity().getApplicationContext(), ShowsService.class);

        /* Send optional extras to Download IntentService */
        intent.putExtra("receiver", mReceiver);
        intent.putExtra(Config.USER_ID, userId);
        intent.putExtra(Config.TOKEN, token);

        getActivity().startService(intent);


        return rootView;
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case Config.STATUS_RUNNING:
                //Log.d(TAG, "running");
                break;
            case Config.STATUS_FINISHED:
                System.out.println("finished");

                ArrayList<SimpleShow> userShowsList = resultData.getParcelableArrayList(Config.SHOWS_LIST);

                UserInterface.showProgress(false, mContentView, mProgressView);

                RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.showsRecyclerView);
                recList.setHasFixedSize(true);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recList.setLayoutManager(llm);


                SimpleShowAdapter simpleShowAdapter = new SimpleShowAdapter(getActivity().getApplicationContext(), userShowsList, token);
                recList.setAdapter(simpleShowAdapter);

                break;
            case Config.STATUS_ERROR:

                Log.e(TAG,"error");
                UserInterface.showProgress(true, mContentView, mProgressView);
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_LONG).show();
                break;
        }

    }
}
