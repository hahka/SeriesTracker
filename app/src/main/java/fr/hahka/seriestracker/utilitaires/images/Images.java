package fr.hahka.seriestracker.utilitaires.images;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by thibautvirolle on 25/01/15.
 */
public class Images {

    private static final String TAG = Images.class.getSimpleName();

    public static String saveToInternalSorage(Context context, Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());

        Log.d(TAG, "saveToInternalStorage");
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("banners", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);
            Log.d(TAG,mypath.getAbsolutePath());
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    public static void saveImage(Context context, Bitmap b,String name){

        FileOutputStream out;
        try {
            out = context.openFileOutput(name, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
