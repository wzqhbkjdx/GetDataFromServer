package com.cgtrc.bym.testapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgtrc.bym.testapplication.bean.DetailStructure;
import com.cgtrc.bym.testapplication.imageloader.ImageLoader;
import com.cgtrc.bym.testapplication.util.ChangePx;
import com.cgtrc.bym.testapplication.util.MakeTheUrl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by BYM on 2016/2/4.
 */
public class articleDetailActivity extends Activity {

    private LinearLayout ll_content;
    private String href = "www.baidu.com";
    private ImageLoader mImageLoader;
    private static final int ASYNC_COMPLETE = 1;
    private List<Element> list = new ArrayList<>();
    private boolean flag = false;
    private TextView title;
    private TextView tv_original;
    private TextView tv_date;

    final Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            Log.i("handler","接收到消息，正在处理");

            if(msg.what == ASYNC_COMPLETE) {
                list = (ArrayList)msg.obj;
                Log.i("handler","处理中");
                Log.i("handler","处理后list的大小" + list.size());
                if(list.size() != 0){
                    System.out.println("主线程中得到的list的大小：" + list.size());
                    for(Element s : list){
                        System.out.println(s);
                    }
                }
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_layout);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        title = (TextView) findViewById(R.id.tv_title);
        tv_original = (TextView) findViewById(R.id.tv_original);
        tv_date = (TextView) findViewById(R.id.tv_date);

        mImageLoader = new ImageLoader(this);
        mImageLoader.setRequiredSize(5 * (int) getResources().getDimension(R.dimen.litpic_width));

        //得到intent传递过来的url
        href = getIntent().getStringExtra("href");
        Log.i("href", href);

        //异步加载
        initViewAndData(href);

    }

    private void initViewAndData(String href) {

        new MyAsyncTask().execute(href);


    }



    class MyAsyncTask extends AsyncTask<String,Integer,List<Element>> {

        private List<Element> elementList = new ArrayList<>();



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Element> doInBackground(String... params) {
            String path = params[0];

            try {
                //Jsoup解析
                Document doc = Jsoup.connect(path).timeout(10000).get();
                //Log.i("activityofDetail", "" + doc);
                //遍历得到的每一个元素
                Elements elements = doc.getAllElements();
                for(Element e : elements){
                    String nodeName = e.nodeName();
                    //System.out.println(nodeName);
                    if(nodeName.equals("title") || nodeName.equals("h3") || nodeName.equals("h2") ||
                            nodeName.equals("span") || nodeName.equals("p") ) {
                        elementList.add(e);
                        System.out.println( nodeName + "add success");
                        System.out.println(e.text());

                    } else if(nodeName.equals("img") ) {
                        elementList.add(e);
                        System.out.println( nodeName + "add success");
                        System.out.println(e.attr("src"));
                    } else if(nodeName.equals("a")) {
                        elementList.add(e);
                        System.out.println( nodeName + "add success");
                        System.out.println(e.attr("href"));
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            return elementList;
        }

        @Override
        protected void onPostExecute(List<Element> list) {
            super.onPostExecute(list);
            System.out.println("异步线程结束listsize：" + list.size());
            Message msg = Message.obtain();
            msg.what = ASYNC_COMPLETE;
            msg.obj = list;
            boolean success = handler.sendMessage(msg);
            if(success){
                Log.i("handler","handler成功发送消息");
            }

            for(Element s : list){
                updateView(s);
            }


        }

    }


    private void updateView(Element s) {

        int topMargin = ChangePx.dip2px(this, 10);
        LinearLayout.LayoutParams layoutParamsTextMain = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsTextMain.topMargin = topMargin;
        layoutParamsTextMain.bottomMargin = topMargin;
        layoutParamsTextMain.leftMargin = topMargin;
        layoutParamsTextMain.rightMargin = topMargin;
        layoutParamsTextMain.gravity = Gravity.LEFT;


        if (s.nodeName().equals("title")) { //加载新闻的标题
            title.setText(s.text());
        } else if (s.nodeName().equals("h3")) { //加载新闻来源
            tv_original.setText(s.text());
        } else if (s.nodeName().equals("span")) {//加载时间
            tv_date.setText(s.text());
        } else if (s.nodeName().equals("h2")) { //加载二级标题
            int textSize = ChangePx.dip2px(this, 10);
            TextView tv = new TextView(this);
            tv.setTextSize(textSize);
            tv.setText(s.text());
            ll_content.addView(tv, layoutParamsTextMain);
        } else if (s.nodeName().equals("p")) {//加载内容
            int textSize = ChangePx.dip2px(this, 8);
            TextView tv = new TextView(this);
            tv.setTextSize(textSize);
            tv.setText(s.text());
            ll_content.addView(tv, layoutParamsTextMain);
        } else if (s.nodeName().equals("img")) {//加载图片
            ImageView iv = new ImageView(this);
            mImageLoader.DisplayImage(s.attr("src"),iv);
            ll_content.addView(iv, layoutParamsTextMain);
        }  else if (s.nodeName().equals("a")) {//加载原文链接

        }






    }

}
