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
import fr.hahka.seriestracker.simpleshow.SimpleShow;
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.DownloadImageTask;
import fr.hahka.seriestracker.utilitaires.UserInterface;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class UserFragment extends Fragment implements DownloadResultReceiver.Receiver{

    View rootView;
    View mContentView;
    View mProgressView;

    private String userId;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        mContentView = rootView.findViewById(R.id.profileContainer);
        mProgressView = rootView.findViewById(R.id.loadingContainer);
        UserInterface.showProgress(true, mContentView, mProgressView);

        userId = getArguments().getString(Config.USER_ID);
        token = getArguments().getString(Config.TOKEN);

        displayUserInformations(userId, token);

        return rootView;
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {
            case Config.STATUS_RUNNING:
                break;
            case Config.STATUS_FINISHED:

                int userId = resultData.getInt(Config.USER);

                displayUserInformations(String.valueOf(userId), token);
                UserInterface.showProgress(false, mContentView, mProgressView);

                break;
            case Config.STATUS_ERROR:

                UserInterface.showProgress(true, mContentView, mProgressView);
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_LONG).show();
                break;
        }


    }

    private void displayUserInformations(String userId, String token) {


        RealmConfiguration config = new RealmConfiguration.Builder(getActivity().getApplicationContext()).build();
        Realm realm = Realm.getInstance(config);
        User newUser = null;

        int test = Integer.parseInt(userId);

        // TODO : getByUser
        RealmQuery<User> query = realm.where(User.class)
                .equalTo("id", test);


        RealmResults<User> result1 = query.findAll();

        if(result1.size() == 1) {
            newUser = result1.last();


            RealmQuery<SimpleShow> queryBis = realm.where(SimpleShow.class)
                    .equalTo("userId", Integer.parseInt(userId));


            RealmResults<SimpleShow> result2 = queryBis.findAll();


            TextView mTitleView = (TextView) rootView.findViewById(R.id.profileTitleTextView);
            mTitleView.setText("Résumé de " + newUser.getLogin());


            TextView episodesTextView = (TextView) rootView.findViewById(R.id.episodesTextView);
            episodesTextView.setText(newUser.getEpisodes() + " ÉPISODES");
            TextView saisonsTextView = (TextView) rootView.findViewById(R.id.saisonsTextView);
            saisonsTextView.setText(newUser.getSeasons() + " SAISONS");
            TextView seriesTextView = (TextView) rootView.findViewById(R.id.seriesTextView);
            seriesTextView.setText(result2.size() + " SÉRIES");
            TextView badgesTextView = (TextView) rootView.findViewById(R.id.badgesTextView);
            badgesTextView.setText(newUser.getBadges() + " BADGES");

            ProgressBar avancement = (ProgressBar) rootView.findViewById(R.id.avancementProgressBar);
            avancement.setProgress((int) newUser.getProgress());

            TextView avancementTextView = (TextView) rootView.findViewById(R.id.avancementTextView);
            avancementTextView.setText(newUser.getProgress() + "% : " + newUser.getEpisodesToWatch() + " ÉPISODES À REGARDER");


            String urlAvatar;
            if((urlAvatar = newUser.getAvatar()).length() > 1)
                new DownloadImageTask((ImageView) rootView.findViewById(R.id.avatarImageView)).execute(urlAvatar);


            UserInterface.showProgress(false, mContentView, mProgressView);

        } else if(result1.size() == 0){

            callUserService(userId, token);

        } else if(result1.size() > 1) {

            // TODO : clear database

        }

    }

    private void callUserService(String userId, String token) {
        if(userId != null && token != null) {

            UserInterface.showProgress(true, mContentView, mProgressView);

            DownloadResultReceiver mReceiver = new DownloadResultReceiver(new Handler());
            mReceiver.setReceiver(this);
            Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity().getApplicationContext(), UserService.class);


            /* Send optional extras to Download IntentService */
            intent.putExtra("receiver", mReceiver);
            intent.putExtra(Config.USER_ID, userId);
            intent.putExtra(Config.TOKEN, token);
            intent.putExtra("requestId", 101);

            getActivity().startService(intent);
        }
    }
}
