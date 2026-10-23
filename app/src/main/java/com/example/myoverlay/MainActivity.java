package com.example.myoverlay;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

public class MainActivity extends Activity {

    static {
        System.loadLibrary("native-lib");
    }

    private native String getMessage();

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(40, 40, 40, 40);
        layout.setBackgroundColor(Color.rgb(25, 25, 30));

        TextView title = new TextView(this);
        title.setText("GoldEg");
        title.setTextColor(Color.WHITE);
        title.setTextSize(30);

        TextView message = new TextView(this);
        message.setText(getMessage());
        message.setTextColor(Color.WHITE);
        message.setTextSize(20);

        Button button = new Button(this);
        button.setText("Нажми меня");

        button.setOnClickListener(v ->
            message.setText("C++ работает!")
        );

        layout.addView(title);
        layout.addView(message);
        layout.addView(button);

        setContentView(layout);
    }
}
