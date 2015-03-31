package fr.hahka.seriestracker.utilitaires;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import fr.hahka.seriestracker.utilitaires.images.AndroidBitmapUtil;
import fr.hahka.seriestracker.utilitaires.images.BitmapTasks;

import static fr.hahka.seriestracker.utilitaires.images.BitmapTasks.fillViewWithBitmap;

/**
 * Created by thibautvirolle on 20/01/15.
 * AsyncTask pour télécharger des images et les afficher sur des ImageView
 */
public class BitmapWorkerTask extends AsyncTask<String, Void, android.graphics.Bitmap> {

    ImageView bmImage;
    int width, height;
    float imvRatio;
    String key;

    public BitmapWorkerTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    public BitmapWorkerTask(ImageView bmImage, String key) {
        this.bmImage = bmImage;
        this.key = key;
    }


    // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... urls) {

            this.width = bmImage.getWidth();
            this.height = bmImage.getHeight();
            this.imvRatio = (float) width / (float) height;

            Bitmap bitmap = null;

            if(urls[0] != null && !urls[0].equals("")){
                try {
                    InputStream in;
                    in = new java.net.URL(urls[0]).openStream();

                    bitmap = BitmapFactory.decodeStream(in);
                    BitmapTasks.addBitmapToMemoryCache(urls[1], bitmap);

                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            }


            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {

            Log.d(BitmapWorkerTask.class.getSimpleName(),"BitmapWorkerTask finished");

            if(result != null) {
                fillViewWithBitmap(bmImage, result);
                try {
                    AndroidBitmapUtil.save(result,"banners/"+key+".bmp");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



        }


}
