package myshoes.com.restwebservices;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
    Map<String, Bitmap> mBitmaps = new HashMap<>();


    private static final String IMAGE_URL = "http://560057.youcanlearnit.net/services/images/";
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
            response = HttpHelper.downloadUrl(uri.toString(), "nadias", "NadiasPassword");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "onHandleIntent: " + uri.toString());


//        //calling gson object.
//        Gson gson = new Gson();
//        //Getting the value from the response and bind it with dataitems object of DataItem class.
//        DataItem[] dataItems = gson.fromJson(response, DataItem[].class);


        DataItem[] dataItems = MyXMLParser.parseFeed(response);


        for (DataItem item : dataItems) {
            String imageUrl = IMAGE_URL + item.getImage();
            InputStream in = null;
            try {
                in = (InputStream) new URL(imageUrl).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                mBitmaps.put(item.getItemName(), bitmap);

                Log.e("Images are ",""+imageUrl+"/"+mBitmaps);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //Below steps to send response to the BroadcastReceiver from the IntentService.
        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, dataItems);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        //It will send the response to the corresponding parent class which has called this class.
        manager.sendBroadcast(messageIntent);


    }
}
