package com.cgtrc.bym.testapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgtrc.bym.testapplication.bean.DetailStructure;
import com.cgtrc.bym.testapplication.util.ChangePx;
import com.cgtrc.bym.testapplication.util.MakeTheUrl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BYM on 2016/2/4.
 */
public class articleDetailActivity extends Activity {

    private LinearLayout ll_content;
    private String href = "www.baidu.com";
    private final String content = "中国男子足球队主教练选聘报名于2016年1月29日截止。鉴于时间紧迫" +
            "为了全力备战完成中国男子足球队近期比赛任务(2018世界杯预选赛小组赛)，\n" +
            "        中国足协在征求选聘专家组意见和推荐建议后，经慎重研究，决定由高洪波作为2018世界杯预选赛小组赛中国男子足球队主教练，\n" +
            "        带领中国男足全力备战并完成3月24日与马尔代夫、3月29日与卡塔尔队的两场比赛任务。同时，考虑到中国足球的长远发展，\n" +
            "        中国足协将更为慎重专业的继续进行中国男子足球队主教练选聘工作。";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_layout);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);

        //得到intent传递过来的url
        href = getIntent().getStringExtra("href");
        Log.i("href", href);

        //异步加载数据


        initViewAndData(content,href);

    }

    private void initViewAndData(String content, String href) {

        int textSize = ChangePx.dip2px(this, 8);
        TextView tv = new TextView(this);

        //字体大小
        tv.setTextSize(textSize);
        //边距（dp值）
        int topMargin = ChangePx.dip2px(this, 10);
        LinearLayout.LayoutParams layoutParamsImageMain = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsImageMain.topMargin = topMargin;
        layoutParamsImageMain.bottomMargin = topMargin;
        layoutParamsImageMain.leftMargin = topMargin;
        layoutParamsImageMain.rightMargin = topMargin;
        layoutParamsImageMain.gravity= Gravity.CENTER_HORIZONTAL;
        tv.setText(content);
        ll_content.addView(tv,layoutParamsImageMain);

        //获取数据
        getDate(href);

    }

    private void getDate(String href){
        List<String> list = new ArrayList<>();
        MyAsyncTask myTask = (MyAsyncTask) new MyAsyncTask(list).execute(href);


//        System.out.println("list size" + list.size());
//        for(String s : list) {
//            System.out.println(s);
//        }
    }


    class MyAsyncTask extends AsyncTask<String,Integer,List<String>> {

        private List<String> elementList = new ArrayList<>();

        public MyAsyncTask(List<String> list){
            this.elementList = list;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<String> doInBackground(String... params) {
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
                            nodeName.equals("span") || nodeName.equals("p") || nodeName.equals("h4") ) {
                        elementList.add(e.text());
                        System.out.println( nodeName + "add success");
                        System.out.println(e.text());
                    } else if(nodeName.equals("img") ) {
                        elementList.add(e.attr("src"));
                        System.out.println( nodeName + "add success");
                        System.out.println(e.attr("src"));
                    } else if(nodeName.equals("a")) {
                        elementList.add(e.attr("href"));
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
        protected void onPostExecute(List<String> list) {
            super.onPostExecute(list);
            list = elementList;
            System.out.println("listsize：" + list.size());
            updateView();
        }
    }

    private void updateView() {

    }






}
