package com.example.thibautvirolle.betaseries.planning;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thibautvirolle.betaseries.R;
import com.example.thibautvirolle.betaseries.episodes.Episode;
import com.example.thibautvirolle.betaseries.shows.ShowsService;
import com.example.thibautvirolle.betaseries.utilitaires.Config;
import com.example.thibautvirolle.betaseries.utilitaires.DownloadResultReceiver;

import java.util.ArrayList;


public class PlanningFragment extends Fragment implements DownloadResultReceiver.Receiver {

    private static String TAG = PlanningFragment.class.getSimpleName();
    private ArrayList<Episode> planningList = new ArrayList<>();
    private static View mContentView;
    private static View mProgressView;
    private boolean displayArchived = true;
    private String token = "";
    private String userId;

    private DownloadResultReceiver mReceiver;

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_planning, container, false);

        userId = getArguments().getString("user_id");
        token = getArguments().getString("token");



        mContentView = rootView.findViewById(R.id.planningListContainer);
        mProgressView = rootView.findViewById(R.id.loadingContainer);


        planningList = getArguments().getParcelableArrayList(Config.PLANNING_LIST);

        ListView planningListView = (ListView) rootView.findViewById(R.id.planningListView);
        planningListView.setAdapter(new PlanningAdapter(getActivity().getApplicationContext(),planningList));
        showProgress(false);


        return rootView;
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Log.d(TAG,String.valueOf(resultCode));
        switch (resultCode) {
            case ShowsService.STATUS_RUNNING:

                //setProgressBarIndeterminateVisibility(true);
                break;
            case ShowsService.STATUS_FINISHED:
                /* Hide progress & extract result from bundle */
                //setProgressBarIndeterminateVisibility(false);

                planningList = resultData.getParcelableArrayList("planning");
                //Toast.makeText(this, String.valueOf(userShowsList.size()), Toast.LENGTH_SHORT).show();
                Log.d(TAG,"Data received");

                ListView planningListView = (ListView) rootView.findViewById(R.id.showsListView);
                planningListView.setAdapter(new PlanningAdapter(getActivity().getApplicationContext(),planningList));
                showProgress(false);

                break;
            case ShowsService.STATUS_ERROR:
                /* Handle the error */
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_LONG).show();
                break;
        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mContentView.setVisibility(show ? View.GONE : View.VISIBLE);
            mContentView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mContentView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mContentView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
