package com.example.myoverlay;

import android.app.Service;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OverlayService extends Service {
    private WindowManager windowManager;
    private View panel;
    private WindowManager.LayoutParams params;

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundNotice();
        showPanel();
    }

    private void startForegroundNotice() {
        String channel = "goldeg_overlay";
        NotificationManager nm =
            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel nc = new NotificationChannel(
                channel, "GoldEg floating menu",
                NotificationManager.IMPORTANCE_LOW
            );
            nm.createNotificationChannel(nc);
        }

        Notification.Builder builder = Build.VERSION.SDK_INT >= 26
            ? new Notification.Builder(this, channel)
            : new Notification.Builder(this);

        Notification notification = builder
            .setContentTitle("GoldEg")
            .setContentText("Плавающее меню активно")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build();

        startForeground(42, notification);
    }

    private void showPanel() {
        if (Build.VERSION.SDK_INT >= 23 &&
                !android.provider.Settings.canDrawOverlays(this)) {
            stopSelf();
            return;
        }

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(16, 12, 16, 12);
        box.setBackgroundColor(Color.rgb(25, 28, 38));

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);

        TextView title = new TextView(this);
        title.setText("  GoldEg  ");
        title.setTextColor(Color.rgb(255, 190, 40));
        title.setTextSize(20);
        header.addView(title);

        Button close = new Button(this);
        close.setText("×");
        header.addView(close);
        box.addView(header);

        TextView status = new TextView(this);
        status.setText("FLOAT MENU: ON\nGame data: not connected");
        status.setTextColor(Color.WHITE);
        status.setPadding(4, 8, 4, 8);
        box.addView(status);

        Button hide = new Button(this);
        hide.setText("Скрыть меню");
        box.addView(hide);

        int type = Build.VERSION.SDK_INT >= 26
            ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            : WindowManager.LayoutParams.TYPE_PHONE;

        params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 30;
        params.y = 120;

        header.setOnTouchListener(new View.OnTouchListener() {
            private int startX, startY;
            private float touchX, touchY;

            @Override
            public boolean onTouch(View v, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    startX = params.x;
                    startY = params.y;
                    touchX = e.getRawX();
                    touchY = e.getRawY();
                    return true;
                }
                if (e.getAction() == MotionEvent.ACTION_MOVE) {
                    params.x = startX + (int)(e.getRawX() - touchX);
                    params.y = startY + (int)(e.getRawY() - touchY);
                    if (panel != null) {
                        windowManager.updateViewLayout(panel, params);
                    }
                    return true;
                }
                return true;
            }
        });

        close.setOnClickListener(v -> stopSelf());
        hide.setOnClickListener(v -> stopSelf());

        panel = box;
        windowManager.addView(panel, params);
    }

    @Override
    public void onDestroy() {
        if (windowManager != null && panel != null) {
            try {
                windowManager.removeView(panel);
            } catch (Exception ignored) {}
        }
        panel = null;
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
