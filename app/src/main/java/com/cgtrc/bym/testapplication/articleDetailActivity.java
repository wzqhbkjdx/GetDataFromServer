package com.cgtrc.bym.testapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgtrc.bym.testapplication.util.ChangePx;

/**
 * Created by BYM on 2016/2/4.
 */
public class articleDetailActivity extends Activity {
    private LinearLayout ll_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_layout);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);

        String href = getIntent().getStringExtra("href");
        Log.i("href", href);

        String content = "中国男子足球队主教练选聘报名于2016年1月29日截止。鉴于时间紧迫" +
                "为了全力备战完成中国男子足球队近期比赛任务(2018世界杯预选赛小组赛)，\n" +
                "        中国足协在征求选聘专家组意见和推荐建议后，经慎重研究，决定由高洪波作为2018世界杯预选赛小组赛中国男子足球队主教练，\n" +
                "        带领中国男足全力备战并完成3月24日与马尔代夫、3月29日与卡塔尔队的两场比赛任务。同时，考虑到中国足球的长远发展，\n" +
                "        中国足协将更为慎重专业的继续进行中国男子足球队主教练选聘工作。";


        initViewAndData(content);

    }

    private void initViewAndData(String href) {

        int textSize = ChangePx.dip2px(this, 8);
        TextView tv = new TextView(this);


        tv.setTextSize(textSize);

        //上边距（dp值）
        int topMargin = ChangePx.dip2px(this, 10);


        LinearLayout.LayoutParams layoutParamsImageMain = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParamsImageMain.topMargin = topMargin;
        layoutParamsImageMain.bottomMargin = topMargin;
        layoutParamsImageMain.leftMargin = topMargin;
        layoutParamsImageMain.rightMargin = topMargin;
        layoutParamsImageMain.gravity= Gravity.CENTER_HORIZONTAL;




        tv.setText(href);
        ll_content.addView(tv,layoutParamsImageMain);


    }


}
