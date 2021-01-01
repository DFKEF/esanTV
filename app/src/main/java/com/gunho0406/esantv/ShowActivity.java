package com.gunho0406.esantv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ShowActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    int currentPage = 0;
    Timer timer;
    int pos=0;
    TextView titletxtview, nametxt, datetxt, subtext, liketxt;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 8000; // time in milliseconds between successive task executions.
    TextView dot[];
    ArrayList<Item> list = new ArrayList<>();
    String url = "http://13.209.232.72/";
    URLConnector task;
    String userID;
    public final String PREFERENCE = "userinfo";
    String user,bitmap,title,date,verify,subject, content, getid, date_row, date_start, teacher;
    int imgnum,like_num;
    ArrayList<String> teacherlist = new ArrayList<>();
    ArrayList<String> subjectlist = new ArrayList<>();
    ImageView profileImg;
    String result;

    ArrayList<String> bitmaplist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        Date dt = Calendar.getInstance().getTime();
        date_row = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(dt);
        Log.e("date",date_row);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH,-1);
        Date da_st = cal.getTime();
        date_start = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(da_st);
        Log.e("start",date_start);


        startTeachers();

        /*php php = new php(date_row,date_start);
        php.execute();*/
        startTask();

        subtext = findViewById(R.id.sub);
        titletxtview = findViewById(R.id.title_txt);
        profileImg = (ImageView) findViewById(R.id.profile);
        datetxt = (TextView) findViewById(R.id.date);
        nametxt = (TextView) findViewById(R.id.name);
        liketxt = (TextView) findViewById(R.id.liketxt);

        init(pos);


        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == list.get(pos).imgnum) {
                    currentPage = 0;
                    pos = pos+1;
                    if(pos==list.size()) {
                        pos = 0;
                    }
                    init(pos);

                }
                addDot(currentPage);
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

    private void Parse(String result) throws JSONException {
        JSONObject root = new JSONObject(result);

        JSONArray ja = root.getJSONArray("result");
        String su;

        for(int i = 0; i < ja.length();i++)
        {
            JSONObject jo = ja.getJSONObject(i);
            user = jo.getString("user");
            bitmap = url+"cards/"+jo.getString("bitmap");
            title = jo.getString("title");
            date = jo.getString("date");
            verify = jo.getString("verify");
            su = jo.getString("subject");
            content = jo.getString("content");
            imgnum = jo.getInt("imgnum");
            getid = jo.getString("userID");
            like_num = jo.getInt("like_count");
            teacher = jo.getString("teacher");
            subject = su+"/"+teacher;
            if(verify.equals("Y")){
                list.add(new Item(user,bitmap,title,date,subject,content,imgnum,getid,like_num));
            }
        }

    }

    public void init(int pos) {
        String bit = list.get(pos).bitmap;
        viewPager2 = findViewById(R.id.viewPager2);
        bitmaplist.clear();
        for (int j=1; j<=list.get(pos).imgnum; j++) {
            String row = bit.replace("_1.jpg","_"+j+".jpg");
            bitmaplist.add(row);
        }
        viewPager2.setAdapter(new ViewPagerAdapter(this,bitmaplist));
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        titletxtview.setText(list.get(pos).title);
        profileImg.setBackground(new ShapeDrawable(new OvalShape()));
        profileImg.setClipToOutline(true);
        Glide.with(this)
                .load(url+"profiles/"+list.get(pos).getid+"_profile.jpg").placeholder(R.drawable.ic_baseline_account_circle_24)
                .centerCrop()
                .override(400,400)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(profileImg);
        nametxt.setText(list.get(pos).user);
        String hello = list.get(pos).date.replace("-","/");
        datetxt.setText("Posted. "+hello);
        subtext.setText(list.get(pos).subject);
        liketxt.setText(String.valueOf(list.get(pos).like_count));
    }

    private void startTask(){
        task = new URLConnector(url+"parse_like.php");
        task.start();
        try{
            task.join();
        }
        catch(InterruptedException e){

        }
        String result = task.getResult();

        try {
            Parse(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addDot(int page_position) {
        LinearLayout layout_dot = (LinearLayout) findViewById(R.id.layout_dot);

        dot = new TextView[bitmaplist.size()];
        layout_dot.removeAllViews();

        for (int i = 0; i < dot.length; i++) {
            dot[i] = new TextView(this);
            dot[i].setText(Html.fromHtml("&#9679;"));
            dot[i].setTextSize(35);
            //set default dot color
            dot[i].setTextColor(Color.parseColor("#757575"));
            layout_dot.addView(dot[i]);
        }
        //set active dot color
        dot[page_position].setTextColor(Color.parseColor("#FAFAFA"));
    }

    private void startTeachers(){
        task = new URLConnector(url+"teacher.php");
        task.start();
        try{
            task.join();
        }
        catch(InterruptedException e){

        }
        String result = task.getResult();

        try {
            Teachers(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Teachers(String result) throws JSONException {
        JSONObject root = new JSONObject(result);

        JSONArray ja = root.getJSONArray("result");

        for(int i = 0; i < ja.length();i++)
        {
            String ver;
            JSONObject jo = ja.getJSONObject(i);
            ver = jo.getString("verify");
            if(ver.equals("Y")){
                teacherlist.add(jo.getString("name"));
                subjectlist.add(jo.getString("subject"));
            }
        }
    }

    /*public class php extends AsyncTask<Void, Integer, String> {

        String data = "";
        String date_row, date_start;
        int imgnum;
        int finishcode = 0;

        public php(String date_row, String date_start) {
            this.date_row = date_row;
            this.date_start = date_start;
        }
        @Override
        protected String doInBackground(Void... unused) {
            //인풋 파라메터값 생성

            String param = "start=" + date_start + "&end=" + date_row;
            try {
                // 서버연결
                URL home = new URL(
                        url+"parse_like.php");
                HttpURLConnection conn = (HttpURLConnection) home.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                // 안드로이드 -> 서버 파라메터값 전달
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                // 서버 -> 안드로이드 파라메터값 전달
                InputStream is = null;
                BufferedReader in = null;
                String data = "";

                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();
                Log.e("RECV DATA",data);



            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("data",data);
            return data;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            try {
                Parse(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }*/
}