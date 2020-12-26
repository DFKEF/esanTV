package com.gunho0406.esantv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ShowActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    int currentPage = 0;
    Timer timer;
    TextView title;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        viewPager2 = findViewById(R.id.viewPager2);
        title = findViewById(R.id.title_txt);

        final ArrayList<DataPage> list = new ArrayList<>();
        list.add(new DataPage("https://pgnqdrjultom1827145.cdn.ntruss.com/img/08/3c/083cce5bfb61e10946ce40f740da33bc67a79fecf5a007bcd8cb09dda67d076b_v1.jpg"));
        list.add(new DataPage("https://pgnqdrjultom1827145.cdn.ntruss.com/img/28/49/2849f8b4eeb33525ea06d2639531a8ae194e115cb9484e003cdebadccb91a72a_v1.jpg"));
        list.add(new DataPage("https://pgnqdrjultom1827145.cdn.ntruss.com/img/19/98/1998749fd08bfab7e9b73c48812b7e935d193811d1c5f8d6be3304884dacf640_v1.jpg"));

        viewPager2.setAdapter(new ViewPagerAdapter(list));
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == list.size()) {
                    currentPage = 0;
                    title.setText("올해도 솔로 크리스마스");
                }
                viewPager2.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }
}