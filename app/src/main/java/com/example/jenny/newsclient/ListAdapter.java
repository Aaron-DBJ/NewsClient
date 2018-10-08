package com.example.jenny.newsclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    private Context context;
    private List<NewsBean> newsBeanList;
    private ListView mListView;
    private LruCache<String, Bitmap> memoryCache;
    private int mFirstVisibleItem, mVisibleItemCount;
    private boolean isFirstEnter = true;
    private Set<ImageDownloadTask> tasks;

    public ListAdapter(Context context, List<NewsBean> newsBeanList, ListView listView) {
        this.context = context;
        mListView = listView;
        this.newsBeanList = newsBeanList;
        tasks = new HashSet<>();
        int maxSize = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxSize/8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        mListView.setOnScrollListener(this);

    }

    @Override
    public int getCount() {
        return newsBeanList.size();
    }

    @Override
    public String getItem(int position) {
        return newsBeanList.get(position).getImageUrl();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        private ImageView image_view;
        private TextView title_tv;
        private TextView date_tv;
        private TextView author_tv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        String imageUrl = getItem(position);
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.news_list_item, null);
            holder.author_tv = convertView.findViewById(R.id.item_author);
            holder.title_tv = convertView.findViewById(R.id.item_title);
            holder.date_tv = convertView.findViewById(R.id.item_date);
            holder.image_view = convertView.findViewById(R.id.item_image);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.image_view.setImageBitmap(getImage(imageUrl));
        holder.title_tv.setText(getNewsTitle(position));
        holder.date_tv.setText(getNewsDate(position));
        holder.author_tv.setText(getNewsAuthor(position));
        holder.image_view.setTag(imageUrl);
        return convertView;
    }

    private String getNewsTitle(int position) {
        return newsBeanList.get(position).getTitle();
    }

    private String getNewsDate(int position) {
        return newsBeanList.get(position).getDate();
    }

    private String getNewsAuthor(int position) {
        return newsBeanList.get(position).getAuthor();
    }


    private Bitmap getImage(String imageUrl){
        Bitmap bitmap = getBitmapFromCache(imageUrl);
        if (bitmap !=null){
            return bitmap;
        }else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_photo);
        }
        return bitmap;

    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE){
            loadImages(mFirstVisibleItem, mVisibleItemCount);
        }else {
            cancelAllTask();
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
        if (isFirstEnter && firstVisibleItem>0){
            loadImages(firstVisibleItem, visibleItemCount);
            isFirstEnter = false;
        }
    }

    public void cancelAllTask(){
        for (ImageDownloadTask task :tasks){
            task.cancel(false);
        }
    }

    private void loadImages(int firstVisibleItem, int visibleItemCount){
        for (int i = firstVisibleItem; i<firstVisibleItem + visibleItemCount;i++) {
            String imageUrl = getItem(i);
            Bitmap bitmap = getBitmapFromCache(imageUrl);
            if (bitmap == null){
                ImageDownloadTask imageTask = new ImageDownloadTask();
                imageTask.execute(imageUrl);
                tasks.add(imageTask);
            }else {
                ImageView imageView = mListView.findViewWithTag(imageUrl);
                if (imageUrl != null && bitmap!=null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }


    class ImageDownloadTask extends AsyncTask<String, Void, Bitmap>{
        private String imageUrl;

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = mListView.findViewWithTag(imageUrl);
            if (bitmap!=null && imageView != null){
                imageView.setImageBitmap(bitmap);
            }
            tasks.remove(this);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            imageUrl = strings[0];
            Bitmap bitmap = downloadImage(strings[0]);
            if (bitmap !=null){
                addBitmapToCache(strings[0], bitmap);
            }
            return bitmap;
        }
    }
    public Bitmap downloadImage(String imageUrl){
        HttpURLConnection connection;
        InputStream inputStream;
        URL url;
        Bitmap bitmap = null;
        try {
            url = new URL(imageUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(3000);
            connection.setConnectTimeout(5000);
            inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
    public void addBitmapToCache(String key, Bitmap bitmap){
        if (getBitmapFromCache(key)==null){
            memoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromCache(String key){
        return memoryCache.get(key);
    }


}
