package fr.hahka.seriestracker.user;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import fr.hahka.seriestracker.R;
import fr.hahka.seriestracker.utilitaires.DownloadImageTask;

public class UserFragment extends Fragment {

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        User user = getArguments().getParcelable("user");

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


        new DownloadImageTask((ImageView) rootView.findViewById(R.id.avatarImageView))
                .execute(user.getAvatar());


        return rootView;
    }


}
