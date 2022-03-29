package application.vision.translation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

public class MainActivity2 extends AppCompatActivity {
    public static String str="";
    public static MainActivity2 window;
    AdView adView;
    AdRequest adRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        window = this;
        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        adView.setAdUnitId(Helper.Banner);
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        ((LinearLayout)findViewById(R.id.bannerholder)).addView(adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                MainActivity.window.edit.setText( ((EditText)findViewById(R.id.edit1)).getText().toString().trim().replace("\n",". ") );
                MainActivity.window.Tr();
                finish();
            }
        });
    }
    public void Do(){
        ((EditText)findViewById(R.id.edit1)).setText(str);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.loader).animate().alpha(0).setDuration(400);
            }
        }, 1000);
    }
}