package com.example.myoverlay;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.os.*;
import android.provider.Settings;
import android.view.*;
import android.widget.*;
import java.util.*;

public class OverlayService extends Service {
    private WindowManager wm;
    private View panel, demo;
    private WindowManager.LayoutParams panelParams, demoParams;
    private TextView status;
    private boolean demoOn = false;

    @Override public void onCreate() {
        super.onCreate();
        startNotice();
        showPanel();
    }

    private void startNotice() {
        String id = "goldeg_overlay";
        NotificationManager nm =
            (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            nm.createNotificationChannel(new NotificationChannel(
                id, "GoldEg", NotificationManager.IMPORTANCE_LOW));
        }
        Notification.Builder b = Build.VERSION.SDK_INT >= 26
            ? new Notification.Builder(this, id)
            : new Notification.Builder(this);
        startForeground(42, b.setContentTitle("GoldEg")
            .setContentText("Плавающее меню активно")
            .setSmallIcon(android.R.drawable.ic_dialog_info).build());
    }

    private GradientDrawable bg(int color) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(18);
        d.setStroke(2, Color.rgb(255, 50, 50));
        return d;
    }

    private Button makeButton(String text) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setBackground(bg(Color.rgb(45, 30, 36)));
        return b;
    }

    private void showPanel() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            stopSelf();
            return;
        }

        wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(12, 8, 12, 8);
        box.setBackground(bg(Color.rgb(22, 22, 29)));

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);

        TextView title = new TextView(this);
        title.setText("  GoldEg  ");
        title.setTextColor(Color.rgb(255, 65, 65));
        title.setTextSize(21);
        header.addView(title, new LinearLayout.LayoutParams(0,
            -2, 1));

        Button close = makeButton("×");
        header.addView(close);
        box.addView(header);

        status = new TextView(this);
        status.setText("FLOAT MENU: ON\nESP: DEMO OFF");
        status.setTextColor(Color.WHITE);
        status.setPadding(4, 8, 4, 8);
        box.addView(status);

        Button inject = makeButton("INJECT / ОТКРЫТЬ ИГРУ");
        box.addView(inject);

        Button esp = makeButton("DEMO ESP: ON / OFF");
        box.addView(esp);

        Button hide = makeButton("СКРЫТЬ ДЕМО");
        box.addView(hide);

        Button exit = makeButton("ЗАКРЫТЬ МЕНЮ");
        box.addView(exit);

        int type = Build.VERSION.SDK_INT >= 26
            ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            : WindowManager.LayoutParams.TYPE_PHONE;

        panelParams = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT);
        panelParams.gravity = Gravity.TOP | Gravity.LEFT;
        panelParams.x = 25;
        panelParams.y = 120;

        header.setOnTouchListener(new View.OnTouchListener() {
            int sx, sy;
            float tx, ty;
            public boolean onTouch(View v, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    sx = panelParams.x;
                    sy = panelParams.y;
                    tx = e.getRawX();
                    ty = e.getRawY();
                    return true;
                }
                if (e.getAction() == MotionEvent.ACTION_MOVE) {
                    panelParams.x = sx + (int)(e.getRawX() - tx);
                    panelParams.y = sy + (int)(e.getRawY() - ty);
                    if (panel != null) wm.updateViewLayout(panel, panelParams);
                    return true;
                }
                return true;
            }
        });

        inject.setOnClickListener(v -> launchGame());
        esp.setOnClickListener(v -> toggleDemo());
        hide.setOnClickListener(v -> removeDemo());
        close.setOnClickListener(v -> stopSelf());
        exit.setOnClickListener(v -> stopSelf());

        panel = box;
        wm.addView(panel, panelParams);
    }

    private void launchGame() {
        try {
            Intent launcher = new Intent(Intent.ACTION_MAIN, null);
            launcher.addCategory(Intent.CATEGORY_LAUNCHER);
            PackageManager pm = getPackageManager();
            List<ResolveInfo> apps = pm.queryIntentActivities(launcher, 0);

            ResolveInfo match = null;
            for (ResolveInfo r : apps) {
                String label = String.valueOf(r.loadLabel(pm)).toLowerCase(Locale.ROOT);
                if (label.contains("oxide") ||
                    (label.contains("survival") && label.contains("island"))) {
                    match = r;
                    break;
                }
            }

            if (match == null) {
                status.setText("Игра не найдена автоматически.\nОткрой Oxide вручную.");
                return;
            }

            Intent open = new Intent(Intent.ACTION_MAIN);
            open.addCategory(Intent.CATEGORY_LAUNCHER);
            open.setClassName(match.activityInfo.packageName,
                              match.activityInfo.name);
            open.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(open);
            status.setText("Игра запускается.\nESP остаётся DEMO.");
        } catch (Exception e) {
            status.setText("Не удалось открыть игру.\nОткрой её вручную.");
        }
    }

    private void toggleDemo() {
        if (demoOn) {
            removeDemo();
            return;
        }

        demo = new View(this) {
            final Paint p = new Paint(3);
            @Override protected void onDraw(Canvas c) {
                super.onDraw(c);
                p.setColor(Color.RED);
                p.setStyle(Paint.Style.STROKE);
                p.setStrokeWidth(3);
                float w = getWidth(), h = getHeight();

                c.drawRect(w*.18f, h*.28f, w*.34f, h*.65f, p);
                c.drawRect(w*.62f, h*.24f, w*.79f, h*.61f, p);

                p.setStyle(Paint.Style.FILL);
                p.setTextSize(30);
                c.drawText("DEMO 1", w*.18f, h*.26f, p);
                c.drawText("DEMO 2", w*.62f, h*.22f, p);

                p.setStyle(Paint.Style.STROKE);
                p.setStrokeWidth(3);
                float x = w/2, y = h/2;
                c.drawLine(x-24, y, x+24, y, p);
                c.drawLine(x, y-24, x, y+24, p);
                c.drawCircle(x, y, 10, p);
            }
        };

        int type = Build.VERSION.SDK_INT >= 26
            ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            : WindowManager.LayoutParams.TYPE_PHONE;

        demoParams = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT);
        demoParams.gravity = Gravity.TOP | Gravity.LEFT;

        try {
            wm.addView(demo, demoParams);
            demoOn = true;
            status.setText("ESP: DEMO ON\nРамки нарисованы для теста.");
        } catch (Exception e) {
            demo = null;
            status.setText("Не удалось показать демо.\nПроверь разрешение поверх окон.");
        }
    }

    private void removeDemo() {
        if (wm != null && demo != null) {
            try { wm.removeView(demo); } catch (Exception ignored) {}
        }
        demo = null;
        demoOn = false;
        if (status != null) status.setText("FLOAT MENU: ON\nESP: DEMO OFF");
    }

    @Override public void onDestroy() {
        removeDemo();
        if (wm != null && panel != null) {
            try { wm.removeView(panel); } catch (Exception ignored) {}
        }
        panel = null;
        stopForeground(true);
        super.onDestroy();
    }

    @Override public IBinder onBind(Intent intent) { return null; }
}
