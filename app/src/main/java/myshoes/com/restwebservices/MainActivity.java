package myshoes.com.restwebservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import myshoes.com.restwebservices.model.DataItem;
import myshoes.com.restwebservices.utils.NetworkHelper;

public class MainActivity extends AppCompatActivity {


    private boolean networkOk;
    TextView output;
    Button runCode;

    private static final String JSON_URL = "http://560057.youcanlearnit.net/services/json/itemsfeed.php";
    BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

//            String message = intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);
            DataItem[] dataItems = (DataItem[]) intent.getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);
            for (DataItem items : dataItems) {
                output.append(items.getItemName() + "\n");
            }

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
            i.setData(Uri.parse(JSON_URL));
            startService(i);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mBroadCastReceiver);
    }
}
