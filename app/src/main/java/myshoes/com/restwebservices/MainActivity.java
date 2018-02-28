package myshoes.com.restwebservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import myshoes.com.restwebservices.model.DataItem;
import myshoes.com.restwebservices.utils.NetworkHelper;

public class MainActivity extends AppCompatActivity {


    private boolean networkOk;
    TextView output;
    Button runCode;

    List<DataItem> mItemList;

    Map<String, Bitmap> mBitmaps = new HashMap<>();

    private static final String JSON_URL = "http://560057.youcanlearnit.net/services/json/itemsfeed.php";

    private static final String IMAGE_URL = "http://560057.youcanlearnit.net/services/images/";

    private static final String XML_URL = "http://560057.youcanlearnit.net/services/xml/itemsfeed.php";
    //Getting response back from the intent service using these following steps upto line no 44.
    BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

//            String message = intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);
            DataItem[] dataItems = (DataItem[]) intent.getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);
//            for (DataItem items : dataItems) {
//                output.append(items.getItemName() + "\n");
//            }

            //Changed from array to arraylist.
            mItemList = Arrays.asList(dataItems);
            for (int i = 0; i < mItemList.size(); i++) {
                output.append(mItemList.get(i).getItemName() + "\n");
            }


            /*
            To retrieve the  images from server, we have to use Asynchronous thread.


            Note:
            To bind the bitmap data into imageview without using Picasso or glide. Follow below lines code.


            String imageFile=item.getImage();
            InputStream inputStream=mContext.getAssets().open(imageFile);
            Drawable d=Drawable.creaeFromStream(inputStream,null);
            holder.imageview.setImageDrawable(d);


             */

            // Log.e("SERVICE MESSAGE", "" + message);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = findViewById(R.id.output);
        runCode = findViewById(R.id.run_code);
        networkOk = NetworkHelper.hasNetworkAccess(this);
        if (networkOk) {
            Toast.makeText(getApplicationContext(), "COnnected with network", Toast.LENGTH_LONG).show();
        }
//This line registers the LocalBroadcastManager and get the data from it by calling the intent name(MYService.MY_SERVICE_MESSAGE).
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadCastReceiver, new IntentFilter(MyService.MY_SERVICE_MESSAGE));


        runCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callService();
            }
        });
    }


    void callService() {
        if (networkOk) {
            Intent i = new Intent(this, MyService.class);
            i.setData(Uri.parse(XML_URL));
            startService(i);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mBroadCastReceiver);
    }

    //To retrieve Images


}
