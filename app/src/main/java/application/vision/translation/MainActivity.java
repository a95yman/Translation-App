package application.vision.translation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    String from="",to="ar", text="", translated="", frommic="en";
    WebView web;
    public static MainActivity window;

    Point p = new Point();
    int cs=0;
    public EditText edit;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void SAVED(){
        String source =  File.readFromFile(MainActivity.this, "SAVEorigine");
        String target =  File.readFromFile(MainActivity.this, "SAVEtranslated");
        String targetto =  File.readFromFile(MainActivity.this, "SAVEto");
        String T[] = source.trim().split("\n");
        String T2[] = target.trim().split("\n");
        String T3[] = targetto.trim().split("\n");
        if(T.length>0 && T2.length>0 && T3.length>0 ){


            for(int i = T.length-1;i>=0;i--){
                if(T2.length-1>=i && T3.length-1>=i) {
                    if(!T[i].isEmpty() && !T2[i].isEmpty() && !T3[i].isEmpty()) {
                        TextRight tr = new TextRight(MainActivity.this, T2[i], T3[i].trim());
                        ((LinearLayout) findViewById(R.id.parr2)).addView(tr, 0);
                        TextLeft tl = new TextLeft(MainActivity.this, T[i]);
                        ((LinearLayout) findViewById(R.id.parr2)).addView(tl, 0);

                        tr.HideEls();

                    }
                }
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollBottom2();
                }
            }, 1000);



        }
    }
    void Langs(String query){
        ((LinearLayout) findViewById(R.id.addlangschild)).removeAllViews();
        for (String packs : new String[]{"Afrikaans=af","Albanian=sq","Amharic=am","Arabic=ar",
        "Armenian=hy","Azerbaijani=az","Basque=eu","Belarusian=be","Bengali=bn","Bosnian=bs",
        "Bulgarian=bg","Catalan=ca","Cebuano=ceb","Chichewa=ny","Chinese=zh-CN","Corsican=co",
        "Croatian=hr","Czech=cs","Danish=da","Dutch=nl","English=en","Esperanto=eo","Estonian=et",
        "Filipino=tl","Finnish=fi","French=fr","Frisian=fy","Galician=gl","Georgian=ka","German=de",
        "Greek=el","Gujarati=gu","Haitian Creole=ht","Hausa=ha","Hawaiian=haw","Hebrew=iw","Hindi=hi",
        "Hmong=hmn","Hungarian=hu","Icelandic=is","Igbo=ig","Indonesian=id","Irish=ga","Italian=it",
        "Japanese=ja","Javanese=jw","Kannada=kn","Kazakh=kk","Khmer=km","Kinyarwanda=rw","Korean=ko",
        "Kurdish=ku","KYRGYZ=ky","Lao=lo","Latin=la","Latvian=lv","Lithuanian=lt","Luxembourgish=lb",
        "Macedonian=mk","Malagasy=mg","Malay=ms","Malayalam=ml","Maltese=mt","Maori=mi","Marathi=mr",
        "Mongolian=mn","Myanmar=my","Nepali=ne","Norwegian=no","Odia=or","Pashto=ps","Persian=fa",
        "Polish=pl","Portuguese=pt","Punjabi=pa","Romanian=ro","Russian=ru","Samoan=sm","Scots Gaelic=gd",
        "Serbian=sr","Sesotho=st","Shona=sn","Sindhi=sd","Sinhala=si","Slovak=sk","Slovenian=sl","Somali=so",
        "Spanish=es","Sundanese=su","Swahili=sw","Swedish=sv","Tajik=tg","Tamil=ta","Tatar=tt","Telugu=te",
        "Thai=th","Turkish=tr","Turkmen=tk","Ukrainian=uk","Urdu=ur","Uyghur=ug","Uzbek=uz","Vietnamese=vi",
        "Welsh=cy","Xhosa=xh","Yiddish=yi","Yoruba=yo","Zulu=zu"}){
            if(query!=null && !query.isEmpty()){
                if(packs.toLowerCase().indexOf(query.toLowerCase()) != -1){
                    Language ln = new Language(this, packs.split("=")[0],
                            packs.split("=")[1].trim());
                    ((LinearLayout) findViewById(R.id.addlangschild)).addView(ln);
                }
            }
            else {
                Language ln = new Language(this, packs.split("=")[0],
                        packs.split("=")[1].trim());
                ((LinearLayout) findViewById(R.id.addlangschild)).addView(ln);
            }
        }
    }
    EditText searchLangs;
    AdView adView, adView2;
    private InterstitialAd mInterstitialAd;
    int numbers=0;
    public boolean checkad=true;
    public void StartNow(){
        if(checkad==true){
            checkad=false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    reset = true;
                }
            }, 30 * 60 * 1000);
        }
    }
    boolean reset=false;
    public void LoadAd(){
        if(reset==true){
            Helper.Step=2;
            checkad=true;
            reset=false;
            numbers=0;
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Reminder")
                    .setMessage("Your reward has ended, Ads are shown for each 2 actions. Wanna increase the number now?")
                    .setIcon(R.drawable.warning)
                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            startActivity(new Intent(getApplicationContext(), ManageAds.class));
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("no", null)
                    .show();
        }
        numbers++;
        if(numbers>=Helper.Step) {
            numbers=0;
            InterstitialAd.load(this, Helper.Interstitial, adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd;
                            mInterstitialAd.show(MainActivity.this);
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            mInterstitialAd = null;
                        }
                    });
        }
    }
    String f, t, tmp;
    AdRequest adRequest;
    boolean yesshow=true;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        window = this;
        String tm = File.readFromFile(this, "info");
        if(tm!=null && !tm.isEmpty()){
            yesshow=false;
        }
        adRequest = new AdRequest.Builder().build();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adView = new AdView(MainActivity.this);
                adView.setAdSize(AdSize.BANNER);
                adView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                adView.setAdUnitId(Helper.Banner);
                adView.loadAd(adRequest);

                adView2 = new AdView(MainActivity.this);
                adView2.setAdSize(AdSize.BANNER);
                adView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                adView2.setAdUnitId(Helper.Banner);

                adView2.loadAd(adRequest);

                ((LinearLayout)findViewById(R.id.bannerholder)).addView(adView);
                ((LinearLayout)findViewById(R.id.bannerholder2)).addView(adView2);
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
            }
        }, 1000);

        searchLangs = findViewById(R.id.searchlangs);
        searchLangs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!searchLangs.getText().toString().isEmpty())
                Langs(searchLangs.getText().toString());
                else Langs("");
            }
        });
        SAVED();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                findViewById(R.id.slowcon).setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.splashscreen).animate().alpha(0).setDuration(400);
                    }
                }, 2000);

            }
        }, 13000);
        edit = findViewById(R.id.maintext);
        getWindowManager().getDefaultDisplay().getSize(p);
        findViewById(R.id.mainmenu).animate().translationX(-p.x);
        findViewById(R.id.chooselangs).animate().translationY(p.y);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.chooselangs).setVisibility(View.VISIBLE);
                findViewById(R.id.mainmenu).setVisibility(View.VISIBLE);
            }
        }, 1000);
        String str = File.readFromFile(this, "target");
        String def = File.readFromFile(this, "default");
        if(def!=null && !def.isEmpty()){
            frommic=def.trim().split("=")[1].trim();
            ((TextView) findViewById(R.id.txt0)).setText(def.trim().split("=")[0].trim());
        }
        if(str!=null && !str.isEmpty())
            ChangeLn(str.trim().split("=")[0].trim(),str.trim().split("=")[1].trim());
        else ChangeLn("Arabic","ar");
        findViewById(R.id.swipelangs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(! ((TextView)findViewById(R.id.txt0)).getText().toString().trim().toLowerCase().equals("auto") ){
                     f =((TextView)findViewById(R.id.txt0)).getText().toString();
                     t =((TextView)findViewById(R.id.txt2)).getText().toString();
                     tmp = to;
                    to = frommic;
                    frommic = tmp;
                    ((TextView)findViewById(R.id.txt0)).setText(t);
                    ((TextView)findViewById(R.id.txt2)).setText(f);
                    File.writeToFile(MainActivity.this, "target", f+"="+to.trim());
                    File.writeToFile(MainActivity.this, "default", t+"="+frommic.trim());
                }
            }
        });
        String source =  File.readFromFile(MainActivity.this, "origine");
        String target =  File.readFromFile(MainActivity.this, "translated");
        String targetto =  File.readFromFile(MainActivity.this, "to");
        //Toast.makeText(window, source, Toast.LENGTH_SHORT).show();
        //Toast.makeText(window, target, Toast.LENGTH_SHORT).show();
        //Toast.makeText(window, targetto, Toast.LENGTH_SHORT).show();
        String T[] = source.trim().split("\n");
        String T2[] = target.trim().split("\n");
        String T3[] = targetto.trim().split("\n");
        Ref(source, T, T2,T3);
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //grant the permission
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 700);
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //grant the permission
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 701);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }
        web = new WebView(this);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setSupportZoom(false);
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setAppCacheEnabled(false);
        web.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                if(started>0)
                callJavaScript(web, "Send",to,text);
                else {
                    callJavaScript(web, "Send","ar","hi");
                }
            }
        });
        web.setWebChromeClient(new CustomWebChromeClient());
        JavaScriptInterface jsInterface = new JavaScriptInterface(this);
        web.addJavascriptInterface(jsInterface, "JSInterface");
        web.loadUrl("file:///android_asset/translate/index.html?source="+from);
        findViewById(R.id.translate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tr();
            }
        });
        Langs("");
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cs=0;
                findViewById(R.id.chooselangs).animate().translationY(p.y).setDuration(700);
            }
        });
        findViewById(R.id.choosesource).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cs=1;
                ((TextView)findViewById(R.id.txt1)).setText("Choose Source Language");
                findViewById(R.id.chooselangs).animate().alpha(1);
                findViewById(R.id.chooselangs).animate().translationY(0).setDuration(700);
            }
        });
        findViewById(R.id.choosetarget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cs=1;
                ((TextView)findViewById(R.id.txt1)).setText("Choose Target Language");
                findViewById(R.id.chooselangs).animate().alpha(1);
                findViewById(R.id.chooselangs).animate().translationY(0).setDuration(700);
            }
        });
        findViewById(R.id.mic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
                    checkPermission();
                    return;
                }
                String language =  frommic;
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,language);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language);
                intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, language);
                startActivityForResult(intent, 1);

            }
        });
        findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    //grant the permission
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 701);
                    return;
                }
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 2);
            }
        });
        findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 700);
                    return;
                }
                if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.Q) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                    imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 33);
                }
                else{
                    doProcess(view);
                }
            }
        });
        nav = findViewById(R.id.nav);
        for (int i =0; i<nav.getChildCount(); i++){
            int finalI = i;
            nav.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(finalI==0 || finalI==1) {
                        for (int j = 0; j < nav.getChildCount(); j++) {
                            if (j == 0) {
                                ((RelativeLayout) nav.getChildAt(j)).getChildAt(0).setBackgroundResource(R.drawable.circle_none);
                                ((RelativeLayout) ((RelativeLayout) nav.getChildAt(j)).getChildAt(0)).getChildAt(0).setBackgroundResource(R.drawable.ic_baseline_text_snippet_24);
                            } else if (j == 1) {
                                ((RelativeLayout) nav.getChildAt(j)).getChildAt(0).setBackgroundResource(R.drawable.circle_none);
                                ((RelativeLayout) ((RelativeLayout) nav.getChildAt(j)).getChildAt(0)).getChildAt(0).setBackgroundResource(R.drawable.ic_baseline_home_24);
                            } else if (j == 2) {
                                //   ((RelativeLayout)((RelativeLayout)nav.getChildAt(j)).getChildAt(0)).getChildAt(0).setBackgroundResource(R.drawable.ic_baseline_settings_24);
                            }
                        }
                        if (finalI == 0) {
                            ((RelativeLayout) nav.getChildAt(finalI)).getChildAt(0).setBackgroundResource(R.drawable.circle);
                            ((RelativeLayout) ((RelativeLayout) nav.getChildAt(finalI)).getChildAt(0)).getChildAt(0).setBackgroundResource(R.drawable.ic_baseline_text_snippet_24_black);
                            findViewById(R.id.page2).setVisibility(View.VISIBLE);
                        } else if (finalI == 1) {
                            ((RelativeLayout) nav.getChildAt(finalI)).getChildAt(0).setBackgroundResource(R.drawable.circle);
                            ((RelativeLayout) ((RelativeLayout) nav.getChildAt(finalI)).getChildAt(0)).getChildAt(0).setBackgroundResource(R.drawable.ic_baseline_home_24_black);
                            findViewById(R.id.page2).setVisibility(View.INVISIBLE);
                        } else if (finalI == 2) {
                            // ((RelativeLayout)((RelativeLayout)nav.getChildAt(finalI)).getChildAt(0)).getChildAt(0).setBackgroundResource(R.drawable.ic_baseline_settings_24_black);
                        }
                    }
                    else{

                        //open settings
                        OpenMenu();



                    }
                }
            });

        }
        findViewById(R.id.hidemen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideMenu();
            }
        });
        findViewById(R.id.menuhome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.menuhome).setBackgroundColor(Color.parseColor("#50FFFFFF"));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.menuhome).setBackgroundColor(Color.TRANSPARENT);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                HideMenu();
                            }
                        }, 100);
                    }
                }, 100);
            }
        });
        findViewById(R.id.rateapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.rateapp).setBackgroundColor(Color.parseColor("#50FFFFFF"));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.rateapp).setBackgroundColor(Color.TRANSPARENT);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Uri uri = Uri.parse("market://details?id=" + "application.vision.translation");
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                try {
                                    startActivity(goToMarket);
                                } catch (ActivityNotFoundException e) {
                                }
                            }
                        }, 100);
                    }
                }, 100);
            }
        });
        findViewById(R.id.managads).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.managads).setBackgroundColor(Color.parseColor("#50FFFFFF"));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.managads).setBackgroundColor(Color.TRANSPARENT);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getApplicationContext(), ManageAds.class));
                            }
                        }, 100);
                    }
                }, 100);
            }
        });
        findViewById(R.id.instructions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.instructions).setBackgroundColor(Color.parseColor("#50FFFFFF"));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.instructions).setBackgroundColor(Color.TRANSPARENT);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getApplicationContext(), Instructions.class));
                            }
                        }, 100);
                    }
                }, 100);
            }
        });
        findViewById(R.id.copyrights).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.copyrights).setBackgroundColor(Color.parseColor("#50FFFFFF"));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.copyrights).setBackgroundColor(Color.TRANSPARENT);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getApplicationContext(), Copyrights.class));
                            }
                        }, 100);
                    }
                }, 100);
            }
        });
        findViewById(R.id.shareapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.shareapp).setBackgroundColor(Color.parseColor("#50FFFFFF"));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.shareapp).setBackgroundColor(Color.TRANSPARENT);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                    shareIntent.setType("text/plain");
                                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "iTranslate Pro");
                                    String shareMessage= "\nLet me recommend you this application\n\n";
                                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + "application.vision.translation" +"\n\n";
                                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                                    startActivity(Intent.createChooser(shareIntent, "Choose one"));
                                } catch(Exception e) {
                                    //e.toString();
                                }
                            }
                        }, 100);
                    }
                }, 100);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(cs==2){
            HideMenu();
        }
        else if(cs==1){
            cs=0;
            findViewById(R.id.chooselangs).animate().translationY(p.y).setDuration(700);
        }
        else if(cs==0) {
            Intent setIntent = new Intent(Intent.ACTION_MAIN);
            setIntent.addCategory(Intent.CATEGORY_HOME);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent);
        }
    }

    void OpenMenu(){
        cs=2;
        findViewById(R.id.mainmenu).animate().translationX(0).setDuration(400);
        findViewById(R.id.mainparent).animate().scaleX(0.8f).setDuration(400);
        findViewById(R.id.mainparent).animate().scaleY(0.8f).setDuration(400);
        findViewById(R.id.mainparent).animate().translationX(p.x-250).setDuration(400);
    }
    void HideMenu(){
        cs=0;
        findViewById(R.id.mainparent).animate().scaleX(1).setDuration(400);
        findViewById(R.id.mainparent).animate().scaleY(1).setDuration(400);
        findViewById(R.id.mainmenu).animate().translationX(-p.x).setDuration(400);
        findViewById(R.id.mainparent).animate().translationX(0).setDuration(400);
    }
    LinearLayout nav;
    public void doProcess(View view) {
        //open the camera => create an Intent object
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 34);
    }
    private Uri imageUri;
    private Bitmap thumbnail;

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                ((EditText) findViewById(R.id.maintext)).setText(
                        Objects.requireNonNull(result).get(0));
                new Handler().postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        Tr();
                    }
                }, 1000);
            }
            return;
        }
          if (requestCode == 2 && resultCode == RESULT_OK
                && null != data) {

            final Uri uri = data.getData();
            try {
                InputImage image = InputImage.fromFilePath(MainActivity.this, uri);
                recognizeText(image);

            } catch (IOException e) {
                e.printStackTrace();
            }

    return;
        }
        if (requestCode == 33 && resultCode == RESULT_OK) {

            try {
                InputImage image = InputImage.fromFilePath(MainActivity.this, imageUri);
                recognizeText(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if (requestCode == 34 && resultCode == RESULT_OK) {

            final Uri uri = data.getData();
            try {
                InputImage image = InputImage.fromFilePath(MainActivity.this, uri);
                recognizeText(image);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
    }
    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException
    {
        BitmapFactory.Options o = new BitmapFactory.Options();

        o.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(getContentResolver()
                .openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 72;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;

        int scale = 1;

        while (true)
        {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
            {
                break;
            }
            width_tmp /= 2;

            height_tmp /= 2;

            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();

        o2.inSampleSize = scale;

        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                .openInputStream(selectedImage), null, o2);

        return bitmap;
    }
    @SuppressWarnings("deprecation")
    private String getPath(Uri selectedImaeUri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = managedQuery(selectedImaeUri, projection, null, null,
                null);

        if (cursor != null)
        {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            return cursor.getString(columnIndex);
        }

        return selectedImaeUri.getPath();
    }
    private  int getRotation(Context context, Uri imageUri) {
        int rotation;
            rotation = getRotationFromCamera(context, imageUri);
        return rotation;
    }

    private  int getRotationFromCamera(Context context, Uri imageFile) {
        int rotate = 0;
        try {

            context.getContentResolver().notifyChange(imageFile, null);
            ExifInterface exif = new ExifInterface(imageFile.getPath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }
    public void ChangeLn(String text, String mark){
        cs=0;
        if(!((TextView)findViewById(R.id.txt1)).getText().toString().equals("Choose Source Language")) {
            to = mark.trim();
            File.writeToFile(this, "target", text+"="+mark);
            ((TextView) findViewById(R.id.txt2)).setText(text);
        }
        else {
            if(!text.isEmpty()) {
                frommic=mark.trim();
                File.writeToFile(this, "default", text+"="+mark);
                ((TextView) findViewById(R.id.txt0)).setText(text);
                Toast.makeText(window, text + " is set for voice recognition using microphone!", Toast.LENGTH_SHORT).show();
            }
        }
        if(!text.isEmpty())
            findViewById(R.id.chooselangs).animate().translationY(p.y).setDuration(700);
        ((EditText)findViewById(R.id.searchlangs)).setText("");
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void Ref(String source, String T[], String T2[], String T3[]){
        if(T.length<=0)
        {
            findViewById(R.id.maintext).setEnabled(true);
            findViewById(R.id.maintext).setClickable(true);
            return;
        }
        findViewById(R.id.maintext).setEnabled(false);
        findViewById(R.id.maintext).setClickable(false);
        if( source.trim().split("\n").length>0){
            for(int i = T.length-1;i>=0;i--){
                if(T2.length-1>=i && T3.length-1>=i) {
                    if(!T[i].isEmpty() && !T2[i].isEmpty() && !T3[i].isEmpty()) {
                        TextRight tr = new TextRight(MainActivity.this, T2[i], T3[i].trim());
                        ((LinearLayout) findViewById(R.id.parr)).addView(tr, 0);
                        TextLeft tl = new TextLeft(MainActivity.this, T[i]);
                        ((LinearLayout) findViewById(R.id.parr)).addView(tl, 0);

                        int finalI = i;
                        tr.saveme.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                LoadAd();
                                String str = File.readFromFile(MainActivity.this, "SAVEorigine").trim()+"\n"+T[finalI];
                                File.writeToFile(MainActivity.this, "SAVEorigine", str);
                                String  str2 = File.readFromFile(MainActivity.this, "SAVEtranslated").trim()+"\n"+T2[finalI];
                                File.writeToFile(MainActivity.this, "SAVEtranslated", str2);
                                String  str3 = File.readFromFile(MainActivity.this, "SAVEto").trim()+"\n"+T3[finalI];
                                File.writeToFile(MainActivity.this, "SAVEto", str3);
                                Toast.makeText(MainActivity.this, "Saved!", Toast.LENGTH_SHORT).show();

                                tr.saveme.setEnabled(false);
                                tr.saveme.setClickable(false);
                                tr.saveme.animate().alpha(0.5f);

                                TextLeft tll = new TextLeft(MainActivity.this, T[finalI]);
                                ((LinearLayout) findViewById(R.id.parr2)).addView(tll);
                                TextRight trr = new TextRight(MainActivity.this, T2[finalI], T3[finalI]);
                                ((LinearLayout) findViewById(R.id.parr2)).addView(trr);

                                trr.HideEls();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        scrollBottom2();
                                    }
                                }, 100);

                            }
                        });


                    }
                }
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollBottom();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.maintext).setEnabled(true);
                            findViewById(R.id.maintext).setClickable(true);
                        }
                    }, 100);
                }
            }, 1000);
        }
    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},
                    234);
        }
    }
    void scrollBottom(){
        View lastChild = ((ScrollView)findViewById(R.id.scroll1)).getChildAt(((ScrollView)findViewById(R.id.scroll1)).getChildCount() - 1);
        int bottom = lastChild.getBottom() + ((ScrollView)findViewById(R.id.scroll1)).getPaddingBottom();
        int sy = ((ScrollView)findViewById(R.id.scroll1)).getScrollY();
        int sh = ((ScrollView)findViewById(R.id.scroll1)).getHeight();
        int delta = bottom - (sy + sh);

        ((ScrollView)findViewById(R.id.scroll1)).scrollBy(0, delta);

    }
    void scrollBottom2(){
        View lastChild = ((ScrollView)findViewById(R.id.scroll2)).getChildAt(((ScrollView)findViewById(R.id.scroll2)).getChildCount() - 1);
        int bottom = lastChild.getBottom() + ((ScrollView)findViewById(R.id.scroll2)).getPaddingBottom();
        int sy = ((ScrollView)findViewById(R.id.scroll2)).getScrollY();
        int sh = ((ScrollView)findViewById(R.id.scroll2)).getHeight();
        int delta = bottom - (sy + sh);

        ((ScrollView)findViewById(R.id.scroll2)).scrollBy(0, delta);

    }
    void Do(String text){
        if(started==0){
            started=1;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.splashscreen).animate().alpha(0).setDuration(400);

                }
            }, 100);
        }
            else translated=text.trim();
    }
    public class JavaScriptInterface {
        private Activity activity = MainActivity.this;

        public JavaScriptInterface(Activity activity) {
            this.activity = MainActivity.this;
        }

        @JavascriptInterface
        public void ReceiveText(String text){
            Do(text);
        }
        @JavascriptInterface
        public void Show(String text){
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
        }


    }
    private void callJavaScript(WebView view, String methodName, Object...params){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("javascript:try{");
        stringBuilder.append(methodName);
        stringBuilder.append("(");
        String separator = "";
        for (Object param : params) {
            stringBuilder.append(separator);
            separator = ",";
            if(param instanceof String){
                stringBuilder.append("'");
            }
            stringBuilder.append(param.toString().replace("'", "\\'"));
            if(param instanceof String){
                stringBuilder.append("'");
            }

        }
        stringBuilder.append(")}catch(error){console.error(error.message);}");
        final String call = stringBuilder.toString();


        view.loadUrl(call);
    }
    class CustomWebChromeClient extends WebChromeClient {
        private static final String TAG = "CustomWebChromeClient";

        @Override
        public boolean onConsoleMessage(ConsoleMessage cm) {
            Log.d(TAG, String.format("%s @ %d: %s", cm.message(),
                    cm.lineNumber(), cm.sourceId()));
            return true;
        }
    }
    int started=0;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Tr(){
        if(yesshow){
            File.writeToFile(this, "info","false");
            yesshow=false;
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Decrease amount of ads!")
                    .setMessage("Annoyed by the amount of ads? Proceed to Manage Ads page to decrease it ;)")
                    .setIcon(R.drawable.warning)
                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            startActivity(new Intent(getApplicationContext(), ManageAds.class));
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("no", null)
                    .show();
        }
        translated="";
        String txt = ((EditText)findViewById(R.id.maintext)).getText().toString().trim();
        if(txt.isEmpty()) return;
        LoadAd();
        ((EditText)findViewById(R.id.maintext)).setEnabled(false);
        ((EditText)findViewById(R.id.maintext)).setClickable(false);
        findViewById(R.id.mic).setEnabled(false);
        findViewById(R.id.mic).setClickable(false);
        findViewById(R.id.camera).setEnabled(false);
        findViewById(R.id.camera).setClickable(false);
        findViewById(R.id.gallery).setEnabled(false);
        findViewById(R.id.gallery).setClickable(false);
        findViewById(R.id.choosesource).setEnabled(false);
        findViewById(R.id.choosesource).setClickable(false);
        findViewById(R.id.choosetarget).setEnabled(false);
        findViewById(R.id.choosetarget).setClickable(false);
        findViewById(R.id.swipelangs).setEnabled(false);
        findViewById(R.id.swipelangs).setClickable(false);
        ((EditText)findViewById(R.id.maintext)).setText("");
        text = txt;
        TextLeft tl = new TextLeft(MainActivity.this, txt);
        ((LinearLayout)findViewById(R.id.parr)).addView(tl);
        TextRight tr = new TextRight(MainActivity.this, "", to);
        ((LinearLayout)findViewById(R.id.parr)).addView(tr);
        tr.setVisibility(View.GONE);
        web.loadUrl("file:///android_asset/translate/index.html?source="+from);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //tl.Do();
                scrollBottom();
                if(translated.isEmpty())
                    new Handler().postDelayed(this, 100);
                else{
                    String str = File.readFromFile(MainActivity.this, "origine").trim()+"\n"+text;
                    File.writeToFile(MainActivity.this, "origine", str);
                    String str2 = File.readFromFile(MainActivity.this, "translated").trim()+"\n"+translated;
                    File.writeToFile(MainActivity.this, "translated", str2);
                    String str3 = File.readFromFile(MainActivity.this, "to").trim()+"\n"+to;
                    File.writeToFile(MainActivity.this, "to", str3);
                    String trs=translated;
                    String tx=text;
                    tr.Text(translated);
                    String too = to;
                     tr.saveme.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {
                             LoadAd();
                            String str = File.readFromFile(MainActivity.this, "SAVEorigine").trim()+"\n"+tx;
                             File.writeToFile(MainActivity.this, "SAVEorigine", str);
                             String  str2 = File.readFromFile(MainActivity.this, "SAVEtranslated").trim()+"\n"+trs;
                             File.writeToFile(MainActivity.this, "SAVEtranslated", str2);
                             String  str3 = File.readFromFile(MainActivity.this, "SAVEto").trim()+"\n"+too;
                             File.writeToFile(MainActivity.this, "SAVEto", str3);
                             Toast.makeText(MainActivity.this, "Saved!", Toast.LENGTH_SHORT).show();

                             tr.saveme.setEnabled(false);
                             tr.saveme.setClickable(false);
                             tr.saveme.animate().alpha(0.5f);

                             TextLeft tll = new TextLeft(MainActivity.this, tx);
                             ((LinearLayout) findViewById(R.id.parr2)).addView(tll);
                             TextRight trr = new TextRight(MainActivity.this, trs, too);
                             ((LinearLayout) findViewById(R.id.parr2)).addView(trr);

                             trr.HideEls();
                             new Handler().postDelayed(new Runnable() {
                                 @Override
                                 public void run() {
                                     scrollBottom2();
                                 }
                             }, 1000);
                         }
                     });
                    translated="";
                    ((EditText)findViewById(R.id.maintext)).setEnabled(true);
                    ((EditText)findViewById(R.id.maintext)).setClickable(true);
                    findViewById(R.id.mic).setEnabled(true);
                    findViewById(R.id.mic).setClickable(true);
                    findViewById(R.id.camera).setEnabled(true);
                    findViewById(R.id.camera).setClickable(true);
                    findViewById(R.id.gallery).setEnabled(true);
                    findViewById(R.id.gallery).setClickable(true);
                    findViewById(R.id.choosesource).setEnabled(true);
                    findViewById(R.id.choosesource).setClickable(true);
                    findViewById(R.id.choosetarget).setEnabled(true);
                    findViewById(R.id.choosetarget).setClickable(true);
                    findViewById(R.id.swipelangs).setEnabled(true);
                    findViewById(R.id.swipelangs).setClickable(true);
                    tr.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //tr.Do();
                            scrollBottom();
                        }
                    }, 100);
                }
            }
        }, 100);
    }


    private void recognizeText(InputImage image) {
        startActivity(new Intent(getApplicationContext(), MainActivity2.class));
        // [START get_detector_default]
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        // [END get_detector_default]

        // [START run_detector]
        Task<Text> result =
                recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onSuccess(Text visionText) {
                                // Task completed successfully
                                // [START_EXCLUDE]
                                // [START get_text]
                                String str="";
                                for (Text.TextBlock block : visionText.getTextBlocks()) {
                                    Rect boundingBox = block.getBoundingBox();
                                    Point[] cornerPoints = block.getCornerPoints();
                                    String text = block.getText();
                                    str+=text+". ";
                                    for (Text.Line line: block.getLines()) {
                                        // ...
                                        for (Text.Element element: line.getElements()) {
                                            // ...
                                        }
                                    }
                                }
                                //((TextView)findViewById(R.id.maintext)).setText(str);
                                MainActivity2.str=str;
                                MainActivity2.window.Do();


                                // [END get_text]
                                // [END_EXCLUDE]
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                        MainActivity2.window.finish();
                                        Toast.makeText(MainActivity.this, "Failed! please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
        // [END run_detector]
    }
    private void processTextBlock(Text result) {
        // [START mlkit_process_text_block]
        String resultText = result.getText();
        for (Text.TextBlock block : result.getTextBlocks()) {
            String blockText = block.getText();
            Point[] blockCornerPoints = block.getCornerPoints();
            Rect blockFrame = block.getBoundingBox();
            for (Text.Line line : block.getLines()) {
                String lineText = line.getText();
                Point[] lineCornerPoints = line.getCornerPoints();
                Rect lineFrame = line.getBoundingBox();
                for (Text.Element element : line.getElements()) {
                    String elementText = element.getText();
                    Point[] elementCornerPoints = element.getCornerPoints();
                    Rect elementFrame = element.getBoundingBox();
                }
            }
        }
        // [END mlkit_process_text_block]
    }

    private TextRecognizer getTextRecognizer() {
        // [START mlkit_local_doc_recognizer]
        TextRecognizer detector = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        // [END mlkit_local_doc_recognizer]

        return detector;
    }
}