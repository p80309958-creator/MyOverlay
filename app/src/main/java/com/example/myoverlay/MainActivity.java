package com.example.myoverlay;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends Activity {
    private final int gold = Color.rgb(255, 190, 55);
    private final int bg = Color.rgb(17, 17, 22);
    private final int card = Color.rgb(29, 29, 37);
    private LinearLayout panel;
    private android.content.SharedPreferences prefs;

    private GradientDrawable shape(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        return d;
    }

    private TextView text(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setPadding(16, 12, 16, 12);
        return t;
    }

    private void section(String value) {
        TextView t = text(value, 14, gold);
        t.setTypeface(null, Typeface.BOLD);
        panel.addView(t);
    }

    private void toggle(String key, String label, boolean defaultValue) {
        Switch s = new Switch(this);
        s.setText(label);
        s.setTextColor(Color.WHITE);
        s.setTextSize(15);
        s.setPadding(16, 12, 16, 12);
        s.setChecked(prefs.getBoolean(key, defaultValue));
        s.setOnCheckedChangeListener((button, checked) ->
                prefs.edit().putBoolean(key, checked).apply());
        panel.addView(s);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);
        getWindow().getDecorView().setSystemUiVisibility(0);
        prefs = getSharedPreferences("goldeg_settings", MODE_PRIVATE);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(bg);

        panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(18, 24, 18, 28);
        scroll.addView(panel);

        TextView title = text("GoldEg", 32, gold);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, Typeface.BOLD);
        panel.addView(title);

        TextView subtitle = text("CONTROL CENTER  •  v2.0", 12, Color.GRAY);
        subtitle.setGravity(Gravity.CENTER);
        panel.addView(subtitle);

        TextView enemies = text("ENEMIES: 0", 18, gold);
        enemies.setGravity(Gravity.CENTER);
        enemies.setTypeface(null, Typeface.BOLD);
        enemies.setBackground(shape(card, 16));
        LinearLayout.LayoutParams ep = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        ep.setMargins(0, 18, 0, 10);
        panel.addView(enemies, ep);

        section("СТАТУС");
        TextView status = text("●  GoldEg запущен", 15,
                Color.rgb(100, 220, 130));
        status.setBackground(shape(card, 14));
        panel.addView(status);

        Button inject = new Button(this);
        inject.setText("INJECT");
        inject.setTextSize(17);
        inject.setTextColor(Color.BLACK);
        inject.setAllCaps(false);
        inject.setTypeface(null, Typeface.BOLD);
        inject.setBackground(shape(gold, 18));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        bp.setMargins(0, 18, 0, 6);
        panel.addView(inject, bp);

        TextView result = text("Ожидание действия", 13, Color.LTGRAY);
        result.setGravity(Gravity.CENTER);
        panel.addView(result);

        inject.setOnClickListener(v ->
                result.setText("Интерфейс готов. Подключение к игре не реализовано."));

        section("ИНТЕРФЕЙС");
        toggle("gold_theme", "Золотая тема", true);
        toggle("compact", "Компактный режим", false);
        toggle("notifications", "Показывать уведомления", true);

        section("НАСТРОЙКИ");
        toggle("vibration", "Вибрация кнопок", false);
        toggle("save_settings", "Сохранять настройки", true);

        TextView footer = text(
                "GoldEg v2.0\nНастройки интерфейса сохраняются автоматически.\nENEMIES — демонстрационная надпись.",
                12, Color.GRAY);
        panel.addView(footer);

        setContentView(scroll);
    }
}
