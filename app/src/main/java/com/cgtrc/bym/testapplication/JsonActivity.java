package com.cgtrc.bym.testapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cgtrc.bym.testapplication.imageloader.ImageLoader;
import com.cgtrc.bym.testapplication.util.FastJsonTools;
import com.cgtrc.bym.testapplication.util.HttpUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by BYM on 2016/2/15.
 */
public class JsonActivity extends Activity {

    private ArrayList<Article> articlesList; //新闻容器
    private ImageLoader imageLoader;//异步图片加载器
    private NewsListAdapter adapter;
    private PullToRefreshListView ptrListView;
    private static final String URL = "http://192.168.228.135:8080/test/NewsServlet?action_flag=ArticalList";

    static class ViewHolder {
        public TextView title;
        public TextView summary;
        public ImageView image;
        public TextView postTime;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json_layout);

        initView();


    }

    private void initView() {
        articlesList = new ArrayList<>();

        imageLoader = new ImageLoader(this);
        imageLoader.setRequiredSize(20 * (int)getResources().getDimension(R.dimen.litpic_width));

        adapter = new NewsListAdapter();

        ptrListView = (PullToRefreshListView) findViewById(R.id.mylistview);
        ptrListView.setAdapter(adapter);

        ptrListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0 && position <= articlesList.size()){
                    Article article = articlesList.get(position-1);
                    String urll = article.getUrl();
                    Log.i("urll", urll);

                    Intent intent = new Intent(JsonActivity.this,articleDetailActivity.class);
                    intent.putExtra("href",urll);
                    startActivity(intent);
                }
            }
        });

        ptrListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                System.out.println("JsonActivity load url");
                loadNewsList(URL,1,true);
            }

            @Override
            public void onLoadingMore() {
                int pageIndex = articlesList.size() / 10 + 1;
                Log.i("pageIndex", "pageIndex = " + pageIndex);
                loadNewsList(URL,pageIndex,false);
            }
        });

        loadNewsList(URL,1,true);

    }

    private void loadNewsList(final String url, final int i, final boolean isRefresh) {

        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if(msg.what == 1){
                    ArrayList<Article> articalList = (ArrayList<Article>) msg.obj;
                    if(isRefresh) {
                        articlesList.clear();
                        ptrListView.onRefreshComplete(new Date().toLocaleString());
                    }
                    for(Article article : articalList){
                        articlesList.add(article);
                    }
                    adapter.notifyDataSetChanged();
                    if (articlesList.size() < 10) {
                        ptrListView.onLoadingMoreComplete(true);
                    } else if (articlesList.size() == 10) {
                        ptrListView.onLoadingMoreComplete(false);
                    }
                } else if(msg.what == -1){
                    Toast.makeText(JsonActivity.this,"json解析发生错误！",Toast.LENGTH_LONG).show();
                }
            }
        };
        new Thread(){
            public void run(){
                Message msg = Message.obtain();
                //Article artical = new Article();
                List<Article> aList  = new ArrayList<Article>();
                try{
                    String jsonString = HttpUtils.getJsonContent(url,"utf-8");
                    aList = FastJsonTools.getArticals(jsonString,Article.class);

                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                    msg.obj = e;
                }
                msg.what = 1;
                msg.obj = aList;
                handler.sendMessage(msg);


            }
        }.start();


    }



    class NewsListAdapter extends BaseAdapter{
        private int mLastAnimatedPosition;

        @Override
        public int getCount() {
            return articlesList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                LayoutInflater layoutInflater = LayoutInflater.from(JsonActivity.this);
                viewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.item_article_list,null);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.summary = (TextView) convertView.findViewById(R.id.summary);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.img);
                viewHolder.postTime = (TextView) convertView.findViewById(R.id.postTime);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Article artical = articlesList.get(position);
            viewHolder.title.setText(artical.getTitle());
            viewHolder.summary.setText(artical.getSummary());
            viewHolder.postTime.setText(artical.getPostTime());
            if(!artical.getImageUrl().equals("")){
                imageLoader.DisplayImage(artical.getImageUrl(),viewHolder.image);
            } else {
                viewHolder.image.setVisibility(View.GONE);//图片的链接内容为空，将imageview的显示状态设置为GONE
            }
            return convertView;
        }
    }
}
