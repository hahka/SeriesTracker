package fr.hahka.seriestracker.episodes.episodes;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import fr.hahka.seriestracker.DownloadResultReceiver;
import fr.hahka.seriestracker.R;
import fr.hahka.seriestracker.api.APIService;
import fr.hahka.seriestracker.api.ApiParamHashMap;
import fr.hahka.seriestracker.utilitaires.Config;
import fr.hahka.seriestracker.utilitaires.UserInterface;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static fr.hahka.seriestracker.R.drawable.ic_shows;

public class EpisodesActivity extends Activity implements DownloadResultReceiver.Receiver{

    ViewPager viewPager;
    MyPagerAdapter myPagerAdapter;
    private ArrayList<Episode> userShowEpisodesList = new ArrayList<>();
    private View mContentView;
    private View mProgressView;

    private String showId;

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

        UserInterface.showProgress(true, mContentView, mProgressView);

        DownloadResultReceiver mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        //Intent intent = new Intent(Intent.ACTION_SYNC, null, getApplicationContext(), APIService.class);
        Intent intent = new Intent(this, APIService.class);

        Intent callingIntent = getIntent();
        showId = callingIntent.getStringExtra(Config.SHOW_ID);
        String token = callingIntent.getStringExtra(Config.TOKEN);

        /* Send optional extras to Download IntentService */
        intent.putExtra("rest", "GET");
        intent.putExtra("resource", "shows");
        intent.putExtra("action", "episodes");

        ApiParamHashMap params = new ApiParamHashMap();
        params.put("id", showId);
        params.put("token", token);

        intent.putExtra("params", params);

        intent.putExtra("receiver", mReceiver);
        intent.putExtra(Config.SHOW_ID, showId);
        intent.putExtra(Config.TOKEN, token);

        startService(intent);

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        Log.d("toto", "100");

        switch (resultCode) {
            case Config.STATUS_RUNNING:

                Log.d("toto", "101");

                break;
            case Config.STATUS_FINISHED:

                Log.d("toto", "102");

                RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext()).build();
                Realm realm = Realm.getInstance(config);

                RealmQuery<Episode> query = realm.where(Episode.class)
                        .equalTo("showId", Integer.parseInt(showId));

                RealmResults<Episode> result1 = query.findAll();

                result1.sort("episode");
                result1.sort("season");

                for(Episode episode : result1){
                    userShowEpisodesList.add(episode);
                }


                viewPager = (ViewPager) findViewById(R.id.myviewpager);
                myPagerAdapter = new MyPagerAdapter(getApplicationContext(), userShowEpisodesList);
                viewPager.setAdapter(myPagerAdapter);

                UserInterface.showProgress(false, mContentView, mProgressView);


                break;
            case Config.STATUS_ERROR:

                Log.d("toto", "103");

                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                break;
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

            EpisodesAdapter adapter = new EpisodesAdapter(EpisodesActivity.this, seasonEpisodesList);

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
