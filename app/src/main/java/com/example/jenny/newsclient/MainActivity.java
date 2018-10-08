package com.example.jenny.newsclient;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.jenny.newsclient.newsFragments.FragmentDomestic;
import com.example.jenny.newsclient.newsFragments.FragmentEconomy;
import com.example.jenny.newsclient.newsFragments.FragmentEntertainment;
import com.example.jenny.newsclient.newsFragments.FragmentFashion;
import com.example.jenny.newsclient.newsFragments.FragmentInternational;
import com.example.jenny.newsclient.newsFragments.FragmentMilitary;
import com.example.jenny.newsclient.newsFragments.FragmentPE;
import com.example.jenny.newsclient.newsFragments.FragmentSociety;
import com.example.jenny.newsclient.newsFragments.FragmentTechnology;
import com.example.jenny.newsclient.newsFragments.NewsListFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{
    private ListView listView;
    private android.support.v7.widget.Toolbar toolbar;
    private List<NewsBean> newsList;
    private String url = "http://v.juhe.cn/toutiao/index?type=top&key=2d4197e80bb645151a07eb22a32a9034";
    private String jsonData;
    private ListAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> views;
    private List<String> titles;
    private RefreshableView refreshableView;
    private FragmentTransaction transaction;
    private FragmentManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        /**
         * 在activity中动态加载fragment，这里重复了下面代码注释掉也不影响程序。
         * 因为使用了ViewPager，加载fragment的功能就交给了FragmentPagerAdapter来处理。
         * 若不使用viewpager这类特殊的控件，则必须按照下面方法在activity中加载fragment。
         */
        NewsListFragment listFragment = new NewsListFragment();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_holder, listFragment);
        transaction.addToBackStack(null);
        transaction.commit();


    }

    private void initView(){
        //listView = findViewById(R.id.list_view);
        newsList = new ArrayList<>();
        //listView.setOnItemClickListener(this);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        refreshableView = findViewById(R.id.refreshable_view);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        views = new ArrayList<>();
        titles = new ArrayList<>();

        /**
         * 两个fragment及以下可以正常运行，从三个fragment开始就报错，还在找错因!?
         * 复用一个Fragment超过三次就会报错，但是直接写三个以上的Fragment就正确？？？
         */
//        for (int i = 0;i < TITLES.length-5;i++){
//            NewsListFragment fragment = new NewsListFragment();
//            fragment.setNewsType(category[i]);
//            views.add(fragment);
//            titles.add(TITLES[i]);
//        }

        NewsListFragment fragmentTop = new NewsListFragment();
        views.add(fragmentTop);
        titles.add("头条");

        FragmentInternational fragmentInternational = new FragmentInternational();
        views.add(fragmentInternational);
        titles.add("国际");

        FragmentSociety fragmentSociety = new FragmentSociety();
        views.add(fragmentSociety);
        titles.add("社会");

        FragmentDomestic fragmentDomestic = new FragmentDomestic();
        views.add(fragmentDomestic);
        titles.add("国内");

        FragmentPE fragmentPE = new FragmentPE();
        views.add(fragmentPE);
        titles.add("体育");

        FragmentMilitary fragmentMilitary = new FragmentMilitary();
        views.add(fragmentMilitary);
        titles.add("军事");

        FragmentEntertainment fragmentEntertainment = new FragmentEntertainment();
        views.add(fragmentEntertainment);
        titles.add("娱乐");

        FragmentTechnology fragmentTechnology = new FragmentTechnology();
        views.add(fragmentTechnology);
        titles.add("科技");

        FragmentEconomy fragmentEconomy = new FragmentEconomy();
        views.add(fragmentEconomy);
        titles.add("财经");

        FragmentFashion fragmentFashion = new FragmentFashion();
        views.add(fragmentFashion);
        titles.add("时尚");

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, views);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cancelAllTask();
    }

}
