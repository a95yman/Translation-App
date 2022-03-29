package application.vision.translation;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TextLeft extends RelativeLayout {
    LayoutInflater inflater;
    public TextLeft(Context context, String text) {
        super(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.textleft, this, true);
        ((TextView)findViewById(R.id.txt)).setText(text);
    }
    public void Do(){
        findViewById(R.id.parr).animate().alpha(1).setDuration(500);
        //findViewById(R.id.parr).animate().translationY(0).setDuration(500);
    }
}
