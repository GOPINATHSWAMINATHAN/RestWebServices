package myshoes.com.restwebservices;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import myshoes.com.restwebservices.model.DataItem;
import myshoes.com.restwebservices.utils.HttpHelper;

/**
 * Created by gopinath on 27/02/18.
 */

public class MyService extends IntentService {


    public static final String TAG = "MyService";
    String response;
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param " " Used to name the worker thread, important only for debugging.
     */
    public MyService() {
        super("Learning Services");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Uri uri = intent.getData();

        try {
            response = HttpHelper.downloadUrl(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "onHandleIntent: " + uri.toString());

        Gson gson = new Gson();
        DataItem[] dataItems = gson.fromJson(response, DataItem[].class);
        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);

        messageIntent.putExtra(MY_SERVICE_PAYLOAD, dataItems);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);


    }
}
