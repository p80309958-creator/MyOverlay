package com.example.myoverlay;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.ScrollView;
import android.widget.Button;

public class MainActivity extends Activity {
    int gold = Color.rgb(255, 190, 55);
    int bg = Color.rgb(17, 17, 22);
    LinearLayout panel;

    GradientDrawable shape(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        return d;
    }

    TextView text(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setPadding(18, 14, 18, 14);
        return t;
    }

    void section(String name) {
        TextView t = text(name, 17, gold);
        t.setTypeface(null, Typeface.BOLD);
        panel.addView(t);
    }

    void toggle(String name, boolean enabled) {
        Switch s = new Switch(this);
        s.setText(name);
        s.setTextColor(Color.WHITE);
        s.setTextSize(15);
        s.setChecked(enabled);
        s.setPadding(18, 8, 18, 8);
        s.setButtonTintList(null);
        panel.addView(s);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(bg);
        panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(18, 22, 18, 22);
        scroll.addView(panel);

        TextView title = text("GoldEg", 30, gold);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, Typeface.BOLD);
        panel.addView(title);

        TextView subtitle = text("CONTROL CENTER  •  v1.1", 12, Color.GRAY);
        subtitle.setGravity(Gravity.CENTER);
        panel.addView(subtitle);

        TextView enemies = text("ENEMIES: 0", 16, gold);
        enemies.setGravity(Gravity.CENTER);
        enemies.setTypeface(null, Typeface.BOLD);
        panel.addView(enemies);

        section("STATUS");
        TextView status = text("●  Приложение запущено", 15,
                Color.rgb(100, 220, 130));
        panel.addView(status);

        Button inject = new Button(this);
        inject.setText("INJECT");
        inject.setTextSize(18);
        inject.setTextColor(Color.BLACK);
        inject.setAllCaps(true);
        inject.setBackground(shape(gold, 18));
        LinearLayout.LayoutParams injectParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        injectParams.setMargins(18, 20, 18, 20);
        panel.addView(inject, injectParams);

        TextView injectStatus = text("Ожидание действия", 14, Color.GRAY);
        injectStatus.setGravity(Gravity.CENTER);
        panel.addView(injectStatus);

        inject.setOnClickListener(v ->
                injectStatus.setText("GoldEg запущен — игровая функция не подключена"));

        section("ИНТЕРФЕЙС");
        toggle("Золотая тема", true);
        toggle("Компактный режим", false);
        toggle("Показывать уведомления", true);

        section("НАСТРОЙКИ");
        toggle("Вибрация кнопок", false);
        toggle("Сохранять настройки", true);

        TextView info = text(
                "GoldEg — тестовое меню Android.\n" +
                "Переключатели пока меняют только состояние интерфейса.",
                13, Color.LTGRAY);
        panel.addView(info);

        setContentView(scroll);
    }
}
