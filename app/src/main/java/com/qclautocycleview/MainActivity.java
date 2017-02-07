package com.qclautocycleview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public AutoCycleView cycleView;

    List<String> mViewList = new ArrayList<String>();//顶部用于循环的布局集合

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cycleView = (AutoCycleView) findViewById(R.id.cycle_view);
        initCycleViewPager();
        cycleView.setViewList(mViewList, this);
        cycleView.startCycle();//开始自动滑动
    }


    /*
    *  添加顶部循环滑动数据
    * 添加数据的时候需要注意在正常数据的基础上把最后一个数据添加到第一个前面，把第一个数据添加到最后一个数据后面，用来循环
    * 比如一共有1,2,3三个数据，为了实现循环需要另外添加两个数据，
    * 这样数据就成了3,1,2,3,1  这样就可以实现循环滑动的效果了
    * */
    public void initCycleViewPager() {
        mViewList = new ArrayList<String>();
        mViewList.add("第三页");
        mViewList.add("第一页");
        mViewList.add("第二页");
        mViewList.add("第三页");
        mViewList.add("第一页");
    }
}
