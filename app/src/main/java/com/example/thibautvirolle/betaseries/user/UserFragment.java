package com.example.thibautvirolle.betaseries.user;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.thibautvirolle.betaseries.R;

import java.io.InputStream;


public class UserFragment extends Fragment {

    View rootView;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        user = getArguments().getParcelable("user");

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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
