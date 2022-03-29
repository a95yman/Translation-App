package application.vision.translation;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.Locale;

public class TextRight extends RelativeLayout {
    LayoutInflater inflater;
    public void HideEls(){
        saveme.setVisibility(GONE);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TextRight(Context context, String text, String to) {
        super(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.textright, this, true);
        saveme = findViewById(R.id.saveme);
        this.text=text;
        ((TextView)findViewById(R.id.txt)).setText(text);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.forLanguageTag(to.trim()));
                    findViewById(R.id.speak).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MainActivity.window.LoadAd();
                            tts.speak(TextRight.this.text, TextToSpeech.QUEUE_ADD, null);
                        }
                    });
                }

            }
        });
        findViewById(R.id.copy).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setClipboard(context, TextRight.this.text);
                Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }
    public RelativeLayout saveme;
    String text;
    TextToSpeech tts;
    public void Text(String text){
        this.text=text;
        ((TextView)findViewById(R.id.txt)).setText(text);
    }
    public void Do(){
        findViewById(R.id.parr).animate().alpha(1).setDuration(500);
    }
}
