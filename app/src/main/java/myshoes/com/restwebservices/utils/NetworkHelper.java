package myshoes.com.restwebservices.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by gopinath on 27/02/18.
 */

public class NetworkHelper {

    public static boolean hasNetworkAccess(Context context)
    {
        ConnectivityManager cv= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo activeNetwork=cv.getActiveNetworkInfo();
            return activeNetwork!=null&&activeNetwork.isConnectedOrConnecting();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
