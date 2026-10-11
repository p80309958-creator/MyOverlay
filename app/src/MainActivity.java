package com.example.myoverlay;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Switch;

public class MainActivity extends Activity {
    private LinearLayout layout;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);
        layout.setBackgroundColor(Color.rgb(28, 32, 40));

        TextView title = new TextView(this);
        title.setText("MY OVERLAY MENU");
        title.setTextColor(Color.WHITE);
        title.setTextSize(24);
        layout.addView(title);

        addSwitch("Show test boxes");
        addSwitch("Show test names");
        addSwitch("Show test radar");

        setContentView(layout);
    }

    private void addSwitch(String label) {
        Switch control = new Switch(this);
        control.setText(label);
        control.setTextColor(Color.WHITE);
        control.setTextSize(18);
        control.setPadding(0, 20, 0, 20);
        layout.addView(control, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ));
    }
}
