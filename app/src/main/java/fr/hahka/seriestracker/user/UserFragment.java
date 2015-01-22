package fr.hahka.seriestracker.user;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import fr.hahka.seriestracker.DownloadResultReceiver;
import fr.hahka.seriestracker.R;
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.DownloadImageTask;
import fr.hahka.seriestracker.utilitaires.UserInterface;

public class UserFragment extends Fragment implements DownloadResultReceiver.Receiver{

    View rootView;
    View mContentView;
    View mProgressView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        mContentView = rootView.findViewById(R.id.profileContainer);
        mProgressView = rootView.findViewById(R.id.loadingContainer);
        UserInterface.showProgress(true, mContentView, mProgressView);


        DownloadResultReceiver mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity().getApplicationContext(), UserService.class);


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
            case Config.STATUS_RUNNING:
                break;
            case Config.STATUS_FINISHED:

                User user = resultData.getParcelable(Config.USER);

                TextView mTitleView = (TextView) rootView.findViewById(R.id.profileTitleTextView);
                mTitleView.setText("Résumé de " + user.getLogin());


                TextView episodesTextView = (TextView) rootView.findViewById(R.id.episodesTextView);
                episodesTextView.setText(user.getEpisodes() + " ÉPISODES");
                TextView saisonsTextView = (TextView) rootView.findViewById(R.id.saisonsTextView);
                saisonsTextView.setText(user.getSeasons() + " SAISONS");
                TextView seriesTextView = (TextView) rootView.findViewById(R.id.seriesTextView);
                seriesTextView.setText(user.getShowsList().size() + " SÉRIES");
                TextView badgesTextView = (TextView) rootView.findViewById(R.id.badgesTextView);
                badgesTextView.setText(user.getBadges() + " BADGES");

                ProgressBar avancement = (ProgressBar) rootView.findViewById(R.id.avancementProgressBar);
                avancement.setProgress((int) user.getProgress());

                TextView avancementTextView = (TextView) rootView.findViewById(R.id.avancementTextView);
                avancementTextView.setText(user.getProgress() + "% : " + user.getEpisodesToWatch() + " ÉPISODES À REGARDER");


                String urlAvatar;
                if((urlAvatar = user.getAvatar()).length() > 1)
                    new DownloadImageTask((ImageView) rootView.findViewById(R.id.avatarImageView)).execute(urlAvatar);


                UserInterface.showProgress(false, mContentView, mProgressView);



                break;
            case Config.STATUS_ERROR:

                UserInterface.showProgress(true, mContentView, mProgressView);
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_LONG).show();
                break;
        }


    }
}
