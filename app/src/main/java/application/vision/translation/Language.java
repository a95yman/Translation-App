package application.vision.translation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Language extends RelativeLayout {
    LayoutInflater inflater;
    public Language(Context context, String text, String mark) {
        super(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.language, this, true);
        ((TextView)findViewById(R.id.txt)).setText(text);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity.window.ChangeLn(text, mark);
            }
        });
    }
}
