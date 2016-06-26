package fr.hahka.seriestracker.utilitaires.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import fr.hahka.seriestracker.utilitaires.BitmapWorkerTask;

/**
 * Created by thibautvirolle on 19/01/15.
 * Classe de gestion des images (cache, ajout cache etc...)
 */
public class BitmapTasks extends AsyncTask<String, Void, Bitmap> {

    private static final String TAG = BitmapTasks.class.getSimpleName();
    private static LruCache<String, Bitmap> mMemoryCache;
    private final WeakReference<ImageView> imageViewReference;

    public BitmapTasks(ImageView imageView) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        this.imageViewReference = new WeakReference<ImageView>(imageView);
    }

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

            fillViewWithBitmap(mImageView, bitmap);

        } else {

            File image = AndroidBitmapUtil.getOutputMediaFile("banners/"+String.valueOf(key)+".bmp");
            if(image != null){
                Bitmap b = BitmapFactory.decodeFile(image.getAbsolutePath());
                fillViewWithBitmap(mImageView,b);

                BitmapTasks.addBitmapToMemoryCache(key, b);

            } else {

                BitmapWorkerTask task = new BitmapWorkerTask(mImageView, key);
                task.execute(url,key);
            }

        }
    }

    public static void fillViewWithBitmap(final ImageView imv, Bitmap b) {

        if(imv != null && b != null){
            final float ratio = (float) b.getWidth() / (float) b.getHeight();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            byte[] imageAsBytes = Base64.decode(encoded.getBytes(),Base64.DEFAULT);

            final Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);


            imv.post(new Runnable() {
                @Override
                public void run(){

                    imv.setImageBitmap(Bitmap.createScaledBitmap(bitmap,
                            imv.getWidth(),
                            (int) (imv.getWidth() / ratio),
                            false));
                }
            });


        }


    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(String... params) {

        String key = params[0];

        if(key != null) {
            Bitmap bitmap = getBitmapFromMemCache(params[0]);
            if (bitmap != null) {

                return bitmap;

            } else {

                File image = AndroidBitmapUtil.getOutputMediaFile("banners/" + String.valueOf(key) + ".bmp");
                if (image != null) {

                    Bitmap b = BitmapFactory.decodeFile(image.getAbsolutePath());
                    BitmapTasks.addBitmapToMemoryCache(key, b);

                    return bitmap;

                } else {
                    if (params[1] != null && !params[1].equals("")) {
                        try {
                            InputStream in;
                            in = new java.net.URL(params[1]).openStream();

                            bitmap = BitmapFactory.decodeStream(in);
                            BitmapTasks.addBitmapToMemoryCache(params[0], bitmap);

                            return bitmap;

                        } catch (Exception e) {
                            Log.e("Error", e.getMessage());
                            //e.printStackTrace();
                        }
                    }
                }

            }
        } else {
            return getBitmapFromMemCache("blackground");
        }

        return null;

    }

    // Once complete, see if ImageView is still around and set bitmap.
    // In the layout, imageview's height is set as wrap_content :
    // This way, all the imageViews are loaded on screen with a height of 0 and they all uses
    // the inCreateViewHolder so they are all loaded
    @Override
    protected void onPostExecute(Bitmap bitmap) {

        //fillViewWithBitmap(imageViewReference.get(), bitmap);
        if (bitmap != null) {

            //On a besoin du ratio pour étirer l'image
            final float ratio = (float) bitmap.getWidth() / (float) bitmap.getHeight();

            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap,
                        imageView.getWidth(),
                        (int) (imageView.getWidth() / ratio),
                        false));
            }
        }
    }


}
