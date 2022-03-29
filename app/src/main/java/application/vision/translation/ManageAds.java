package application.vision.translation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class ManageAds extends AppCompatActivity {
    private RewardedAd mRewardedAd;
    AdRequest adRequest;
    ProgressDialog progressdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_ads);
         adRequest = new AdRequest.Builder().build();
        numbers = findViewById(R.id.numbers);
        numbers.setText(Helper.Step+"");
        ((TextView)findViewById(R.id.notice)).setText("You have an ad shown for each "+Helper.Step+" actions you make, such as translation through text, mic or camera! Watch an ad now to increment the number by 2.");
         progressdialog = new ProgressDialog(ManageAds.this);
        progressdialog.setMessage("Make sure to fully watch the ad!");
        findViewById(R.id.watchadnow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                progressdialog.show();

                RewardedAd.load(ManageAds.this, Helper.Rewarded,
                        adRequest, new RewardedAdLoadCallback() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error.
                                mRewardedAd = null;
                            }

                            @Override
                            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                                mRewardedAd = rewardedAd;
                                progressdialog.cancel();
                                Activity activityContext = ManageAds.this;
                                mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                                    @Override
                                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                        // Handle the reward.
                                        int rewardAmount = rewardItem.getAmount();
                                        String rewardType = rewardItem.getType();
                                        Helper.Step+=2;
                                        numbers.setText(Helper.Step+"");
                                        ((TextView)findViewById(R.id.notice)).setText("You have an ad shown for each "+Helper.Step+" actions you make, such as translation through text, mic or camera! Watch an ad now to increment the number by 2.");
                                        MainActivity.window.StartNow();
                                        Toast.makeText(activityContext, "Reward earned!", Toast.LENGTH_SHORT).show();
                                    }


                                });
                            }
                        });



            }
        });
    }
    TextView numbers;
}