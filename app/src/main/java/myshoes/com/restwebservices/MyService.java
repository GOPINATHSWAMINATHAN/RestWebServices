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
import myshoes.com.restwebservices.utils.MyXMLParser;

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

    
    //This onHandleIntent(@Nullable Intent intent) method has been overrided from IntentService.
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Uri uri = intent.getData();

        
        try {
            //Called the downloadUrl method from HttpHelper class to get the response from it.
            response = HttpHelper.downloadUrl(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "onHandleIntent: " + uri.toString());




//        //calling gson object.
//        Gson gson = new Gson();
//        //Getting the value from the response and bind it with dataitems object of DataItem class.
//        DataItem[] dataItems = gson.fromJson(response, DataItem[].class);



        DataItem[] dataItems= MyXMLParser.parseFeed(response);

        //Below steps to send response to the BroadcastReceiver from the IntentService.
        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, dataItems);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        //It will send the response to the corresponding parent class which has called this class.
        manager.sendBroadcast(messageIntent);


    }
}
