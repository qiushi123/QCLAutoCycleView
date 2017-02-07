package com.qclautocycleview;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 可以自动循环轮播的viewpager控件
 * 实现功能
 * 1，自动循环轮播，可以设置时间
 * 2，可以手动实现循环滑动
 */
public class AutoCycleView extends RelativeLayout {
    private final static String CYCLE_VIEW = "AtuoCycleView";//打印log用的

    private List<String> mViewList;
    private ViewPager mViewpage;
    private Activity mContext;
    private CyclePagerAdapter mAdapter;
    private CycleRunable mCycleRunable = new CycleRunable();

    private CycleIndexView mCycleIdxView;//圆点


    public AutoCycleView(Context context) {
        super(context);
        init(context);
    }

    public AutoCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoCycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /*
    * 初始化
    * */
    public void init(Context context) {

        mViewList = new ArrayList<String>();

        /*
        * 把viewpager和圆点添加到布局中
        * */
        mViewpage = new ViewPager(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mViewpage.setLayoutParams(layoutParams);
        addView(mViewpage);
        mViewpage.setAdapter(mAdapter = new CyclePagerAdapter());
        mViewpage.addOnPageChangeListener(new CycleViewChangeListener());

        //自定义滑动时的圆点
        mCycleIdxView = new CycleIndexView(context);
        addView(mCycleIdxView);

    }

    /*
    * 传入所需的数据
    * */
    public void setViewList(List<String> viewList, Activity mainActivity) {
        mContext = mainActivity;
        mViewList = viewList;
        //增加循环项

        mViewpage.setCurrentItem(1);
        mAdapter.notifyDataSetChanged();

        createIdxView(viewList.size() - 2);//创建和viewpager数据对应的圆点

    }

    /*
    * 创建所需圆点
    * */
    public void createIdxView(int size) {
        if (null != mCycleIdxView) {
            mCycleIdxView.setViewCount(size);//设置圆点个数
            LayoutParams layoutParams = new LayoutParams(mCycleIdxView.getCycleIdxViewWidth(), mCycleIdxView.getCycleIdxViewHeight());
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);//居于底部
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);//水平居中
            mCycleIdxView.setLayoutParams(layoutParams);
        }
    }

    /*
    * 设置自动轮播时间间隔
    * */
    public void startCycle(long time) {
        mCycleRunable.setCycleTime(time);
        mCycleRunable.startCycle();
    }

    /*
    * 开启循环
    * */
    public void startCycle() {
        mCycleRunable.startCycle();
    }


    /*
    * viewpager对应的适配器
    * */
    public class CyclePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            View view = inflater.inflate(R.layout.pager_item, null);
            TextView tv = (TextView) view.findViewById(R.id.text);
            tv.setText(mViewList.get(position));
            container.addView(view);

            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "点击了" + mViewList.get(position), Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            //暂停自动滚动
            mCycleRunable.puaseCycle();

        } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            //启动自动滚动
            mCycleRunable.startCycle();
        }
        return super.dispatchTouchEvent(ev);
    }

    /*
    * 轮播实现
    * */
    public void changePager() {
        if (mViewList.isEmpty()) {
            Log.e(CYCLE_VIEW, "data is empty!");
            throw new IllegalStateException("data is empty!");
        }
        int item = Math.min(mViewpage.getCurrentItem(), mViewList.size() - 1);
        //mViewpage.setCurrentItem(++item);


        if (item == mViewList.size() - 1) {
            mViewpage.setCurrentItem(0);
        } else {
            mViewpage.setCurrentItem(++item);
        }

    }

    /*
    *
    * */
    class CycleRunable implements Runnable {
        private boolean isAnimotion = false;
        private long mDefaultCycleTime = 1000L;//设置默认轮播时间 单位毫秒
        private long mLastTime;

        public void setCycleTime(long time) {
            mDefaultCycleTime = time;
        }

        @Override
        public void run() {
            if (isAnimotion) {
                long now = SystemClock.currentThreadTimeMillis();
                if (now - this.mLastTime >= this.mDefaultCycleTime) {
                    changePager();//大于指定时间间隔时就轮播下一个
                    this.mLastTime = now;
                }

                AutoCycleView.this.post(this);
            }
        }

        public void startCycle() {//开启自动循环
            if (this.isAnimotion) {
                return;
            }
            this.mLastTime = SystemClock.currentThreadTimeMillis();
            this.isAnimotion = true;
            AutoCycleView.this.post(this);
        }

        public void puaseCycle() {//暂停自动轮播
            this.isAnimotion = false;
        }
    }


    class CycleViewChangeListener implements ViewPager.OnPageChangeListener {
        //用户自己
        private boolean needJumpToRealPager = true;

        public void setNeedJumpFlag(boolean isNeedJump) {
            needJumpToRealPager = isNeedJump;
        }

        public boolean getNeedJumpFlag() {
            return needJumpToRealPager;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //            Log.d(CYCLE_VIEW, "onPageSelected position is "+position);

            if (null != mCycleIdxView && mViewpage.getCurrentItem() != 0 && mViewpage.getCurrentItem() != mViewList.size() - 1) {
                mCycleIdxView.setCurIndex(position - 1);//绑定圆点和viewpager的条目
            }
            //如果是头或者尾,等滑动
            if (mViewpage.getCurrentItem() == 0 && getNeedJumpFlag()) {
                setNeedJumpFlag(false);
                mViewpage.setCurrentItem(mViewList.size() - 1, false);
                mViewpage.setCurrentItem(mViewList.size() - 2);
            } else if (mViewpage.getCurrentItem() == mViewList.size() - 1 && getNeedJumpFlag()) {
                setNeedJumpFlag(false);
                mViewpage.setCurrentItem(0, false);
                mViewpage.setCurrentItem(1);
            } else {
                setNeedJumpFlag(true);
            }
            //            mViewpage

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //            Log.d(CYCLE_VIEW, "onPageScrollStateChanged state is "+state);

        }
    }


}
