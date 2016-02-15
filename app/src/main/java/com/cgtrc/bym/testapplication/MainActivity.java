package com.cgtrc.bym.testapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * PTBS：
 * 解决JSON解析存在的一些小问题
 *  缓存新闻详情页的文字
 *   缓存新闻列表页的内容
 *    调试优化性能与布局
 *     页面布局设计及整体框架的搭建
 *
 */


public class MainActivity extends Activity implements View.OnClickListener{
    private Button bt_jump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_jump = (Button) findViewById(R.id.bt_jump);
        bt_jump.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_jump:
                Intent intent = new Intent(MainActivity.this,JsonActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }
}
