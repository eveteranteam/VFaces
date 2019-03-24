package ua.gov.mva.vfaces.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Util class which reads raw file from resources.
 */
public class RawResourceReader {

    public static final String TAG = "RawResourceReader";

    public static String readTextFileFromRawResource(final int resourceId, Context context) {
        final BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        context.getResources()
                                .openRawResource(resourceId)));

        final StringBuilder builder = new StringBuilder();
        String nextLine;
        try {
            while ((nextLine = bufferedReader.readLine()) != null) {
                builder.append(nextLine);
                builder.append('\n');
            }
        } catch (IOException e) {
            Log.e(TAG, "Can't read file from resources. " + e);
            return null;
        }
        return builder.toString();
    }
}