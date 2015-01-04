package com.example.thibautvirolle.betaseries.shows;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thibautvirolle.betaseries.R;
import com.example.thibautvirolle.betaseries.utilitaires.Config;
import com.example.thibautvirolle.betaseries.utilitaires.DownloadResultReceiver;

import java.util.ArrayList;


public class ShowsFragment extends Fragment implements DownloadResultReceiver.Receiver {

    private static String TAG = ShowsFragment.class.getSimpleName();
    private ArrayList<Show> userShowsList = new ArrayList<>();
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

        rootView = inflater.inflate(R.layout.fragment_shows, container, false);


        userId = getArguments().getString("user_id");
        token = getArguments().getString("token");

        mContentView = rootView.findViewById(R.id.showsListContainer);
        mProgressView = rootView.findViewById(R.id.progressBar);

        Log.d(TAG,"onCreate");
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity().getApplicationContext(), ShowsService.class);

        intent.putExtra("url", "https://api.betaseries.com/members/infos?id="+userId+"&only=shows"+"&key="+ Config.API_KEY);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("requestId", 101);
        getActivity().startService(intent);

        return rootView;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_archived) {

            ListView showsListView = (ListView) rootView.findViewById(R.id.showsListView);

            if(displayArchived){
                showsListView.setAdapter(new ShowsNotArchivedAdapter(getActivity().getApplicationContext(),userShowsList,token));
                displayArchived = false;
                item.setTitle(R.string.menu_show_archived);
            } else {
                showsListView.setAdapter(new ShowsAdapter(getActivity().getApplicationContext(),userShowsList,token));
                displayArchived = true;
                item.setTitle(R.string.menu_hide_archived);
            }

            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_reload) {

            /* Starting Download Service */
            mReceiver = new DownloadResultReceiver(new Handler());
            mReceiver.setReceiver(this);
            Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity().getApplicationContext(), ShowsService.class);

        /* Send optional extras to Download IntentService */
            intent.putExtra("url", "https://api.betaseries.com/members/infos?id="+userId+"&only=shows"+"&key="+ Config.API_KEY);
            intent.putExtra("receiver", mReceiver);
            intent.putExtra("requestId", 101);

            getActivity().startService(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case ShowsService.STATUS_RUNNING:

                //setProgressBarIndeterminateVisibility(true);
                break;
            case ShowsService.STATUS_FINISHED:
                /* Hide progress & extract result from bundle */
                //setProgressBarIndeterminateVisibility(false);

                userShowsList = resultData.getParcelableArrayList("shows");
                //Toast.makeText(this, String.valueOf(userShowsList.size()), Toast.LENGTH_SHORT).show();
                Log.d(TAG,"Data received");

                ListView showsListView = (ListView) rootView.findViewById(R.id.showsListView);
                showsListView.setAdapter(new ShowsAdapter(getActivity().getApplicationContext(),userShowsList,token));
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
