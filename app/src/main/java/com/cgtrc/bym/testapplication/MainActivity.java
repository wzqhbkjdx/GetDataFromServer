package com.cgtrc.bym.testapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * PTBS：
 * //解决JSON解析存在的一些小问题:从服务器返回list
 * //listview加载图片的方法 防止oom 使用Volley库中的ImageLoader
 * 缓存新闻详情页的文字 数据持久化
 * 缓存新闻列表页的内容 数据持久化
 * 应用程序架构
 * 调试优化性能与布局 学习现有的布局和源码
 * 页面布局设计及整体框架的搭建
 * 依赖注入有较好的性能的原因 看看这些开源库Dagger2
 *
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
