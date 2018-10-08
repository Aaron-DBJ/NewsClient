package com.example.jenny.newsclient.newsFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jenny.newsclient.ListAdapter;
import com.example.jenny.newsclient.NewsBean;
import com.example.jenny.newsclient.NewsContentActivity;
import com.example.jenny.newsclient.NewsInterface;
import com.example.jenny.newsclient.R;
import com.example.jenny.newsclient.RefreshableView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentFashion extends Fragment implements AdapterView.OnItemClickListener{
    private ListView mListView;
    private List<NewsBean> newsList;
    private ListAdapter adapter ;
    //    private String baseUrl = "http://v.juhe.cn/toutiao/";
    private String jsonData;
    private String baseUrl = "http://toutiao-ali.juheapi.com/toutiao/";
    private RefreshableView refreshableView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsList = new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mListView = view.findViewById(R.id.list_fragment);
        newsList = new ArrayList<>();
        mListView.setOnItemClickListener(this);
        refreshableView = view.findViewById(R.id.refreshable_view);

        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try{
                    Thread.sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                refreshableView.finishRefreshing();
            }
        }, 0);
        downloadJSONData();
        return view;
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == 0x1) {
                mListView.setAdapter(adapter);
            }
            return true;
        }
    });



    public void downloadJSONData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .build();
                NewsInterface newsInterface = retrofit.create(NewsInterface.class);
                Call<ResponseBody> call = newsInterface.getNews("shishang");
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            jsonData = response.body().string();
                            newsList = parseJSON(jsonData);
                            Log.d("MainActivity", "新闻数量:"+newsList.size());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        adapter = new ListAdapter(getContext(), newsList, mListView);
                        Message message = handler.obtainMessage();
                        message.obj = adapter;
                        message.what = 0x1;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("MainActivity", "下载JSON失败");
                    }
                });
            }

        }).start();
    }

    public List<NewsBean> parseJSON(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject object = jsonObject.getJSONObject("result");
            JSONArray jsonArray = object.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String title = obj.getString("title");
                String date = obj.getString("date");
                String imageUrl = obj.getString("thumbnail_pic_s");
                String author = obj.getString("author_name");
                String detail = obj.getString("url");

                NewsBean newsBean = new NewsBean(imageUrl, title, author, date, detail);
                newsList.add(newsBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.cancelAllTask();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(getActivity().getApplication(), NewsContentActivity.class);
        intent.putExtra("url", newsList.get(position).getDetail());
        startActivity(intent);

    }
}
