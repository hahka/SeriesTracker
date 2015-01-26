package fr.hahka.seriestracker.utilitaires;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by thibautvirolle on 26/01/15.
 * Debugger divers
 */
public class DebugUtils {

    private static final String TAG = DebugUtils.class.getSimpleName();

    public static void streamDebugger(InputStream is) throws IOException {

        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = r.readLine()) != null) {
            Log.d(TAG, line);
        }
    }

}
