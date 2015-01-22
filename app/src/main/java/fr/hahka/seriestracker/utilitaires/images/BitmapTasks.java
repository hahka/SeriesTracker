package fr.hahka.seriestracker.utilitaires.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import fr.hahka.seriestracker.utilitaires.BitmapWorkerTask;

/**
 * Created by thibautvirolle on 19/01/15.
 * Classe de gestion des images (cache, ajout cache etc...)
 */
public class BitmapTasks {


    private static final String TAG = BitmapTasks.class.getSimpleName();

    private static LruCache<String, Bitmap> mMemoryCache;

    public static void setCache() {

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

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public static void loadBitmap(String key, String url, ImageView mImageView) {

        final Bitmap bitmap = getBitmapFromMemCache(key);
        if (bitmap != null) {
            Log.d(TAG, key + " : chargée depuis cache");
            //mImageView.setImageResource(R.drawable.ic_launcher);
            fillViewWithBitmap(mImageView,bitmap);

        } else {
            //Log.d(TAG,key + " : chargée depuis api");
            //mImageView.setImageResource(R.drawable.ic_launcher);
            BitmapWorkerTask task = new BitmapWorkerTask(mImageView);
            task.execute(url,key);

        }
    }


    public static void fillViewWithBitmap(final ImageView imv, Bitmap b) {

        Log.d(TAG,"filling start");
        //Log.d(TAG,""+imv.getWidth()+" / "+imv.getHeight()+" : "+b.getWidth()+" / "+b.getHeight() );
        if(b != null){
            final float ratio = (float) b.getWidth() / (float) b.getHeight();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            byte[] imageAsBytes = Base64.decode(encoded.getBytes(),Base64.DEFAULT);

            final Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

            /*Log.d(TAG,"bitmap : "+bitmap.getWidth()+"/"+bitmap.getHeight());
            Log.d(TAG,"imv : "+imv.getWidth()+"/"+imv.getHeight());
            Log.d(TAG,"ratio : "+ratio);*/

            imv.post(new Runnable() {
                @Override
                public void run(){
                    imv.setImageBitmap(
                            Bitmap.createScaledBitmap(bitmap,
                                    imv.getWidth(),
                                    (int) (imv.getWidth() / ratio),
                                    false));
                }
            });


            /*Palette.generateAsync(bitmap,new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Log.d(TAG,"TOTO");
                }
            });*/

            Log.d(TAG,"filling end");
        }


    }


}
