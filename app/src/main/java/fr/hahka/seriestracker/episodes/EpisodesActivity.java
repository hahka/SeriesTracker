package fr.hahka.seriestracker.episodes;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.ArrayList;

import fr.hahka.seriestracker.R;
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.JsonParser;
import fr.hahka.seriestracker.utilitaires.UserInterface;

import static fr.hahka.seriestracker.R.drawable.ic_shows;

public class EpisodesActivity extends Activity {

    ViewPager viewPager;
    MyPagerAdapter myPagerAdapter;
    private ArrayList<Episode> userShowEpisodesList = new ArrayList<>();
    private View mContentView;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.episodes_activity);

        setTitle("Épisodes");

        ActionBar ab = getActionBar();
        if(ab != null) {
            ab.setIcon(ic_shows);
        }


        mContentView = findViewById(R.id.episodesListContainer);
        mProgressView = findViewById(R.id.loadingContainer);

        Intent callingIntent = getIntent();
        String showId = callingIntent.getStringExtra("showId");
        String token = callingIntent.getStringExtra("token");

        API request = new API("https://api.betaseries.com/shows/episodes?id=" + showId + "&token=" + token + "&key=" + Config.API_KEY);
        request.execute((Void) null);
        UserInterface.showProgress(true, mContentView, mProgressView);

    }

    private class API extends AsyncTask<Void, Void, Boolean> {

        private String target = "";

        public API(String pTarget) {
            this.target = pTarget;
        }


        @Override
        protected Boolean doInBackground(Void... voids) {

            HttpGet httpget = new HttpGet(target);

            try {
                HttpClient httpclient = new DefaultHttpClient();

                HttpResponse response = httpclient.execute(httpget);
                InputStream is = response.getEntity().getContent();

                userShowEpisodesList = JsonParser.readShowEpisodesJsonStream(is);

                is.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return (!userShowEpisodesList.isEmpty());

        }


        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {

                viewPager = (ViewPager) findViewById(R.id.myviewpager);
                myPagerAdapter = new MyPagerAdapter(getApplicationContext(), userShowEpisodesList);
                viewPager.setAdapter(myPagerAdapter);

                UserInterface.showProgress(false, mContentView, mProgressView);

            }

        }

        @Override
        protected void onCancelled() {
            // TODO : annulation requête
        }


    }


    private class MyPagerAdapter extends PagerAdapter {

        int NumberOfPages;
        ArrayList<Episode> episodesList;
        ArrayList<Integer> seasons = new ArrayList<>();
        Context context;

        String[] title;

        public MyPagerAdapter(Context context, ArrayList<Episode> liste) {
            this.context = context;
            this.episodesList = liste;

            ArrayList<String> titles = new ArrayList<>();

            for (int i = 0; i < liste.size(); i++) {
                int season = liste.get(i).getSeason();
                if (!seasons.contains(season)) {
                    seasons.add(season);
                    titles.add("Saison " + season);
                }
            }

            title = titles.toArray(new String[titles.size()]);
            NumberOfPages = title.length;

        }

        @Override
        public int getCount() {
            return NumberOfPages;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ListView episodesListView = new ListView(EpisodesActivity.this);

            ArrayList<Episode> seasonEpisodesList = new ArrayList<>();

            for (int i = 0; i < userShowEpisodesList.size(); i++) {
                Episode episode = userShowEpisodesList.get(i);
                if (episode.getSeason() == seasons.get(position))
                    seasonEpisodesList.add(episode);
            }

            EpisodesAdapter adapter = new EpisodesAdapter(seasonEpisodesList);

            episodesListView.setAdapter(adapter);

            LinearLayout layout = new LinearLayout(EpisodesActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            LayoutParams layoutParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            layout.setLayoutParams(layoutParams);

            layout.addView(episodesListView);
            container.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

    }


}
