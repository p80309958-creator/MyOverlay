package com.example.myoverlay;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Gravity;
import android.graphics.Color;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(28, 28, 28, 28);
        root.setBackgroundColor(Color.rgb(18, 20, 28));

        TextView title = new TextView(this);
        title.setText("GoldEg");
        title.setTextColor(Color.rgb(255, 190, 40));
        title.setTextSize(30);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        TextView info = new TextView(this);
        info.setText("Плавающее меню поверх приложений");
        info.setTextColor(Color.WHITE);
        info.setTextSize(16);
        info.setGravity(Gravity.CENTER);
        root.addView(info);

        Button start = new Button(this);
        start.setText("ВКЛЮЧИТЬ ПЛАВАЮЩЕЕ МЕНЮ");
        root.addView(start);

        start.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23 &&
                    !Settings.canDrawOverlays(this)) {
                Intent permission = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())
                );
                startActivity(permission);
                return;
            }

            Intent service = new Intent(this, OverlayService.class);
            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(service);
            } else {
                startService(service);
            }
            info.setText("Меню запущено. Можно вернуться в игру.");
        });

        setContentView(root);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
