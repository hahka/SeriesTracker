package fr.hahka.seriestracker.utilitaires;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

import static fr.hahka.seriestracker.utilitaires.images.BitmapTasks.fillViewWithBitmap;

/**
 * Created by thibautvirolle on 07/01/15.
 * AsyncTack pour télécharger une image à partir d'un lien
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        if(urldisplay != null) {
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }

        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {

        fillViewWithBitmap(bmImage,result);

    }
}
