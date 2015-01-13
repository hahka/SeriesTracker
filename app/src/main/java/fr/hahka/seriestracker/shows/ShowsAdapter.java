package fr.hahka.seriestracker.shows;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import fr.hahka.seriestracker.R;
import fr.hahka.seriestracker.episodes.EpisodesActivity;

/**
 * Created by thibautvirolle on 07/12/14.
 * Adapter pour la liste des shows
 */
public class ShowsAdapter extends BaseAdapter {

    private static String TAG = ShowsAdapter.class.getSimpleName();
    private ArrayList<Show> showsList = new ArrayList<>();
    private Context context;
    private String token;

    private LruCache<String, Bitmap> mMemoryCache;

    public ShowsAdapter(Context context, ArrayList<Show> liste, String token) {
        this.context = context;
        this.showsList = liste;
        this.token = token;


        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public void loadBitmap(String key, ImageView mImageView) {

        final Bitmap bitmap = getBitmapFromMemCache(key);
        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
            Log.d(TAG,"bitmap found");
        } else {
            mImageView.setImageResource(R.drawable.ic_launcher);
            BitmapWorkerTask task = new BitmapWorkerTask(mImageView);
            task.execute("http://cdn.betaseries.com//betaseries//images//fonds//original//1275_1362361611.jpg");
            Log.d(TAG,"bitmap downloaded");
        }
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {


        ImageView bmImage;

        public BitmapWorkerTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urls[0]).openStream();

                bitmap = BitmapFactory.decodeStream(in);
                addBitmapToMemoryCache(urls[0], bitmap);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }


            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {

            bmImage.setImageBitmap(result);
        }

    }


    @Override
    public int getCount() {
        return showsList.size();
    }

    @Override
    public Show getItem(int position) {
        return showsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Show show = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_row, viewGroup, false);
        }

        TextView titletv = (TextView) view.findViewById(R.id.showTitleTextView);
        titletv.setText(show.getTitle());

        ImageView showImageView = (ImageView) view.findViewById(R.id.showImageView);

        /*new DownloadImageTask(showImageView)
                .execute("http://cdn.betaseries.com//betaseries//images//fonds//original//1275_1362361611.jpg");*/

        //Log.d(TAG,"Image downloaded");

        loadBitmap("http://cdn.betaseries.com//betaseries//images//fonds//original//1275_1362361611.jpg",showImageView);
        //loadBitmap(String.valueOf(show.getId()),showImageView);


        TextView idtv = (TextView) view.findViewById(R.id.showIdTextView);
        idtv.setText(String.valueOf(show.getId()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView idtv = (TextView) view.findViewById(R.id.showIdTextView);
                String showId = idtv.getText().toString();

                Intent userShowEpisodesIntent;
                userShowEpisodesIntent = new Intent(context, EpisodesActivity.class);
                userShowEpisodesIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                userShowEpisodesIntent.putExtra("showId", showId);
                userShowEpisodesIntent.putExtra("token", token);
                context.startActivity(userShowEpisodesIntent);

            }
        });


        return view;
    }
}

