package com.cgtrc.bym.testapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.cgtrc.bym.testapplication.imageloader.ImageLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {

    private static final String TAG = "articleListActivity";
    private static final boolean DEBUG = true;
    private static final String DURL = "http://192.168.228.135:8080/test/";

    private ArrayList<Article> mArticleList;
    private PullToRefreshListView mListView;
    private VideoListAdapter mAdapter;
    private ImageLoader mImageLoader;

    static class ViewHolder {
        public TextView title;
        public TextView summary;
        public ImageView image;
        public TextView postTime;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置标题
        setTitle(getIntent().getStringExtra("title"));

        //新闻内容的容器
        mArticleList = new ArrayList<Article>();
        final String href = "http://192.168.228.135:8080/test/Detial.html";

        //图片加载器
        mImageLoader = new ImageLoader(this);

        //设置图片大小
        mImageLoader.setRequiredSize(5 * (int) getResources().getDimension(R.dimen.litpic_width));

        //适配器
        mAdapter = new VideoListAdapter();

        //得到下拉刷新的listView
        mListView = (PullToRefreshListView) findViewById(R.id.listview);

        //设置listView的监听器
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //点击listView中的item，打开另外一个Activity
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position > 0 && position <= mArticleList.size()) {
                    Article A1 = mArticleList.get(0);
                    String urll = A1.getUrl();
                    Log.i("urll", urll);

                    Intent intent  = new Intent(MainActivity.this, articleDetailActivity.class);
                    intent.putExtra("href",urll);
                    startActivity(intent);


                }
            }
        });

        mListView.setAdapter(mAdapter);
        mListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            public void onRefresh() {
                System.out.println("load url");
                //加载新闻列表
                loadNewsList(href, 1, true);
            }

            public void onLoadingMore() {
                int pageIndex = mArticleList.size() / 10 + 1;
                Log.i("pageIndex", "pageIndex = " + pageIndex);
                loadNewsList(href, pageIndex, false);
            }
        });
        loadNewsList(href, 1, true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    class VideoListAdapter extends BaseAdapter {
        private int mLastAnimatedPosition;

        public int getCount() {
            return mArticleList.size();
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                viewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.item_article_list, null);
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.summary = (TextView) convertView.findViewById(R.id.summary);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.img);
                viewHolder.postTime = (TextView) convertView.findViewById(R.id.postTime);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Article article = mArticleList.get(position);
            viewHolder.title.setText(article.getTitle());
            viewHolder.summary.setText(article.getSummary());
            viewHolder.postTime.setText(article.getPostTime());
            if (!article.getImageUrl().equals("")) {
                //异步加载图片
                mImageLoader.DisplayImage(article.getImageUrl(), viewHolder.image);
            } else {
                //如果该连接不包含图片，则imageView的显示状态设置为GONE
                viewHolder.image.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    public ArrayList<Article> parseArticleList(String href, final int page) {

//        try {
//            //url拼接处理
//            href = _MakeURL(href, new HashMap<String, Object>() {{
//                put("PageNo", page);
//            }});
//            Log.i("URL1122", "RUL1122 = " + href);
//
//            //得到解析的内容
//            Document doc = Jsoup.connect(href).timeout(10000).get();
//            Log.i("content", "content = " );
//
//            //得到title
//            Elements masthead = doc.select("title");
//            Log.i("title", "title = ");
//
//            Elements masthead2 = doc.select("body");
//            Log.i("url", "url = " + masthead2);
//
//            Elements h1 = masthead2.select("h1");
//            Log.i("url", "url = " + h1);


//            Elements articleElements = masthead.select("div.archive-list-item");
//            for (int i = 0; i < articleElements.size(); i++) {
//                Article article = new Article();
//                Element articleElement = articleElements.get(i);
//                Element titleElement = articleElement.select("h4 a").first();
//                Element summaryElement = articleElement.select("div.post-intro p").first();
//                Element imgElement = null;
//                if (articleElement.select("img").size() != 0) {
//                    imgElement = articleElement.select("img").first();
//                }
//                Element timeElement = articleElement.select(".date").first();
//                String url = "http://www.jcodecraeer.com" + titleElement.attr("href");
//                String title = titleElement.text();
//                String summary = summaryElement.text();
//                if (summary.length() > 70)
//                    summary = summary.substring(0, 70);
//                String imgsrc = "";
//                if (imgElement != null) {
//                    imgsrc = "http://www.jcodecraeer.com" + imgElement.attr("src");
//                }
//
//                String postTime = timeElement.text();
//                article.setTitle(title);
//                article.setSummary(summary);
//                article.setImageUrl(imgsrc);
//                article.setPostTime(postTime);
//                article.setUrl(url);
//                articleList.add(article);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        ArrayList<Article> articleList = new ArrayList<Article>();
        Article article = new Article();

        //url拼接处理
        href = _MakeURL(href, new HashMap<String, Object>() {{
            put("PageNo", page);
        }});
        System.out.println(href);


        try {
            //得到解析的内容
            Document doc = Jsoup.connect(href).timeout(10000).get();
            Log.i("解析的内容content", "" + doc);

            //得到title
            Elements title = doc.select("title");


            article.setTitle(title.text());
            article.setSummary("");
            article.setImageUrl("");
            article.setPostTime("");
            article.setUrl(href);

        } catch (IOException e) {
            e.printStackTrace();
        }


            articleList.add(article);


        return articleList;
    }

    /**
     * 加载新闻列表
     * @param href
     * @param page
     * @param isRefresh
     */
    private void loadNewsList(final String href, final int page, final boolean isRefresh) {

        //处理消息
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    ArrayList<Article> articleList = (ArrayList<Article>) msg.obj;
                    if (isRefresh) {
                        mArticleList.clear();    //下拉刷新之前先将数据清空
                        mListView.onRefreshComplete(new Date().toLocaleString());
                    }
                    for (Article article : articleList) {
                        mArticleList.add(article);
                    }
                    mAdapter.notifyDataSetChanged();
                    if (articleList.size() < 10) {
                        mListView.onLoadingMoreComplete(true);
                    } else if (articleList.size() == 10) {
                        mListView.onLoadingMoreComplete(false);
                    }
                }
            }
        };

        //异步加载
        new Thread() {
            public void run() {
                Message msg = new Message();
                ArrayList<Article> articleList = new ArrayList<Article>();
                try {
                    articleList = parseArticleList(href, page);
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                    msg.obj = e;
                }
                msg.what = 1;
                msg.obj = articleList;
                handler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 处理网页内容的分多页显示的情况
     * @param p_url
     * @param params
     * @return
     */
    private static String _MakeURL(String p_url, Map<String, Object> params) {
        StringBuilder url = new StringBuilder(p_url);
        if (url.indexOf("?") < 0)
            url.append('?');
        for (String name : params.keySet()) {
            url.append('&');
            url.append(name);
            url.append('=');
            url.append(String.valueOf(params.get(name)));
            //不做URLEncoder处理
            //url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
        }
        return url.toString().replace("?&", "?");
    }


}
