package com.example.thibautvirolle.betaseries.shows;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.thibautvirolle.betaseries.R;
import com.example.thibautvirolle.betaseries.user.User;
import com.example.thibautvirolle.betaseries.utilitaires.DownloadResultReceiver;

import java.util.ArrayList;


public class ShowsFragment extends Fragment {

    private static String TAG = ShowsFragment.class.getSimpleName();
    private static View mContentView;
    private static View mProgressView;
    View rootView;
    private ArrayList<Show> userShowsList = new ArrayList<>();
    private boolean displayArchived = true;
    private String token = "";
    private String userId;
    private DownloadResultReceiver mReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.shows_fragment, container, false);

        token = getArguments().getString("token");

        mContentView = rootView.findViewById(R.id.showsListContainer);
        mProgressView = rootView.findViewById(R.id.progressBar);

        User user = getArguments().getParcelable("user");
        userShowsList = user.getShowsList();
        //Toast.makeText(this, String.valueOf(userShowsList.size()), Toast.LENGTH_SHORT).show();

        ListView showsListView = (ListView) rootView.findViewById(R.id.showsListView);
        showsListView.setAdapter(new ShowsAdapter(getActivity().getApplicationContext(), userShowsList, token));
        showProgress(false);

        return rootView;
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
